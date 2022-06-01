package com.example.gclef

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var intent: Intent
    private var songList: ArrayList<Song> = ArrayList()
    private var userList: ArrayList<UserInfo> = ArrayList()
    private var urlList: ArrayList<String> = ArrayList()


    var fireStore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth

    init {  // 파이어베이스 데이터 ArrayList에 담음
        fireStore = FirebaseFirestore.getInstance()

        fireStore?.collection("Post")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                songList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject<Song>()
                    songList.add(item!!)
                }

                notifyDataSetChanged()
            }
        fireStore?.collection("User")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                userList.clear()


                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject<UserInfo>()
                    userList.add(item!!)


                }
                notifyDataSetChanged()

            }

    }

    // xml파일을 inflate하여 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)



        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun putSongList(): ArrayList<Song> {
            Log.i("q", "list: " + songList[0])
            return songList
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        viewHolder.songTitle.text = songList[position].songTitle
        viewHolder.songDetail.text = songList[position].songDetail






        for (i: Int in 1 until userList.size) {
            if(userList[i].uid == songList[position].uid) {
                viewHolder.userName.text = userList[i].userName
                songList[position].userName = userList[i].userName
            }
        }

        Glide.with(viewHolder)
            .load(songList[position].imageUrl)
            .override(250,250)
            .centerCrop()
            .into(viewHolder.coverImage)


        //var location = IntArray(2)
        //var p = location[1]
        var y = 0
        val mediaPlayer = MediaPlayer()



        //while(true) {
        /*if(p - y == 3) {
            //play
            mediaPlayer.setDataSource(songList[y].soundUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()
            Log.i("q","playing : $p y: $y")

        } else {
            // next
            mediaPlayer.stop()
            Log.i("q","stop : $p y: $y")

        }*/


        viewHolder.setOnClickListener {
            intent = Intent(viewHolder.context, InPostActivity::class.java)
            intent.putExtra("data", songList[position])
            Glide.with(viewHolder)
                .load(songList[position].imageUrl)
                .override(250,250)
                .centerCrop()
                .into(viewHolder.coverImage)
            viewHolder.context.startActivity(intent)

        }



    }

    override fun getItemCount(): Int {
        return songList.size
    }
    fun putSongList(): ArrayList<Song> {
        Log.i("q", "list: " + songList)
        return songList
    }
}