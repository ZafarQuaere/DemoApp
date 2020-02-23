package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.biz_list.BizData
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import com.zaf.econnecto.network_call.response_model.home.CategoryData
import com.zaf.econnecto.service.BusinessListService
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen1_fragemnt.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBizScreen1Fragment : Fragment() {

    lateinit var navController: NavController


    companion object {
        fun newInstance() = AddBizScreen1Fragment()
    }

    private lateinit var viewModel: AddBizScreen1ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen1_fragemnt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        btnNext.setOnClickListener {
            if (editCategory.text.toString().trim().isEmpty()){
                LogUtils.showErrorDialog(activity,getString(R.string.ok),getString(R.string.please_select_business_category))
            }else{
                callApi("zafima20@gmail.com")
                var bundle = bundleOf("categoryId" to editCategory.text.toString().trim())
                navController!!.navigate(R.id.action_screen1_to_screen2,bundle)
            }
        }

        textAddCategory.setOnClickListener {

            val categoryArray = resources.getStringArray(R.array.biz_category)

            viewModel.openBottomSheetDialog(activity,categoryArray, object: DialogButtonClick{
                override fun onCancelClick() {
                   LogUtils.showToast(activity,"Cancel Click")
                }

                override fun onOkClick() {
                    LogUtils.showToast(activity,"Ok Click")
                }

            })
        }

    }

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true *//* enabled by default *//*) {
            override fun handleOnBackPressed() { // Handle the back button event
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }*/

    private fun callApi(email: String) {
        //TODO("Have to implement this api call in viewmodel calls")
        val bizListServie = ServiceBuilder.buildConnectoService(BusinessListService::class.java)

        val requestCall = bizListServie.getBusinessList(email)

        requestCall.enqueue(object: Callback<BizListData> {

            override fun onResponse(call: Call<BizListData>, response: Response<BizListData>) =
                    if (response.isSuccessful){
                        LogUtils.DEBUG("Response : "+response.body())
                        val body: BizListData? = response.body()
                        val data : MutableList<BizData>? = body!!.data

                    }else{
                       // LogUtils.showToast(activity,"toast")
                    }

            override fun onFailure(call: Call<BizListData>, t: Throwable) {
            }

        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen1ViewModel::class.java)
        // TODO: Use the ViewModel

    }

    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(item: String?)
    }
}




