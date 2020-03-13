package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.zaf.econnecto.service.EConnectoServices
import com.zaf.econnecto.service.ServiceBuilder
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen1_fragemnt.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBizScreen1Fragment : Fragment() {

    lateinit var navController: NavController
    var estdYear : Int = 0

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
        KotUtil.updateActionBar(activity, AddBizScreen1Fragment.javaClass.simpleName, "Screen 1", null, null)
        navController = Navigation.findNavController(view)

        btnNext.setOnClickListener {
            val bizName : String = editBizName.text.toString().trim()
            val category1 : String = editCategory1.text.toString().trim()
            val category2 : String = editCategory2.text.toString().trim()
            val category3 : String = editCategory3.text.toString().trim()

            when {
                bizName.isEmpty() -> {
                    LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.enter_valid_business_name))
                }
                bizName.length < 3 -> {
                    LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.enter_valid_business_name))
                }
                !KotUtil.validateEstd(estdYear) -> {
                    LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.enter_valid_establishment_year))
                }
                category1.isEmpty() -> {
                    LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.select_atleast_one_category_plz_click_on_add_category_text))
                }
                else -> {
                    val bizDetailData = BizDetailData(bizName, estdYear.toInt() , category1, category2, category3)
                    val bundle = bundleOf("bizDetail" to bizDetailData)
                    navController.navigate(R.id.action_screen1_to_screen2, bundle)
                }
            }
        }

        editEstdYear.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty()){
                    estdYear = s.toString().toInt()
                } else estdYear = 0
            }

        })

        textAddCategory.setOnClickListener {
            val categoryArray = resources.getStringArray(R.array.biz_category)
            viewModel.openBottomSheetDialog(activity, categoryArray, object : OnCategoryItemClickListener {
                override fun onCategoryItemClick(item: String?) {
                    if (item != null) {
                        updateCategoryUI(item)
                    }
                }
            })
        }
    }

    private fun updateCategoryUI(item: String) {
        when {
            editCategory1.text.toString().isEmpty() -> {
                tilCategory1.visibility = View.VISIBLE
                editCategory1.setText(item)
                textAddCategory.text = activity!!.getString(R.string.add_more_category)
            }
            editCategory2.text.toString().isEmpty() -> {
                tilCategory2.visibility = View.VISIBLE
                editCategory2.setText(item)
                if (editCategory2.text.toString().trim() == editCategory1.text.toString().trim()) {
                    LogUtils.showToast(activity, "Select different category")
                    editCategory2.setText("")
                    tilCategory2.visibility = View.GONE
                }
            }
            editCategory3.text.toString().isEmpty() -> {
                tilCategory3.visibility = View.VISIBLE
                editCategory3.setText(item)
                if (editCategory3.text.toString().trim() == editCategory2.text.toString().trim() || editCategory3.text.toString().trim() == editCategory1.text.toString().trim()) {
                    LogUtils.showToast(activity, "Select different category")
                    editCategory3.setText("")
                    tilCategory3.visibility = View.GONE
                } else {
                    textAddCategory.visibility = View.GONE
                }
            }
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen1ViewModel::class.java)
    }

    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(item: String?)
    }
}
