package io.untaek.animal_new.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.reactivex.rxkotlin.toObservable
import io.untaek.animal_new.R
import io.untaek.animal_new.component.PickContentButton
import io.untaek.animal_new.databinding.ActivityMainBinding
import io.untaek.animal_new.tab.tool.MainFragmentAdapter
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.util.ContentUtil
import io.untaek.animal_new.viewmodel.UploadViewModel
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import me.majiajie.pagerbottomtabstrip.item.NormalItemView

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_UPLOAD = 2359
    }
    /**
     * Received Result from @UploadFragment
     *
     * @requestType: REQUEST_CAMERA, REQUEST_GALLERY
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", requestCode.toString())

        if(resultCode != Activity.RESULT_OK) {
            return
        }

        if(requestCode == PickContentButton.REQUEST_GALLERY || requestCode == PickContentButton.REQUEST_CAMERA) {
            var uri: Uri? = null
            val vm = ViewModelProviders.of(this).get(UploadViewModel::class.java)

            if(requestCode == PickContentButton.REQUEST_GALLERY) {
                uri = data?.data
                Log.d("MainActivity", "onActivityResult Gallery $uri")
            }else if (requestCode == PickContentButton.REQUEST_CAMERA) {
                uri = vm.currentUri
                Log.d("MainActivity", "onActivityResult Camera $uri")
            }

            vm.currentUri = uri
            vm.currentSize = ContentUtil.getSize(this, uri!!)
            vm.currentMime = ContentUtil.getMime(this, uri)

            val content = Content(mime = vm.currentMime!!, url = vm.currentUri.toString(), width = vm.currentSize?.x!!, height = vm.currentSize?.y!!)

            Intent(this, WritePostDetailActivity::class.java).also {
                it.putExtra("content", content)
                startActivityForResult(it, REQUEST_UPLOAD)
            }
        }
        else if(requestCode == REQUEST_UPLOAD) {
            val vm = ViewModelProviders.of(this).get(UploadViewModel::class.java)
            val description = data?.getStringExtra("description") ?: ""
            val tags = (data?.getStringArrayExtra("tags") ?: arrayOf<String>())
                .mapIndexed { index, s ->
                    index.toString() to s
                }
                .toMap()

            Log.d("MainActivity", "Upload $description $tags")
            vm.upload2(description, tags)
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewPager.adapter = MainFragmentAdapter(supportFragmentManager)
        initNavigation()
    }

    private fun initNavigation() {
        val nav = binding.tab.custom()
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "Timeline"))
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "Rank"))
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "Upload"))
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "My Page"))
            .build()

        nav.addSimpleTabItemSelectedListener { index, _ ->
            changeTab(index)
        }
    }

    private fun tabItem(drawable: Int, checkedDrawable: Int, text: String): BaseTabItem {
        return NormalItemView(this).apply {
            initialize(drawable, checkedDrawable, text)
        }
    }

    private fun changeTab(index: Int) {
        binding.viewPager.setCurrentItem(index, false)
    }
}
