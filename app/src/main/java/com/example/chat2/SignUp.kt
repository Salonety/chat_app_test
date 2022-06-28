package com.example.chat2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class SignUp : AppCompatActivity() {
    val nameEt by lazy {
        findViewById<EditText>(R.id.nameEt)
    }
    val userImgView by lazy {
        findViewById<ImageView>(R.id.userImgView)
    }
    val edt_Gender by lazy {
        findViewById<EditText>(R.id.edt_Gender)
    }
    val edt_nation by lazy {
        findViewById<EditText>(R.id.edt_nation)
    }
    val edt_DOB by lazy {
        findViewById<EditText>(R.id.edt_DOB)
    }
    val edt_address by lazy {
        findViewById<EditText>(R.id.edt_address)
    }
    val edt_about by lazy {
        findViewById<EditText>(R.id.edt_about)
    }
    val edt_email by lazy {
        findViewById<EditText>(R.id.edt_email)
    }
    val nextBtn by lazy {
        findViewById<Button>(R.id.nextBtn)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

    }
}