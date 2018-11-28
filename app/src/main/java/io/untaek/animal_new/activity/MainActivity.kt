package io.untaek.animal_new.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.R
import io.untaek.animal_new.component.PickContentButton
import io.untaek.animal_new.databinding.ActivityMainBinding
import io.untaek.animal_new.tab.tool.MainFragmentAdapter
import io.untaek.animal_new.util.ContentUtil
import io.untaek.animal_new.viewmodel.UploadViewModel
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import me.majiajie.pagerbottomtabstrip.item.NormalItemView

class MainActivity : AppCompatActivity() {

    /**
     * Received Result from @UploadFragment
     *
     * @type: REQUEST_CAMERA, REQUEST_GALLERY
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", requestCode.toString())

        if(resultCode != Activity.RESULT_OK) {
            return
        }

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

        vm.upload()
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
