package com.example.gclef

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.ArrayList

class SearchFragment : Fragment() {
    private var fireStore: FirebaseFirestore? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var auth: FirebaseAuth
    private lateinit var spinner: Spinner
    private lateinit var search_option: String
    private lateinit var searchBtn:Button
    private lateinit var searchText: EditText
    private lateinit var searchview: SearchView
    private lateinit var searchviewAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onStart() {
        super.onStart()

        mediaPlayer = MediaPlayer()
        val thread = Thread()

        fireStore = FirebaseFirestore.getInstance()
        var urlList: ArrayList<String> = ArrayList()
        var cnt = 0
        var linearLayoutManager: LinearLayoutManager

        var optionList=resources.getStringArray(R.array.search_option)

        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser


        //검색버튼
        search_btn.setOnClickListener {
            var word = search_bar.text.toString()
            search_recyclerview.adapter = SearchAdapter(word)
            search_recyclerview.layoutManager = LinearLayoutManager(context?.applicationContext)

        }

    }

}