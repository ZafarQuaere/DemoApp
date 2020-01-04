package com.zaf.econnecto.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zaf.econnecto.R

public class kotUtil {

    companion object {
        // Extension function to replace fragment
        fun AppCompatActivity?.replaceFragment(activity: AppCompatActivity, fragment: Fragment) {
            val fragmentManager = this?.supportFragmentManager
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.lytMain, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}