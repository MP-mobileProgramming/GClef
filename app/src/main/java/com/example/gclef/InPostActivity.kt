package com.example.gclef

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_in_post.*

class InPostActivity : AppCompatActivity()  {
    private lateinit var data : Song
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar1: SeekBar
    lateinit var runnable: Runnable
    private lateinit var auth: FirebaseAuth
    private var handler=Handler()
    private var likeList = Like()
    private var tmpList: ArrayList<Like> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_post)

        data = intent.getSerializableExtra("data") as Song
        val adapter = RecyclerViewAdapter()
        val path = data.path.toString()
        var mFireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser


        Glide.with(this)
            .load(data.imageUrl)
            .override(250,250)
            .centerCrop()
            .transform(RoundedCorners(15))
            .into(coverImageInPost)
        songTitleInPost.text = data.songTitle
        songDetailInPost.text = data.songDetail
        userNameInPost.text = data.userName


        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(applicationContext, Uri.parse(data.soundUrl))
        mediaPlayer.prepare()
        playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)

        //seekBar initialize
        seekBar1=findViewById(R.id.seekBarInPost);
        seekBar1.progress=0
        seekBar1.max=mediaPlayer.duration

       playButton.setOnClickListener {
            if(mediaPlayer.isPlaying) {
                Toast.makeText(this, "재생 중", Toast.LENGTH_SHORT).show()
                playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
                mediaPlayer.pause()
            }
            else {
                Toast.makeText(this, "재생 시작", Toast.LENGTH_SHORT).show()
                mediaPlayer.start()
                playButton.setBackgroundResource(R.drawable.ic_baseline_pause_24)
            }
        }

        seekBar1.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                //when the position of seekbar has changed
                if(changed){
                    mediaPlayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        runnable=Runnable{
            seekBar1.progress=mediaPlayer.currentPosition
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
        //when music is finished, back to 0
        mediaPlayer.setOnCompletionListener {
            playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
            seekBar1.progress=0
        }


        /**
         * like
         */
        var mPath = ""
        likeButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(likeButton.isChecked) {
                likeList.likeUid = currentUser?.uid
                likeList.postPath = data.path

                likeButton.setBackgroundResource(R.drawable.ic_baseline_favorite_black_24)
                mFireStore?.collection("Like")
                    ?.document()?.set(likeList)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "좋아요", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener { exception ->
                        // 실패할 경우
                        Log.w("MainActivity", "Error getting documents: $exception")
                    }

            } else {
                likeButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)

                // like 컬렉션 스냅샷
                mFireStore?.collection("Like")
                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        // ArrayList 비워줌
                        tmpList.clear()
                        Log.i("q", "dsfsfsdfsf")

                        for (snapshot in querySnapshot!!.documents) {
                            var item = Like()
                            if(snapshot.getString("postPath")!!.contains(data.path.toString())) {
                                if(snapshot.getString("likeUid")!!.contains(currentUser?.uid.toString())) {
                                    item = snapshot.toObject<Like>()!!
                                    if (item != null) {
                                        mPath = snapshot.id
                                        Log.i("q", mPath)

                                    }
                                }
                            }
                            Log.i("q", "dsfaskdfs")

                            tmpList.add(item!!)
                        }
                    }
                Log.i("q", mPath)


                mFireStore?.collection("Like")
                    ?.document(mPath)
                    ?.delete()
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "좋아요 취소", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener { exception ->
                        // 실패할 경우
                        Log.w("MainActivity", "Error getting documents: $exception")
                    }

            }
        })


        /**
         * COMMENT WRITE & READ
         * 1. Comment Write
         * 2. Comment Read
        **/
        val commentPost = Comment()
        var fireStore: FirebaseFirestore?
        var userList: ArrayList<UserInfo> = ArrayList()
        var time = System.currentTimeMillis()
        commentButton.setOnClickListener {
            commentPost.comment = commentEditText.text.toString()
            commentPost.uid = currentUser?.uid
            commentPost.timeStamp = time

            commentEditText.text = null

            adapter.fireStore?.collection("User")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    userList.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject<UserInfo>()
                        userList.add(item!!)
                    }
                    for (i: Int in 0 until userList.size) {
                        if(userList[i].uid == commentPost.uid) {
                            commentPost.userId = userList[i].userName
                        }
                    }
                }



            adapter.fireStore?.collection("Post")?.document(path)?.collection("Comment")
                ?.document()?.set(commentPost)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "댓글 작성 완료", Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("MainActivity", "Error getting documents: $exception")
                }
        }


        commentEditText.setOnEditorActionListener{ textView, action, event ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(commentEditText.windowToken, 0)
                handled = true
            }

            handled
        }
        commentRecyclerView.adapter = CommentAdapter(path)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)

    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }

}