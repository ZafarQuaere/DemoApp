package com.zaf.econnecto.ui.fragments.add_business

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import androidx.navigation.fragment.navArgs

import com.zaf.econnecto.R
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen3FragmentArgs.fromBundle
import com.zaf.econnecto.utils.LogUtils
import kotlin.reflect.KProperty

class AddBizScreen3Fragment : Fragment() {

    val bizInfo  by lazy {
         fromBundle(arguments!!).bizInfo
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mydata = bizInfo
        LogUtils.showErrorDialog(activity,"Ok","${mydata.email} ${mydata.name} ${mydata.age}")



    }
    companion object {
        fun newInstance() = AddBizScreen3Fragment()
    }

    private lateinit var viewModel: AddBizScreen3ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen3_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnSubmitAddBiz).setOnClickListener { activity?.finish() }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen3ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}




