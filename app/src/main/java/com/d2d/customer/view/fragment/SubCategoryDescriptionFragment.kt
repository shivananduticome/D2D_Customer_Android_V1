package com.d2d.customer.view.fragment

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BulletSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackSubCategoryDescription
import com.d2d.customer.adapter.AdapterSubCategoryDescription
import com.d2d.customer.adapter.AddOnsAdapter
import com.d2d.customer.databinding.SubCategoryDescriptionFragmentBinding
import com.d2d.customer.model.*
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.util.toBulletedList
import com.d2d.customer.viewmodel.RegistrationLoginViewModel
import com.d2d.customer.viewmodel.SubCategoryDescriptionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import java.util.*

class SubCategoryDescriptionFragment : Fragment(), CallbackSubCategoryDescription {
    private lateinit var binding: SubCategoryDescriptionFragmentBinding
    private lateinit var viewModel: SubCategoryDescriptionViewModel
    private lateinit var adapterSubCategoryDescription: AdapterSubCategoryDescription
    private lateinit var addOnsAdapter: AddOnsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var mainCategoryId: String? = null
    private var subCategoryId: String? = null
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listView: ListView
    private var subCategoryName: String? = null
    private var menuItemList = mutableListOf<SubCategoryMenuData>()
    private lateinit var tvAddToCart: TextView
    private var registrationSharedPreferences: SharedPreferences? = null
    private var userId: String? = null
    private var itemTotal: String? = null
    private var addOnPrice: String? = null
    private lateinit var tvItemTotalPrice: TextView
    private var itemBasePrice: String? = null
    private lateinit var dialogAddToCart:BottomSheetDialog
    private lateinit var dialogAddOns:BottomSheetDialog

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
    private var vegOrNonVeg:String?= null
    private lateinit var progressDialog:Dialog
    private lateinit var lnrRegistrationHeader:LinearLayout
    private lateinit var lnrVerifyOtpHeader:LinearLayout
    private lateinit var lnrLoginHeader:LinearLayout
    private lateinit var addToCartPostData: AddToCartPostData
    private var addOnsList = mutableListOf<AddOnSelections>()
    private lateinit var addOnSelectionsList: AddOnSelections
    private  var otpInputSharedPreferences: SharedPreferences? = null
    private lateinit var otpLinearLayout:LinearLayout
    private lateinit var loginRelativeLayout:RelativeLayout
    private lateinit var  registrationRelativeLayout:RelativeLayout
    private lateinit var tvResentOtp:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SubCategoryDescriptionFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SubCategoryDescriptionViewModel::class.java)
        registrationLoginViewModel = ViewModelProvider(this).get(RegistrationLoginViewModel::class.java)
        sharedPreferences = (activity as AppCompatActivity).getSharedPreferences(resources.getString(R.string.subCategory_sharedPreference_data), Context.MODE_PRIVATE)
        registrationSharedPreferences = context?.getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        otpInputSharedPreferences = context?.getSharedPreferences(resources.getString(R.string.sharedPreference_otp_input), Context.MODE_PRIVATE)

        userId = registrationSharedPreferences?.getString("userId", "")
        mainCategoryId = sharedPreferences.getString("mainCategoryId", "")
        subCategoryId = sharedPreferences.getString("subCategoryId", "")
        subCategoryName = sharedPreferences.getString("subCategoryName", "")
        binding.tvSubCategoryName.text = subCategoryName

        storeMobileNumber = registrationSharedPreferences?.getString("mobileNumber","")
        storeCountryCode = registrationSharedPreferences?.getString("countryCode","")

        storeMobileNumber = otpInputSharedPreferences?.getString("mobileNumber","")
        storeCountryCode = otpInputSharedPreferences?.getString("countryCode","")

        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)


        val view: View = binding.root
        initAdapter()
        getSuCategoryRelatedMenuData(mainCategoryId!!, subCategoryId!!)
        clickEvents()
        return view
    }

    private fun initAdapter() {
        val recyclerView = binding.CategoryDescriptionRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapterSubCategoryDescription = AdapterSubCategoryDescription(this)
        recyclerView.adapter = adapterSubCategoryDescription

    }

    private fun getSuCategoryRelatedMenuData(mainCategoryId: String, subCategoryId: String) {
        progressDialog.show()
        viewModel.subCategoryRelatedMenuDetailsObservable()
            .observe(viewLifecycleOwner, Observer<MenuDetailsResponseDataModel> {
                if (it.statusCode == 400) {
                    progressDialog.dismiss()
                    val tvErrorMessage = binding.tvErrorMessage
                    tvErrorMessage.visibility = View.VISIBLE
                    tvErrorMessage.text = it.message
                    Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                } else {
                    progressDialog.dismiss()
                    binding.rltSearchAndSwitch.visibility = View.VISIBLE
                    adapterSubCategoryDescription.menuList = it.subCategoryMenuData.toMutableList()
                    menuItemList = it.subCategoryMenuData.toMutableList()
                    //Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    adapterSubCategoryDescription.notifyDataSetChanged()

                }
            })
        viewModel.apiCallToGetSubCategoryRelatedMenuDetails(mainCategoryId, subCategoryId)
    }


    private fun clickEvents() {
        binding.tvLeftArrow.setOnClickListener {
            activity?.finish()
        }/*Click on Press the Left Arrow and go back to previous page*/

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // search(query)
                //  Toast.makeText(context,query,Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                // search(newText)
                //Toast.makeText(context,searchText,Toast.LENGTH_LONG).show()
                searchFilter(searchText)
                return true
            }
        })/*getting the Search Text*/



        binding.vegSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                getVegMenuRelatedData(mainCategoryId!!, subCategoryId!!)
            } else {
                getSuCategoryRelatedMenuData(mainCategoryId!!, subCategoryId!!)
            }
        }/*Veg Switch*/
    }


    /*search filter*/
    private fun searchFilter(searchText: String) {
        val filteredMenuList = mutableListOf<SubCategoryMenuData>()
        for (data in menuItemList) {
            if (data.itemName.toLowerCase(Locale.getDefault()).contains(searchText) ||
                data.itemDescription.toLowerCase(Locale.getDefault()).contains(searchText)) {
                filteredMenuList.add(data)
                adapterSubCategoryDescription.menuList = filteredMenuList
                adapterSubCategoryDescription.notifyDataSetChanged()
            }
        }
    }/*get filter the Search Related textview*/


    private fun getVegMenuRelatedData(mainCategoryId: String, subCategoryId: String, ) {
        progressDialog.show()
        viewModel.getVegMenuObservable()
            .observe(viewLifecycleOwner, Observer<MenuDetailsResponseDataModel> {
                if (it.statusCode == 400) {
                    progressDialog.dismiss()
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                } else {
                    progressDialog.dismiss()
                    adapterSubCategoryDescription.menuList = it.subCategoryMenuData.toMutableList()
                    menuItemList = it.subCategoryMenuData.toMutableList()
                    adapterSubCategoryDescription.notifyDataSetChanged()
                }
            })
        viewModel.apiCallVegMenu(mainCategoryId, subCategoryId)
    }/*get Veg Menu related data*/



    override fun addToCartData(itemName: String, itemDescription: String, itemPrice: String, itemMainCategoryName: String, itemSubCategoryName: String,
                               itemFoodType: Boolean, itemBaseQuantity: Int, itemId: String, itemImageUrl: String) {
        val addToCartSheet = layoutInflater.inflate(R.layout.bottom_dialog_add_to_cart, null)
        val vegOrNonVegImageView = addToCartSheet.findViewById<ImageView>(R.id.vegOrNonVegImageView)
        val tvItemName = addToCartSheet.findViewById<TextView>(R.id.tvItemName)
        val tvItemDescription = addToCartSheet.findViewById<TextView>(R.id.tvDescription)
        val tvAllergyInfo = addToCartSheet.findViewById<TextView>(R.id.tvAllergyInfo)
        val tvItemPrice = addToCartSheet.findViewById<TextView>(R.id.tvPrice)
        val buttonAddToCart = addToCartSheet.findViewById<Button>(R.id.buttonAddToCart)
        val imageView = addToCartSheet.findViewById<ImageView>(R.id.imageView)
        val bulletedList = listOf(resources.getString(R.string.allergy_info_contents)).toBulletedList()
         tvAllergyInfo.text = bulletedList
        if (itemFoodType) {
            vegOrNonVegImageView.setBackgroundResource(R.drawable.veg)
             } else {
             vegOrNonVegImageView.setBackgroundResource(R.drawable.non_veg)
          }

        tvItemName.text = itemName
        tvItemDescription.text = itemDescription
        tvItemPrice.text = resources.getString(R.string.aed) + " " + itemPrice


        if (itemImageUrl =="" || itemImageUrl.equals(null)){
            imageView.setBackgroundResource(R.drawable.subcategory)
        }else{
            Picasso.get().load(itemImageUrl).into(imageView)
        }

        dialogAddToCart = BottomSheetDialog(activity as AppCompatActivity)
        dialogAddToCart.setContentView(addToCartSheet)
        dialogAddToCart.show()

        buttonAddToCart.setOnClickListener {
                getAddOnDialog(itemMainCategoryName, itemSubCategoryName, itemFoodType, itemName, itemId, itemBaseQuantity, itemPrice, itemImageUrl, itemDescription)
                // addToCartPostData(itemMainCategoryName, itemSubCategoryName, itemFoodType, itemName, itemId, itemBaseQuantity, itemPrice, itemImageUrl, itemDescription, dialog)
        }
    }
    

    override fun getSelectedAddOnsValue(amount: String) {
        addOnPrice = amount
        tvItemTotalPrice.text = resources.getString(R.string.aed) + " " + (itemBasePrice!!.toInt() + amount.toInt()).toString()
    }/*to get the selected add-ons amount*/

    override fun addOnsList(addOnSelectList:MutableList<AddOnItem>) {
       // addOnsList.clear()
        for (data in addOnSelectList){
            addOnSelectionsList = AddOnSelections(data.addOnTitleId,data.addOnName,data.amount,
            data.itemId,data.addOnId,data.status,data.type,data.addOnBaseQuantity)
            addOnsList.addAll(listOf(addOnSelectionsList))

        }
    }

    override fun unCheckRemoveAddons(addOnId: String) {
      try {
          val iterator = addOnsList.iterator()
             while (iterator.hasNext()){
                 val item = iterator.next()
                 if (item.addOnId == addOnId){
                    // Toast.makeText(context,item.addOnId.toString()+"="+addOnId,Toast.LENGTH_SHORT).show()
                     iterator.remove()
                     Toast.makeText(context,addOnsList.size.toString(),Toast.LENGTH_SHORT).show()
                 }
             }
         //Toast.makeText(context,addOnsList.size.toString(),Toast.LENGTH_SHORT).show()
      }catch (e:java.lang.Exception){
        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()

      }

    }

    override fun userUserRegistration() {
        registrationLoginBottomDialog()
    }/*to check the user user register or not*/


    private fun getAddOnDialog(itemMainCategoryName: String, itemSubCategoryName: String, itemFoodType: Boolean, itemName: String, itemId: String, itemBaseQuantity: Int,
                               itemPrice: String, itemImageUrl: String, itemDescription: String){
        val addOnsSheet = layoutInflater.inflate(R.layout.bottom_dialog_addons, null)
        val recyclerView = addOnsSheet.findViewById<RecyclerView>(R.id.addonsRecyclerview)
        val tvItemName = addOnsSheet.findViewById<TextView>(R.id.tvItemName)
        tvItemTotalPrice = addOnsSheet.findViewById<TextView>(R.id.tvItemPrice)
        tvAddToCart = addOnsSheet.findViewById<TextView>(R.id.tvAddToCart)
        tvItemName.text = itemName
        itemBasePrice = itemPrice
        tvItemTotalPrice.text = resources.getString(R.string.aed) + " " + itemPrice
        fetchAddonsData(itemId)/*fetch the Addons Methods data*/
        dialogAddOns = BottomSheetDialog(activity as AppCompatActivity)
        addOnsAdapter = AddOnsAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = addOnsAdapter
        dialogAddOns.setContentView(addOnsSheet)
        dialogAddOns.show()

        tvAddToCart.setOnClickListener {
            if (addOnPrice.equals("") || addOnPrice.equals(null)) {
                itemTotal = itemPrice
            } else {
                itemTotal = (itemPrice.toInt() + addOnPrice!!.toInt()).toString()
              //  Toast.makeText(context,itemTotal.toString(),Toast.LENGTH_LONG).show()
            }
            if (addOnsList.size == 0){

                Toast.makeText(context,"Selection is Required!!",Toast.LENGTH_LONG).show()
            }else {
                addToCartPostData(itemMainCategoryName, itemSubCategoryName, itemFoodType, itemName, itemId, itemBaseQuantity, itemTotal!!, itemImageUrl, itemDescription)
            }
        }
    }


    private fun fetchAddonsData(itemId:String){
        progressDialog.show()
        viewModel.addOnsObservable().observe(viewLifecycleOwner, Observer<AddOnsDataModel> {
            if (it.statusCode == 400) {
                progressDialog.dismiss()
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            } else {
                progressDialog.dismiss()
               // Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                addOnsAdapter.addOnsList = it.AddOnDetails.toMutableList()
                addOnsAdapter.notifyDataSetChanged()
            }
        })
        viewModel.apiCallAddons(itemId)
    }/*Fetch the addOns data*/


    private fun addToCartPostData(itemMainCategoryName: String, itemSubCategoryName: String, itemFoodType: Boolean, itemName: String, itemId: String, itemBaseQuantity: Int,
                                  itemPrice: String, itemImageUrl: String, itemDescription: String) {
        progressDialog.show()

        viewModel.addToCartObservable().observe(viewLifecycleOwner, Observer<CommonResponse> {
                if (it.statusCode == 400) {
                    progressDialog.dismiss()
                    addOnPrice = null
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                } else if (it.statusCode == 200) {
                    progressDialog.dismiss()
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    addOnPrice = null
                    dialogAddToCart.dismiss()
                    dialogAddOns.dismiss()
                    activity?.finish()
                }
            })
       // addOnsList.distinct()
        val distinctAddOnList = addOnsList.distinct().toList()
        addToCartPostData = AddToCartPostData(itemMainCategoryName, itemSubCategoryName, itemFoodType, itemName, itemId,itemBaseQuantity, itemPrice, itemImageUrl, itemDescription, userId!!, itemPrice,distinctAddOnList)
        viewModel.apiCallAddToCart(addToCartPostData)
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
        registrationLoginViewModel.registrationObservable().observe(this, Observer<UserRegistrationResponseModel> {
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
        registrationLoginViewModel.optVerifyObservable().observe(this, Observer<VerifyOtpResponseModel> {
            if (it.statusCode == 400){
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }else{
               // Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
               /* val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)*/
                progressDialog.dismiss()
                editor = registrationSharedPreferences!!.edit()
                editor.putString("fullName",it.UserOtpDetails.fullName)
                editor.putString("mobileNumber",it.UserOtpDetails.mobileNo)
                editor.putString("email",it.UserOtpDetails.email)
                editor.putString("countryCode",it.UserOtpDetails.countryCode)
                editor.putString("userId",it.UserOtpDetails.userId)
                editor.commit()
                //Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                storeMobileNumber = registrationSharedPreferences?.getString("mobileNumber","")
                storeCountryCode = registrationSharedPreferences?.getString("countryCode","")
                userId = registrationSharedPreferences?.getString("userId","")
                bottomSheetDialog.dismiss()
            }
        })
        registrationLoginViewModel.apiCallOtpVerify(countryCode,mobileNo,mobileOtp)
    }/*this method is used to verify otp*/

    private fun userLogin(countryCode:String, mobileNo:String, loginResendOTP:Boolean){
        progressDialog.show()
        registrationLoginViewModel.userLoginObservable().observe(this, Observer<UserLoginResponseModel> {
            if (it.statusCode == 400) {
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }else{
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



