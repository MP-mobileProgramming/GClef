package com.example.gclef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_in_post.*

class InPostActivity : AppCompatActivity()  {
    private lateinit var data : Song
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_post)

        data = intent.getSerializableExtra("data") as Song

        Glide.with(this)
            .load(data.imageUrl)
            .override(250,250)
            .centerCrop()
            .into(coverImageInPost)
        songTitleInPost.text = data.songTitle
        songDetailInPost.text = data.songDetail
        userNameInPost.text = data.userName

    }
}