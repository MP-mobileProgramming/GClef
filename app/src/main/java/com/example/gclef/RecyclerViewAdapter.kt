package com.example.gclef

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var songList: ArrayList<Song> = ArrayList()
    private var userList: ArrayList<userInfo> = ArrayList()

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
                    var item = snapshot.toObject<userInfo>()
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

        for (i: Int in 1 until userList.size) {
            if(userList[i].uid == songList[position].uid) {
                viewHolder.userName.text = userList[i].userName
            }
        }


    }

    override fun getItemCount(): Int {
        return songList.size
    }

}