package com.example.chat2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.hbb20.CountryCodePicker

const val PHONE_NUMBER="phone"
class phoneNoActivity : AppCompatActivity() {
    val phoneNoEt:EditText by lazy{
        findViewById<EditText>(R.id.phoneNumberEt)

    }
    val button:Button by lazy{
        findViewById<Button>(R.id.nextBtn)
    }
    lateinit var countrycode:String
    lateinit var phoneNumber: String

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_phone_no)
            phoneNoEt.addTextChangedListener {
               button.isEnabled=!(it.isNullOrEmpty() || it.length< 10)
            }
            button.setOnClickListener {
                checkNumber()
            }
        }

    private fun checkNumber() {
     countrycode  = findViewById<CountryCodePicker>(R.id.ccp).selectedCountryCodeWithPlus
        phoneNumber =countrycode + phoneNoEt.text.toString()
        startActivity(Intent(this,OtpActivity::class.java).putExtra(PHONE_NUMBER,phoneNumber))
        finish()

    }
}
