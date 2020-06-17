package com.zaf.econnecto.ui.fragments.details_frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.zaf.econnecto.R
import com.zaf.econnecto.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    lateinit var binding : FragmentFirstBinding
    lateinit var view1: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_first, container, false)
        binding = DataBindingUtil.bind(view1)!!

        binding.textFirstFrag.text = "Binding Utils "
        return view1
    }


}

