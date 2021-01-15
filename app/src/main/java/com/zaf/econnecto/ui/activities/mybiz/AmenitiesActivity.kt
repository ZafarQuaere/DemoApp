package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.adapters.AmenitiesAddEditAdapter
import com.zaf.econnecto.ui.interfaces.AmenityAddedListener
import com.zaf.econnecto.ui.interfaces.IGeneralAmenityList
import com.zaf.econnecto.utils.LogUtils
import kotlinx.android.synthetic.main.layout_amenities.*


class AmenitiesActivity : AppCompatActivity(), IGeneralAmenityList, AmenityAddedListener {
    private lateinit var adapterAddEdit: AmenitiesAddEditAdapter
    lateinit var recyclerAmenity: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var emptyTextView: TextView
    var mContext: Activity = this
    private lateinit var myBizViewModel: MyBusinessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_amenities)
        myBizViewModel = ViewModelProviders.of(this).get(MyBusinessViewModel::class.java)
        myBizViewModel.bizAllAmenityList(this, this)
        initUI()
    }

    private fun initUI() {
        recyclerAmenity = findViewById<RecyclerView>(R.id.recycler_amenities)
        recyclerAmenity.setHasFixedSize(true)
        emptyTextView = findViewById(R.id.emptyTextView)
        clickEvents()
    }

    private fun clickEvents() {
        textSubmit.setOnClickListener {
            val selectedItem = adapterAddEdit.getSelected()
            LogUtils.DEBUG("Selected Items : $selectedItem")
            myBizViewModel.addAmenityApi(mContext, this, selectedItem)
        }

        textBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun updateAmenityList(data: List<GeneralAmenities>?) {
        if (data != null) {
            val layoutManager = LinearLayoutManager(this)
            recyclerAmenity.layoutManager = layoutManager
            recyclerAmenity.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
            adapterAddEdit = AmenitiesAddEditAdapter(this, data)
            adapterAddEdit.setListItem(data as MutableList<GeneralAmenities>)
            recyclerAmenity.adapter = adapterAddEdit
        } else {
            LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), getString(R.string.something_wrong_from_server_plz_try_again)) { onBackPressed() }
        }
    }

    override fun updateAmenities() {
        LogUtils.showDialogSingleActionButton(mContext, getString(R.string.ok), getString(R.string.amenity_added_successfully)) {
            val returnIntent = Intent()
            returnIntent.putExtra("AddAmenity", getString(R.string.amenity_added_successfully))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}