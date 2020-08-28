package com.groupchat

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.CommonKey
import com.Preference
import com.e.groupchat.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Preference.storeKey(this, CommonKey.userId, userId)

        tvGetStart.setOnClickListener {
            val i = Intent(this, Chat::class.java)
            startActivity(i)
        }
    }
}