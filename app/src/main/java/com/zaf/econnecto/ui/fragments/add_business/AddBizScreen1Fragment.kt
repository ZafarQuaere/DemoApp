package com.zaf.econnecto.ui.fragments.add_business

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
import kotlinx.android.synthetic.main.add_biz_screen1_fragemnt.*

class AddBizScreen1Fragment : Fragment() {

    lateinit var navController: NavController

    companion object {
        fun newInstance() = AddBizScreen1Fragment()
    }

    private lateinit var viewModel: AddBizScreen1ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_biz_screen1_fragemnt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       var bundle = bundleOf("categoryId" to "categoryId")

        navController = Navigation.findNavController(view)
        btnNext.setOnClickListener {
            navController!!.navigate(R.id.action_screen1_to_screen2,bundle)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBizScreen1ViewModel::class.java)
        // TODO: Use the ViewModel


    }

    public fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager!!.beginTransaction()
        transaction.add(R.id.lytMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}


