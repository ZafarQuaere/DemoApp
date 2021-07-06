package com.zaf.econnecto.ui.activities.mybiz

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_operating_hour.*
import java.util.*


class OperatingHour : AppCompatActivity() {

    private lateinit var myBizViewModel: MyBusinessViewModel
    /**
     * 1) first time keep monday enable, others disable, 2) Copy for weekday will also be disabled until he enters data in monday field
     * 2) copy for weekdays will also update the toggle on/off.
     * 3)
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_operating_hour)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        myBizViewModel.bizOperatingHours(this, null, PrefUtil.getBizId(this))
        initUI()
    }

    private fun initUI() {
        textSubmit.setOnClickListener {
            updateOperatingHours()
            /*val returnIntent = Intent()
            returnIntent.putExtra("result", "data from secondActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()*/
        }

        textBack.setOnClickListener {
            onBackPressed();
        }
        myBizViewModel.opHourList.observe(this, Observer { data -> updateOpHourData(data) })
        selectTimeClickEvents()
    }

    private fun updateOperatingHours() {
        myBizViewModel.updateOperatingHours()
    }


    private fun updateOpHourData(data: List<OPHoursData>) {
        monFromTime.text = data[0].open_time
        monEndTime.text = data[0].close_time

        tueFromTime.text = data[1].open_time
        tueEndTime.text = data[1].close_time

        wedFromTime.text = data[2].open_time
        wedEndTime.text = data[2].close_time

        thuFromTime.text = data[3].open_time
        thuEndTime.text = data[3].close_time

        friFromTime.text = data[4].open_time
        friEndTime.text = data[4].close_time

        satFromTime.text = data[5].open_time
        satEndTime.text = data[5].close_time

        sunFromTime.text = data[6].open_time
        sunEndTime.text = data[6].close_time
    }

    private fun selectTimeClickEvents() {
        val list = listOf<TextView>(monFromTime,monEndTime,tueFromTime,tueEndTime,wedFromTime,wedEndTime,
                                thuFromTime,thuEndTime,friFromTime,friEndTime,satFromTime,satEndTime,sunFromTime,sunEndTime)
        for (text in list){
            text.setOnClickListener { onTimeClick(text) }
        }
        checkCopyForWeekday.setOnCheckedChangeListener { buttonView, isChecked ->
            setWeekdaysTiming(isChecked)
        }
    }

    private fun setWeekdaysTiming( setSameTiming : Boolean) {
        if (setSameTiming) {
            val fromTime = monFromTime.text.toString()
            val endTime = monEndTime.text.toString()
            tueFromTime.text = fromTime
            tueEndTime.text = endTime
            wedFromTime.text = fromTime
            wedEndTime.text = endTime
            thuFromTime.text = fromTime
            thuEndTime.text = endTime
            friFromTime.text = fromTime
            friEndTime.text = endTime
            satFromTime.text = fromTime
            satEndTime.text = endTime
        } else {
            tueFromTime.text = getString(R.string.default_start_time)
            tueEndTime.text = getString(R.string.default_end_time)
            wedFromTime.text = getString(R.string.default_start_time)
            wedEndTime.text = getString(R.string.default_end_time)
            thuFromTime.text = getString(R.string.default_start_time)
            thuEndTime.text = getString(R.string.default_end_time)
            friFromTime.text = getString(R.string.default_start_time)
            friEndTime.text = getString(R.string.default_end_time)
            satFromTime.text = getString(R.string.default_start_time)
            satEndTime.text = getString(R.string.default_end_time)
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
                    R.id.tueEndTime ->  tueEndTime.text = String.format("%d:%d %s", hours, minute, am_pm)
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