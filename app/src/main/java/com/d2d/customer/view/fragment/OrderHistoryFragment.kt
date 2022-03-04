package com.d2d.customer.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackOrderHistory
import com.d2d.customer.adapter.*
import com.d2d.customer.databinding.OrderHistoryFragmentBinding
import com.d2d.customer.model.*
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.viewmodel.OrderHistoryViewModel
import com.d2d.customer.viewmodel.SubscriptionDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text
import kotlin.math.roundToInt

class OrderHistoryFragment : Fragment(), CallbackOrderHistory {
    private lateinit var binding: OrderHistoryFragmentBinding
    private lateinit var viewModel: OrderHistoryViewModel
    private lateinit var viewModeUpcomingMeals: SubscriptionDetailsViewModel
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private lateinit var orderCancelReasonAdapter: OrderCancelReasonAdapter
    private var cancelUserId:String? = null
    private var cancelOrderId:String? = null
    private var orderCancelReason:String? = null
    private var sharedPreferences:SharedPreferences? = null
    private var userId:String? = null
    private lateinit var progressDialog:Dialog
    private lateinit var viewOrderHistoryAdapter: ViewOrderHistoryAdapter
    private lateinit var upcomingMealsAdapter: UpcomingMealsAdapter
    private var vatCalculation:Double = 0.0
    private var itemTotal:Int =0
    private var afterRemoveVatItemTotal:Int=0



