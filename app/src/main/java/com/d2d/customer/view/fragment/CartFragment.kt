package com.d2d.customer.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackViewCart
import com.d2d.customer.adapter.CartAdapter
import com.d2d.customer.databinding.FragmentCartBinding
import com.d2d.customer.model.*
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.view.AddFragmentToActivity
import com.d2d.customer.view.DashboardActivity
import com.d2d.customer.viewmodel.CartViewModel
import java.lang.Exception

class CartFragment : Fragment(), CallbackViewCart {

    private var userId: String? = null
    private lateinit var viewModel: CartViewModel
    private var _binding: FragmentCartBinding? = null
    lateinit var cartAdapter: CartAdapter
    private var sharedPreferences: SharedPreferences? = null

   /* private var placeOrderItemList = mutableListOf<itemArray>()
    private var addOnsList = mutableListOf<AddOnsList>()
    private var addOnsTempList = mutableListOf<AddOnsList>()
    private lateinit var itemArray: itemArray
    private lateinit var addOnsListPostData:AddOnsList*/

    private lateinit var placeOrderSendDataModel: PlaceOrderSendDataModel
    private var address:String? = null
    private var houseNoOrFloor:String? = null
    private var landMark:String? = null
    private var totalAmount:String? = null
    private var categoryType:String? =""
    private lateinit var progressDialog:Dialog
    private var saveAddressAs:String? = null
    private var deliveryCharge:String? = null

