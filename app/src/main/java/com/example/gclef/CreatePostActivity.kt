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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

        // 글쓰기 코드 구현






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
        }

        soundUri =
            "https://firebasestorage.googleapis.com/v0/b/gclef-21fc8.appspot.com/o/sounds%2F$soundFileName?alt=media"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = RecyclerViewAdapter()
        var userList: ArrayList<UserInfo> = ArrayList()
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser


        when(item.itemId) {
            R.id.postButton-> {

                songPost.songTitle = songTitlePostEditText.text.toString()
                songPost.songDetail = songDetailPostEditText.text.toString()
                songPost.uid = currentUser?.uid


                songPost.imageUrl = imageUri
                songPost.soundUrl = soundUri



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

    /*override fun onPause() {
        super.onPause()
        var imageStorageRef = imageStorage
        var soundStorageRef = soundStorage

        imageStorageRef.reference.downloadUrl.addOnSuccessListener { uri ->
            songPost.imageUrl = uri.toString()
            Log.i("src", songPost.imageUrl!!)
        }


    }*/


}