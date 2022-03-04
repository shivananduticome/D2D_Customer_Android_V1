package com.d2d.customer.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.d2d.customer.R
import com.d2d.customer.databinding.ActivityDashboardBinding
import com.d2d.customer.model.ViewCartResponseModel
import com.d2d.customer.util.ConnectionType
import com.d2d.customer.util.NetworkMonitorUtil
import com.d2d.customer.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import java.lang.Exception


class DashboardActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDashboardBinding
    private val networkMonitor = NetworkMonitorUtil(this)
    var notificationsBadge : View?  = null
    private var sharedPreferences: SharedPreferences? = null
    private var userId:String? = null
    private lateinit var viewModel:CartViewModel
    private  var cartItemCount:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        viewModel= ViewModelProvider(this).get(CartViewModel::class.java)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        checkInternetConnection()

    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = this.getSharedPreferences(theme.resources?.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        userId=sharedPreferences?.getString("userId","")
        checkInternetConnection()
        cartCount(userId!!)
    }

    private fun checkInternetConnection(){
        networkMonitor.register()
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                //Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                // Toast.makeText(this,"Wifi Connection", Toast.LENGTH_SHORT).show()

                            }
                            ConnectionType.Cellular -> {
                                // Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                // Toast.makeText(this,"Cellular Connection", Toast.LENGTH_SHORT).show()
                            }
                            else -> { }
                        }
                    }
                    false -> {
                        //Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        // Toast.makeText(this,resources.getString(R.string.no_internet_please_check_your_network_connection), Toast.LENGTH_SHORT).show()
                        noInternetDialog()
                    }
                }
            }
        }
    }

    private fun noInternetDialog() {
        val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.no_internet_connection))
            builder.setMessage(R.string.no_internet_please_check_your_network_connection)
            builder.setIcon(resources.getDrawable(R.drawable.ic_baseline_cloud_off_24))
        //performing positive action
        builder.setPositiveButton("Ok"){dialogInterface, which ->
           // Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
            alertDialog.cancel()
            alertDialog.setCancelable(false)
            alertDialog.show()
    }

    private fun cartCount(userId:String){
        viewModel.getCartDetailsObservable().observe(this, Observer<ViewCartResponseModel> {
            if (it.statusCode==400){
               // Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
            }else{
                cartItemCount = it.ViewCartData.toMutableList().size.toString()
                if (cartItemCount.equals("")||cartItemCount.equals(null)){
                    removeBadge()
                }else {
                    addBadge(cartItemCount!!)
                }
            }

        })
        viewModel.apiCall(userId)

    }

    private fun getBadge() : View {
        if (notificationsBadge != null){
            return notificationsBadge!!
        }
        val mbottomNavigationMenuView = binding.navView.getChildAt(0) as BottomNavigationMenuView
        notificationsBadge = LayoutInflater.from(this).inflate(R.layout.custom_badge_layout, mbottomNavigationMenuView,false)
        return notificationsBadge!!
    }

     private fun addBadge(count : String) {
         try {
             getBadge()
             // Toast.makeText(this,count,Toast.LENGTH_SHORT).show()
             val tvTitle = notificationsBadge?.findViewById<TextView>(R.id.notifications_badge)
             tvTitle?.text =count
             binding.navView.addView(notificationsBadge)
         }catch (e:Exception){
             // Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_LONG).show()
         }

    }

    private fun removeBadge() {
        try {
            binding.navView.removeView(notificationsBadge)
        }catch (e:Exception){
            //Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_LONG).show()
        }
    }


}

//https://betterprogramming.pub/how-to-create-notification-badges-with-google-bottom-navigation-bar-330925474f6d