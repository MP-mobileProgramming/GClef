package com.example.gclef

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.list_item.*

class BoardFragment : Fragment() {
    private var fireStore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var seekBar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onStart() {
        super.onStart()

        // recycler view - 파이어베이스에 있는 정보 불러옴
        recyclerView.adapter = RecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        mediaPlayer = MediaPlayer()
        val thread = Thread()

        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        // Create a post - 글쓰기 누르면 - CreatePostActivity 로 이동
        fab.setOnClickListener {
            if(currentUser?.uid != null) {
                //로그인 상태
                val postIntent =
                    Intent(context?.applicationContext, CreatePostActivity::class.java)
                startActivity(postIntent)
                Toast.makeText(context, "클릭", Toast.LENGTH_LONG).show()
            }
            else {
                // 로그아웃 상태
                Toast.makeText(context, "로그인이 필요합니다", Toast.LENGTH_LONG).show()
            }
        }



    }

    fun Thread() {
        val task = Runnable {
            while (mediaPlayer.isPlaying) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {

                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }

    }


}