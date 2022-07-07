package com.example.narastagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    lateinit var userName: EditText
    lateinit var userPassword: EditText
    lateinit var emailLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        emailLoginBtn = findViewById(R.id.email_login_button)

        emailLoginBtn.setOnClickListener {
            signInAndSignup()
        }
    }

    fun initView(activity: LoginActivity) {
        userName = activity.findViewById(R.id.email_edittext)
        userPassword = activity.findViewById(R.id.password_edittext)
    }

    // 회원가입
    fun signInAndSignup() {
        auth?.createUserWithEmailAndPassword(userName.toString(), userPassword.toString())
            ?.addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    //아이디 생성됬을때
                    moveMainPage(it.result?.user)
                } else if (it.exception?.message.isNullOrEmpty()) {
                    //로그인 오류 메세지
                    Toast.makeText(this,it.exception?.message,Toast.LENGTH_LONG).show()
                } else {
                    //둘다 아닐경우 로그인 화면으로 이동
                    signInEmail()
                }
            }
    }

    // 로그인
    fun signInEmail() {
        auth?.signInWithEmailAndPassword(userName.toString(), userPassword.toString())
            ?.addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    // 로그인 완료
                    moveMainPage(it.result?.user)
                } else {
                    // 로그인 실패
                    Toast.makeText(this,it.exception?.message,Toast.LENGTH_LONG).show()
                }

            }
    }

    // 메인페이지 이동
   fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}