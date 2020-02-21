package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.add_biz_screen2_fragment.*

class AddBizScreen2Fragment : Fragment() {

    lateinit var navController: NavController

    private lateinit var viewModel: AddBizScreen2ViewModel

    val args: AddBizScreen2FragmentArgs by navArgs()

    companion object {
        fun newInstance() = AddBizScreen2Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  LogUtils.showErrorDialog(activity,"Ok",args.categoryId)

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
       var model = AddBizModel("my id","zafar",30,"abc@gmail.com","1234567890")
        var bundle = bundleOf("bizInfo" to model)

        btnNext.setOnClickListener {
            navController!!.navigate(R.id.action_screen2_to_screen3,bundle)
        }

      /*  btnPrevious.setOnClickListener {
             val callback: OnBackPressedCallback = object : OnBackPressedCallback(viewLifecycleOwner ) {
                override fun handleOnBackPressed() { // Handle the back button event
                    isEnabled = false
                }
            }*/
         /*   requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                // handle back event
               // isEnabled = true
            }*/
           // requireActivity().onBackPressedDispatcher.addCallback(this, callback)

          //  navController = Navigation.findNavController(view)


      //  }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen2ViewModel::class.java)
        viewModel.updatePinCodeData(activity,editPinCode)

    }


}


