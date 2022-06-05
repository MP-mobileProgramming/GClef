package com.example.gclef

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class CreatePostActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var imageStorage: FirebaseStorage
    lateinit var soundStorage: FirebaseStorage
    lateinit var firestore: FirebaseFirestore
    lateinit var runnable: Runnable
    private var handler= Handler()

    private var viewProfile : View? = null
    var pickImageFromAlbum = 0
    var pickSoundFromFolder = 1
    var selectImageUri : Uri? = null
    var selectSoundUri : Uri? = null

    var imgFileName : String = ""
    var soundFileName : String = ""

    private var songPost = Song()
    private lateinit var imageUri : String
    private lateinit var soundUri : String
    private lateinit var imageStorageRef : FirebaseStorage
    lateinit var soundStorageRef : FirebaseStorage

    var startTime : Int = 0
    var tmp : Int = 0

    private var userList: ArrayList<UserInfo> = ArrayList()
    var fireStore: FirebaseFirestore? = null

    var user : String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        //adapter 불러와서 파이어베이스에 데이터 저장
        val adapter = RecyclerViewAdapter()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser
        var mediaPlayer = MediaPlayer()



        imageStorage = FirebaseStorage.getInstance()
        soundStorage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //https://kante-kante.tistory.com/20 참조
        //사진 업로드 코드 구현
        imageUploadButton.setOnClickListener {
            var imagePickIntent = Intent(Intent.ACTION_PICK)
            imagePickIntent.type = "image/*"
            startActivityForResult(imagePickIntent, pickImageFromAlbum)

        }


        // 오디오 업로드 코드 구현
        songUploadButton.setOnClickListener {
            var soundPickIntent = Intent(Intent.ACTION_GET_CONTENT)
            soundPickIntent.type = "audio/*"
            startActivityForResult(soundPickIntent, pickSoundFromFolder)

        }

        selectHighlight.setOnClickListener {
            if (soundUri != "") {
                seekBarCreatePost.visibility = View.VISIBLE
                mediaPlayer.setDataSource(this, Uri.parse(soundUri))
                mediaPlayer.prepare()
                mediaPlayer.start()
                seekBarCreatePost.max = mediaPlayer.duration
                seekBarCreatePost.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                        startTime = seekBarCreatePost.progress
                        Log.i("q", startTime.toString())
                        if (changed) {
                            mediaPlayer.seekTo(pos)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                runnable = Runnable {
                    seekBarCreatePost.progress = mediaPlayer.currentPosition
                    handler.postDelayed(runnable, 1000)
                }
                handler.postDelayed(runnable, 1000)

            }
            else {
                Toast.makeText(this, "음악을 업로드 해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        completeHighlight.setOnClickListener {
            songPost.highLightTime = startTime
            Toast.makeText(this, "하이라이트 설정 완료", Toast.LENGTH_SHORT).show()
            mediaPlayer.stop()
        }

        fireStore = FirebaseFirestore.getInstance()

        fireStore?.collection("User")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                userList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject<UserInfo>()
                    userList.add(item!!)
                }
                for (i: Int in 0 until userList.size) {
                    if(userList[i].uid == currentUser?.uid) {
                        user = userList[i].userName
                    }

                }
            }





    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                selectImageUri = data?.data
                ImageUpload(this)
            }

        }
        else if(requestCode == pickSoundFromFolder) {
            if (resultCode == Activity.RESULT_OK) {
                selectSoundUri = data?.data
                SoundUpload(this)
            }
        }
    }

    private fun ImageUpload(view: CreatePostActivity) {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        imgFileName = "IMAGE_" + timeStamp + "_png"
        imageStorageRef = imageStorage
        imageStorageRef.reference?.child("images")?.child(imgFileName)
            .putFile(selectImageUri!!)?.addOnSuccessListener {
               Toast.makeText(this, "이미지 업로드 완료", Toast.LENGTH_SHORT).show()
        }

        imageUri =
            "https://firebasestorage.googleapis.com/v0/b/gclef-21fc8.appspot.com/o/images%2F$imgFileName?alt=media"
    }
    private fun SoundUpload(view: CreatePostActivity) {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        soundFileName = "AUDIO_" + timeStamp + "_wav"
        soundStorageRef = soundStorage
        soundStorageRef.reference?.child("sounds")?.child(soundFileName)
            .putFile(selectSoundUri!!)?.addOnSuccessListener {
                Toast.makeText(this, "노래 업로드 완료", Toast.LENGTH_SHORT).show()
                selectHighlight.visibility = View.VISIBLE
                completeHighlight.visibility = View.VISIBLE
            }

        soundUri =
            "https://firebasestorage.googleapis.com/v0/b/gclef-21fc8.appspot.com/o/sounds%2F$soundFileName?alt=media"
        tmp = 1
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = RecyclerViewAdapter()
        var userList: ArrayList<UserInfo> = ArrayList()


        when(item.itemId) {
            R.id.postButton-> {
                var time = System.currentTimeMillis()
                var currentUser = auth.currentUser


                songPost.songTitle = songTitlePostEditText.text.toString()
                songPost.songDetail = songDetailPostEditText.text.toString()
                songPost.uid = currentUser?.uid


                songPost.imageUrl = imageUri
                songPost.soundUrl = soundUri
                songPost.timeStamp = time
                songPost.userName = user

                //add Post
                adapter.fireStore?.collection("Post")
                    ?.document()?.set(songPost)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    ?.addOnFailureListener { exception ->
                        // 실패할 경우
                        Log.w("MainActivity", "Error getting documents: $exception")
                    }


            }
        }
        return super.onOptionsItemSelected(item)
    }

}