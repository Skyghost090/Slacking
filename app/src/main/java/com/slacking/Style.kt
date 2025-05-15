package com.slacking

import android.app.Application
import com.google.android.material.color.DynamicColors

class Style : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}