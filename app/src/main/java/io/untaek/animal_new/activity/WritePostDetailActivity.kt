package io.untaek.animal_new.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import io.untaek.animal_new.R
import io.untaek.animal_new.component.scrollBottom
import io.untaek.animal_new.databinding.ActivityWritePostDetailBinding
import io.untaek.animal_new.type.Content

class WritePostDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityWritePostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.getSerializableExtra("content") == null) {
            finish()
        }

        val tags = ObservableField<List<String>>()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_post_detail)
        binding.content = intent.getSerializableExtra("content") as Content
        binding.description = ObservableField()
        binding.tags = tags

        binding.editTag.setTagAddCallBack {
            tags.set(binding.editTag.tagList)
            return@setTagAddCallBack true
        }
        binding.editTag.setTagDeletedCallback {
            tags.set(binding.editTag.tagList)
        }

        binding.editTag.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if(b) scrollBottom(binding.scrollView)
        }
    }
}
