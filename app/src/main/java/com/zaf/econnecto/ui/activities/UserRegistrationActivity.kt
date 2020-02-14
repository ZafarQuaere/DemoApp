package com.zaf.econnecto.ui.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.zaf.econnecto.R

class UserRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        addActionBar()

        findViewById<ImageButton>(R.id.imgBtnCloseAB).setOnClickListener { finish() }
    }

    private fun addActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar))

    }

}
