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
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.ui.interfaces.CategoryDataListener
import com.zaf.econnecto.utils.KotUtil
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.add_biz_screen1_fragemnt.*

class AddBizScreen1Fragment : Fragment() {

    lateinit var navController: NavController
    var estdYear: Int = 0
    var categoryList: MutableList<CategoryListData>? = null
    var category1: String? = null
    var category2: String? = null
    var category3: String? = null
    var categoryid1: String = ""
    var categoryid2: String = ""
    var categoryid3: String = ""

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
        KotUtil.updateActionBar(activity, AddBizScreen1Fragment.javaClass.simpleName, requireActivity().getString(R.string.add_business), null, null)
        navController = Navigation.findNavController(view)

        btnNext.setOnClickListener {
            validateUIandNavigate()
        }

        textChangeListeners()


        textAddCategory.setOnClickListener {
            //val categoryArray = resources.getStringArray(R.array.biz_category)
            viewModel.openBottomSheetDialog(activity, categoryList, object : OnCategoryItemClickListener {
                override fun onCategoryItemClick(item: CategoryListData?) {
                    if (item != null) {
                        updateCategoryUI(item)
                    }
                }
            })
        }
    }

    private fun textChangeListeners() {
        editEstdYear.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    estdYear = s.toString().toInt()
                } else estdYear = 0
            }
        })

        editCategory1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ch: Editable?) {}
            override fun beforeTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (cs.toString().trim().isEmpty()) {
                    //LogUtils.showErrorDialog(activity,activity!!.getString(R.string.ok),activity!!.getString(R.string.please_tap_on_add_category_text_below_to_select_category))
                    tilCategory1.visibility = View.GONE
                    textAddCategory.visibility = View.VISIBLE
                    categoryid1 = ""
                    editEstdYear.requestFocus()
                }
            }
        })
        editCategory2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ch: Editable?) {}
            override fun beforeTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (cs.toString().trim().isEmpty()) {
                    tilCategory2.visibility = View.GONE
                    // LogUtils.showErrorDialog(activity,activity!!.getString(R.string.ok),activity!!.getString(R.string.please_tap_on_add_category_text_below_to_select_category))
                    textAddCategory.visibility = View.VISIBLE
                    categoryid2 = ""
                    editEstdYear.requestFocus()
                }
            }
        })
        editCategory3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ch: Editable?) {}
            override fun beforeTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (cs.toString().trim().isEmpty()) {
                    tilCategory3.visibility = View.GONE
                    // LogUtils.showErrorDialog(activity,activity!!.getString(R.string.ok),activity!!.getString(R.string.please_tap_on_add_category_text_below_to_select_category))
                    textAddCategory.visibility = View.VISIBLE
                    categoryid3 = ""
                    editEstdYear.requestFocus()
                }
            }
        })
    }

    private fun validateUIandNavigate() {
        val bizName: String = editBizName.text.toString().trim()
        val shortDesc: String = editShortDesc.text.toString().trim()
        val estdYear: String = editEstdYear.text.toString().trim()
        category1 = editCategory1.text.toString().trim()
        category2 = editCategory2.text.toString().trim()
        category3 = editCategory3.text.toString().trim()

        when {
            bizName.isEmpty() -> {
                LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.enter_valid_business_name))
            }
            shortDesc.isEmpty() -> {
                LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.please_enter_a_short_description))
            }
            bizName.length < 3 -> {
                LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.enter_valid_business_name))
            }
            estdYear.isEmpty() -> {
                LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.please_enter_foundation_year_of_business))
            }
            !KotUtil.validateEstd(estdYear.toInt()) -> {
                LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.enter_valid_establishment_year))
            }
            category1!!.isEmpty() && category2!!.isEmpty() && category3!!.isEmpty() -> {
                LogUtils.showErrorDialog(activity, getString(R.string.ok), getString(R.string.select_atleast_one_category_plz_click_on_add_category_text))
            }
            else -> {
                val bizDetailData = BizDetailData(bizName, shortDesc, estdYear.toInt(), categoryid1, categoryid2, categoryid3)
                val bundle = bundleOf("bizDetail" to bizDetailData)
                navController.navigate(R.id.action_screen1_to_screen2, bundle)
            }
        }
    }

    private fun updateCategoryUI(item: CategoryListData) {
        when {
            editCategory1.text.toString().isEmpty() -> {
                tilCategory1.visibility = View.VISIBLE
                editCategory1.setText(item.categoryName)
                categoryid1 = item.categoryId
                if (editCategory1.text.toString().trim() == editCategory2.text.toString().trim() || editCategory1.text.toString().trim() == editCategory3.text.toString().trim()) {
                    LogUtils.showToast(activity, requireActivity().getString(R.string.already_selected_plz_select_different_category))
                    editCategory1.setText("")
                }
                //textAddCategory.text = activity!!.getString(R.string.add_more_category)
            }
            editCategory2.text.toString().isEmpty() -> {
                tilCategory2.visibility = View.VISIBLE
                editCategory2.setText(item.categoryName)
                categoryid2 = item.categoryId
                if (editCategory2.text.toString().trim() == editCategory1.text.toString().trim() || editCategory2.text.toString().trim() == editCategory3.text.toString().trim()) {
                    LogUtils.showToast(activity, requireActivity().getString(R.string.already_selected_plz_select_different_category))
                    editCategory2.setText("")
                    tilCategory2.visibility = View.GONE
                }
            }
            editCategory3.text.toString().isEmpty() -> {
                tilCategory3.visibility = View.VISIBLE
                editCategory3.setText(item.categoryName)
                categoryid3 = item.categoryId
                if (editCategory3.text.toString().trim() == editCategory2.text.toString().trim() || editCategory3.text.toString().trim() == editCategory1.text.toString().trim()) {
                    LogUtils.showToast(activity, requireActivity().getString(R.string.already_selected_plz_select_different_category))
                    editCategory3.setText("")
                    tilCategory3.visibility = View.GONE
                } else {
                    textAddCategory.visibility = View.GONE
                }
            }
            else -> {
                LogUtils.showErrorDialog(activity, requireActivity().getString(R.string.ok), requireActivity().getString(R.string.you_have_already_selected_max_no_of_category))
                textAddCategory.visibility = View.GONE
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen1ViewModel::class.java)
        //TODO for now it is reading data from file, we can implement it using api call (implemented in below method)
        categoryList = Utils.readDataFromFile(activity)

        updateUIOnBackPressed()


    }

    private fun updateUIOnBackPressed() {
        /*  if(categoryList == null ) {
            viewModel.callCategoryApi(activity, object : CategoryDataListener {
                override fun onCategoryListLoaded(listData: MutableList<CategoryListData>?) {
                    categoryList = listData!!
                }
            })
        } else {*/
        if (!category1.isNullOrEmpty()) {
            tilCategory1.visibility = View.VISIBLE
        }
        if (!category2.isNullOrEmpty()) {
            tilCategory2.visibility = View.VISIBLE
        }
        if (!category3.isNullOrEmpty()) {
            tilCategory3.visibility = View.VISIBLE
        }
        if (category1 != null && category2 != null && category3 != null) {
            textAddCategory.visibility = View.GONE
        }
    }


    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(item: CategoryListData?)
    }
}
