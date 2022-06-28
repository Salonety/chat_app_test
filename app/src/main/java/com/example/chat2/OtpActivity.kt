package com.example.chat2

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
    val counterTv by lazy {
        findViewById<TextView>(R.id.counterTv)
    }
    val resendBtn by lazy {
        findViewById<Button>(R.id.resendBtn)
    }
    lateinit var PhoneNumber: String
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var progressDialog:ProgressDialog
    private lateinit var mCounter:CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        init()
        startVerify()
    }
        private fun startVerify() {
        startPhoneNumberVerification(PhoneNumber)
            startCounter(6000)
         progressDialog= createDialog("Sending verification code",false)
            progressDialog.show()
    }

    private fun startCounter(time: Long) {
      resendBtn.isEnabled=false
        counterTv.isEnabled=true
        mCounter=object:CountDownTimer(time,1000){


            override fun onFinish() {
           resendBtn.isEnabled=true
                counterTv.isVisible=false
            }
            override fun onTick(timeLeft: Long) {
             counterTv.text="seconds Remaining:"+timeLeft/1000
            }

        }.start()
    }

    private fun init() {
        verificationBtn.setOnClickListener {
            val credential =
                PhoneAuthProvider.getCredential(storedVerificationId!!, otpEt.text.toString())
            SignInAuth(credential)
        }
        resendBtn.setOnClickListener {
            resendVerificationCode(PhoneNumber,resendToken)
            startCounter(6000)
            progressDialog= createDialog("Sending verification code again",false)
            progressDialog.show()
        }
        try {
            PhoneNumber = intent.getStringExtra(PHONE_NUMBER)!!
            verifyTv.text = "verify $PhoneNumber"
        } catch (e: Exception) {
            Toast.makeText(this, "Number not found try again!!!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }


        auth = Firebase.auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                progressDialog.dismiss()
                storedVerificationId = verificationId
              resendToken=token

            }


            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                progressDialog.dismiss()
                val smsCode = credential.smsCode
                otpEt.text = smsCode
                Log.i("Verification Completed", "The verification has been Completed")
                SignInAuth(credential)
            }


            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
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

            } else {
                progressDialog= createDialog("Phone number verification failed",false)
                progressDialog.show()
            }
        }
    }

    private fun ShowHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun ShowSignUpActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
}
    fun Context.createDialog(message:String, isCancelable:Boolean):ProgressDialog{

    return ProgressDialog(this).apply {
        setCancelable(isCancelable)
        setMessage(message)
        setCanceledOnTouchOutside(false)
    }


    }


