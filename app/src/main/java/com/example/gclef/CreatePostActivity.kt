package com.example.gclef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)


        imageUploadButton.setOnClickListener {
            Toast.makeText(this, "사진 업로드", Toast.LENGTH_LONG).show()

        }

        songUploadButton.setOnClickListener {
            Toast.makeText(this, "노래 업로드", Toast.LENGTH_LONG).show()
        }

        postButton.setOnClickListener {
            Toast.makeText(this, "업로드", Toast.LENGTH_LONG).show()

        }




    }
}