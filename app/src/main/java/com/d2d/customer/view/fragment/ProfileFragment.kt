package com.d2d.customer.view.fragment

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.d2d.customer.R
import com.d2d.customer.databinding.FragmentProfileBinding
import com.d2d.customer.model.UserLoginResponseModel
import com.d2d.customer.model.UserRegistrationResponseModel
import com.d2d.customer.model.VerifyOtpResponseModel
import com.d2d.customer.view.AddFragmentToActivity
import com.d2d.customer.viewmodel.ProfileViewModel
import com.d2d.customer.viewmodel.RegistrationLoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private lateinit var registrationSharedPreferences: SharedPreferences
    private var fullName: String? = null
    private var mobileNumber: String? = null
    private var email: String? = null
    private var userId: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var registrationLoginViewModel: RegistrationLoginViewModel
    private var countryCode: String? = null
    private var storeMobileNumber: String? = null
    private var storeCountryCode: String? = null
    private var verifyOtp: String? = null
    private lateinit var tvInEdtFullName: TextInputEditText
    private lateinit var tvInEdtEmail: TextInputEditText
    private lateinit var tvRegistrationCountryCode: AutoCompleteTextView
    private lateinit var tvInEdtRegistrationMobileNumber: TextInputEditText
    private lateinit var OtpTextView: OtpTextView
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegistration: Button
    private lateinit var tvLoginCountryCode: AutoCompleteTextView
    private lateinit var tvInEdtLoginMobileNumber: TextInputEditText
    private lateinit var lnrRegistrationHeader: LinearLayout
    private lateinit var lnrVerifyOtpHeader: LinearLayout
    private lateinit var lnrLoginHeader: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        registrationLoginViewModel =
            ViewModelProvider(this).get(RegistrationLoginViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        registrationSharedPreferences = (activity as AppCompatActivity).getSharedPreferences(
            resources.getString(R.string.registration_details_sharedPreferences),
            Context.MODE_PRIVATE
        )
        editor = registrationSharedPreferences!!.edit()
        fullName = registrationSharedPreferences.getString("fullName", "")
        mobileNumber = registrationSharedPreferences.getString("mobileNumber", "")
        email = registrationSharedPreferences.getString("email", "")
        userId = registrationSharedPreferences.getString("userId", "")
        storeMobileNumber = registrationSharedPreferences?.getString("mobileNumber", "")
        storeCountryCode = registrationSharedPreferences?.getString("countryCode", "")
        val view: View = binding.root
        toCheckUserRegisterOrNot(userId)
        setDataToUI()
        clickEvent()
        //(activity as AppCompatActivity).supportActionBar?.hide()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toCheckUserRegisterOrNot(userID: String?) {
        if (userID.equals("") || userID.equals(null)) {
            Toast.makeText(context, "Before Add to Cart Please Register!!", Toast.LENGTH_LONG)
                .show()
            /* val intent = Intent(context, RegistrationLoginVerifyOtpActivity::class.java)
            context?.startActivity(intent)*/
            // registrationLoginBottomDialog()
        }
    }

    private fun clickEvent() {

        binding.lnrOrderHistory.setOnClickListener {
            val intent = Intent(context, AddFragmentToActivity::class.java)
            intent.putExtra("FragmentName", "OrderHistoryFragment")
            startActivity(intent)
        }

        binding.lnrAddress.setOnClickListener {
            val intent = Intent(context, AddFragmentToActivity::class.java)
            intent.putExtra("FragmentName", "AddressFragment")
            startActivity(intent)
        }

        binding.imageViewLogout.setOnClickListener {
            // registrationLoginBottomDialog()
            alertDialog()

        }
    }

    private fun setDataToUI() {
        binding.tvName.setText(fullName)
        binding.tvMobileNumber.setText(mobileNumber)
        binding.tvEmail.setText(email)
    }

    private fun registrationLoginBottomDialog() {
        val bottomLayout = layoutInflater.inflate(R.layout.bottom_dialog_login_gistration_otp, null)
        lnrRegistrationHeader = bottomLayout.findViewById(R.id.lnrRegistrationHeader)
        lnrLoginHeader = bottomLayout.findViewById(R.id.lnrLoginHeader)
        lnrVerifyOtpHeader = bottomLayout.findViewById(R.id.lnrVerifyOtpHeader)
        val bottomSheetDialog = BottomSheetDialog(activity as AppCompatActivity)
        val lnrLoginHeader = bottomLayout.findViewById<LinearLayout>(R.id.lnrLoginHeader)
        val lnrRegistrationHeader =
            bottomLayout.findViewById<LinearLayout>(R.id.lnrRegistrationHeader)
        val loginView = bottomLayout.findViewById<View>(R.id.viewLineLogin)
        val registrationView = bottomLayout.findViewById<View>(R.id.viewLineRegister)
        val loginRelativeLayout = bottomLayout.findViewById<RelativeLayout>(R.id.rltLogin)
        val registrationRelativeLayout =
            bottomLayout.findViewById<RelativeLayout>(R.id.rltRegistration)
        buttonLogin = bottomLayout.findViewById<Button>(R.id.btnLogin)
        buttonRegistration = bottomLayout.findViewById<Button>(R.id.btnRegistration)
        val buttonVerifyOtp = bottomLayout.findViewById<Button>(R.id.btnVerifyOtp)
        val otpRelativeLayout = bottomLayout.findViewById<LinearLayout>(R.id.lnrOtp)


        tvLoginCountryCode = bottomLayout.findViewById<AutoCompleteTextView>(R.id.loginCountryCode)
        tvInEdtLoginMobileNumber =
            bottomLayout.findViewById<TextInputEditText>(R.id.loginMobileNumber)

        tvInEdtFullName = bottomLayout.findViewById<TextInputEditText>(R.id.fullName)
        tvInEdtEmail = bottomLayout.findViewById<TextInputEditText>(R.id.email)
        tvRegistrationCountryCode =
            bottomLayout.findViewById<AutoCompleteTextView>(R.id.registrationCountryCode)
        tvInEdtRegistrationMobileNumber =
            bottomLayout.findViewById<TextInputEditText>(R.id.registrationMobileNumber)
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
            otpRelativeLayout.visibility = View.GONE

        }
        lnrRegistrationHeader.setOnClickListener {
            loginView.visibility = View.GONE
            registrationView.visibility = View.VISIBLE
            registrationView.visibility = View.VISIBLE
            loginRelativeLayout.visibility = View.GONE
            registrationRelativeLayout.visibility = View.VISIBLE
            otpRelativeLayout.visibility = View.GONE
        }/**/


        buttonLogin.setOnClickListener {
            countryCode = tvLoginCountryCode.text.toString()
            mobileNumber = tvInEdtLoginMobileNumber.text.toString()
            inputValidation()
            if (countryCode.equals("") || countryCode.equals(null)) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.country_code_is_required),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mobileNumber.equals("") || mobileNumber.equals(null)) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.mobile_number_is_required),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mobileNumber!!.length < 9) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.please_provide_the_valid_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                userLogin(
                    countryCode!!,
                    mobileNumber!!
                )/*this method is used to call the user login*/
                otpRelativeLayout.visibility = View.VISIBLE
                lnrRegistrationHeader.visibility = View.GONE
                lnrLoginHeader.visibility = View.GONE
                lnrVerifyOtpHeader.visibility = View.VISIBLE
                loginRelativeLayout.visibility = View.GONE
                registrationRelativeLayout.visibility = View.GONE
            }
        }/*button for user Login*/


        buttonRegistration.setOnClickListener {
            fullName = tvInEdtFullName.text.toString()
            mobileNumber = tvInEdtRegistrationMobileNumber.text.toString()
            email = tvInEdtEmail.text.toString()
            countryCode = tvRegistrationCountryCode.text.toString()
            inputValidation()/*This function is used to input validation*/
            if (fullName.equals("") || fullName.equals(null)) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.full_name_is_required),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (countryCode.equals("") || countryCode.equals(null)) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.country_code_is_required),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mobileNumber.equals("") || mobileNumber.equals(null)) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.mobile_number_is_required),
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                userRegistration(fullName!!, mobileNumber!!, email!!, countryCode!!)
                otpRelativeLayout.visibility = View.VISIBLE
                lnrRegistrationHeader.visibility = View.GONE
                lnrLoginHeader.visibility = View.GONE
                lnrVerifyOtpHeader.visibility = View.VISIBLE
                loginRelativeLayout.visibility = View.GONE
                registrationRelativeLayout.visibility = View.GONE
            }
        }/*User Registration*/


        buttonVerifyOtp.setOnClickListener {
            if (verifyOtp.equals("") || verifyOtp.equals(null)) {
                Toast.makeText(context, "Please Enter Otp ", Toast.LENGTH_SHORT).show()
            } else {
                verifyOtp(storeCountryCode!!, storeMobileNumber!!, verifyOtp!!, bottomSheetDialog)
            }
        }/*click button to otp verification*/

        val countryCode = resources.getStringArray(R.array.countryCode_array)
        val arrayAdapter =
            ArrayAdapter((activity as AppCompatActivity), R.layout.dropdown_item, countryCode)
        tvLoginCountryCode.setAdapter(arrayAdapter)
        tvRegistrationCountryCode.setAdapter(arrayAdapter)
        /*   tvLoginCountryCode.setText("971")
           tvRegistrationCountryCode.setText("971")*/

        bottomSheetDialog.setContentView(bottomLayout)
        bottomSheetDialog.show()
        bottomSheetDialog.setCanceledOnTouchOutside(false)

    }

    private fun inputValidation() {
        tvInEdtFullName.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                //tvInEdtFullName.error = resources.getString(R.string.full_name_is_required)
            }
        }

        tvInEdtEmail.doOnTextChanged { text, start, before, count ->
            if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(text!!).matches())) {
                tvInEdtEmail.error = resources.getString(R.string.provide_valid_email_id)
            }
        }

        tvRegistrationCountryCode.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                //tvRegistrationCountryCode.error = resources.getString(R.string.country_code_is_required)
            }
        }

        tvInEdtRegistrationMobileNumber.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 9) {
                buttonRegistration.isEnabled = false
            } else {
                buttonRegistration.isEnabled = true
            }
        }

        tvLoginCountryCode.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                // tvLoginCountryCode.error = resources.getString(R.string.country_code_is_required)
            }
        }

        tvInEdtLoginMobileNumber.doOnTextChanged { text, start, before, count ->
            if (text!!.length < 9) {
                buttonLogin.isEnabled = false
            } else {
                buttonLogin.isEnabled = true
            }
        }
    }

    private fun userRegistration(
        fullName: String,
        mobileNo: String,
        email: String,
        countryCode: String
    ) {
        registrationLoginViewModel.registrationObservable()
            .observe(viewLifecycleOwner, Observer<UserRegistrationResponseModel> {
                if (it.statusCode == 400) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    editor = registrationSharedPreferences!!.edit()
                    editor.putString("fullName", it.RegisterData.fullName)
                    editor.putString("mobileNumber", it.RegisterData.mobileNo)
                    editor.putString("email", it.RegisterData.email)
                    editor.putString("countryCode", it.RegisterData.countryCode)
                    editor.putString("userId", it.RegisterData.userId)
                    editor.commit()
                    storeMobileNumber = it.RegisterData.mobileNo
                    storeCountryCode = it.RegisterData.countryCode
                   // Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    /* storeMobileNumber = registrationSharedPreferences?.getString("mobileNumber","")
                storeCountryCode = registrationSharedPreferences?.getString("countryCode","")*/
                }
            })
        registrationLoginViewModel.apiCallRegistration(fullName, mobileNo, email, countryCode)
    }/*This method is used api call for the user Registration*/

    private fun verifyOtp(
        countryCode: String,
        mobileNo: String,
        mobileOtp: String,
        bottomSheetDialog: BottomSheetDialog
    ) {
        registrationLoginViewModel.optVerifyObservable()
            .observe(viewLifecycleOwner, Observer<VerifyOtpResponseModel> {
                if (it.statusCode == 400) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    editor = registrationSharedPreferences.edit()
                    editor.putString("fullName", it.UserOtpDetails.fullName)
                    editor.putString("mobileNumber", it.UserOtpDetails.mobileNo)
                    editor.putString("email", it.UserOtpDetails.email)
                    editor.putString("countryCode", it.UserOtpDetails.countryCode)
                    editor.putString("userId", it.UserOtpDetails.userId)
                    editor.commit()
                    storeMobileNumber = it.UserOtpDetails.mobileNo
                    storeCountryCode = it.UserOtpDetails.countryCode
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    /*  val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)*/
                    bottomSheetDialog.dismiss()
                }
            })
        registrationLoginViewModel.apiCallOtpVerify(countryCode, mobileNo, mobileOtp)
    }/*this method is used to verify otp*/

    private fun userLogin(countryCode: String, mobileNo: String) {
        registrationLoginViewModel.userLoginObservable()
            .observe(viewLifecycleOwner, Observer<UserLoginResponseModel> {
                if (it.statusCode == 400) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                } else {
                   // Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    storeMobileNumber = it.LoginDetails.mobileNo
                    storeCountryCode = it.LoginDetails.countryCode
                }
            })
        registrationLoginViewModel.apiCallUserLogin(countryCode, mobileNo)
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(activity as AppCompatActivity)
        //set title for alert dialog
        builder.setTitle(R.string.logging_out)
        //set message for alert dialog
        builder.setMessage(R.string.are_you_sure_wants_you_wants_to_exit)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            editor.clear()
            editor.commit()
           val naviController = findNavController()
               naviController.popBackStack()
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}