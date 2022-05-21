package com.example.gclef

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.list_item.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class CreatePostActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var imageStorage: FirebaseStorage
    lateinit var soundStorage: FirebaseStorage
    lateinit var firestore: FirebaseFirestore
    private var viewProfile : View? = null
    var pickImageFromAlbum = 0
    var pickSoundFromFolder = 1
    var selectImageUri : Uri? = null
    var selectSoundUri : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        //adapter 불러와서 파이어베이스에 데이터 저장
        val adapter = RecyclerViewAdapter()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser


        imageStorage = FirebaseStorage.getInstance()
        soundStorage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //https://kante-kante.tistory.com/20 참조
        //사진 업로드 코드 구현
        imageUploadButton.setOnClickListener {
            Toast.makeText(this, "사진 업로드", Toast.LENGTH_LONG).show()
            var imagePickIntent = Intent(Intent.ACTION_PICK)
            imagePickIntent.type = "image/*"
            startActivityForResult(imagePickIntent, pickImageFromAlbum)

        }


        // 오디오 업로드 코드 구현
        songUploadButton.setOnClickListener {
            Toast.makeText(this, "노래 업로드", Toast.LENGTH_LONG).show()
            var soundPickIntent = Intent(Intent.ACTION_GET_CONTENT)
            soundPickIntent.type = "audio/*"
            startActivityForResult(soundPickIntent, pickSoundFromFolder)
        }

        // 글쓰기 코드 구현






    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                selectImageUri = data?.data
            }

        }
        else if(requestCode == pickSoundFromFolder) {
            if (resultCode == Activity.RESULT_OK) {
                selectSoundUri = data?.data
            }

        }
    }

    private fun ImageUpload(view: CreatePostActivity) {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imgFileName = "IMAGE_" + timeStamp + "_png"
        var imageStorageRef = imageStorage?.reference?.child("images")?.child(imgFileName)

        imageStorageRef?.putFile(selectImageUri!!)?.addOnSuccessListener {
            Toast.makeText(this, "이미지 업로드 완료", Toast.LENGTH_SHORT).show()
        }
    }

    private fun SoundUpload(view: CreatePostActivity) {
        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var soundFileName = "AUDIO_" + timeStamp + "_wav"
        var soundStorageRef = soundStorage?.reference?.child("sounds")?.child(soundFileName)

        soundStorageRef?.putFile(selectSoundUri!!)?.addOnSuccessListener {
            Toast.makeText(this, "노래 업로드 완료", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = RecyclerViewAdapter()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        when(item.itemId) {
            R.id.postButton-> {
                var songPost = Song()
                songPost.songTitle = songTitlePostEditText.text.toString()
                songPost.songDetail = songDetailPostEditText.text.toString()
                songPost.imageUrl = selectImageUri.toString()
                songPost.soundUrl = selectSoundUri.toString()
                songPost.uid = currentUser?.uid



                adapter.fireStore?.collection("Post")
                    ?.document()?.set(songPost)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show()
                        ImageUpload(this)
                        SoundUpload(this)
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