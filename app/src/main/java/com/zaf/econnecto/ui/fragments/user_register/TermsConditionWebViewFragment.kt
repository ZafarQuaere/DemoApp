package com.zaf.econnecto.ui.fragments.user_register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.AppConstant
import com.zaf.econnecto.utils.KotUtil
import kotlinx.android.synthetic.main.fragment_terms_condition.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class TermsConditionWebViewFragment : Fragment() {
    var webView: WebView? = null
    var webUrl: String? = AppConstant.URL_TERMS_CONDITIONS
    var progressBar: ProgressBar? = null

    companion object {
        fun newInstance() = TermsConditionWebViewFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_condition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webView)
        progressBar = view.findViewById(R.id.progressbar)
        KotUtil.updateActionBar(activity, TermsConditionWebViewFragment.javaClass.simpleName, activity!!.getString(R.string.terms_condition_label), null, null)
        fab.setOnClickListener {
            if (webView!!.canGoBack()) {
                webView!!.goBack()
                if (webView!!.goBack() != null) {
                    progressBar!!.visibility = View.VISIBLE
                }
            } else {
                //if activity then on backpress
                //super.onBackPressed()
                //else if fragment then popbackstack
                activity!!.supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        webLoad(webUrl)
      /*  progressBar!!.progressDrawable.setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN
        )*/
    }


    @UiThread
    private suspend fun removeHeader() {
        // Load the html into jsoup
        val doc: Document? = Jsoup.connect(AppConstant.URL_TERMS_CONDITIONS).get()
        val header: Elements? = doc!!.getElementsByClass("default-header")
        header!!.remove()
        val footer: Elements? = doc.getElementsByClass("footer-area section-gap")
        footer!!.remove()

    }

    @SuppressLint("SetJavaScriptEnabled")
    private  fun webLoad(Url: String?) {
        try {
            //TODO to remove header and footer from webview
           /* GlobalScope.launch {
                removeHeader()
            }*/

            val webSettings = webView!!.settings
            webView!!.settings.javaScriptEnabled = true
            webView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webView!!.settings.setAppCacheEnabled(true)
            webView!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            webSettings.domStorageEnabled = true
            webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            webSettings.useWideViewPort = true
            webView!!.webChromeClient = MyChromeClient()
            webView!!.webViewClient = MyWebViewClient()
            webView!!.loadUrl(Url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class MyChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar!!.progress = newProgress
            if (newProgress == 100) {
                progressBar!!.visibility = View.GONE
            }
        }
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            progressBar!!.visibility = View.VISIBLE
            view.loadUrl(url)
            return true
        }
    }

}


