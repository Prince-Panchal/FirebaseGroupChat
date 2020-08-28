package com.groupchat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.CommonKey
import com.Preference
import com.e.groupchat.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Chat : AppCompatActivity() {
    lateinit var firebaseFirestore: FirebaseFirestore

    lateinit var datClass: ChatDataClass
    var arrayList = ArrayList<ChatDataClass>()
    lateinit var adapter: ChatRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)
        FirebaseApp.initializeApp(applicationContext)
        firebaseFirestore = FirebaseFirestore.getInstance()


        getAllData()
        btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun getAllData() {
        firebaseFirestore.collection("IChat")
            .document("100")
            .collection("message").orderBy("date")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                for (item in querySnapshot!!.documentChanges) {
                    when (item.type) {
                        DocumentChange.Type.ADDED -> {
                            datClass = ChatDataClass()
                            datClass.userId = item.document.getString("user_id").toString()
                            datClass.message = item.document.getString("message").toString()
                            arrayList.add(datClass)
                            setAdapter(arrayList)
                            rvChat.scrollToPosition(arrayList.size - 1)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun sendMessage() {


        val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date())
        if (etMessege.text.trim().isNotEmpty()) {
            val data = hashMapOf(
                "user_id" to Preference.retrieveKey(this, CommonKey.userId),
                "room_id" to "100",
                "date" to date,
                "message" to etMessege.text.toString()
            )
            firebaseFirestore.collection("IChat")
                .document("100")
                .collection("message").add(data).addOnSuccessListener {
                }.addOnFailureListener {

                }
        } else {
            Toast.makeText(this, "Please type Message first", Toast.LENGTH_LONG).show()
        }


        etMessege.setText("")
    }


    private fun setAdapter(arrayList: ArrayList<ChatDataClass>) {
        adapter = ChatRecyclerView(this, arrayList)
        rvChat.adapter = adapter
    }

    data class ChatDataClass(var userId: String = "", var message: String = "")
}