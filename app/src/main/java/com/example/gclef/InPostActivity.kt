package com.example.gclef

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
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
    private lateinit var seekBar1: SeekBar
    lateinit var runnable: Runnable
    private var handler=Handler()

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

    }
}