package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zaf.econnecto.R
import kotlinx.android.synthetic.main.activity_edit_details.*

class EditDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        initUI();
    }

    private fun initUI() {
        textUpdateBizDetails.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", "data from seconActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        textBack.setOnClickListener {
            onBackPressed()
        }
    }
}