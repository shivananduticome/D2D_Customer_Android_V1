package com.d2d.customer.view.fragment

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackSubscription
import com.d2d.customer.adapter.UpcomingMealsAdapter
import com.d2d.customer.adapter.SubscriptionPackagePlansAdapter
import com.d2d.customer.databinding.SubscriptionDetailsFragmentBinding
import com.d2d.customer.model.*
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.util.toBulletedList
import com.d2d.customer.view.AddFragmentToActivity
import com.d2d.customer.viewmodel.AddressViewModel
import com.d2d.customer.viewmodel.RegistrationLoginViewModel
import com.d2d.customer.viewmodel.SubscriptionDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class SubscriptionDetailsFragment : Fragment(), CallbackSubscription {
    private lateinit var viewModel: SubscriptionDetailsViewModel
    private lateinit var addressViewModel:AddressViewModel
    private lateinit var binding: SubscriptionDetailsFragmentBinding
    private lateinit var upcomingMealsAdapter: UpcomingMealsAdapter
    private  var subscriptionPackagePlansAdapter: SubscriptionPackagePlansAdapter? = null
    private  var sharedPreferences:SharedPreferences? = null
    private var userId:String? = null
    private var beforeVatPlanPrice:String? = null
    private var afterVatPlanPrice:String? = null
    private var subscriptionTitle:String? = null
    private var planStartDate:String? = null
    private var houseNoFloor:String? = null
    private var landMark:String? = null
    private var subscriptionPlanNumberOfDay:String? = null
    private var vatCalculation:Double =0.0
    private lateinit var tvBeforeVat:TextView
    private lateinit var tvVat:TextView
    private lateinit var tvAfterVat:TextView
    private var itemPrice:String? = null
    private var packagePlanSharedPreferences: SharedPreferences? = null
    private var subscriptionPlanDate:String? = null

    private  var subscriptionDescription:String? = null
    private  var subscriptionTypeId: String? = null
    private  var subscriptionImage: String? = null
    private  var subscriptionLeastAmount: String? = null
    private  var subscriptionType: String? = null
    private  var subscriptionPlanId:String? = null
    private lateinit var progressDialog:Dialog
    private var date1: Date? = null
    private var date2: Date? = null
    private var address:String? = null
    private var saveAddressAs:String? = null
    private var sectorId:String? = null
    private var  MAX_SELECTABLE_DATE_IN_FUTURE = 365

    /* for Registration*/
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var registrationLoginViewModel: RegistrationLoginViewModel
    private var  fullName:String? = null
    private var  mobileNumber:String? = null
    private var  email:String? = null
    private var  countryCode:String? = null
    private var  storeMobileNumber:String? = null
    private var  storeCountryCode:String? = null
    private var  verifyOtp:String? = null
    private lateinit  var tvInEdtFullName: TextInputEditText
    private lateinit  var tvInEdtEmail: TextInputEditText
    private lateinit  var tvRegistrationCountryCode:AutoCompleteTextView
    private lateinit  var tvInEdtRegistrationMobileNumber: TextInputEditText
    private lateinit  var  OtpTextView: OtpTextView
    private lateinit var buttonLogin:Button
    private lateinit var buttonRegistration:Button
    private lateinit var tvLoginCountryCode:AutoCompleteTextView
    private lateinit var tvInEdtLoginMobileNumber: TextInputEditText
    private lateinit var lnrRegistrationHeader:LinearLayout
    private lateinit var lnrVerifyOtpHeader:LinearLayout
    private lateinit var lnrLoginHeader:LinearLayout
    private  var otpInputSharedPreferences: SharedPreferences? = null
    private lateinit var otpLinearLayout:LinearLayout
    private lateinit var loginRelativeLayout:RelativeLayout
    private lateinit var  registrationRelativeLayout:RelativeLayout
    private lateinit var tvResentOtp:TextView
    private var registrationSharedPreferences: SharedPreferences? = null





    companion object {
        fun newInstance() = SubscriptionDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = SubscriptionDetailsFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SubscriptionDetailsViewModel::class.java]
        addressViewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        sharedPreferences = context?.getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences),Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId","")
        packagePlanSharedPreferences =context?.getSharedPreferences(context?.resources?.getString(R.string.subscription_plan_details_sharedPreferences),Context.MODE_PRIVATE)
        subscriptionDescription = packagePlanSharedPreferences?.getString("subscriptionDescription","")
        subscriptionTypeId = packagePlanSharedPreferences?.getString("subscriptionId","")
        subscriptionImage = packagePlanSharedPreferences?.getString("subscriptionImage","")
        subscriptionLeastAmount = packagePlanSharedPreferences?.getString("subscriptionLeastAmount","")
        subscriptionType = packagePlanSharedPreferences?.getString("subscriptionTitle","")
        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)
        /* for Registration*/
        registrationSharedPreferences = context?.getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        registrationLoginViewModel = ViewModelProvider(this)[RegistrationLoginViewModel::class.java]
        otpInputSharedPreferences = context?.getSharedPreferences(resources.getString(R.string.sharedPreference_otp_input), Context.MODE_PRIVATE)
        storeMobileNumber = otpInputSharedPreferences?.getString("mobileNumber","")
        storeCountryCode = otpInputSharedPreferences?.getString("countryCode","")

        if (userId ==""||userId.equals(null)){
           /* Toast.makeText(context,resources.getString(R.string.please_register),Toast.LENGTH_SHORT).show()
            activity?.finish()*/
            registrationLoginBottomDialog()
        }

        val view = binding.root
        initAdapter()
        upcomingMeals()
        mealPlans()
        fetchAddressList(userId!!)
        commonClickEventsWithSetUpUiData()
        return view
    }


    private fun initAdapter() {
        /*RecyclerView List of the Days wise food exe: Upcoming Meals*/
        val upcomingMealsRecyclerView = binding.upcomingMealsRecyclerView
        upcomingMealsRecyclerView.layoutManager = LinearLayoutManager(activity)
        upcomingMealsAdapter = UpcomingMealsAdapter()
        upcomingMealsRecyclerView.adapter = upcomingMealsAdapter
        upcomingMealsRecyclerView.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        /*Package Plan RecyclerView*/
        val packagePlansRecyclerView = binding.packagePlansRecyclerView
        packagePlansRecyclerView.layoutManager = LinearLayoutManager(activity)
        subscriptionPackagePlansAdapter = SubscriptionPackagePlansAdapter(this)
        packagePlansRecyclerView.adapter = subscriptionPackagePlansAdapter
        packagePlansRecyclerView.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        val bulletedAllergicList = listOf(resources.getString(R.string.allergy_info_contents)).toBulletedList()
        val bulletedTermsConditionList = listOf("Food will be kept at a assigned location near the entrance  everyday around 6AM","No food delivery on Fridays and Saturdays","Subscription cannot be cancelled once bought").toBulletedList()
        val bulletedHowItWorksList = listOf("Breakfast combos are delivered every day at around 6AM to the chosen address","A predefined specially curated food item will be delivered daily","Full flexibility to change subscription plan and delivery address anytime","You can auto-renew subscriptions and get the best price per meal. Subscriptions renew on the same day of every week/month from the start date").toBulletedList()


        binding.tvAllergyInfo.text = bulletedAllergicList
        binding.tvTermsAndConditions.text = bulletedTermsConditionList
        binding.tvHowItsWorks.text =bulletedHowItWorksList

    }

    override fun subscriptionType(
        subscriptionDescription: String, subscriptionId: String, subscriptionImage: String,
        subscriptionLeastAmount: String,
        subscriptionType: String) {
    }


    override fun subscriptionPlan( numberOfPlanDays: String, price: String, subscriptionType: String,
                                   subscriptionId: String) {
       //Toast.makeText(context,numberOfPlanDays,Toast.LENGTH_SHORT).show()
         subscriptionPlanNumberOfDay = numberOfPlanDays
         vatCalculation = (price.toInt())*0.05
         afterVatPlanPrice = (price.toInt()+vatCalculation).toString()
        subscriptionTitle = subscriptionType
         itemPrice = price
    }

    private fun upcomingMeals(){
     viewModel.subscriptionUpcomingMealObservable().observe(viewLifecycleOwner, Observer<SubscriptionUpcomingMealResponseModel> {
         if (it.statusCode == 400){
             Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
         }else {
             upcomingMealsAdapter.upcomingMealsList = it.upcomingMeals.toMutableList()
             upcomingMealsAdapter.notifyDataSetChanged()
         }
     })
        viewModel.apiCallUpcomingMeals(subscriptionTypeId!!)
    }

    private fun mealPlans(){
        progressDialog.show()
        viewModel.subscriptionPlanObservable().observe(viewLifecycleOwner, Observer<SubscriptionPlansResponseModel> {
             if (it.statusCode == 400){
                 progressDialog.dismiss()
                 Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
             }else{
                 progressDialog.dismiss()
                 subscriptionPackagePlansAdapter?.packageTypeList = it.subscriptionPlans.toMutableList()
                 subscriptionPackagePlansAdapter?.notifyDataSetChanged()
             }

        })
        viewModel.apiCallPlan(subscriptionTypeId!!)
    }/*get the Meals Plan like the 7 Days 15Days and 30 Days*/

    private fun fetchAddressList(userId:String){
        progressDialog.show()
            addressViewModel.addressListObservable().observe(viewLifecycleOwner, Observer<ManageAddressDetailsResponseModel> {
            if (it.statusCode == 400){
                progressDialog.dismiss()

            }else{
                progressDialog.dismiss()
                var addressList= mutableListOf<AdressDetail>()
                addressList = it.AdressDetails.toMutableList()

                for (item in addressList){
                  if (item.status){
                    sectorId = item.sectorId
                    address = item.address
                    houseNoFloor = item.houseOrFloorNo
                    landMark = item.landMark
                    saveAddressAs = item.saveAddressAs
                  }
                }
            }
        })
        addressViewModel.apiCallAddressList(userId)
    }


    private fun commonClickEventsWithSetUpUiData(){
        binding.backPressImageView.setOnClickListener {
            activity?.finish()
        }
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val lnrCalendar = binding.lnrCalendar
        val tvDate = binding.tvDate
        val dayOfSaturday = calendar.get(Calendar.SATURDAY)
        val dayOfSunday = calendar.get(Calendar.SUNDAY)
        val weekends = mutableListOf<Calendar>()
       for (i in 1..365){
           if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
           var cal = Calendar.getInstance()
               cal = calendar.clone() as Calendar
               weekends.add(cal)
           }
           calendar.add(Calendar.DATE, 1);
       }

        var calendarArray:Array<Any> = weekends.toTypedArray()

        lnrCalendar.setOnClickListener {
          /* when (dayOfWeek) {
               1 -> "Sunday"
               2 -> "Monday"
               3 -> "Tuesday"
               4 -> "Wednesday"
               5 -> "Thursday"
               6 -> "Friday"
               7 -> "Saturday"
               else -> "Time has stopped"
           }
           Toast.makeText(context,day.toString(),Toast.LENGTH_SHORT).show()
           */
          // Toast.makeText(context,dayOfSunday.toString(),Toast.LENGTH_SHORT).show()
           val datePicker = DatePickerDialog(activity as Context, DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
                 val calendarDate = (""+ dayOfMonth + "-" + (month+1) + "-" + (year-1))
                 val dates = SimpleDateFormat("dd-MM-yyyy")
                 val currentDate = dates.format(Date())
                 date1 = dates.parse(currentDate)
                 date2 = dates.parse(calendarDate)
                 val difference: Long = kotlin.math.abs(date1!!.time - date2!!.time)
                 val differenceDates = difference / (24 * 60 * 60 * 1000)
                 val dayDifference = differenceDates.toString()
                 Toast.makeText(context,currentDate.toString(),Toast.LENGTH_SHORT).show()
                if (dayDifference == "0" ||dayDifference.equals(null)){
                    tvDate.text = ""+ (dayOfMonth+1) + "-" + (month+1) + "-" + (year-1)
                    planStartDate =(""+ (year-1) + "-" + (month+1) + "-" +  (dayOfMonth+1))
                    subscriptionPlanDate =(""+  (dayOfMonth+1) + "-" + (month+1) + "-" + (year-1))
                }else{
                    tvDate.text = ""+ dayOfMonth + "-" + (month+1) + "-" + (year-1)
                    planStartDate =(""+ (year-1) + "-" + (month+1) + "-" + dayOfMonth)
                    subscriptionPlanDate =(""+ dayOfMonth + "-" + (month+1) + "-" + (year-1))
                }

             },year,month,day)
            datePicker.datePicker.minDate = calendar.timeInMillis;
            // datePicker.setDisabledDays(days)

          // Toast.makeText(context,( datePicker.datePicker.firstDayOfWeek).toString(),Toast.LENGTH_SHORT).show()

           datePicker.show()
       }/*This is the Calendar events*/
        Picasso.get().load(subscriptionImage).into(binding.imageView)
        binding.tvPlanTitle.text = subscriptionType
        binding.tvPlanName.text = subscriptionType
        binding.tvPlanDescription.text =subscriptionDescription

        binding.buttonSubscribe.setOnClickListener {
            if (subscriptionPlanNumberOfDay.equals("") || subscriptionPlanNumberOfDay.equals(null)){
               Toast.makeText(context,resources.getString(R.string.choose_subscription_plan),Toast.LENGTH_LONG).show()
            }else if (planStartDate.equals("") || planStartDate.equals(null)) {
                Toast.makeText(context,resources.getString(R.string.select_plan_start_date),Toast.LENGTH_LONG).show()
            }else{
                subscriptionBottomSheetDialog()
            }
        }/*Click events for the Subscription*/
    }




    @SuppressLint("SetTextI18n")
    private fun subscriptionBottomSheetDialog(){
        val subscriptionBottomSheetDialogLayout = layoutInflater.inflate(R.layout.bottom_dialog_subscription,null)
            val tvAddress = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvAddress)
            val tVHouseNoOrFloor = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tVHouseNoOrFloor)
            val tvSector = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvSector)
            val btnPay = subscriptionBottomSheetDialogLayout.findViewById<Button>(R.id.btnFloatPay)
            val tvPlanPrice = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvPlanPrice)
            val tvSubscriptionType = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvSubscriptionType)
            val tvPlans = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvPlans)
            val tvDeliveryCharge = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvDeliveryCharge)
            val tvPlanDate = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvPlaneDate)
            val termsAndConditionCheckBox = subscriptionBottomSheetDialogLayout.findViewById<CheckBox>(R.id.termsAndConditionCheckBox)
            val tvShippingDetails = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvShippingDetails)
            val tvaArrowShippingDetails = subscriptionBottomSheetDialogLayout.findViewById<TextView>(R.id.tvaArrowShippingDetails)
            val cardView = subscriptionBottomSheetDialogLayout.findViewById<CardView>(R.id.cardView)
               tvBeforeVat = subscriptionBottomSheetDialogLayout.findViewById<EditText>(R.id.beforeVatTotal)
               tvVat = subscriptionBottomSheetDialogLayout.findViewById<EditText>(R.id.tvVatTotal)
               tvAfterVat = subscriptionBottomSheetDialogLayout.findViewById<EditText>(R.id.tvAfterVatTotal)

               if (houseNoFloor.equals("") || houseNoFloor.equals(null)||landMark.equals("") || landMark.equals(null) || address.equals("")|| address.equals(null)) {
                cardView.visibility=View.GONE
                tvShippingDetails.text = resources.getText(R.string.change_address)
                 }else{
                 cardView.visibility= View.VISIBLE
                }
        // cardView.visibility = View.VISIBLE
        // tvShippingDetails.text = context?.resources?.getString(R.string.change_address)
               tvAddress.text = address
               tVHouseNoOrFloor.text = houseNoFloor
               tvSector.text = sectorId
               tvPlanPrice.text = resources.getString(R.string.aed)+" "+ itemPrice
               tvSubscriptionType.text = subscriptionTitle
               tvBeforeVat.text = resources.getString(R.string.aed)+" "+ itemPrice
               tvDeliveryCharge.text = resources.getString(R.string.aed)+" "+ "00"
               tvPlans.text =subscriptionPlanNumberOfDay +" "+ resources.getString(R.string.days_plan)
               tvPlanDate.text = resources.getString(R.string.plan_start_date)+" "+subscriptionPlanDate
               tvVat.text =resources.getString(R.string.aed)+" "+  vatCalculation.toString()
               tvAfterVat.text =resources.getString(R.string.aed)+" "+  afterVatPlanPrice
               btnPay.text =resources.getString(R.string.pay_aed)+" "+afterVatPlanPrice


            val bottomDialog = BottomSheetDialog(activity as AppCompatActivity)
                bottomDialog.setContentView(subscriptionBottomSheetDialogLayout)
                bottomDialog.show()

        tvaArrowShippingDetails.setOnClickListener {
            val intent =Intent(context,AddFragmentToActivity::class.java)
                intent.putExtra("FragmentName","AddressFragment")
                startActivity(intent)
                activity?.finish()
        }

        termsAndConditionCheckBox.setOnClickListener {
            if (termsAndConditionCheckBox.isChecked){
                btnPay.isEnabled = true
                btnPay.setTextColor(resources.getColor(R.color.white))
                btnPay.setBackgroundColor(resources.getColor(R.color.d2d_color))
            }else{
                btnPay.isEnabled = false
                btnPay.setBackgroundColor(resources.getColor(R.color.greyLight))
            }
        }

        btnPay.setOnClickListener {
            if (houseNoFloor.equals("") || houseNoFloor.equals(null)||landMark.equals("") || landMark.equals(null)
                || address.equals("")|| address.equals(null)) {
                Toast.makeText(context, resources.getString(R.string.provide_shipping_details), Toast.LENGTH_LONG).show()
            }else {
                progressDialog.show()
                viewModel.placeSubscriptionObservable()
                    .observe(viewLifecycleOwner, Observer<OrderPlacedResponse> {
                        if (it.statusCode == 400) {
                            progressDialog.dismiss()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        } else {
                            progressDialog.dismiss()
                           // Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            activity?.finish()
                        }
                    })

                viewModel.apiCallPlaceSubscriptionPlan(userId!!, sectorId!!,address!!, houseNoFloor!!,saveAddressAs!!,landMark!!, afterVatPlanPrice!!,
                                                        "Subscription", subscriptionPlanNumberOfDay!!, planStartDate!!, subscriptionTitle!!,subscriptionTypeId!!)
            }
        }

    }



    private fun registrationLoginBottomDialog(){
        val bottomLayout = layoutInflater.inflate(R.layout.bottom_dialog_login_gistration_otp,null)
        lnrRegistrationHeader = bottomLayout.findViewById(R.id.lnrRegistrationHeader)
        lnrLoginHeader = bottomLayout.findViewById(R.id.lnrLoginHeader)
        lnrVerifyOtpHeader = bottomLayout.findViewById(R.id.lnrVerifyOtpHeader)
        val bottomSheetDialog = BottomSheetDialog(activity as AppCompatActivity)
        val lnrLoginHeader = bottomLayout.findViewById<LinearLayout>(R.id.lnrLoginHeader)
        val lnrRegistrationHeader = bottomLayout.findViewById<LinearLayout>(R.id.lnrRegistrationHeader)
        val loginView = bottomLayout.findViewById<View>(R.id.viewLineLogin)
        val registrationView = bottomLayout.findViewById<View>(R.id.viewLineRegister)
        loginRelativeLayout = bottomLayout.findViewById<RelativeLayout>(R.id.rltLogin)
        registrationRelativeLayout = bottomLayout.findViewById<RelativeLayout>(R.id.rltRegistration)
        buttonLogin=bottomLayout.findViewById<Button>(R.id.btnLogin)
        buttonRegistration=bottomLayout.findViewById<Button>(R.id.btnRegistration)
        val buttonVerifyOtp = bottomLayout.findViewById<Button>(R.id.btnVerifyOtp)
        otpLinearLayout = bottomLayout.findViewById<LinearLayout>(R.id.lnrOtp)
        tvResentOtp = bottomLayout.findViewById<TextView>(R.id.tvResentOtp)

        tvLoginCountryCode = bottomLayout.findViewById<AutoCompleteTextView>(R.id.loginCountryCode)
        tvInEdtLoginMobileNumber = bottomLayout.findViewById<TextInputEditText>(R.id.loginMobileNumber)

        tvInEdtFullName = bottomLayout.findViewById<TextInputEditText>(R.id.fullName)
        tvInEdtEmail = bottomLayout.findViewById<TextInputEditText>(R.id.email)
        tvRegistrationCountryCode = bottomLayout.findViewById<AutoCompleteTextView>(R.id.registrationCountryCode)
        tvInEdtRegistrationMobileNumber = bottomLayout.findViewById<TextInputEditText>(R.id.registrationMobileNumber)
        tvLoginCountryCode.setText("971")
        tvRegistrationCountryCode.setText("971")
        OtpTextView = bottomLayout.findViewById<OtpTextView>(R.id.tvOtp)

        inputValidation()/*This function is used to input validation*/


        var otpview = OtpTextView
        otpview?.requestFocusOTP()
        otpview?.otpListener = object : OTPListener {
            override fun onInteractionListener() {
            }
            override fun onOTPComplete(otp: String) {
                verifyOtp = otp
            }
        }/*this function is used to get the enter verify otp*/


        lnrLoginHeader.setOnClickListener {
            loginView.visibility = View.VISIBLE
            registrationView.visibility = View.GONE
            registrationView.visibility = View.GONE
            loginRelativeLayout.visibility = View.VISIBLE
            registrationRelativeLayout.visibility = View.GONE
            otpLinearLayout.visibility = View.GONE

        }
        lnrRegistrationHeader.setOnClickListener {
            loginView.visibility = View.GONE
            registrationView.visibility = View.VISIBLE
            registrationView.visibility = View.VISIBLE
            loginRelativeLayout.visibility = View.GONE
            registrationRelativeLayout.visibility = View.VISIBLE
            otpLinearLayout.visibility = View.GONE
        }/**/


        buttonLogin.setOnClickListener {
            countryCode = tvLoginCountryCode.text.toString()
            mobileNumber = tvInEdtLoginMobileNumber.text.toString()
            inputValidation()
            if (countryCode.equals("")|| countryCode.equals(null)){
                Toast.makeText(context,resources.getString(R.string.country_code_is_required),Toast.LENGTH_SHORT).show()
            }
            else if(mobileNumber.equals("") || mobileNumber.equals(null)){
                Toast.makeText(context,resources.getString(R.string.mobile_number_is_required),Toast.LENGTH_SHORT).show()
            }else if (mobileNumber!!.length < 9){
                Toast.makeText(context,resources.getString(R.string.please_provide_the_valid_phone_number),Toast.LENGTH_SHORT).show()
            }else {
                userLogin(countryCode!!, mobileNumber!!,false)/*this method is used to call the user login*/
            }
        }/*button for user Login*/


        buttonRegistration.setOnClickListener {
            fullName =tvInEdtFullName.text.toString()
            mobileNumber = tvInEdtRegistrationMobileNumber.text.toString()
            email = tvInEdtEmail.text.toString()
            countryCode = tvRegistrationCountryCode.text.toString()
            inputValidation()/*This function is used to input validation*/
            if (fullName.equals("") || fullName.equals(null)){
                Toast.makeText(context,resources.getString(R.string.full_name_is_required),Toast.LENGTH_SHORT).show()
            }else if (countryCode.equals("")|| countryCode.equals(null)){
                Toast.makeText(context,resources.getString(R.string.country_code_is_required),Toast.LENGTH_SHORT).show()
            }
            else if (mobileNumber.equals("") || mobileNumber.equals(null)){
                Toast.makeText(context,resources.getString(R.string.mobile_number_is_required),Toast.LENGTH_SHORT).show()

            }else {
                userRegistration(fullName!!, mobileNumber!!, email!!, countryCode!!)
            }
        }/*User Registration*/


        buttonVerifyOtp.setOnClickListener {
            if (verifyOtp.equals("")||verifyOtp.equals(null)){
                Toast.makeText(context,"Please Enter Otp ",Toast.LENGTH_SHORT).show()
            }else {
                verifyOtp(storeCountryCode!!, storeMobileNumber!!, verifyOtp!!, bottomSheetDialog)
            }
        }/*click button to otp verification*/

        val countryCode = resources.getStringArray(R.array.countryCode_array)
        val arrayAdapter = ArrayAdapter((activity as AppCompatActivity),R.layout.dropdown_item,countryCode)
        tvLoginCountryCode.setAdapter(arrayAdapter)
        tvRegistrationCountryCode.setAdapter(arrayAdapter)
        /*   tvLoginCountryCode.setText("971")
           tvRegistrationCountryCode.setText("971")*/

        bottomSheetDialog.setContentView(bottomLayout)
        bottomSheetDialog.show()
        bottomSheetDialog.setCanceledOnTouchOutside(false)

    }

    private fun inputValidation(){
        tvInEdtFullName.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                //tvInEdtFullName.error = resources.getString(R.string.full_name_is_required)
            }
        }

        tvInEdtEmail.doOnTextChanged { text, start, before, count ->
            if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(text!!).matches())){
                tvInEdtEmail.error = resources.getString(R.string.provide_valid_email_id)
            }
        }

        tvRegistrationCountryCode.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                //tvRegistrationCountryCode.error = resources.getString(R.string.country_code_is_required)
            }
        }

        tvInEdtRegistrationMobileNumber.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 9){
                buttonRegistration.isEnabled = false
            }else{
                buttonRegistration.isEnabled = true
            }
        }

        tvLoginCountryCode.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                // tvLoginCountryCode.error = resources.getString(R.string.country_code_is_required)
            }
        }

        tvInEdtLoginMobileNumber.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 9){
                buttonLogin.isEnabled = false
            }else{
                buttonLogin.isEnabled = true
            }
        }
    }

    private fun userRegistration(fullName:String, mobileNo:String, email:String, countryCode:String){
        progressDialog.show()
        registrationLoginViewModel.registrationObservable().observe(viewLifecycleOwner, Observer<UserRegistrationResponseModel> {
            if (it.statusCode == 400){
                progressDialog.dismiss()
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()

            }else{
                try {
                    progressDialog.dismiss()
                    editor = otpInputSharedPreferences!!.edit()
                    editor.putString("mobileNumber",it.RegisterData.mobileNo)
                    editor.putString("countryCode",it.RegisterData.countryCode)
                    editor.commit()
                    //Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    storeMobileNumber = otpInputSharedPreferences?.getString("mobileNumber","")
                    storeCountryCode = otpInputSharedPreferences?.getString("countryCode","")
                    otpLinearLayout.visibility = View.VISIBLE
                    lnrRegistrationHeader.visibility = View.GONE
                    lnrLoginHeader.visibility = View.GONE
                    lnrVerifyOtpHeader.visibility = View.VISIBLE
                    loginRelativeLayout.visibility = View.GONE
                    registrationRelativeLayout.visibility = View.GONE
                    reSendOTP()
                }catch (e:Exception){
                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
                }

            }
        })
        registrationLoginViewModel.apiCallRegistration(fullName,mobileNo,email,countryCode)
    }/*This method is used api call for the user Registration*/

    private fun verifyOtp(countryCode:String,mobileNo:String,mobileOtp:String,bottomSheetDialog:BottomSheetDialog){
        progressDialog.show()
        registrationLoginViewModel.optVerifyObservable().observe(viewLifecycleOwner, Observer<VerifyOtpResponseModel> {
            if (it.statusCode == 400){
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }else{
              try {
                  Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                  progressDialog.dismiss()
                  editor = registrationSharedPreferences!!.edit()
                  editor.putString("fullName",it.UserOtpDetails.fullName)
                  editor.putString("mobileNumber",it.UserOtpDetails.mobileNo)
                  editor.putString("email",it.UserOtpDetails.email)
                  editor.putString("countryCode",it.UserOtpDetails.countryCode)
                  editor.putString("userId",it.UserOtpDetails.userId)
                  editor.commit()
                  storeMobileNumber = registrationSharedPreferences?.getString("mobileNumber","")
                  storeCountryCode = registrationSharedPreferences?.getString("countryCode","")
                  userId = registrationSharedPreferences?.getString("userId","")
                  bottomSheetDialog.dismiss()
                  activity?.finish()
              }catch (e:Exception){
                  Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
              }
            }
        })
        registrationLoginViewModel.apiCallOtpVerify(countryCode,mobileNo,mobileOtp)
        //Toast.makeText(context,countryCode+" "+mobileNo+" "+mobileOtp,Toast.LENGTH_SHORT).show()

    }/*this method is used to verify otp*/

    private fun userLogin(countryCode:String, mobileNo:String, loginResendOTP:Boolean){

        progressDialog.show()
        registrationLoginViewModel.userLoginObservable().observe(viewLifecycleOwner, Observer<UserLoginResponseModel> {
            if (it.statusCode == 400) {
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }else{
               try {
                   if (!loginResendOTP) {
                       progressDialog.dismiss()
                       //Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                       editor = otpInputSharedPreferences!!.edit()
                       editor.putString("mobileNumber", it.LoginDetails.mobileNo)
                       editor.putString("countryCode", it.LoginDetails.countryCode)
                       editor.commit()
                       // Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                       storeMobileNumber = otpInputSharedPreferences?.getString("mobileNumber", "")
                       storeCountryCode = otpInputSharedPreferences?.getString("countryCode", "")
                       otpLinearLayout.visibility = View.VISIBLE
                       lnrRegistrationHeader.visibility = View.GONE
                       lnrLoginHeader.visibility = View.GONE
                       lnrVerifyOtpHeader.visibility = View.VISIBLE
                       loginRelativeLayout.visibility = View.GONE
                       registrationRelativeLayout.visibility = View.GONE
                       reSendOTP()
                   }
               }catch (e:Exception){
                   Toast.makeText(context, e.printStackTrace().toString(), Toast.LENGTH_SHORT).show()
               }
            }
        })
        registrationLoginViewModel.apiCallUserLogin(countryCode,mobileNo)
    }

    private fun reSendOTP(){
        object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvResentOtp.text = context?.resources?.getString(R.string.resend_otp_in) +" "+ millisUntilFinished / 1000
                tvResentOtp.setOnClickListener {
                    tvResentOtp.isEnabled = false
                }
            }
            override fun onFinish() {
                tvResentOtp.text = context?.resources?.getString(R.string.resend_otp)
                tvResentOtp.isEnabled = true
                tvResentOtp.setOnClickListener {
                    userLogin(countryCode!!, mobileNumber!!,true)
                }

            }
        }.start()
    }/*Resend OPT function*/

}