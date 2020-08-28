package com

import android.content.Context
import android.content.SharedPreferences

object Preference {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    val key = "groupChat"
    fun storeKey(context: Context, key: String, value: String) {
        sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()

    }


    fun retrieveKey(context: Context, key: String): String? {
        sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }


}