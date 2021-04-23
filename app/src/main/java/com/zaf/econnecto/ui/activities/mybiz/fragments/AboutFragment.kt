package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.AboutActivity
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessViewModel
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment() {
    private lateinit var myBizViewModel: MyBusinessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()

    }

    private fun updateUI() {
        val data = activity?.let { PrefUtil.getBasicDetailsData(it) }
        if (data != null) {
            lytAddAboutSection.visibility = View.GONE
            textAboutDesc.text = data.aboutDescription
            textAboutWhyUs.text = data.aboutDescription
            editAbout.setOnClickListener {
                startActivityForResult(Intent(activity, AboutActivity::class.java), MyBusinessActivityLatest.UPDATE_ABOUT_US)
            }
        } else {
            lytAddAboutSection.visibility = View.VISIBLE
            lytAddAboutSection.setOnClickListener {
                startActivityForResult(Intent(activity, AboutActivity::class.java), MyBusinessActivityLatest.UPDATE_ABOUT_US)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_ABOUT_US) {
            LogUtils.DEBUG("Coming from About Activity")
//            activity?.let { PrefUtil.getBizId(it) }?.let { amenitiesVm.bizAmenityList(activity as Activity?, null, it) }
//            myBizViewModel.callMyBizBasicDetails(activity, false, null, Utils.getUserID(activity))
        }
    }

}