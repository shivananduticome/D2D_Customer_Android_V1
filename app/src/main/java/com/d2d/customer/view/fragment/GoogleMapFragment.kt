package com.d2d.customer.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.d2d.customer.R
import com.d2d.customer.databinding.GoogleMapBinding
import com.d2d.customer.model.*
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.viewmodel.GoogleMapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class GoogleMapFragment() : Fragment() {
    private lateinit var map:GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: GoogleMapBinding
    private lateinit var tvHome:TextView
    private lateinit var tvWork:TextView
    private lateinit var tvOther:TextView
    private lateinit var tvEnterLabelOthers:TextView
    private lateinit var registrationSharedPreferences: SharedPreferences
    private  lateinit var linearLayoutOthers:LinearLayout
    private lateinit var linearLayoutSaveAddressAsAllOption:LinearLayout
    private var isClickOther:Boolean?=false
    private var userId:String?=null
    private var latLong:String?=""
    private var sectorId:String?= null
    private var houseOrFloorNo:String?= null
    private var landMark:String?= null
    private  var saveAddressAs:String?= null
    private lateinit var viewModel: GoogleMapViewModel
    private var getGMapCurrentAddress= ""
    private lateinit var progressDialog:Dialog
    private var sectorList = mutableListOf<SectorDetail>()
    private var list = mutableListOf<String>()
    private var isGMapAddress:Boolean?=false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /*val myPlace = LatLng(12.9826816,77.594624)
        googleMap.addMarker(MarkerOptions().position(myPlace).title("Marker in India"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace,12.0f))
        googleMap.getUiSettings().setZoomControlsEnabled(true)*/
        googleMap.setOnMapClickListener {
           // Toast.makeText(context,"click on Marker",Toast.LENGTH_SHORT).show()
        }
        map = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true

         setUpMap()
        fun placeMarkerOnMap(location: LatLng) {
            // 1
            val markerOptions = MarkerOptions().position(location)
            // 2
            googleMap.addMarker(markerOptions)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = GoogleMapBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(GoogleMapViewModel::class.java)
        registrationSharedPreferences = (activity as AppCompatActivity).getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        userId = registrationSharedPreferences.getString("userId","")
        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)
        val view: View = binding.root
        clickEvent()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
           fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(activity as AppCompatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity as AppCompatActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

            // 1
             map.isMyLocationEnabled = true

           // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(activity as AppCompatActivity) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
              //  Toast.makeText(context,currentLatLng.toString(),Toast.LENGTH_SHORT).show()
                getAddress(currentLatLng)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f))
                map.addMarker(MarkerOptions().position(currentLatLng).draggable(true))
                map.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
                    override fun onMarkerDrag(marker: Marker) {
                    }
                    override fun onMarkerDragEnd(marker: Marker) {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15.0f))
                        val message = marker.position.latitude.toString() + "" + marker.position.longitude.toString()
                       // Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                       // Log.d(TAG + "_END", message)
                        val currentLatLong =(marker.position.latitude+marker.position.longitude)
                        getAddress(marker.position)
                    }

                    override fun onMarkerDragStart(marker: Marker) {
                        val message = marker!!.position.latitude.toString() + "" + marker.position.longitude.toString()
                        //Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                        //Log.d(TAG + "_DRAG", message)
                    }
                })
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15.0f))
        map.getUiSettings().setZoomControlsEnabled(true)
        // 2
        map.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(activity as AppCompatActivity, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]
            getGMapCurrentAddress = address.getAddressLine(0)
            //Toast.makeText(context,addressText.toString(),Toast.LENGTH_SHORT).show()
           // bottomDialogForCurrentAddress(addressText.toString())
                binding.lnrCurrentAddress.visibility = VISIBLE
                binding.tvCurrentAddress.text = getGMapCurrentAddress.toString()
                latLong = (latLng.latitude).toString() + "," + (latLng.longitude).toString()


        } else{
            getGMapCurrentAddress =""
        }
        return getGMapCurrentAddress
    }

    private fun clickEvent(){
       binding.btnEnterAddress.setOnClickListener {
           bottomDialogForEnterAddress()
       }/*Button click event for the address*/

    }



    private fun bottomDialogForEnterAddress(){
        val layoutForCurrentAddress = layoutInflater.inflate(R.layout.bottom_dialog_enter_address, null)
        val bottomSheetDialogForEnterAddress = BottomSheetDialog(activity as AppCompatActivity)
        val tvCurrency = layoutForCurrentAddress.findViewById<TextView>(R.id.tvLocation)
        val editTextHouseNoOrFloor = layoutForCurrentAddress.findViewById<TextInputEditText>(R.id.editHouseNoOrFloor)
        val editTextLandmark = layoutForCurrentAddress.findViewById<TextInputEditText>(R.id.editLandmark)
        val buttonSaveAddress = layoutForCurrentAddress.findViewById<Button>(R.id.btnSaveAddress)
        val editTexteditOther = layoutForCurrentAddress.findViewById<TextInputEditText>(R.id.editOther)
        val autTvSector = layoutForCurrentAddress.findViewById<AutoCompleteTextView>(R.id.autTvSector)
        val textInputLayoutAddress = layoutForCurrentAddress.findViewById<TextInputLayout>(R.id.textInputLayoutAddress)
        val editTextEditAddress = layoutForCurrentAddress.findViewById<TextInputEditText>(R.id.editAddress)
        viewModel.sectorObservable().observe(viewLifecycleOwner, androidx.lifecycle.Observer<SectorDetailsResponse> {
           if (it.statusCode == 400) {
           }else{
               sectorList = it.SectorDetail.toMutableList()
               for (data in sectorList){
                   list.add(data.sector)
               }
           }
        })
        viewModel.apiCallForSectorDetails()

       // val countryCode = resources.getStringArray(R.array.countryCode_array)
        val arrayAdapter = ArrayAdapter((activity as AppCompatActivity),R.layout.dropdown_item,list)
        autTvSector.setAdapter(arrayAdapter)

        linearLayoutOthers = layoutForCurrentAddress.findViewById<LinearLayout>(R.id.lnrOthers)
            linearLayoutSaveAddressAsAllOption =layoutForCurrentAddress.findViewById<LinearLayout>(R.id.lnrSaveAddressAsAllOption)
            tvHome = layoutForCurrentAddress.findViewById<TextView>(R.id.tvHome)
            tvWork = layoutForCurrentAddress.findViewById<TextView>(R.id.tvWork)
            tvOther = layoutForCurrentAddress.findViewById<TextView>(R.id.tvOther)
            tvEnterLabelOthers = layoutForCurrentAddress.findViewById<TextView>(R.id.tvEnterLabelOthers)

            bottomSheetDialogForEnterAddress.setContentView(layoutForCurrentAddress)
            bottomSheetDialogForEnterAddress.show()
            bottomSheetDialogForEnterAddress.setCanceledOnTouchOutside(false)
        var selectedAddress=""
            if (getGMapCurrentAddress.length >= 40){
                selectedAddress = getGMapCurrentAddress.substring(0,40)+"..."
            }
            tvCurrency.text = selectedAddress
            clickEventsForEnterAddress()

        if (getGMapCurrentAddress ==""|| getGMapCurrentAddress.equals(null)){
            textInputLayoutAddress.visibility =View.VISIBLE
            isGMapAddress = true
        }

        buttonSaveAddress.setOnClickListener {
            houseOrFloorNo = (editTextHouseNoOrFloor.text.toString()).trim()
            landMark = (editTextLandmark.text.toString()).trim()
            sectorId = autTvSector.text.toString()
          //  Toast.makeText(context,isGMapAddress.toString(),Toast.LENGTH_SHORT).show()

            if (isGMapAddress == true){
                getGMapCurrentAddress = (editTextEditAddress.text.toString()).trim()
            }

            if (isClickOther == true){
                saveAddressAs = (editTexteditOther.text.toString()).trim()
            }
            if (getGMapCurrentAddress==""||getGMapCurrentAddress.equals(null)){
                Toast.makeText(context,resources.getString(R.string.address_is_required),Toast.LENGTH_SHORT).show()

            }else if (houseOrFloorNo.equals("") || houseOrFloorNo.equals(null)){
                Toast.makeText(context,resources.getString(R.string.house_number_or_floor_required),Toast.LENGTH_SHORT).show()
            }
           /* else if (landMark.equals("") || landMark.equals(null)){
                Toast.makeText(context,resources.getString(R.string.land_mark_is_required),Toast.LENGTH_SHORT).show()
            }*/
            else if(saveAddressAs.equals("") || saveAddressAs.equals(null)){
                Toast.makeText(context,resources.getString(R.string.please_select_save_address_as),Toast.LENGTH_SHORT).show()
            } else if(sectorId.equals("") || sectorId.equals(null)){
                Toast.makeText(context,resources.getString(R.string.sector_is_required),Toast.LENGTH_SHORT).show()
            }else{
                progressDialog.show()
                viewModel.addressObservable().observe(viewLifecycleOwner, androidx.lifecycle.Observer<AddressResponseModel> {
                    if (it.statusCode == 400){
                        progressDialog.dismiss()
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    }else{
                        progressDialog.dismiss()
                        (activity as AppCompatActivity).finish()
                        bottomSheetDialogForEnterAddress.dismiss()
                    }
                })
                viewModel.apiCallAddress(userId!!,latLong!!,getGMapCurrentAddress!!.toString(),sectorId!!,houseOrFloorNo!!,landMark!!,saveAddressAs!!)
            }
        }
    }/*this function is used to set current location changes address etc*/

    private fun clickEventsForEnterAddress(){
        tvHome.setOnClickListener {
            tvHome.setBackgroundResource(R.drawable.reactangle_border_with_green_blue)
            tvHome.setTextColor(resources.getColor(R.color.white))

            tvOther.setTextColor(resources.getColor(R.color.grey))
            tvWork.setTextColor(resources.getColor(R.color.grey))

            tvOther.setBackgroundResource(R.drawable.green_border_rectangle)
            tvWork.setBackgroundResource(R.drawable.green_border_rectangle)

            saveAddressAs ="Home"
        }

        tvWork.setOnClickListener {
            tvWork.setBackgroundResource(R.drawable.reactangle_border_with_green_blue)
            tvWork.setTextColor(resources.getColor(R.color.white))

            tvHome.setTextColor(resources.getColor(R.color.grey))
            tvOther.setTextColor(resources.getColor(R.color.grey))

            tvHome.setBackgroundResource(R.drawable.green_border_rectangle)
            tvOther.setBackgroundResource(R.drawable.green_border_rectangle)

            saveAddressAs ="Work"
        }


        tvOther.setOnClickListener {
            tvOther.setBackgroundResource(R.drawable.reactangle_border_with_green_blue)
            tvOther.setTextColor(resources.getColor(R.color.white))

            tvHome.setTextColor(resources.getColor(R.color.grey))
            tvWork.setTextColor(resources.getColor(R.color.grey))

            tvHome.setBackgroundResource(R.drawable.green_border_rectangle)
            tvWork.setBackgroundResource(R.drawable.green_border_rectangle)
            linearLayoutSaveAddressAsAllOption.visibility = View.GONE
            linearLayoutOthers.visibility = VISIBLE
            tvEnterLabelOthers.setBackgroundResource(R.drawable.reactangle_border_with_green_blue)
            tvEnterLabelOthers.setTextColor(resources.getColor(R.color.white))
            isClickOther = true
        }
        tvEnterLabelOthers.setOnClickListener {
            linearLayoutSaveAddressAsAllOption.visibility = VISIBLE
            linearLayoutOthers.visibility = View.GONE
            tvEnterLabelOthers.setBackgroundResource(R.drawable.reactangle_border_with_green_blue)
            tvEnterLabelOthers.setTextColor(resources.getColor(R.color.white))
            isClickOther = false
        }

    }


}



//https://developer.here.com/blog/geocoding-addresses-with-kotlin-and-here-in-android
//https://stackoverflow.com/questions/3897409/how-does-one-implement-drag-and-drop-for-android-marker
