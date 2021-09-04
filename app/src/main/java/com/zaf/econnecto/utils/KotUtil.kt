package com.zaf.econnecto.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.mybiz.OPHoursData
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
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class KotUtil {


    private val PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"

    companion object {



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

        fun moveToFragment(activity: AppCompatActivity, parentLayout: Int, fragment: Fragment, data: Any?) {
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager!!.beginTransaction()
            transaction.add(parentLayout, fragment)
            // if data is transfered
            val bundle = Bundle()
            if (data != null) {
                bundle.putSerializable(activity.getString(R.string.key_bundle_data), data as Serializable)
                fragment.arguments = bundle
            }
            transaction.addToBackStack(fragment.javaClass.simpleName)
            transaction.commit()
        }

        fun updateActionBar(activity: FragmentActivity?, className: String, dynamicTitle: String, customHeaderData: Any?,
                            actionBarListener: ActionBarItemClick?) {
            if (activity == null)
                return
            LogUtils.DEBUG(AppConstant.TAG + " Utils >> updateActionBar() called : " + className + "/" + dynamicTitle)

            val toolbarLayout = activity.findViewById<View>(R.id.lytToolbar) as RelativeLayout
            val textTitle = toolbarLayout.findViewById<View>(R.id.textTitle) as TextView
            val textBack = toolbarLayout.findViewById<View>(R.id.textBack) as TextView
            val imgActionBarDrawerIcon = toolbarLayout.findViewById<View>(R.id.imgActionBarDrawerIcon) as ImageView

            textBack.visibility = View.GONE

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
            val kbSize = convertImageSizeToKb(getReadableFileSize(file.length()))
            if (kbSize > 250 ) {
                var compressedFile = Compressor.compress(mContext, file)
                val imgSize = convertImageSizeToKb(getReadableFileSize(compressedFile.length()))
                if (imgSize > 500.0) {
                    compressedFile = compressFurther(mContext, file)
                    LogUtils.DEBUG("$$ 500 KB Size after compress : ${getReadableFileSize(compressedFile.length())}")
                }
                delay(1000)
                return compressedFile
            } else return file
        }

        private fun convertImageSizeToKb(imgSizeStr: String): Double {
            val imgSize: Double = if (imgSizeStr.contains("MB")) {
                imgSizeStr.substring(0, imgSizeStr.length - 3).toDouble() * 1024.0
            } else  {
                imgSizeStr.substring(0, imgSizeStr.length - 3).toDouble()
            }
            LogUtils.DEBUG("Size after compress : $imgSize")

            return imgSize
        }

        private suspend fun compressFurther(mContext: Context, file: File): File {
           return Compressor.compress(mContext, file) {
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

        fun setNoDataUI(activity: Activity, tv: TextView?) {
            tv?.text = activity.getString(R.string.no_data_available)
            tv?.textSize = pixelsToSp(activity,50.0f)
        }


        fun dpToPx(dp: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics).toInt()
        }

        fun pxToDp(px: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().displayMetrics).toInt()
        }

        fun pixelsToSp(context: Context, px: Float): Float {
            val scaledDensity = context.resources.displayMetrics.scaledDensity
            return px / scaledDensity
        }

        fun getOpenCloseTime(dayOfTheWeek: String, data: List<OPHoursData>): String {
            when (dayOfTheWeek){
               "Mon" -> return "${data[0].open_time} : ${data[0].close_time} "
               "Tue" -> return "${data[1].open_time} : ${data[1].close_time} "
               "Wed" -> return "${data[2].open_time} : ${data[2].close_time} "
               "Thu" -> return "${data[3].open_time} : ${data[3].close_time} "
               "Fri" -> return "${data[4].open_time} : ${data[4].close_time} "
               "Sat" -> return "${data[5].open_time} : ${data[5].close_time} "
               "Sun" -> return "${data[6].open_time} : ${data[6].close_time} "
            }
            return "00:00 "
        }

        fun getFollowerCount(str: String): Int {
            val count = str.split(" ").toTypedArray()
            return count[0].toInt()
        }

    }


    public fun loadDataFromAssets(activity: AppCompatActivity, mfileName: String) {
       // var fileName : String = "user_register_failure"
        var fileName : String = mfileName
        val loadJSONFromAsset = FileUtil.loadJSONFromAsset(activity, fileName)
        var obj = JSONObject(loadJSONFromAsset)
        LogUtils.DEBUG("status : ${obj.optInt("status")}  message ${obj.optJSONArray("message").get(0)}")
    }
}