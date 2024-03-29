package com.zaf.econnecto.ui.fragments.user_register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.interfaces.FragmentNavigation
import com.zaf.econnecto.utils.KotUtil
import kotlinx.android.synthetic.main.fragment_user_register.*


class UserRegisterFragment : Fragment(), FragmentNavigation {

    private lateinit var viewModel: UserRegisterViewModel
    lateinit var navController: NavController

    companion object {
        fun newInstance() = UserRegisterFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KotUtil.updateActionBar(activity, UserRegisterFragment.javaClass.simpleName, requireActivity().getString(R.string.register), null, null)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this).get(UserRegisterViewModel::class.java)
        viewModel.registerNavigation(this)

        txtRegister.setOnClickListener {
            activity?.let { it1 -> viewModel.validateUI(it1, editEmail, editPassword, editConfirmPassword, radioGroup, editBirthYear, editPhone)
            }
        }
        txtTermsCondition.setOnClickListener{
            navController.navigate(R.id.action_register_to_terms_condition)
        }
         txtLogin.setOnClickListener {
             requireActivity().finish()
             //callJsonParsing()
         }
    }


    override fun navigate() {
        var bundle = bundleOf("mobile_no" to editPhone.text.toString().trim())
        navController!!.navigate(R.id.action_register_to_phone_verification,bundle)
    }
}
