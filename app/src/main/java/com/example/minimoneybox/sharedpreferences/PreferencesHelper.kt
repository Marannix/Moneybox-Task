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
    private var prefUserFullName = "key_user_full_name"
    private var prefUserLoggedIn = "key_user_logged_in"

    var userPreference = context.getSharedPreferences(prefUserToken, Context.MODE_PRIVATE)!!


    fun setUserHasLoggedIn(hasLoggedIn: Boolean) {
        userPreference.edit().putBoolean(prefUserLoggedIn, hasLoggedIn).apply()
    }

    fun getHasUserLoggedIn() {
        userPreference.getBoolean(prefUserLoggedIn, false)
    }

    fun setToken(token: String) {
        userPreference.edit().putString(prefKeyToken, token).apply()
    }

    fun getToken(): String {
        return userPreference.getString(prefKeyToken, "")!!
    }

    fun setUserFullName(fullname: String) {
        userPreference.edit().putString(prefUserFullName, fullname).apply()
    }

    fun getUserFullName(): String {
        return userPreference.getString(prefUserFullName, "")!!
    }
}