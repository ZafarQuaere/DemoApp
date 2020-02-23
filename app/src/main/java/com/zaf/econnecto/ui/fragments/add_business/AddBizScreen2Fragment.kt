package com.zaf.econnecto.ui.fragments.add_business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zaf.econnecto.R
import com.zaf.econnecto.network_call.response_model.add_biz.PinCodeResponse
import com.zaf.econnecto.ui.interfaces.PinCodeDataListener
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
        viewModel.updatePinCodeData(activity,editPinCode,object: PinCodeDataListener{
            override fun onDataFetched(pincodeData: PinCodeResponse) {
                val data = pincodeData.getData()
                if (data != null){
                    textLocalityLabel.visibility = View.VISIBLE
                    lytLocalitySpin.visibility = View.VISIBLE

                    editCity.setText(pincodeData.getData()!![0]!!.getDistrict())
                    editState.setText(pincodeData.getData()!![0]!!.getStateName())
                    editCountry.setText(getString(R.string.india))
                    val localityArray = arrayOfNulls<String>(data!!.size)
                    for (i in data.indices) {
                        localityArray[i] = data[i]!!.getOfficeName().toString()
                    }

                    val localityAdapter: ArrayAdapter<String> = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, localityArray)
                    localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerLocality.adapter = localityAdapter
                }
            }
        })

    }

}


