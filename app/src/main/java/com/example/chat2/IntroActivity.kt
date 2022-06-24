package com.example.chat2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        findViewById<TextView>(R.id.textView)
        findViewById<ImageView>(R.id.imageView)
        findViewById<Button>(R.id.button).setOnClickListener{
         startActivity(Intent(this, phoneNoActivity::class.java))
        }
    }
}