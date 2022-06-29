package com.example.chat2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class SignUp : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    val nameEt by lazy {
        findViewById<EditText>(R.id.nameEt)
    }
    val userImgView by lazy {
        findViewById<ShapeableImageView>(R.id.userImgView)
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
        storage = Firebase.storage
        userImgView.setOnClickListener {
            openGallery()
        }

    }

    private fun openGallery() {
        val intent= Intent()
        intent.type="image/*"
        startActivityForResult(intent,1001)
    }
}