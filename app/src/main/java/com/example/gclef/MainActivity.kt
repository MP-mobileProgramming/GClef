package com.example.gclef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val boardFragment = BoardFragment()
    private val searchFragment = SearchFragment()
    private val likeFragment = LikeFragment()
    private val myPageFragment =  MyPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationBar()
    }


    //각각 해당하는 프레그먼트로 이동
    private fun initNavigationBar() {
        bottom_nav.setOnItemSelectedListener { item: MenuItem ->
            changeFragment(
                when (item.itemId) {
                    R.id.board -> {
                        boardFragment
                    }
                    R.id.search -> {
                        searchFragment
                    }
                    R.id.like -> {
                        likeFragment
                    }
                    R.id.myPage -> {
                        myPageFragment
                    }
                    else -> {
                        boardFragment
                    }
                } as Fragment
            )
            true
        }
        bottom_nav.selectedItemId = R.id.board
    }



    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
    }

}