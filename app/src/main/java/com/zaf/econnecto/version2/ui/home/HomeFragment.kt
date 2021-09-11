package com.zaf.econnecto.version2.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.zaf.econnecto.R
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.model.HomeResponse
import com.zaf.econnecto.network_call.response_model.biz_list.BizData
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData
import com.zaf.econnecto.ui.activities.AddBusinessActivity
import com.zaf.econnecto.ui.activities.LoginActivity
import com.zaf.econnecto.ui.activities.MainActivity
import com.zaf.econnecto.ui.activities.ViewBusinessActivity
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.adapters.CardsImageRecyclerAdapter
import com.zaf.econnecto.ui.adapters.SearchAdapter
import com.zaf.econnecto.ui.fragments.BizListFragment
import com.zaf.econnecto.ui.interfaces.DialogButtonClick
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {
    private lateinit var mContext : Context
    lateinit var searchAdapter : SearchAdapter
    lateinit var autoTextSearch : AutoCompleteTextView
    lateinit var categoryList: MutableList<CategoryListData>

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var bizList: MutableList<BizData>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.callHomeApi(context as Activity?)
        viewModel.callBizListApi(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()
        textGetFreeBizAccount.text = if (Utils.getBusinessStatus(mContext) == "0") mContext.getString(R.string.get_free_business_account)
        else mContext.getString(R.string.my_account)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        registerListener()
        initSearch(view)
        return view;
    }

    private fun initSearch(view: View) {
        autoTextSearch = view.findViewById(R.id.autoTextSearch)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).showAddBizFab(false)
        lytParent.visibility = View.GONE
        textGetFreeBizAccount.setOnClickListener {
            openAddBusiness()
        }

        textViewAllBiz.setOnClickListener {
            Utils.moveToFragment(mContext, BizListFragment(), BizListFragment::class.java.simpleName, null)
            Utils.updateActionBar(mContext, BizListFragment::class.java.simpleName, mContext.getString(R.string.business_list), null, null)
        }
    }

    private fun openAddBusiness() {
        if (Utils.isLoggedIn(mContext)) {
            if (Utils.getBusinessStatus(mContext) == "0") {
                startActivity(Intent(mContext, AddBusinessActivity::class.java))
            } else {
                startActivity(Intent(mContext, MyBusinessActivityLatest::class.java))
            }
        } else {
            LogUtils.showDialogDoubleButton(
                    mContext,
                    mContext.getString(R.string.cancel),
                    mContext.getString(R.string.ok),
                    mContext.getString(R.string.you_need_to_login_first_to_add_a_business),
                    object : DialogButtonClick {
                        override fun onOkClick() {
                            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                            // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        }
                        override fun onCancelClick() {}
                    })
        }
    }

    private fun registerListener() {
        viewModel.homeResponse.observe(viewLifecycleOwner, Observer { homeResponse -> updateUI(homeResponse) })
        viewModel.bizListData.observe(viewLifecycleOwner, Observer { bizList -> updateBizList(bizList) })
    }

    private fun updateBizList(bizData: BizListData?) {
        if (bizData != null) {
            bizList  = bizData.data
//            categoryList = Utils.readDataFromFile(activity)
//            searchAdapter = SearchAdapter(mContext as Activity, android.R.layout.simple_list_item_1, categoryList)
            searchAdapter = SearchAdapter(mContext as Activity, android.R.layout.simple_list_item_1, bizList)
            autoTextSearch.setAdapter(searchAdapter)
            autoTextSearch.threshold = 1
            autoTextSearch.setOnItemClickListener { parent, view, position, id ->
                run {
                    val selectedBiz: BizData =  parent.adapter.getItem(position) as BizData
//                    autoTextSearch.setText(selectedBiz.businessName)
//                    autoTextSearch.setSelection(selectedBiz.businessName.length)
                    autoTextSearch.setText("")
                    LogUtils.DEBUG("Selected Biz name: " + selectedBiz.businessName + " Business id: " + selectedBiz.businessId + " Owner id: " + selectedBiz.ownerId)
                    val intent = Intent(activity, ViewBusinessActivity::class.java)
                    intent.putExtra(getString(R.string.key_biz_id), selectedBiz.businessId)
                    intent.putExtra(getString(R.string.key_owner_id), selectedBiz.ownerId)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateUI(homeResponse: HomeResponse?) {
        lytParent.visibility = View.VISIBLE
        if (homeResponse != null && homeResponse.status == AppConstant.SUCCESS) {
            updateBannerImage(homeResponse.data.banner_image)
            updateGetBizImage(homeResponse.data.getbusiness_image)
            updateCardsUI(homeResponse.data.card_image)
            updateBottomImage(homeResponse.data.bottom_image)
        } else {
            LogUtils.showToast(mContext, homeResponse?.message)
        }
    }

    private fun updateBottomImage(bottomImage: List<String>) {
        for(i in bottomImage){
            Picasso.get().load(i).into(imgHomeBottom)
        }
    }

    private fun updateCardsUI(cardImage: List<String>) {
        recylerCards.layoutManager = LinearLayoutManager(mContext)
        recylerCards.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        val cardsImageRecyclerAdapter = CardsImageRecyclerAdapter(mContext, cardImage as MutableList<String>)
        recylerCards.adapter = cardsImageRecyclerAdapter
    }

    private fun updateGetBizImage(getbusinessImage: List<String>) {
        for(i in getbusinessImage){
            Picasso.get().load(i).into(imgGetBiz)
        }
    }

    private fun updateBannerImage(bannerImage: List<String>) {
        for(i in bannerImage){
            Picasso.get().load(i).into(imgHomeBanner)
        }
    }

}
