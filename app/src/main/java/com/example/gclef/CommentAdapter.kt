package com.example.gclef

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentAdapter(path: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var intent: Intent
    private var commentList: ArrayList<Comment> = ArrayList()
    private var userList: ArrayList<UserInfo> = ArrayList()
    private var urlList: ArrayList<String> = ArrayList()



    private var fireStore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth

    init {  // 파이어베이스 데이터 ArrayList에 담음
        fireStore = FirebaseFirestore.getInstance()

        if (path != null) {
            fireStore?.collection("Post")?.document(path)?.collection("Comment")
                ?.orderBy("timeStamp")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    commentList.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject<Comment>()
                        commentList.add(item!!)
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
            LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)



        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser
        var timeStamp = getTime(commentList[position].timeStamp)


        for (i: Int in 0 until userList.size) {
            if(userList[i].uid == commentList[position].uid) {
                commentList[position].userId = userList[i].userName
            }
        }

        viewHolder.userIdTextView.text = commentList[position].userId
        viewHolder.commentTextView.text = commentList[position].comment
        viewHolder.timeTextView.text = timeStamp

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    /**
     *  Get Comment Time
     */
    object TimeMAX {
        const val sec = 60
        const val min = 60
        const val hour = 24
        const val day = 30
        const val month = 12
    }
    fun getTime(regTime: Long): String? {
        var time = System.currentTimeMillis()
        var diffTime = (time - regTime) / 1000
        var msg: String? = null
        when {
            diffTime < TimeMAX.sec -> {
                msg = "방금 전"
            }
            TimeMAX.sec.let { diffTime /= it; diffTime } < TimeMAX.min -> {
                msg = diffTime.toString() + "분 전"
            }
            TimeMAX.min.let { diffTime /= it; diffTime } < TimeMAX.hour -> {
                msg = diffTime.toString() + "시간 전"
            }
            TimeMAX.hour.let { diffTime /= it; diffTime } < TimeMAX.day -> {
                msg = diffTime.toString() + "일 전"
            }
            TimeMAX.day.let { diffTime /= it; diffTime } < TimeMAX.month -> {
                msg = diffTime.toString() + "달 전"
            }
            else -> {
                msg = diffTime.toString() + "년 전"
            }
        }
        return msg
    }

}