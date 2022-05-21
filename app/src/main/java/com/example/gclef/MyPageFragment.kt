package com.example.gclef

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_mypage.*

class MyPageFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onStart() {
        super.onStart()


        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        if(currentUser?.uid != null ) {
            goToLoginButton.text = "LOG OUT"
            goToLoginButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "로그아웃", Toast.LENGTH_LONG).show()
                goToLoginButton.text = "LOG IN"
            }
        }
        else {
            goToLoginButton.text = "LOG IN"

            goToLoginButton.setOnClickListener {
                val loginIntent = Intent(context?.applicationContext, LoginActivity::class.java)
                startActivity(loginIntent)
                Handler().postDelayed({
                   goToLoginButton.text = "LOG OUT"
                }, 1000)
            }
        }



    }



}