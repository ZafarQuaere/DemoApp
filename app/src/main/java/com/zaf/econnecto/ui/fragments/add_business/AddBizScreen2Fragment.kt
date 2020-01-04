package com.zaf.econnecto.ui.fragments.add_business

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.*

class AddBizScreen2Fragment : Fragment() {

    lateinit var navController: NavController

    private lateinit var viewModel: AddBizScreen2ViewModel
    private  var args :String? = null

   // val args1: AddBizScreen2FragmentArgs by navArgs()

    companion object {
        fun newInstance() = AddBizScreen2Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         args = arguments?.getString("categoryId")
        LogUtils.showToast(activity,args.toString())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen2_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
       // teditFirst.setText(args)

        //TODO have to pass model class to next fragment
//        var model = AddBizModel("12345")
//        var bundle = bundleOf(model to AddBizModel)
        btnNext.setOnClickListener {
            navController!!.navigate(R.id.action_screen2_to_screen3)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen2ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
