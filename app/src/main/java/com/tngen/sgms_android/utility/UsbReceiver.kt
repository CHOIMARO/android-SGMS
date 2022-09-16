package com.tngen.sgms_android.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import com.tngen.sgms_android.SgmsApplication.Companion.resumed
import com.tngen.sgms_android.presentation.main.MainActivity

abstract class UsbReceiver () : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {

    }
}