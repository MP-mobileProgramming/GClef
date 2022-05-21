package com.example.gclef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        val adapter = RecyclerViewAdapter()


        signUpButton.setOnClickListener {
            val email = signEmailEditText.text.toString()
            val password = signPasswordEditText.text.toString()
            val userName = signUserNameEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                        var userInfo = UserInfo()
                        userInfo.uid = auth?.currentUser?.uid
                        userInfo.userName = userName

                        adapter.fireStore?.collection("User")?.document()?.set(userInfo)
                        finish()
                    }
                    else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                    }
                }
        }

    }
}