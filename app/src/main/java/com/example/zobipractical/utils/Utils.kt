package com.example.zobipractical.utils

import android.content.Context
import android.net.ConnectivityManager

class Utils {
    companion object{
        fun getConnectivityStatus(context: Context): Boolean {
            var status = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    status = true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    status = true
                }
            } else {
                status = false
            }
            return status
        }
    }
}