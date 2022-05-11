package com.example.gclef

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_board.*

class BoardFragment : Fragment() {
    private var fireStore: FirebaseFirestore? = null

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


        // Create a post - 글쓰기 누르면 - CreatePostActivity 로 이동
        fab.setOnClickListener {
            val postIntent = Intent(context?.applicationContext, CreatePostActivity::class.java)
            startActivity(postIntent)
            Toast.makeText(context, "클릭", Toast.LENGTH_LONG).show()
        }

    }



}