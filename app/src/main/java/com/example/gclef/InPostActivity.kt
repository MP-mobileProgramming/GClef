package com.example.gclef

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_in_post.*

class InPostActivity : AppCompatActivity()  {
    private lateinit var data : Song
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_post)

        data = intent.getSerializableExtra("data") as Song

        Glide.with(this)
            .load(data.imageUrl)
            .override(250,250)
            .centerCrop()
            .into(coverImageInPost)
        songTitleInPost.text = data.songTitle
        songDetailInPost.text = data.songDetail
        userNameInPost.text = data.userName


        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(applicationContext, Uri.parse(data.soundUrl))
        mediaPlayer.prepare()
        playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)


       playButton.setOnClickListener {
            if(mediaPlayer.isPlaying) {
                Toast.makeText(this, "재생 중", Toast.LENGTH_LONG).show()
                playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
                mediaPlayer.pause()

            }
            else {
                Toast.makeText(this, "재생 시작", Toast.LENGTH_LONG).show()
                mediaPlayer.start()
                playButton.setBackgroundResource(R.drawable.ic_baseline_pause_24)
            }
        }

    }
}