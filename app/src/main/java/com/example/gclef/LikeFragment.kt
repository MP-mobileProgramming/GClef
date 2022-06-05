package com.example.gclef

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.fragment_like.*
import java.util.ArrayList
import kotlin.concurrent.thread

class LikeFragment : Fragment() {
    private var fireStore: FirebaseFirestore? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var auth: FirebaseAuth
    private lateinit var spinner: Spinner
    private lateinit var search_option: String

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    override fun onStart() {
        super.onStart()
        like_recyclerview.adapter = LikeAdapter()
        like_recyclerview.layoutManager = LinearLayoutManager(context?.applicationContext)



        fireStore = FirebaseFirestore.getInstance()
        var songList: ArrayList<Song> = ArrayList()
        var cnt = 0

        fireStore?.collection("Post")
            ?.orderBy("timeStamp", Query.Direction.DESCENDING)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                songList.clear()
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject<Song>()

                    songList.add(item!!)
                }
            }
        var savedpositon = -1
        var scrollPosition = 0
        if(songList.size != 0) {
            thread(start = true) {
                while (true) {
                    Thread.sleep(1000)
                    Log.i("q", "스레드 실행 중 저장위치:$savedpositon")
                    if (savedpositon != scrollPosition) {
                        savedpositon = scrollPosition
                        if (mediaPlayer.isPlaying) {
                            //노래 끄기
                            mediaPlayer.stop()
                        }
                        mediaPlayer.reset()
                        context?.applicationContext?.let {
                            mediaPlayer.setDataSource(
                                it,
                                Uri.parse(songList[scrollPosition].soundUrl)
                            )
                        }
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        mediaPlayer.seekTo(songList[scrollPosition].highLightTime)
                        //새노래 켜기
                        Log.i("q", "노래 끄기, 켜기")
                    }
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }
}