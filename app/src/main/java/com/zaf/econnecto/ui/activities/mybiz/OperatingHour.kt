package com.zaf.econnecto.ui.activities.mybiz

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zaf.econnecto.R
import com.zaf.econnecto.utils.AppConstant
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
        val startList = listOf<TextView>(monFromTime,tueFromTime,wedFromTime,thuFromTime,friFromTime,satFromTime,sunFromTime)
        val endList = listOf<TextView>(monEndTime,tueEndTime,wedEndTime,thuEndTime,friEndTime,satEndTime,sunEndTime)
        myBizViewModel.updateOperatingHours(startList,endList)
    }


    private fun updateOpHourData(data: List<OPHoursData>) {
        val startList = listOf<TextView>(monFromTime,tueFromTime,wedFromTime,thuFromTime,friFromTime,satFromTime,sunFromTime)
        val endList = listOf<TextView>(monEndTime,tueEndTime,wedEndTime,thuEndTime,friEndTime,satEndTime,sunEndTime)
        val switchList = listOf<Switch>(switchMon,switchTue,switchWed,switchThu,switchFri,switchSat,switchSun)
        for (i in 0..6) {
            startList[i].text = data[i].open_time
            endList[i].text = data[i].close_time
            updateToggle(switchList[i],startList[i].text.toString())
        }
    }

    private fun updateToggle(switch: Switch, openTime: String) {
        switch.isChecked = openTime != AppConstant.DEFAULT_TIME
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
            updateToggle(switchTue,fromTime)

            wedFromTime.text = fromTime
            wedEndTime.text = endTime
            updateToggle(switchWed,fromTime)

            thuFromTime.text = fromTime
            thuEndTime.text = endTime
            updateToggle(switchThu,fromTime)

            friFromTime.text = fromTime
            friEndTime.text = endTime
            updateToggle(switchFri,fromTime)

            satFromTime.text = fromTime
            satEndTime.text = endTime
            updateToggle(switchSat,fromTime)
        } else {
            tueFromTime.text = getString(R.string.default_start_time)
            tueEndTime.text = getString(R.string.default_end_time)
            updateToggle(switchTue,tueFromTime.text.toString())

            wedFromTime.text = getString(R.string.default_start_time)
            wedEndTime.text = getString(R.string.default_end_time)
            updateToggle(switchWed,wedFromTime.text.toString())

            thuFromTime.text = getString(R.string.default_start_time)
            thuEndTime.text = getString(R.string.default_end_time)
            updateToggle(switchThu,thuFromTime.text.toString())

            friFromTime.text = getString(R.string.default_start_time)
            friEndTime.text = getString(R.string.default_end_time)
            updateToggle(switchFri,friFromTime.text.toString())

            satFromTime.text = getString(R.string.default_start_time)
            satEndTime.text = getString(R.string.default_end_time)
            updateToggle(switchSat,satFromTime.text.toString())
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
                val am_pm = if (datetime.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                val hours = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay

                when (v!!.id) {
                    R.id.monFromTime -> {
                        monFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchMon, monFromTime.text.toString())
                        checkCopyForWeekday.isEnabled = true
                    }
                    R.id.monEndTime -> monEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)

                    R.id.tueFromTime -> {
                        tueFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchTue, tueFromTime.text.toString())
                    }
                    R.id.tueEndTime -> tueEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)

                    R.id.wedFromTime -> {
                        wedFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchWed, wedFromTime.text.toString())
                    }
                    R.id.wedEndTime -> wedEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)

                    R.id.thuFromTime -> {
                        thuFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchThu, thuFromTime.text.toString())
                    }
                    R.id.thuEndTime -> thuEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)

                    R.id.friFromTime -> {
                        friFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchFri, friFromTime.text.toString())
                    }
                    R.id.friEndTime -> friEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)

                    R.id.satFromTime -> {
                        satFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchSat, satFromTime.text.toString())
                    }
                    R.id.satEndTime -> satEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)

                    R.id.sunFromTime -> {
                        sunFromTime.text = String.format("%d:%d %s", hours, minute, am_pm)
                        updateToggle(switchSun, sunFromTime.text.toString())
                    }
                    R.id.sunEndTime -> sunEndTime.text =
                        String.format("%d:%d %s", hours, minute, am_pm)
                    else -> LogUtils.showToast(this@OperatingHour, "Tue tueEndTime Time")
                }
            }
        }, hour, minute, false)
        mTimePicker.show()
    }
}