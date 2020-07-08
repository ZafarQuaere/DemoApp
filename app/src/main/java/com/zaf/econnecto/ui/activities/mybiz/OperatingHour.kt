package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
            returnIntent.putExtra("result", "data from secondActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        textBack.setOnClickListener {
            onBackPressed();
        }
        selectTimeClickEvents()

    }

    private fun selectTimeClickEvents() {
        val list = listOf<TextView>(monFromTime,monEndTime,tueFromTime,tueEndTime,wedFromTime,wedEndTime,
                                thuFromTime,thuEndTime,friFromTime,friEndTime,satFromTime,satEndTime,sunFromTime,sunEndTime)
        for (text in list){
            text.setOnClickListener { onTimeClick(text) }
        }
        /*monFromTime.setOnClickListener {
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
        wedFromTime.setOnClickListener {
            onTimeClick(wedFromTime)
        }
        wedEndTime.setOnClickListener {
            onTimeClick(wedEndTime)
        }
        thuFromTime.setOnClickListener {
            onTimeClick(thuFromTime)
        }
        thuEndTime.setOnClickListener {
            onTimeClick(thuEndTime)
        }
        friFromTime.setOnClickListener {
            onTimeClick(friFromTime)
        }
        friEndTime.setOnClickListener {
            onTimeClick(friEndTime)
        }
        satFromTime.setOnClickListener {
            onTimeClick(satFromTime)
        }
        satEndTime.setOnClickListener {
            onTimeClick(satEndTime)
        }
        sunFromTime.setOnClickListener {
            onTimeClick(sunFromTime)
        }
        sunEndTime.setOnClickListener {
            onTimeClick(sunEndTime)
        }*/
        checkCopyForWeekday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                LogUtils.showToast(this, "isChecked : $isChecked")
            } else {
                LogUtils.showToast(this, "isChecked : $isChecked")
            }
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

                    R.id.wedFromTime -> wedFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.wedEndTime ->  wedEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.thuFromTime -> thuFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.thuEndTime ->  thuEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.friFromTime -> friFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.friEndTime ->  friEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.satFromTime -> satFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.satEndTime ->  satEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.sunFromTime -> sunFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    R.id.sunEndTime ->  sunEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                    else -> LogUtils.showToast(this@OperatingHour, "Tue tueEndTime Time")

                }
            }
        }, hour, minute, false)
        mTimePicker.show()

    }

}