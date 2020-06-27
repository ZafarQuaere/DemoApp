package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.StaggeredImageAdapter
import com.zaf.econnecto.utils.storage.PrefUtil
import kotlinx.android.synthetic.main.layout_photos.*


class PhotosActivity : AppCompatActivity() {
    lateinit var recyclerPhotos: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_photos)
        initUI()
    }

    private fun initUI() {
        recyclerPhotos = findViewById<RecyclerView>(R.id.recycler_photos)
        recyclerPhotos!!.setHasFixedSize(true)
//        gridView()
        staggeredGridView()
       clickEvents()
    }

    private fun staggeredGridView() {
       val layoutManager = StaggeredGridLayoutManager( 3, LinearLayout.VERTICAL)
        recyclerPhotos!!.setLayoutManager(layoutManager)
        recyclerPhotos!!.setItemAnimator(DefaultItemAnimator())
        val list = PrefUtil.getImageData(this)
        val adapter = StaggeredImageAdapter(this, list)
        recyclerPhotos!!.adapter = adapter
    }



    private fun clickEvents() {
        textSubmit.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", "data from secondActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }
}