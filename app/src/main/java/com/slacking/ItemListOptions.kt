package com.slacking

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.jaredrummler.ktsh.Shell

class ItemListOptions {
    private val sharePrefs = sharePrefs()
    //private val prefs = Prefs()
    private fun addCommand(id: String, prefs: Prefs): Shell.Command.Result {
        val addCommandApp =  Shell.SU.run(
            "device_config put game_overlay ${id} mode=1,downscaleFactor=${prefs.resolutionApp}" +
                    ":mode=2,downscaleFactor=${prefs.resolutionApp}:mode=3,downscaleFactor=${prefs.resolutionApp}"
        )
        return addCommandApp
    }
    fun apps(context_: Context, prefs: Prefs){
        val dialog = Dialog(context_)
        dialog.setContentView(R.layout.activity_apps_dialog)
        val gameList = dialog.findViewById<TextView>(R.id.textView5)
        val appIdTextEdit = dialog.findViewById<EditText>(R.id.editTextText)
        val text_ = Shell.SU.run("cat /data/system/game_mode_intervention.list | tr , ' ' | awk '{print $1, $6}'").output.toString()
        appIdTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Call onDone result here
                val command_ = addCommand(appIdTextEdit.text.toString(), prefs)
                Toast.makeText(context_, prefs.resolutionApp.toString(), Toast.LENGTH_LONG).show()
                if (!command_.isSuccess)
                    Toast.makeText(context_, "App not found", Toast.LENGTH_SHORT).show()
                true
            }
            false
        }

        gameList.text = text_.drop(1).dropLast(1)
        dialog.show()
    }

    fun quality(gameQualitySharedPref: SharedPreferences, context_: Context, prefs: Prefs){
        val dialog = Dialog(context_)
        dialog.setContentView(R.layout.activity_quality_dialog)
        val seekbarVal = dialog.findViewById<SeekBar>(R.id.seekBar)
        val resolutionVal = dialog.findViewById<TextView>(R.id.textView2)
        resolutionVal.text = sharePrefs.getpref(gameQualitySharedPref, "resolution")
        seekbarVal.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {}
                override fun onStartTrackingTouch(seek: SeekBar) {}
                override fun onStopTrackingTouch(seek: SeekBar) {
                    var formatVal = seekbarVal.progress.toFloat() / 10
                    sharePrefs.changeResolution(formatVal.toString(), gameQualitySharedPref)
                    prefs.resolutionApp = sharePrefs.getpref(gameQualitySharedPref, "resolution")
                    Toast.makeText(context_, prefs.resolutionApp.toString(), Toast.LENGTH_LONG).show()
                    resolutionVal.text = sharePrefs.getpref(gameQualitySharedPref, "resolution")

                }
            })
        dialog.show()
    }

    fun activity(context_: Context, sharedPrefs: SharedPreferences, prefs: Prefs){
        val dialog = Dialog(context_)
        dialog.setContentView(R.layout.activity_governor_dialog)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
        dialog.show()
        fun selectOption(): RadioButton? {
            val selectedOption: Int = radioGroup!!.checkedRadioButtonId
            val radioButton = dialog.findViewById<RadioButton>(selectedOption)
            return radioButton
        }
        Toast.makeText(context_, prefs.resolutionApp.toString(), Toast.LENGTH_LONG).show()
        when(sharePrefs.getpref(sharedPrefs, "state")){
            "Performance" -> {
                val button = dialog.findViewById<RadioButton>(R.id.radioButton2)
                button.isChecked = true
                Shell.SU.run("cmd power set-fixed-performance-mode-enabled true")
            }
            "Battery Saver" -> {
                val button = dialog.findViewById<RadioButton>(R.id.radioButton3)
                button.isChecked = true
                Shell.SU.run("cmd power set-fixed-performance-mode-enabled false")
            }
        }
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            sharePrefs.saveGovernor(selectOption()?.text.toString(),sharedPrefs)
        }
    }
}