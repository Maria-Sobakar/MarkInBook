package com.fuh.markinbook

import android.content.Context
import android.content.SharedPreferences


object PreferencesManager {
    private const val USER_TOKEN_KEY = "UserTokenKey"
    private const val EMAIL_KEY = "EmailKey"
    private const val NAME = "PreferencesToken"
    private const val DEVICE_TOKEN_KEY = "DeviceTokenKey"
    private const val AUTHORIZED_KEY = "AuthorizedKey"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var userToken: String
        get() = preferences.getString(USER_TOKEN_KEY, "default") ?: ""
        set(value) {
            preferences.edit {
                it.putString(USER_TOKEN_KEY, value)
            }
        }

    var email: String
        get() = preferences.getString(EMAIL_KEY, "") ?: ""
        set(value) = preferences.edit {
            it.putString(EMAIL_KEY, value)
        }

    var deviceToken: String?
        get() = preferences.getString(DEVICE_TOKEN_KEY, "")
        set(value) = preferences.edit {
            it.putString(DEVICE_TOKEN_KEY, value)
        }
}