package com.example.gclef

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var songList: ArrayList<Song> = ArrayList()
    var fireStore: FirebaseFirestore? = null

    init {  // 파이어베이스 데이터 ArrayList에 담음
        fireStore = FirebaseFirestore.getInstance()
        fireStore?.collection("Posting")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                songList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject<Song>()
                    songList.add(item!!)


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

        viewHolder.songTitle.text = songList[position].songTitle
        viewHolder.songDetail.text = songList[position].songDetail
    }

    override fun getItemCount(): Int {
        return songList.size
    }

}