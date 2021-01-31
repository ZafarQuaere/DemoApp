package com.zaf.econnecto.ui.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.zaf.econnecto.R

class AddBusinessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business)
        addActionBar()
    }

    private fun addActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
    }

}
