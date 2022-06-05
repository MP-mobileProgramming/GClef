package com.example.gclef

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.list_item.view.*

class LikeAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var intent: Intent
    private var songList: ArrayList<Song> = ArrayList()
    private var userList: ArrayList<UserInfo> = ArrayList()
    private var likePathList: ArrayList<Like> = ArrayList()

    var fireStore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth

    init {  // 파이어베이스 데이터 ArrayList에 담음
        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        fireStore?.collection("Like")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                likePathList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString("likeUid")!!.contains(currentUser?.uid.toString())) {
                        var item = snapshot.toObject<Like>()
                        likePathList.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        songList.clear()
        for (i: Int in 0.. likePathList.size) {
            fireStore?.collection("Post")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌

                    for (snapshot in querySnapshot!!.documents) {
                        if (snapshot.id == likePathList[i].postPath.toString()) {
                            var item = snapshot.toObject<Song>()
                            songList.add(item!!)
                        }
                    }
                    notifyDataSetChanged()

                }
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

    }


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
            .into(viewHolder.coverImage)


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
}