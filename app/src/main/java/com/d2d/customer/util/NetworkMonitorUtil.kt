package com.d2d.customer.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

enum class ConnectionType {
    Wifi, Cellular
}

class NetworkMonitorUtil(context: Context) {

    private var mContext = context

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    lateinit var result: ((isAvailable: Boolean?, type: ConnectionType?) -> Unit)

    @Suppress("DEPRECATION")
    fun register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Use NetworkCallback for Android 9 and above
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager.activeNetwork == null) {
                // UNAVAILABLE
               try {
                   result(false,null)
               }catch (e:Exception){
                  // Toast.makeText(mContext,"No Internet Please Check Your Network Connection!!", Toast.LENGTH_SHORT).show()

               }
            }

            // Check when the connection changes
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)

                    // UNAVAILABLE
                    try {
                        result(false,null)
                    }catch (e:Exception){
                       // Toast.makeText(mContext,"No Internet Please Check Your Network Connection!!", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                            // WIFI
                            result(true,ConnectionType.Wifi)
                        }
                        else -> {
                            // CELLULAR
                            result(true,ConnectionType.Cellular)
                        }
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            // Use Intent Filter for Android 8 and below
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            mContext.registerReceiver(networkChangeReceiver, intentFilter)
        }
    }

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            mContext.unregisterReceiver(networkChangeReceiver)
        }
    }

    @Suppress("DEPRECATION")
    private val networkChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            if (activeNetworkInfo != null) {
                // Get Type of Connection
                when (activeNetworkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> {

                        // WIFI
                        result(true, ConnectionType.Wifi)
                    }
                    else -> {

                        // CELLULAR
                        result(true, ConnectionType.Cellular)
                    }
                }
            } else {

                // UNAVAILABLE
                try {
                    result(false,null)
                }catch (e:Exception){
                    //Toast.makeText(mContext,"No Internet Please Check Your Network Connection!!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}

//https://johncodeos.com/how-to-check-for-internet-connection-in-android-using-kotlin/