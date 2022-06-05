package com.example.gclef

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
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


        goToLoginButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "로그인", Toast.LENGTH_LONG).show()
                if(currentUser?.uid == null) {
                    val loginIntent = Intent(context?.applicationContext, LoginActivity::class.java)
                    startActivity(loginIntent)
                }

            } else {
                Toast.makeText(context, "로그아웃", Toast.LENGTH_LONG).show()
                if(currentUser?.uid != null) {
                    FirebaseAuth.getInstance().signOut()
                }
            }
        })

        radioGroup.check(R.id.defaultRadio)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.lightRadio -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    check(true)
                }
                R.id.darkRadio -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    check(true)
                }
                R.id.defaultRadio -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        check(true)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                        check(true)
                    }
                }
            }
        }

    }



}