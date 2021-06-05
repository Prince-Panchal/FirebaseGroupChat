package com.princechat

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.CommonKey
import com.Preference
import com.e.groupchat.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.princechat.signup.MakeAccount
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tvSignUp
import kotlinx.android.synthetic.main.make_account.*


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(applicationContext)
        firebaseFirestore = FirebaseFirestore.getInstance()
        val userId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Preference.storeKey(this, CommonKey.userId, userId)

        tvGetStart.setOnClickListener {
            if (validation()) {
                //login()

                loginViaDB()
            }

        }
        tvSignUp.setOnClickListener {
            val i = Intent(this, MakeAccount::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_top)

        }
    }

    private fun loginViaDB() {
        val db = firebaseFirestore.collection("Signup")
            .document("users")
            .collection("userDetails")

        db.whereEqualTo("email", etUsername.text.toString()).get().addOnSuccessListener {
            if (it.documents.size!=0){
                db.whereEqualTo("password",etPassword.text.toString()).get().addOnSuccessListener {
                    if (it.documents.size!=0){
                        startActivity(Intent(this,Chat::class.java))
                    }else{
                        Toast.makeText(this, "Password is  incorrect", Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                Toast.makeText(this, "Email is incorrect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
        mAuth!!.signInWithEmailAndPassword(etUsername.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginnSuccess", "signInWithEmail:success")
                    val user: FirebaseUser = mAuth!!.currentUser!!
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Failed", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed. ${task.exception!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }
    }

    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            val i = Intent(this, Chat::class.java).putExtra("name", user.displayName).putExtra("id",user.uid)
            startActivity(i)
            overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_top)
        }
    }

    private fun validation(): Boolean {
        when {
            etUsername.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
                return false
            }
            etPassword.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }
}