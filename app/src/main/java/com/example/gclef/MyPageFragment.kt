package com.example.gclef

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_mypage.*

class MyPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onStart() {
        super.onStart()

        goToLoginButton.setOnClickListener {
            val loginIntent = Intent(context?.applicationContext, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}