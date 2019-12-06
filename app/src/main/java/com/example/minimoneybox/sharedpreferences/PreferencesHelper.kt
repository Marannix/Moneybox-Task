package com.example.minimoneybox.sharedpreferences

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(
    context: Context
) {

    private var prefUserToken = "moneybox_user_preference"
    private var prefKeyToken = "key_token_object"

    var userPreference = context.getSharedPreferences(prefUserToken, Context.MODE_PRIVATE)!!

    fun setToken(token: String) {
        userPreference.edit().putString(prefKeyToken, token).apply()
    }

    fun getToken(): String {
        return userPreference.getString(prefKeyToken, "")!!
    }
}