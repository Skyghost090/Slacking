package com.slacking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jaredrummler.ktsh.Shell

class notificationAction : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if(resolutionApp == "")
            resolutionApp = "1"

        if(sched == "Performance")
            Shell.SU.run("cmd power set-fixed-performance-mode-enabled true")
        else
            Shell.SU.run("cmd power set-fixed-performance-mode-enabled false")

        Shell.SU.run(
            "sleep 3;device_config put game_overlay $(dumpsys activity activities | grep topResumedActivity=ActivityRecord | " +
                    "tr ={/ ' ' | awk '{print $5}') mode=1,downscaleFactor=$resolutionApp:mode=2,downscaleFactor=$resolutionApp:mode=3," +
                    "downscaleFactor=$resolutionApp"
        )
    }
}