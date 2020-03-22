package com.zaf.econnecto.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen2Fragment
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen3Fragment
import com.zaf.econnecto.ui.interfaces.ActionBarItemClick
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

        fun validateDOB(birthYear: String): Boolean {
            if (birthYear.isNullOrEmpty()) return false

            val birthYear = Integer.parseInt(birthYear)
            val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
            if (birthYear < 1920)
                return false
            else if (currentYear - birthYear > 10)
                return true
            return false
        }


        fun validateEstd(year: Int): Boolean {
            if (year == 0) {
                return true
            }
            val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
            if (year < 1800)
                return false
            else if (year > currentYear)
                return false
            return true
        }

        fun displayResponseError(context: Context, messages: List<String?>?) {
            LogUtils.showErrorDialog(context, context.getString(R.string.ok), messages!!.get(0).toString())
        }

        fun updateActionBar(activity: FragmentActivity?, className: String, dynamicTitle: String, customHeaderData: Any?,
                            actionBarListener: ActionBarItemClick?) {
            if (activity == null)
                return
            LogUtils.DEBUG(AppConstant.TAG + " Utils >> updateActionBar() called : " + className + "/" + dynamicTitle)

            val toolbarLayout = activity.findViewById<View>(R.id.lytToolbar) as RelativeLayout
            val rlytSearch = toolbarLayout.findViewById<View>(R.id.rlytSearch) as RelativeLayout
            val textTitle = toolbarLayout.findViewById<View>(R.id.textTitle) as TextView
            val textBack = toolbarLayout.findViewById<View>(R.id.textBack) as TextView
            val txtSearch = toolbarLayout.findViewById<View>(R.id.txtSearch) as TextView
            val txtSearchBack = toolbarLayout.findViewById<View>(R.id.txtSearchBack) as TextView
            val txtSearchClear = toolbarLayout.findViewById<View>(R.id.txtSearchClear) as TextView
            val editSearch = toolbarLayout.findViewById<View>(R.id.editSearch) as EditText
            val imgActionBarDrawerIcon = toolbarLayout.findViewById<View>(R.id.imgActionBarDrawerIcon) as ImageView

            textBack.visibility = View.GONE
            txtSearch.visibility = View.GONE
            rlytSearch.visibility = View.GONE

            //  txtSearchBack.setVisibility(View.GONE);
            textTitle.text = dynamicTitle

            if (className.equals(AddBizScreen1Fragment.javaClass.simpleName)){
                textBack.visibility = View.VISIBLE
                textBack.setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() }

            } else if (className.equals(AddBizScreen2Fragment.javaClass.simpleName)){
                textBack.visibility = View.VISIBLE
                textBack.setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() }
            }
            else if (className.equals(AddBizScreen3Fragment.javaClass.simpleName)){
                textBack.visibility = View.VISIBLE
                textBack.setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() }
            }
        }
    }
}