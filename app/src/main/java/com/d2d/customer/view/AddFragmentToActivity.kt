package com.d2d.customer.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.d2d.customer.R
import com.d2d.customer.util.ConnectionType
import com.d2d.customer.util.NetworkMonitorUtil
import com.d2d.customer.view.fragment.*

class AddFragmentToActivity : AppCompatActivity() {
    var fragmentName:String? = null
    private val networkMonitor = NetworkMonitorUtil(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_details_fragments_to)
        fragmentName = intent.getStringExtra("FragmentName")

        if(fragmentName.equals("SubCategoryDescriptionFragment")){
            supportFragmentManager!!.beginTransaction()
                .add(R.id.fragmentContainer, SubCategoryDescriptionFragment(),"SubCategoryDescriptionFragment")
                .commit()
            }else if (fragmentName.equals("OrderHistoryFragment")) {
            supportFragmentManager!!.beginTransaction()
                .add(R.id.fragmentContainer, OrderHistoryFragment(), "OrderHistoryFragment")
                .commit()

        }else if(fragmentName.equals("SubscriptionDetailsFragment")){
            supportFragmentManager!!.beginTransaction()
                .add(R.id.fragmentContainer, SubscriptionDetailsFragment(),"SubscriptionDetailsFragment")
                .commit()
        }else if (fragmentName.equals("AddressFragment")){
            supportFragmentManager!!.beginTransaction()
                .add(R.id.fragmentContainer, AddressFragment(),"AddressFragment")
                .commit()
        }else if (fragmentName.equals("GoogleMapFragment")){
            supportFragmentManager?.beginTransaction()
                .add(R.id.fragmentContainer,GoogleMapFragment(),"GoogleMapFragment")
                .commit()
        }

    }


    override fun onResume() {
        super.onResume()
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


    private fun noInternetDialog(){
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

}