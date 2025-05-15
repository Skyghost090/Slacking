package com.slacking

import android.content.SharedPreferences

fun getpref(pref: SharedPreferences, prefname: String): String? {
    try {
        return pref.getString(prefname, null)
    } catch (e: Exception){
        return ""
    }
}
var resolutionApp: String? = null