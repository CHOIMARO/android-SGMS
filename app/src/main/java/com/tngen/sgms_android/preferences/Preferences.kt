package com.tngen.sgms_android.preferences

import android.content.Context
import android.widget.Toast
import com.tngen.sgms_android.data.entity.settings.BaselineEntity


class Preferences {
    companion object {
        private var toast: Toast? = null
        fun showToast(context: Context?, message: String?) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            } else {
                toast!!.cancel()
                toast!!.setText(message)
            }
            toast!!.show()
        }
        var baselineEntity : BaselineEntity? = null
    }
}