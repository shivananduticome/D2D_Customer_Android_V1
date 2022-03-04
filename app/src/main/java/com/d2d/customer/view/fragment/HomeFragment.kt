package com.d2d.customer.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackCartCount
import com.d2d.customer.`interface`.CallbackHome
import com.d2d.customer.adapter.ImageSliderAdapter
import com.d2d.customer.adapter.MainCategoryAdapter
import com.d2d.customer.adapter.SubCategoryAdapter
import com.d2d.customer.adapter.SubscriptionPackageAdapter
import com.d2d.customer.databinding.FragmentHomeBinding
import com.d2d.customer.model.MainCategoryResponseModel
import com.d2d.customer.model.SubCategoryResponseModel
import com.d2d.customer.model.SubscriptionTitleDataResponseModel
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.viewmodel.HomeViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.smarteist.autoimageslider.SliderView


class HomeFragment : Fragment(),CallbackHome {

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var subCategoryAdapter: SubCategoryAdapter
    private lateinit var mainCategoryAdapter: MainCategoryAdapter
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private var registrationSharedPreferences: SharedPreferences? = null
    private var userId: String? = null
    private lateinit var subscriptionPackageAdapter: SubscriptionPackageAdapter
    private lateinit var progressDialog: Dialog
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
         viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeViewModel = viewModel
        registrationSharedPreferences = context?.getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        userId = registrationSharedPreferences?.getString("userId", "")
        val view: View = binding.root
        val toolbar = binding.homeCustomToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        //(activity as AppCompatActivity).supportActionBar?.hide()
        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)
        clickEvent()
        initAdapter()
        fetchMainCategoryList()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickEvent(){

    }

    fun initAdapter() {
        /*MainCategory Listing Adapter*/
        val mainCategoryRecyclerView = binding.maiCategoryRecyclerview
        mainCategoryRecyclerView.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        mainCategoryAdapter = MainCategoryAdapter(this)
        mainCategoryRecyclerView.adapter = mainCategoryAdapter

        /*SubCategory Listing Adapter*/
        val subCategoryRecyclerView = binding.subCategoryRecyclerView
        subCategoryRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        /* val decoration  = DividerItemDecoration(activity,DividerItemDecoration.VERTICAL)
         foodShortxDescrecyclerview.addItemDecoration(decoration)*/
        subCategoryAdapter = SubCategoryAdapter()
        subCategoryRecyclerView.adapter = subCategoryAdapter

        /*Subscription RecyclerView*/
        val subscriptionRecyclerView = binding.subscriptionRecyclerView
        subscriptionRecyclerView.layoutManager = LinearLayoutManager(activity)
        subscriptionPackageAdapter = SubscriptionPackageAdapter()
        subscriptionRecyclerView.adapter = subscriptionPackageAdapter

        val imageList: ArrayList<String> = ArrayList()
       // val imageList = ArrayList<SlideModel>()
        imageList.add("https://d2drestmanager.azurewebsites.net/images/slideImage1.jpg")
        imageList.add("https://d2drestmanager.azurewebsites.net/images/sliderImage3.jpg")
        imageList.add("https://d2drestmanager.azurewebsites.net/images/silderImage2.png")
        imageList.add("https://d2drestmanager.azurewebsites.net/images/silderImage4.png")
        imageList.add("https://d2drestmanager.azurewebsites.net/images/silderImage5.jpg")
        setImageInSlider(imageList, binding.imageSlider)
       /* imageList.add(SlideModel("https://d2drestmanager.azurewebsites.net/images/slideImage1.jpg"))
        imageList.add(SlideModel("https://d2drestmanager.azurewebsites.net/images/silderImage2.png"))
        imageList.add(SlideModel("https://d2drestmanager.azurewebsites.net/images/sliderImage3.jpg"))
        imageList.add(SlideModel("https://d2drestmanager.azurewebsites.net/images/silderImage4.png"))
        imageList.add(SlideModel("https://d2drestmanager.azurewebsites.net/images/silderImage5.jpg"))
       // https://devendrac706.medium.com/android-image-slider-with-indicator-auto-image-slider-in-android-studio-kotlin-kotlin-tutorial-ed55e79a0cad
        binding.imageSlider.setImageList(imageList,ScaleTypes.FIT)
        binding.imageSlider.setOnClickListener {
           Toast.makeText(context,"Click Button",Toast.LENGTH_SHORT).show()
        }*/

    }

    override fun mainCategoryClickEvent(mainCategoryId: String?, position: Int?) {
        if (position!!>=2){
            binding.subscriptionRecyclerView.visibility = View.VISIBLE
            binding.subCategoryRecyclerView.visibility = View.GONE
            getSubscriptionTypes()

        }else {
            getSubCategory(mainCategoryId!!)
            binding.subscriptionRecyclerView.visibility = View.GONE
            binding.subCategoryRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun getSubCategory(mainCategoryId: String) {
        progressDialog.show()
        viewModel.getSubCategoryObservable()
            .observe(viewLifecycleOwner, Observer<SubCategoryResponseModel> {
                if (it.statusCode == 400) {
                    progressDialog.dismiss()
                    binding.subCategoryCardView.visibility = View.GONE
                    subCategoryAdapter.notifyDataSetChanged()
                    subCategoryAdapter.subCategoryList.clear()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                } else {
                   progressDialog.dismiss()
                    binding.subCategoryCardView.visibility = View.VISIBLE
                    subCategoryAdapter.subCategoryList = it.SubCategoryData?.toMutableList()

                    subCategoryAdapter.notifyDataSetChanged()
                }
            })
        viewModel.apiCallSubCategory(mainCategoryId!!)
    }//This Method is used to get the Breakfast Category name


    private fun getSubscriptionTypes() {
        progressDialog.show()
        viewModel.subscriptionTypeObservable()
            .observe(viewLifecycleOwner, Observer<SubscriptionTitleDataResponseModel> {
                if (it.statusCode == 400) {
                    progressDialog.dismiss()
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                } else {
                    progressDialog.dismiss()
                    binding.subCategoryCardView.visibility = View.VISIBLE
                    subscriptionPackageAdapter.subscriptionTypeList =
                        it.SubscriptionTitle.toMutableList()
                    subscriptionPackageAdapter.notifyDataSetChanged()
                }
            })
        viewModel.apiCallSubscriptionTypes()
    }


    private fun fetchMainCategoryList(){
        progressDialog.show()
        viewModel.mainCategoryObservable().observe(viewLifecycleOwner, Observer<MainCategoryResponseModel> {
            if (it.statusCode == 400) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }else{
                 progressDialog.dismiss()
                mainCategoryAdapter.mainCategoryList = it.MainCategoryData.toMutableList()
                mainCategoryAdapter.notifyDataSetChanged()
            }
        })
        viewModel.apiCallForMainCategoryListing()
    }

private fun setImageInSlider(images: ArrayList<String>, imageSlider: SliderView) {
    val adapter = ImageSliderAdapter()
    adapter.renewItems(images)
    imageSlider.setSliderAdapter(adapter)
    imageSlider.isAutoCycle = true
    imageSlider.startAutoCycle()
}

}