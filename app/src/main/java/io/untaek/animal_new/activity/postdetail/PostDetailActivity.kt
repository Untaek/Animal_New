package io.untaek.animal_new.activity.postdetail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.ActivityPostDetailBinding
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class PostDetailActivity: AppCompatActivity() {
    companion object {
        const val FROM = "from"

        const val FROM_UNKNOWN = -1
        const val FROM_CONTENT = 0
        const val FROM_COMMENT = 1
        const val FROM_NOTIFICATION = 2
    }

    lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        val from = intent.getIntExtra(FROM, FROM_UNKNOWN)

        when(from) {
            FROM_CONTENT -> fromContent()
            FROM_COMMENT -> fromComment()
            FROM_NOTIFICATION -> fromNotification()
            else -> finish()
        }
    }

    private fun fromContent() {
        val post = intent.getSerializableExtra("post") as Post
        initPost(post)
        selectCurrentFragment(0)
    }

    private fun fromComment() {
        val post = intent.getSerializableExtra("post") as Post
        initPost(post)
        selectCurrentFragment(1)
    }

    private fun fromNotification() {
        val postId = intent.getStringExtra("post_id")
        Log.d("PostDetailActivity", postId)
        initPost(postId)
        selectCurrentFragment(1)
    }

    private fun initPost(post: Post) {
        binding.viewPager.adapter = PostDetailFragmentAdapter(supportFragmentManager)
        binding.vm = ViewModelProviders
            .of(this, PostDetailViewModel.PostDetailViewModelFactory(post))
            .get(PostDetailViewModel::class.java)
    }

    private fun initPost(postId: String) {
        binding.viewPager.adapter = PostDetailFragmentAdapter(supportFragmentManager)
        binding.vm = ViewModelProviders
            .of(this, PostDetailViewModel.PostDetailViewModelFactory(postId))
            .get(PostDetailViewModel::class.java)
    }

    private fun selectCurrentFragment(index: Int) {
        binding.viewPager.currentItem = index
    }
}