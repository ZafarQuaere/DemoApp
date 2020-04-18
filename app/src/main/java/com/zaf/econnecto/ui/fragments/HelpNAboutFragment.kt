package com.zaf.econnecto.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.presenters.HelpNAboutPresenter
import com.zaf.econnecto.ui.presenters.operations.IHelpAbout
import com.zaf.econnecto.utils.Utils

class HelpNAboutFragment : BaseFragment<HelpNAboutPresenter?>(), IHelpAbout {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initPresenter(): HelpNAboutPresenter {
        return HelpNAboutPresenter(activity, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_help_n_about, container, false)
        initUi(view)
        return view
    }

    private fun initUi(view : View) {
        val textWebsite = view!!.findViewById<View>(R.id.textWebsite) as TextView
        val text_fb_link = view!!.findViewById<View>(R.id.text_fb_link) as TextView
        val text_insta_link = view!!.findViewById<View>(R.id.text_insta_link) as TextView
        val text_twitter_link = view!!.findViewById<View>(R.id.text_twitter_link) as TextView
        val textPlaystoreLine = view!!.findViewById<View>(R.id.textPlaystoreLine) as TextView
        textWebsite.movementMethod = LinkMovementMethod.getInstance()
        text_fb_link.movementMethod = LinkMovementMethod.getInstance()
        text_insta_link.movementMethod = LinkMovementMethod.getInstance()
        text_twitter_link.movementMethod = LinkMovementMethod.getInstance()
        textPlaystoreLine.setOnClickListener { view1: View? -> Utils.openInPlayStore(activity) }
        //  textPlaystoreLine.setMovementMethod(LinkMovementMethod.getInstance());
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onWebsiteClick() {}
}