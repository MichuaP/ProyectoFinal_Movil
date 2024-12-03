package com.pmcl.proyectofinal_movil

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class SaveSharedPreference {
    companion object {
        private val PREF_USER_NAME: String = "username"

        fun getSharedPreferences(ctx: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        fun setUserName(ctx: Context, userName: String) {
            val editor = getSharedPreferences(ctx).edit()
            editor.putString(PREF_USER_NAME, userName)
            editor.apply()
        }

        fun getUserName(ctx: Context): String {
            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")?:""
        }

        fun clearUserName(ctx: Context) {
            val editor = getSharedPreferences(ctx).edit()
            editor.clear()
            editor.apply()
        }

    }

}