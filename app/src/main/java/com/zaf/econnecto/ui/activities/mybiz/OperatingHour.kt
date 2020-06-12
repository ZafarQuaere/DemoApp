package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_operating_hour.*
import java.util.*


class OperatingHour : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_operating_hour)
        initUI()
    }

    private fun initUI() {
        textSubmit.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", "data from seconActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        monFromTime.setOnClickListener {
            onTimeClick(monFromTime)
        }
        monEndTime.setOnClickListener {
            onTimeClick(monEndTime)
        }
        tueFromTime.setOnClickListener {
            onTimeClick(tueFromTime)
        }
        tueEndTime.setOnClickListener {
            onTimeClick(tueEndTime)
        }
        textBack.setOnClickListener {
            onBackPressed();
        }

    }

    private fun onTimeClick(v: View?) {
        val mTimePicker: TimePickerDialog
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mCurrentTime.get(Calendar.MINUTE)
        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                val datetime = Calendar.getInstance()
                datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                datetime[Calendar.MINUTE] = minute
                var am_pm = if (datetime.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                var hours = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay

                when (v!!.id) {
                    R.id.monFromTime -> monFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.monEndTime -> monEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.tueFromTime -> tueFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.tueEndTime -> tueEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    else -> LogUtils.showToast(this@OperatingHour, "Tue tueEndTime Time")

                }
            }
        }, hour, minute, false)
        mTimePicker.show()

    }

}