package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen3FragmentArgs.fromBundle
import kotlinx.android.synthetic.main.add_biz_screen3_fragment.*

class AddBizScreen3Fragment : Fragment() {

    private lateinit var navController: NavController
    val bizInfo by lazy {
        fromBundle(arguments!!).bizInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mydata = bizInfo
        // LogUtils.showErrorDialog(activity,"Ok","${mydata.email} ${mydata.name} ${mydata.age}")


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
        btnSubmitAddBiz.setOnClickListener { activity?.finish() }
        navController = Navigation.findNavController(view)
        btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.popBackStack(R.id.screen3, true)
                }

            })

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen3ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}




