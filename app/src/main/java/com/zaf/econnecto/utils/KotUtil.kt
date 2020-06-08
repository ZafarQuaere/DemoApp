package com.zaf.econnecto.utils

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
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
import com.zaf.econnecto.ui.fragments.user_register.PhoneVerificationFragment
import com.zaf.econnecto.ui.fragments.user_register.TermsConditionWebViewFragment
import com.zaf.econnecto.ui.interfaces.ActionBarItemClick
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

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
            else if (className.equals(PhoneVerificationFragment.javaClass.simpleName)){
                textBack.visibility = View.VISIBLE
                textBack.setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() }
            }
            else if (className.equals(TermsConditionWebViewFragment.javaClass.simpleName)){
                textBack.visibility = View.VISIBLE
                textBack.setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() }
            }
        }

        fun getLocationFromAddress(context: Context?, strAddress: String?): Address? {
            val coder = Geocoder(context)
            var address: List<Address>? = null
            //  GeoPoint p1 = null;
            try {
                address = coder.getFromLocationName(strAddress, 5)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (address == null) return null
            val location: Address = address[0]
            val latitude: Double = location.getLatitude()
            val longitude: Double = location.getLongitude()

            // return "Latitude : " +latitude + "  Longitude : " + longitude;
            return location
        }


        @JvmStatic
        suspend fun compressImage(mContext: Context, file: File): File {
            LogUtils.DEBUG("Size before compress : ${getReadableFileSize(file.length())}")
            var compressedFile = Compressor.compress(mContext,file)
            val imgSizeStr = getReadableFileSize(compressedFile.length())
            val imgSize: Double = if (imgSizeStr.contains("MB")) {
                imgSizeStr.substring(0, imgSizeStr.length - 3).toDouble() * 1024.0
            } else {
                imgSizeStr.substring(0, imgSizeStr.length - 3).toDouble()
            }
            LogUtils.DEBUG("Size after compress : $imgSize")
            if (imgSize > 500.0){
                compressedFile = compressFurther(mContext,file)
                LogUtils.DEBUG("$$ 500 KB Size after compress : ${getReadableFileSize(compressedFile.length())}")
            }
            delay(1000)
            return compressedFile
        }

        private suspend fun compressFurther(mContext: Context, file: File): File {
           return Compressor.compress(mContext,file) {
                resolution(1000, 420)
                quality(70)
                format(Bitmap.CompressFormat.WEBP)
                //size(2_097_152) // 2 MB
            }
        }

        /* private fun saveFile(compressFile: File): File {
             val file = File(compressFile.absolutePath)
             val bitmap = BitmapFactory.decodeFile(compressFile.absolutePath)
             val fos = FileOutputStream(file)
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
             fos.flush()
             fos.close()
             return file
         }
 */
        fun getReadableFileSize(size: Long): String {
            if (size <= 0) {
                return "0"
            }
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
        }


    }



    public fun loadDataFromAssets(activity: AppCompatActivity,mfileName: String) {
       // var fileName : String = "user_register_failure"
        var fileName : String = mfileName
        val loadJSONFromAsset = FileUtil.loadJSONFromAsset(activity, fileName)
        var obj = JSONObject(loadJSONFromAsset)
        LogUtils.DEBUG("status : ${obj.optInt("status")}  message ${obj.optJSONArray("message").get(0)}")
    }
}