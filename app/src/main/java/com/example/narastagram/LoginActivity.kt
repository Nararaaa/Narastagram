package com.example.narastagram

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


class LoginActivity : AppCompatActivity(){
    private var auth: FirebaseAuth? = null

   // lateinit var googleSignInClient: Api.Client
    private lateinit var userName: EditText
    private lateinit var userPassword: EditText
    private lateinit var emailLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        emailLoginBtn = findViewById(R.id.email_login_button)

        emailLoginBtn.setOnClickListener {
            signInAndSignup()
        }
       /* var gos = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()*/
       // printHashKey()
    }
    private fun printHashKey() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }
    }

    fun initView(activity: Activity) {
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