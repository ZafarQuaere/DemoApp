package com.zaf.econnecto.version2.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.zaf.econnecto.R
import com.zaf.econnecto.model.HomeResponse
import com.zaf.econnecto.ui.activities.AddBusinessActivity
import com.zaf.econnecto.ui.fragments.BizListFragment
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    private lateinit var mContext : Context
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.callHomeApi(context as Activity?)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerListener()
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    private fun registerListener() {
        viewModel.homeResponse.observe(viewLifecycleOwner, Observer { homeResponse -> updateUI(homeResponse) })
        textGetFreeBizAccount.setOnClickListener {
            startActivity(Intent(activity,AddBusinessActivity::class.java))
        }
        textViewAllBiz.setOnClickListener {
            Utils.moveToFragment(mContext, BizListFragment(), BizListFragment::class.java.simpleName, null)
            Utils.updateActionBar(mContext, BizListFragment::class.java.simpleName, mContext.getString(R.string.business_list), null, null)
        }
    }

    private fun updateUI(homeResponse: HomeResponse) {
        if (homeResponse.status == AppConstant.SUCCESS) {
            updateBannerImage(homeResponse.data.banner_image)
            updateGetBizImage(homeResponse.data.getbusiness_image)
            updateCardsUI(homeResponse.data.card_image)
            updateBottomImage(homeResponse.data.bottom_image)
        } else {
            LogUtils.showToast(mContext, homeResponse.message)
        }
    }

    private fun updateBottomImage(bottomImage: List<String>) {
        for(i in bottomImage){
            Picasso.get().load(i).placeholder(R.drawable.default_biz_profile_pic).into(imgHomeBottom)
        }
    }

    private fun updateCardsUI(cardImage: List<String>) {

    }

    private fun updateGetBizImage(getbusinessImage: List<String>) {
        for(i in getbusinessImage){
            Picasso.get().load(i).placeholder(R.drawable.default_biz_profile_pic).into(imgGetBiz)
        }
    }

    private fun updateBannerImage(bannerImage: List<String>) {
        for(i in bannerImage){
            Picasso.get().load(i).placeholder(R.drawable.default_biz_profile_pic).into(imgHomeBanner)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideActionbar()
    }

    private fun hideActionbar() {
        ( activity as AppCompatActivity).supportActionBar?.hide()
    }
}
