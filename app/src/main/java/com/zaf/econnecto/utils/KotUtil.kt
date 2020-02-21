package com.zaf.econnecto.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zaf.econnecto.R
import java.util.*

class KotUtil {

    private val PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"
    companion object {
        // Extension function to replace fragment
        fun AppCompatActivity?.replaceFragment(activity: AppCompatActivity, fragment: Fragment) {
            val fragmentManager = this?.supportFragmentManager
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.lytMain, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        fun validateDOB(birthYear : String): Boolean {
            if (birthYear.isNullOrEmpty()) return false

            val birthYear = Integer.parseInt(birthYear)
            val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
            if (birthYear < 1920)
                return false
            else if (currentYear - birthYear > 10 )
                return true
            return false
        }

        fun displayResponseError(context: Context, messages: List<String?>?) {
           LogUtils.showErrorDialog(context,context.getString(R.string.ok), messages!!.get(0).toString())
        }
    }
}