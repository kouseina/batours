package com.example.batours.storage

import android.content.Context
import com.example.batours.models.User


class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("full_name", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("profile_pic_url", null),
            )
        }


    fun saveUser(user: User?) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        user?.id?.let { editor.putInt("id", it) }
        editor.putString("email", user?.email)
        editor.putString("full_name", user?.full_name)
        editor.putString("phone", user?.phone)
        editor.putString("profile_pic_url", user?.profile_pic_url)

        editor.apply()

    }

    val token: String?
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", null)
        }

    fun saveToken(token: String?) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", token)

        editor.apply()
    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_KEY = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}