package com.slacking

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaredrummler.ktsh.Shell

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemListWidget_ = findViewById<RecyclerView>(R.id.recyclerview)
        val closeBtn = findViewById<ImageButton>(R.id.imageButton)
        val itemlist = ArrayList<item>()
        val adapter = itemAdapter(itemlist)

        itemListWidget_.layoutManager = LinearLayoutManager(this)

        val serviceIntent = Intent(this, notificationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)

        closeBtn.setOnClickListener {
            Shell.SU.run("cmd power set-fixed-performance-mode-enabled false")
            Shell.SU.run("am force-stop com.slacking")
        }

        fun openUrl(urlParam: String){
            val sharingIntent = Intent(Intent.ACTION_VIEW)
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            sharingIntent.setData(Uri.parse(urlParam))
            val chooserIntent = Intent.createChooser(sharingIntent, "Open With")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(chooserIntent)
        }

        resolutionApp = getpref(getSharedPreferences("gameQuality", MODE_PRIVATE), "resolution")

        itemListWidget_.adapter = adapter
        adapter.setOnClickListener(
            object :
                itemAdapter.OnClickListener{
                override fun onClick(position: Int, model: item) {
                    when(itemlist[position].name){
                        "Quality \uD83D\uDCF1" -> {
                            val dialog = Dialog(this@MainActivity)
                            dialog.setContentView(R.layout.activity_quality_dialog)
                            val seekbarVal = dialog.findViewById<SeekBar>(R.id.seekBar)
                            val resolutionVal = dialog.findViewById<TextView>(R.id.textView2)
                            resolutionVal.text = getpref(getSharedPreferences("gameQuality",
                                MODE_PRIVATE), "resolution")
                            seekbarVal.setOnSeekBarChangeListener(
                                object : SeekBar.OnSeekBarChangeListener {
                                    override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {}
                                    override fun onStartTrackingTouch(seek: SeekBar) {}
                                    override fun onStopTrackingTouch(seek: SeekBar) {
                                        var formatVal = seekbarVal.progress.toFloat() / 10
                                        val sharedPrefs = getSharedPreferences("gameQuality", MODE_PRIVATE)
                                        val tasksPrefs = sharedPrefs.edit()
                                        tasksPrefs.putString("resolution", formatVal.toString())
                                        tasksPrefs.apply()
                                        resolutionApp = getpref(getSharedPreferences("gameQuality", MODE_PRIVATE), "resolution")
                                        resolutionVal.text = getpref(getSharedPreferences("gameQuality",
                                            MODE_PRIVATE), "resolution")
                                    }
                                })
                            dialog.show()
                        }

                        "Activity Mode \uD83D\uDEE0\uFE0F" -> {
                            val sharedPrefs = getSharedPreferences("governor", MODE_PRIVATE)
                            val dialog = Dialog(this@MainActivity)
                            dialog.setContentView(R.layout.activity_governor_dialog)
                            val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
                            dialog.show()
                            fun selectOption(): RadioButton? {
                                val selectedOption: Int = radioGroup!!.checkedRadioButtonId
                                val radioButton = dialog.findViewById<RadioButton>(selectedOption)
                                return radioButton
                            }
                            when(getpref(sharedPrefs, "state")){
                                "Performance" -> {
                                    val button = dialog.findViewById<RadioButton>(R.id.radioButton2)
                                    button.isChecked = true
                                }
                                "Battery Saver" -> {
                                    val button = dialog.findViewById<RadioButton>(R.id.radioButton3)
                                    button.isChecked = true
                                }
                            }
                            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                                fun saveGovernor(Governor: String){
                                    fun savePrefs(){
                                        val prefs = sharedPrefs.edit()
                                        prefs.putString("state", Governor)
                                        prefs.apply()
                                        sched = getpref(sharedPrefs, "state")
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
                                saveGovernor(selectOption()?.text.toString())
                            }
                        }

                        "About \uD83C\uDF0E" -> {
                            openUrl("https://github.com/Skyghost090")
                        }

                        "Autor ©\uFE0F" -> {
                            openUrl("https://github.com/Skyghost090/Slacking")
                        }

                        "Issues Report ⚠\uFE0F" -> {
                            openUrl("https://github.com/Skyghost090/Slacking/issues")
                        }
                    }
                }
            }
        )
        itemlist.add(item("Quality \uD83D\uDCF1", "Game Resolution"))
        itemlist.add(item("Activity Mode \uD83D\uDEE0\uFE0F", "Change Governor"))
        itemlist.add(item("About \uD83C\uDF0E", "Project Repository"))
        itemlist.add(item("Autor ©\uFE0F", "Github Autor"))
        itemlist.add(item("Issues Report ⚠\uFE0F", "Open a issue in github"))
    }
}