    companion object {
        fun newInstance() = OrderHistoryFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[OrderHistoryViewModel::class.java]
        viewModeUpcomingMeals = ViewModelProvider(this)[SubscriptionDetailsViewModel::class.java]
        binding = OrderHistoryFragmentBinding.inflate(inflater,container,false)
        sharedPreferences = context?.getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences),Context.MODE_PRIVATE)
        userId = sharedPreferences?.getString("userId","")
        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)
        val view:View = binding.root
        setUpAdapter()
        getOrderHistoryData()
        return view
    }


    private fun setUpAdapter(){
        val recyclerView = binding.orderHistoryRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
             orderHistoryAdapter = OrderHistoryAdapter(this)
             recyclerView.adapter = orderHistoryAdapter
    }

    private fun getOrderHistoryData(){
        progressDialog.show()
        viewModel.orderHistoryObservable().observe(viewLifecycleOwner, Observer<OrderHistoryResponseModel> {
            if (it.statusCode == 400){
                progressDialog.dismiss()
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }else{
                progressDialog.dismiss()
                //Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                orderHistoryAdapter.orderHistoryList = it.PlacedOrdersList.toMutableList()
             orderHistoryAdapter.notifyDataSetChanged()
            }
        })
        viewModel.apiCallOrderHistory(userId!!)
    }



    override fun orderCancelDialog() {
        bottomDialogForOrderCancelReason()
    }

    override fun placedOrderCancel(userId: String, orderId: String) {
        cancelUserId =userId
        cancelOrderId = orderId
    }

    override fun viewOrderHistory(userId: String?, orderId: String?, categoryType: String?, paymentPaid: Int?, paymentMethod: String?,
                                  address: String?, subscriptionId: String?, orderStatus: String,startDate:String,endDate:String) {
        bottomDialogForViewOrderHistory(userId,orderId,categoryType,paymentPaid,paymentMethod,address,subscriptionId,orderStatus,startDate,endDate)
    }


    override fun orderCancelReason(reason: String) {
        orderCancelReason = reason
    }

    private fun bottomDialogForOrderCancelReason(){
        progressDialog.show()
        val bottomSheetForOrderCancelationReasonLayout = layoutInflater.inflate(R.layout.bottom_dialog_order_cancel,null)
        val cancelReasonRecyclerView = bottomSheetForOrderCancelationReasonLayout.findViewById<RecyclerView>(R.id.cancelReasonRecyclerView)
        val btnSubmit = bottomSheetForOrderCancelationReasonLayout.findViewById<Button>(R.id.btnSubmit)
        val bottomSheetDialog = BottomSheetDialog(activity as AppCompatActivity)
        bottomSheetDialog.setContentView(bottomSheetForOrderCancelationReasonLayout)
        bottomSheetDialog.show()
            orderCancelReasonAdapter= OrderCancelReasonAdapter(this)
            cancelReasonRecyclerView.layoutManager = LinearLayoutManager(activity)
            cancelReasonRecyclerView.adapter = orderCancelReasonAdapter
            viewModel.cancelReasonObservable().observe(viewLifecycleOwner, Observer<OrderCancelReasonResponseModel> {
                if (it.statusCode == 400){
                    progressDialog.dismiss()
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }else{
                    orderCancelReasonAdapter.cancelist = it.CancelReasons.toMutableList()
                    orderCancelReasonAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }

            })
            viewModel.apiCallForOrderCancelReason()

        btnSubmit.setOnClickListener {
            if (orderCancelReason==""||orderCancelReason.equals(null)){
                Toast.makeText(context, resources.getString(R.string.cancel_reason_required), Toast.LENGTH_SHORT).show()
            }else {
                progressDialog.show()
                viewModel.placedOrderCancelObservable()
                    .observe(viewLifecycleOwner, Observer<OrderCancelResponse> {
                        if (it.statusCode == 400) {
                            progressDialog.dismiss()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        } else {
                            progressDialog.dismiss()
                           // Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            bottomSheetDialog.dismiss()
                            getOrderHistoryData()
                            //activity?.finish()
                        }
                    })
                viewModel.apiCallForOrderCancel(cancelUserId!!, cancelOrderId!!, orderCancelReason!!)
            }
        }

    }

    private fun bottomDialogForViewOrderHistory(userId: String?, orderId: String?, categoryType: String?, paymentPaid:Int?, paymentMethod:String?,
                                                address:String?, subscriptionId:String?,orderStatus:String,planStartDate:String,planEndDate:String){
        val bottomLayout = layoutInflater.inflate(R.layout.bottom_dialog_vieworder_history,null)
        val bottomSheetDialog = BottomSheetDialog(activity as AppCompatActivity)
        bottomSheetDialog.setContentView(bottomLayout)
        bottomSheetDialog.show()
        val recyclerViewAllaCart = bottomLayout.findViewById<RecyclerView>(R.id.recyclerViewAllaCart)
        val recyclerViewUpcomingMeals = bottomLayout.findViewById<RecyclerView>(R.id.recyclerViewUpcomingMeals)
        val tvItemQuantityTotal = bottomLayout.findViewById<TextView>(R.id.tvItemQuantityTotal)
        val tvDeliveryCharge = bottomLayout.findViewById<TextView>(R.id.tvDeliveryCharge)
        val tvVat = bottomLayout.findViewById<TextView>(R.id.tvVat)
        val tvTotalGrand = bottomLayout.findViewById<TextView>(R.id.tvTotalGrand)
        val tvDeliveryTo = bottomLayout.findViewById<TextView>(R.id.tvDeliveryTo)
        val tvOrderId = bottomLayout.findViewById<TextView>(R.id.tvOrderId)
        val tvPayment = bottomLayout.findViewById<TextView>(R.id.tvPayment)
        val tvOrderDate = bottomLayout.findViewById<TextView>(R.id.tvOrderDate)
        val tvOrderStatus = bottomLayout.findViewById<TextView>(R.id.tvOrderStatus)
        val relativeLayoutAllaCart = bottomLayout.findViewById<RelativeLayout>(R.id.rltAllaCart)
        val relativeLayoutSubscription = bottomLayout.findViewById<RelativeLayout>(R.id.rltSubscription)
        val tvPlanTotal = bottomLayout.findViewById<TextView>(R.id.tvPlanTotal)
        val tvSubscriptionDeliveryCharge = bottomLayout.findViewById<TextView>(R.id.tvSubscriptionDeliveryCharge)
        val tvSubscriptionVat = bottomLayout.findViewById<TextView>(R.id.tvSubscriptionVat)
        val tvSubscriptionTotalGrand = bottomLayout.findViewById<TextView>(R.id.tvSubscriptionTotalGrand)
        val tvSubscriptionOrderId =bottomLayout.findViewById<TextView>(R.id.tvSubscriptionOrderId)
        val  tvSubscriptionPayment =bottomLayout.findViewById<TextView>(R.id.tvSubscriptionPayment)
        val tvSubscriptionDeliveryTo =bottomLayout.findViewById<TextView>(R.id.tvSubscriptionDeliveryTo)
        val tvSubscriptionOrderStatus= bottomLayout.findViewById<TextView>(R.id.tvSubscriptionOrderStatus)
        val tvSubStartDate = bottomLayout.findViewById<TextView>(R.id.tvSubStartDate)
        val tvSubEndDate =bottomLayout.findViewById<TextView>(R.id.tvSubEndDate)



        recyclerViewAllaCart.layoutManager = LinearLayoutManager(activity)
        viewOrderHistoryAdapter = ViewOrderHistoryAdapter()
        recyclerViewAllaCart.adapter = viewOrderHistoryAdapter
        recyclerViewAllaCart.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        recyclerViewUpcomingMeals.layoutManager = LinearLayoutManager(activity)
        upcomingMealsAdapter = UpcomingMealsAdapter()
        recyclerViewUpcomingMeals.adapter = upcomingMealsAdapter
        recyclerViewUpcomingMeals.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))


      if (categoryType =="" || categoryType.equals(null)) {
          progressDialog.show()
          viewModel.viewOrderHistoryObservable()
              .observe(viewLifecycleOwner, Observer<ViewOrderHistoryResponseModel> {
                  if (it.statusCode == 400) {
                      progressDialog.dismiss()
                      Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                  } else {
                      progressDialog.dismiss()
                      recyclerViewAllaCart.visibility = View.VISIBLE
                      recyclerViewUpcomingMeals.visibility =View.GONE
                      relativeLayoutAllaCart.visibility = View.VISIBLE
                      relativeLayoutSubscription.visibility = View.GONE
                      vatCalculation = ((it.ViewOrderHistory.paymentPaid) * 0.05)
                      itemTotal = (((it.ViewOrderHistory.paymentPaid).toDouble()) - (vatCalculation)).roundToInt()
                      afterRemoveVatItemTotal = vatCalculation.roundToInt()
                      tvItemQuantityTotal.text =
                          context?.resources?.getString(R.string.aed) + " " + itemTotal.toString()
                      tvDeliveryCharge.text =
                          context?.resources?.getString(R.string.aed) + " " + it.ViewOrderHistory.deliveryCharge
                      tvVat.text =
                          context?.resources?.getString(R.string.aed) + " " + afterRemoveVatItemTotal.toString()
                      tvTotalGrand.text =
                          context?.resources?.getString(R.string.aed) + " " + (itemTotal + afterRemoveVatItemTotal).toString()
                      tvDeliveryTo.text = it.ViewOrderHistory.address
                      tvOrderId.text = it.ViewOrderHistory.orderId
                      tvPayment.text = it.ViewOrderHistory.paymentMethod
                      tvOrderDate.text = it.ViewOrderHistory.orderDate
                      if (it.ViewOrderHistory.status =="Cancelled"){
                          tvOrderStatus.text = it.ViewOrderHistory.orderStatus
                          tvOrderStatus.setTextColor(resources.getColor(R.color.red))
                      }else{
                          tvOrderStatus.setTextColor(resources.getColor(R.color.d2d_color))
                      }
                      Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                      viewOrderHistoryAdapter.viewOrderHistoryList = it.ViewOrderHistory.orderDetails.toMutableList()
                      viewOrderHistoryAdapter.notifyDataSetChanged()
                  }
              })
          viewModel.apiCallViewOrderHistory(userId!!, orderId!!)


      }else {
        //  Toast.makeText(context, subscriptionId, Toast.LENGTH_SHORT).show()
            progressDialog.show()
          viewModeUpcomingMeals.subscriptionUpcomingMealObservable()
              .observe(viewLifecycleOwner, Observer<SubscriptionUpcomingMealResponseModel> {
                  if (it.statusCode == 400) {
                      progressDialog.dismiss()
                      Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                  } else {
                      progressDialog.dismiss()
                      recyclerViewUpcomingMeals.visibility = View.VISIBLE
                      relativeLayoutSubscription.visibility = View.VISIBLE
                      recyclerViewAllaCart.visibility = View.GONE
                      relativeLayoutAllaCart.visibility = View.GONE
                     // Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                      upcomingMealsAdapter.upcomingMealsList = it.upcomingMeals.toMutableList()
                      upcomingMealsAdapter.notifyDataSetChanged()
                      tvPlanTotal.text = context?.resources?.getString(R.string.aed) + " " + paymentPaid.toString()
                      vatCalculation = (paymentPaid!!.toDouble() * 0.05)
                      tvSubscriptionDeliveryCharge.text = context?.resources?.getString(R.string.aed) + " " +"00"
                      tvSubscriptionVat.text =context?.resources?.getString(R.string.aed) + " " +(vatCalculation.roundToInt().toString())
                      var vat = vatCalculation.toInt()
                      tvSubscriptionTotalGrand.text = context?.resources?.getString(R.string.aed) + " " +(paymentPaid!!+vat).toString()
                      tvSubscriptionOrderId.text = resources.getString(R.string.order_id)+" : "+orderId
                      tvSubscriptionPayment.text = resources.getString(R.string.payment_mode)+" : "+paymentMethod
                      tvSubscriptionDeliveryTo .text = address
                      tvSubscriptionOrderStatus.text = orderStatus
                      tvSubStartDate.text =resources.getString(R.string.plan_start_date)+" : "+ planStartDate
                      tvSubEndDate.text = resources.getString(R.string.plan_end_date)+" : "+planEndDate
                  }
              })
          viewModeUpcomingMeals.apiCallUpcomingMeals(subscriptionId!!)

      }
    }

}