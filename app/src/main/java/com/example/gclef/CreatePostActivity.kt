package com.example.gclef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        //adapter 불러와서 파이어베이스에 데이터 저장
        val adapter = RecyclerViewAdapter()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser


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
            var songPost = Song()
            songPost.songTitle = songTitlePostEditText.text.toString()
            songPost.songDetail = songDetailPostEditText.text.toString()
            songPost.uid = currentUser?.uid



            adapter.fireStore?.collection("Post")
                ?.document()?.set(songPost)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
                ?.addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("MainActivity", "Error getting documents: $exception")
                }

        }




    }
}