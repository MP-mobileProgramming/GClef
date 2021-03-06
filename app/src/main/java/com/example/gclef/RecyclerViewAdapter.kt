package com.example.gclef

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_board.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

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
            ?.orderBy("timeStamp", Query.Direction.DESCENDING)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                songList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject<Song>()
                    if (item != null) {
                        item.path = snapshot.id
                    }
                    songList.add(item!!)
                }

                notifyDataSetChanged()
            }

        var mPath = fireStore?.collection("Post")?.document()

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

    }


    @SuppressLint("WrongConstant")
    @RequiresApi(31)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        viewHolder.songTitle.text = songList[position].songTitle
        viewHolder.songDetail.text = songList[position].songDetail

        for (i: Int in 0 until userList.size) {
            if(userList[i].uid == songList[position].uid) {
                viewHolder.userName.text = userList[i].userName
                songList[position].userName = userList[i].userName
            }
        }

        Glide.with(viewHolder)
            .load(songList[position].imageUrl)
            .override(250,250)
            .centerCrop()
            .transform(RoundedCorners(15))
            .into(viewHolder.coverImage)


        var y = 0
        val mediaPlayer = MediaPlayer()


        viewHolder.setOnClickListener {
            if (currentUser?.uid != null) {
                intent = Intent(viewHolder.context, InPostActivity::class.java)
                intent.putExtra("data", songList[position])
                Glide.with(viewHolder)
                    .load(songList[position].imageUrl)
                    .override(250, 250)
                    .centerCrop()
                    .transform(RoundedCorners(10))
                    .into(viewHolder.coverImage)
                viewHolder.context.startActivity(intent)

            }
            else {
                // 로그아웃 상태
                Toast.makeText(viewHolder.context, "로그인이 필요합니다", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun getItemCount(): Int {
        return songList.size
    }

}