package com.zaf.econnecto.ui.activities.mybiz.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.AboutActivity
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessActivityLatest
import com.zaf.econnecto.ui.activities.mybiz.MyBusinessViewModel
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment() {
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        lytAboutData.setOnClickListener {
            startActivityForResult(Intent(activity, AboutActivity::class.java), MyBusinessActivityLatest.UPDATE_ABOUT_US)
        }
        val about = PrefUtil.getAboutText(mContext)
        val why = PrefUtil.getWhyUsText(mContext)
        if ( about?.isNotEmpty() == true && why?.isNotEmpty() == true) {
            lytAboutData.visibility = View.VISIBLE
            textNoAboutData.visibility = View.GONE
            textAboutDesc.text = about
            textAboutWhyUs.text = why
            editAbout.setOnClickListener {
                startActivityForResult(Intent(activity, AboutActivity::class.java), MyBusinessActivityLatest.UPDATE_ABOUT_US)
            }
        } else {
            textNoAboutData.visibility = View.VISIBLE
            lytAboutData.visibility = View.GONE
            textNoAboutData.setOnClickListener {
                startActivityForResult(Intent(activity, AboutActivity::class.java), MyBusinessActivityLatest.UPDATE_ABOUT_US)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MyBusinessActivityLatest.UPDATE_ABOUT_US) {
            LogUtils.DEBUG("Coming from About Activity")
            AboutActivity.about.observe(viewLifecycleOwner, Observer { text ->
                run {
                    if (text.isNotEmpty()) {
                        lytAboutData.visibility = View.VISIBLE
                        textNoAboutData.visibility = View.GONE
                        textAboutDesc.text = text
                    }
                }
            })
            AboutActivity.whyUs.observe(viewLifecycleOwner, Observer { text ->
                run {
                    if (text.isNotEmpty()) {
                        textAboutWhyUs.text = text
                    }
                }
            })
        }
    }
}