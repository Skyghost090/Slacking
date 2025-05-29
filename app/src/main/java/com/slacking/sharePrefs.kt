package com.slacking

import android.content.SharedPreferences

class sharePrefs{
    private val prefs = Prefs()
    fun getpref(pref: SharedPreferences, prefname: String): String? {
        try {
            return pref.getString(prefname, null)
        } catch (e: Exception){
            return ""
        }
    }
    fun changeResolution(resolution_: String, pref: SharedPreferences){
        if(prefs.resolutionApp == "" && resolution_ == "" || resolution_ == "")
            prefs.resolutionApp = "1"

        prefs.resolutionApp = sharePrefs().getpref(pref, "resolution")
        val edit = pref.edit()
        edit.putString("resolution", resolution_)
        edit.apply()
    }

    fun saveGovernor(Governor: String, pref: SharedPreferences){
        fun savePrefs(){
            val prefsEdit = pref.edit()
            prefsEdit.putString("state", Governor)
            prefsEdit.apply()
            prefs.sched = getpref(pref, "state")
        }
        when(Governor){
            "Performance" -> {
                savePrefs()
            }
            "Battery Saver" -> {
                savePrefs()
            }
        }
    }
}