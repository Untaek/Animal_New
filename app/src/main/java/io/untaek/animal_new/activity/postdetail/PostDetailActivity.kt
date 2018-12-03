package io.untaek.animal_new.activity.postdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.ActivityPostDetailBinding
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class PostDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityPostDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        val post = intent.getSerializableExtra("post") as Post

        binding.viewPager.adapter = PostDetailFragmentAdapter(supportFragmentManager)
        binding.vm = ViewModelProviders.of(this, PostDetailViewModel.PostDetailViewModelFactory(post)).get(PostDetailViewModel::class.java)
    }
}