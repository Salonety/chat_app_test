package com.example.chat2

import android.net.wifi.hotspot2.pps.Credential
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class OtpActivity : AppCompatActivity() {
    val verifyTv by lazy {
        findViewById<TextView>(R.id.verifyTv)
    }
    val otpEt by lazy {
        findViewById<TextView>(R.id.sentcodeEt)
    }
    val verificationBtn by lazy {
        findViewById<Button>(R.id.verificationBtn)
    }
    lateinit var PhoneNumber: String
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        try {
            PhoneNumber = intent.getStringExtra(PHONE_NUMBER)!!
            verifyTv.text = "verify $PhoneNumber"
            init()
        } catch (e: Exception) {
            Toast.makeText(this, "Number not found try again!!!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun init() {
        verificationBtn.setOnClickListener {
            val credential =
                PhoneAuthProvider.getCredential(storedVerificationId!!, otpEt.text.toString())
            SignInAuth(credential)
        }

        auth = Firebase.auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, p1)
                storedVerificationId = verificationId

            }


            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val smsCode = credential.smsCode
                otpEt.text = smsCode
                Log.i("Verification Completed", "The verification has been Completed")
                SignInAuth(credential)
            }


            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Invalid Phone number exceeded",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Quota exceeded.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }


            }

        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]


    }


    private fun SignInAuth(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                //First Time Login
                if (task.result?.additionalUserInfo?.isNewUser == true) {
                    ShowSignUpActivity()
                } else {
                    ShowHomeActivity()
                }

            } else{
        }
    }



        }

    private fun ShowHomeActivity() {

    }
}


private  fun ShowSignUpActivity (){

}