    private var editor: SharedPreferences.Editor? = null
    private var cartCount:String? = null
    private var sector:String? = null
    private lateinit var dashboardActivity: DashboardActivity




    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        sharedPreferences = context?.getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId", "")
        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)



        val view: View = binding.root
        if (userId == "" || userId.equals(null)) {
            Toast.makeText(context,"Please Login",Toast.LENGTH_SHORT).show()
            val naviController = findNavController()
                naviController.popBackStack()
        } else {
            toCheckUserRegisterOrNot(userId)
            initAdapter()
            fetchViewCartData()
            clickEvents()
        }


        /*if (activity is DashboardActivity) {
            var  dashboardActivity = activity as DashboardActivity
            dashboardActivity.setBottomNavigationCartCount(cartCount!!)
        }*/

            return view

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun toCheckUserRegisterOrNot(userID: String?) {
        if (userID.equals("")||userID.equals(null)) {
            Toast.makeText(context,"Before Add to Cart Please Register!!", Toast.LENGTH_LONG).show()
           /* val intent = Intent(context,RegistrationLoginVerifyOtpActivity::class.java)
            context?.startActivity(intent)*/
        }
    }/*To check the user is register or not*/

    fun initAdapter(){
        val recyclerView = binding.cartRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        cartAdapter = CartAdapter(this)
        recyclerView.adapter = cartAdapter
    }

    /*this function is used to set all the btnAddToCart click event here*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 141) {

        }
    }

    private fun clickEvents(){
        binding.tvCoupons.setOnClickListener {
            val intent = Intent(context, AddFragmentToActivity::class.java)
            intent.putExtra("FragmentName","CouponsFragment")
            startActivity(intent)
        }

        binding.tvShippingAddress.setOnClickListener {
            val intent = Intent(context, AddFragmentToActivity::class.java)
            intent.putExtra("FragmentName","AddressFragment")
            Toast.makeText(context,"Click a Add",Toast.LENGTH_SHORT).show()
           // startActivityForResult(Intent(context, AddFragmentToActivity::class.java), 141)
            startActivityForResult(intent,141)
            // startActivity(intent)

        }



        /*Checkout button*/
        binding.btnCheckOut.setOnClickListener {
            address = binding.tvAddress.text.toString()
            houseNoOrFloor = binding.tVHouseNoOrFloor.text.toString()
            sector= binding.tvSector.text.toString()
            if (houseNoOrFloor.equals("")||houseNoOrFloor.equals(null)||sector.equals("")||sector.equals(null)){
                Toast.makeText(context,resources.getString(R.string.provide_shipping_details),Toast.LENGTH_SHORT).show()
            }else{
                progressDialog.show()
                viewModel.placeObservable().observe(viewLifecycleOwner, Observer<OrderPlacedResponse> {
                    if (it.statusCode == 400){
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                        val naviController = findNavController()
                        naviController.popBackStack()
                    }

                })

                //val placeOrderList = placeOrderItemList.distinct().toList()
              //  placeOrderSendDataModel= PlaceOrderSendDataModel(address!!,deliveryCharge!!,houseNoOrFloor,saveAddressAs,landMark,sector,totalAmount,userId,categoryType!!,placeOrderItemList.distinct().toList())
                placeOrderSendDataModel= PlaceOrderSendDataModel(address!!,deliveryCharge!!,houseNoOrFloor,saveAddressAs,landMark,sector,totalAmount,userId,categoryType!!)

                viewModel.apiCallPlaceOrder(placeOrderSendDataModel)
                // println(placeOrderSetData)
                // Toast.makeText(context,placeOrderSetData.toString(),Toast.LENGTH_LONG).show()
            }

        }

        binding.termsAndConditionCheckBox.setOnClickListener {
            if (binding.termsAndConditionCheckBox.isChecked){
                binding.btnCheckOut.isEnabled = true
                binding.btnCheckOut.setTextColor(resources.getColor(R.color.white))
                binding.btnCheckOut.setBackgroundColor(resources.getColor(R.color.d2d_color))
            }else{
                binding.btnCheckOut.isEnabled = false
                binding.btnCheckOut.setBackgroundColor(resources.getColor(R.color.greyLight))
            }

        }

    }


    override fun onResume() {
        super.onResume()
        fetchViewCartData()
        /*if (activity is DashboardActivity) {
            var  dashboardActivity = activity as DashboardActivity
            dashboardActivity.setBottomNavigationCartCount(cartCount!!)
        }*/
    }/*this method is used to get the data refresh*/





    private fun fetchViewCartData(){
      try {
          progressDialog.show()
          viewModel.getCartDetailsObservable().observe(viewLifecycleOwner, Observer<ViewCartResponseModel> {
              if (it.statusCode == 400){
                  progressDialog.dismiss()
                  binding.lnrAddToCartEmpty.visibility = VISIBLE
                  binding.rltShippingTitle.visibility = GONE
                  binding.cardView.visibility = GONE
                  binding.rltPaymentGateHeader.visibility = GONE
                  binding.tvInvoiceHeader.visibility = GONE
                  binding.rltInvoiceDetails.visibility = GONE
                  binding.invoiceCardView.visibility = GONE
                  binding.lnrTermsAndCondition.visibility = GONE
                  binding.btnCheckOut.visibility = GONE

                  //Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                  cartAdapter.notifyDataSetChanged()
                  cartAdapter.addToCartList.clear()
                //  binding.cartConstraintLayout.visibility = View.INVISIBLE
                  editor?.putString("cartCount","")
                  editor?.commit()
              }else {
                  binding.lnrAddToCartEmpty.visibility = GONE
                  binding.rltShippingTitle.visibility = VISIBLE
                  binding.cardView.visibility = VISIBLE
                  binding.rltPaymentGateHeader.visibility = VISIBLE
                  binding.tvInvoiceHeader.visibility = VISIBLE
                  binding.rltInvoiceDetails.visibility = VISIBLE
                  binding.invoiceCardView.visibility = VISIBLE
                  binding.lnrTermsAndCondition.visibility = VISIBLE
                  binding.btnCheckOut.visibility = VISIBLE
                  progressDialog.dismiss()
                  cartAdapter.addToCartList = it.ViewCartData.toMutableList()
                  cartAdapter.notifyDataSetChanged()
                  totalAmount = it.priceAfterVat
                 // binding.cartConstraintLayout.visibility = View.VISIBLE
                  houseNoOrFloor = it.houseOrFloorNo
                  landMark = it.landMark
                  sector = it.sectorId
                  deliveryCharge = it.deliveryCharge
                  saveAddressAs = it.saveAddressAs
                  setBackendDataToUi(it.address,it.houseOrFloorNo,it.landMark,it.itemCount,it.priceBeforeVat,it.deliveryCharge,it.itemVat,totalAmount!!)
                  viewModel.fetchCartCount(it.ViewCartData.size.toString())
              }
          })
          viewModel.apiCall(userId!!)
      }catch (e:Exception){
          Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show()

      }
    }


    private fun setBackendDataToUi(address:String,houseOrFloor:String,landMark:String,itemCount:String,itemCountPrice:String,deliveryCharge:String,vat:String,totalAmount:String){
        binding.tvItemCountLabel.text = resources.getString(R.string.item) +" "+ (itemCount)
        binding.tvItemCountPrice.text = resources.getString(R.string.aed)+ " "+ itemCountPrice
        binding.tvTotalPrice.text =  resources.getString(R.string.aed)+ " " + totalAmount
        binding.tvVatTotal.text =  resources.getString(R.string.aed)+ " " + vat
        binding.tvDeliveryCharge.text = resources.getString(R.string.aed)+ " " + deliveryCharge
        if (address.equals("")||address.equals(null) || houseOrFloor.equals("")||houseOrFloor.equals(null)){
            binding.cardView.visibility = View.GONE
        }else {
           // binding.tvShippingDetails.setTextColor(resources.getColor(R.color.green))
            binding.tvShippingDetails.text = resources.getString(R.string.change_address)
            binding.cardView.visibility = View.VISIBLE
            binding.tvAddress.text = address
            binding.tVHouseNoOrFloor.text = houseOrFloor
            binding.tvSector.text = sector
        }

    }/*Setup the Required ui data from the Backend*/

    override fun passPlaceOrderList(cartData: MutableList<ViewCartData>,addOnSelectionList: MutableList<addOnSelection>) {
        Log.e("DashboardActivity", addOnSelectionList.toString())
     /*   placeOrderItemList.clear()
      //  addOnsList.clear()
        Toast.makeText(context,addOnSelectionList.size.toString(),Toast.LENGTH_LONG).show()
        addOnSelectionList.forEach{
            addOnsListPostData = AddOnsList(it.addOnId,it.addOnName,it.addOnTitleId,
                it.amount,it.itemId,it.status,it.type,it.addOnBaseQuantity)
            addOnsList.addAll(listOf(addOnsListPostData))
        }
        cartData.forEach{
            itemArray = itemArray(it.cartId,it.itemFoodType, it.itemId, it.itemMainCategoryName,
                it.itemName, it.itemPrice, it.itemBaseQuantity, it.itemSubCategoryName,it.itemImageUrl,addOnsList.distinct().toList())
            placeOrderItemList.addAll(listOf(itemArray))
        }*/
        //Toast.makeText(context,addOnSelectionList.size.toString(),Toast.LENGTH_LONG).show()
    }/*get the ViewCart data from interface*/

    override fun deleteItem(userId: String, cartId: String) {
       // progressDialog.show()
        viewModel.deleteCardItemObservable().observe(viewLifecycleOwner, Observer<DeleteCartItemResponse> {
            if (it.statusCode == 400){
               // progressDialog.dismiss()
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }else{
               // progressDialog.dismiss()
               // Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                fetchViewCartData()
            }
        })
        viewModel.apiCallCartItemDelete(userId,cartId)
    }

    override fun decrementOrIncremenItem(userId: String, cartId: String, itemBaseQuantity: Int, itemPrice: String) {
        progressDialog.show()
        // Toast.makeText(context,itemBaseQuantity+"  "+itemPrice,Toast.LENGTH_LONG).show()
        viewModel.incrementOrDecrementCartObservable().observe(viewLifecycleOwner, Observer<CartIncrementOrDecrementResponse> {
            if (it.statusCode == 400){
                progressDialog.dismiss()
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }else{
                progressDialog.dismiss()
                fetchViewCartData()
            }
        })
        viewModel.callApiIcrementDecrementCartItem(userId,cartId,itemBaseQuantity)
    }

   /* private fun addBadge(count : String) {
        val badge: BadgeDrawable = nav_view.getOrCreateBadge(R.id.navigation_dashboard)
        badge.number = 3
        badge.isVisible = true
    }*/

    //enable options menu in this fragment
   /* override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }*/


}