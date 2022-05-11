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


        //글 정보 저장 코드 구현


        //사진 업로드 코드 구현
        imageUploadButton.setOnClickListener {
            Toast.makeText(this, "사진 업로드", Toast.LENGTH_LONG).show()

        }


        // 오디오 업로드 코드 구현
        songUploadButton.setOnClickListener {
            Toast.makeText(this, "노래 업로드", Toast.LENGTH_LONG).show()
        }

        // 글쓰기 코드 구현
        postButton.setOnClickListener {
            Toast.makeText(this, "업로드", Toast.LENGTH_LONG).show()

        }




    }
}