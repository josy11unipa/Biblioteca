package com.example.biblioteca

import android.content.pm.ActivityInfo
import com.journeyapps.barcodescanner.CaptureActivity

class CustomCaptureActivity : CaptureActivity() {
    override fun getRequestedOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}