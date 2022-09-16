package com.tngen.sgms_android.preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context:Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("com-tngen-sgms-android-pref", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
}