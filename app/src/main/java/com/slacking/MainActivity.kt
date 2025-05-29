package com.slacking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
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
        val sharePrefs = sharePrefs()
        val prefs = Prefs()
        val itemlist = ArrayList<item>()
        val adapter = itemAdapter(itemlist)
        val gameQualitySharedPref = getSharedPreferences("gameQuality", MODE_PRIVATE)

        itemListWidget_.layoutManager = LinearLayoutManager(this)

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

        prefs.resolutionApp = sharePrefs.getpref(gameQualitySharedPref, "resolution")

        itemListWidget_.adapter = adapter
        adapter.setOnClickListener(
            object :
                itemAdapter.OnClickListener{
                override fun onClick(position: Int, model: item) {
                    val itemListOptions = ItemListOptions()
                    when(itemlist[position].name){
                        "Apply Config \uD83C\uDFAE" -> {
                            itemListOptions.apps(this@MainActivity, prefs)
                        }
                        "Quality \uD83D\uDCF1" -> {
                            itemListOptions.quality(gameQualitySharedPref, this@MainActivity, prefs)
                        }

                        "Activity Mode \uD83D\uDEE0\uFE0F" -> {
                            itemListOptions.activity(this@MainActivity, getSharedPreferences("governor", MODE_PRIVATE), prefs)
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
        itemlist.add(item("Apply Config \uD83C\uDFAE", "Game Resolution"))
        itemlist.add(item("Quality \uD83D\uDCF1", "Game Resolution"))
        itemlist.add(item("Activity Mode \uD83D\uDEE0\uFE0F", "Change Governor"))
        itemlist.add(item("About \uD83C\uDF0E", "Project Repository"))
        itemlist.add(item("Autor ©\uFE0F", "Github Autor"))
        itemlist.add(item("Issues Report ⚠\uFE0F", "Open a issue in github"))
    }
}