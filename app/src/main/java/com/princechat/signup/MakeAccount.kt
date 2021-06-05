package com.princechat.signup

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.groupchat.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.princechat.utils.getRandomNumberString
import kotlinx.android.synthetic.main.make_account.*
import java.text.SimpleDateFormat
import java.util.*


class MakeAccount : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_account)
        FirebaseApp.initializeApp(applicationContext)
        firebaseFirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        ivBacMakeAccount.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_top)
        }

        btnSubmit.setOnClickListener {
            if (validation()) {
                newUser()
            }
        }
    }

    private fun newUser() {
        val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date())

        val data = hashMapOf(
            "username" to etFullName.text.toString(),
            "userId" to getRandomNumberString(),
            "date" to date,
            "email" to etEmail.text.toString(),
            "password" to etConfirmPass.text.toString(),
            "phone" to etPhone.text.toString()
        )


        val db = firebaseFirestore.collection("Signup")
            .document("users")
            .collection("userDetails")

        db.whereEqualTo("email", etEmail.text.toString()).get().addOnSuccessListener {
            if (it.documents.size==0){
                db.add(data).addOnSuccessListener {
                    Toast.makeText(this, "Registered Successfully..", Toast.LENGTH_SHORT).show()
                    finish()
                    btnSubmit.isActivated = false
                }.addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "This email already exists try another", Toast.LENGTH_SHORT).show()
            }
        }



    }


    private fun makeNewAccount() {
        mAuth!!.createUserWithEmailAndPassword(
            etEmail.text.toString(),
            etConfirmPass.text.toString()
        )
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("IssSuccess", " Welcome ${task.result?.user?.displayName}")
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Failure", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
    }


    private fun validation(): Boolean {
        when {
            etFullName.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter valid fullname", Toast.LENGTH_SHORT).show()
                return false
            }
            etEmail.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
                return false
            }
            etPhone.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter valid phone", Toast.LENGTH_SHORT).show()
                return false
            }
            etPass.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return false
            }
            etConfirmPass.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show()
                return false
            }
            etConfirmPass.text.toString() != etPass.text.toString() -> {
                Toast.makeText(this, "Confirm password doesnt match", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }
}