package com.zaf.econnecto.ui.activities.mybiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaf.econnecto.R
import com.zaf.econnecto.model.CategoryListData
import com.zaf.econnecto.ui.adapters.BizCategoryRecyclerAdapter
import com.zaf.econnecto.ui.interfaces.AllCategoriesListener
import com.zaf.econnecto.ui.interfaces.CategoryAddedListener
import com.zaf.econnecto.ui.interfaces.OnBizCategoryItemClickListener
import com.zaf.econnecto.utils.LogUtils
import com.zaf.econnecto.utils.Utils
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : AppCompatActivity(), AllCategoriesListener, OnBizCategoryItemClickListener, CategoryAddedListener {

    lateinit var myBizViewModel: MbCategoryViewModel
    lateinit var categoryAdapter: BizCategoryRecyclerAdapter
    lateinit var categoryList: MutableList<CategoryListData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        myBizViewModel = ViewModelProviders.of(this).get(MbCategoryViewModel::class.java)
        myBizViewModel.bizAllCategories(this, this)
        initUI()

    }

    private fun initUI() {
        val recyclerCategories = findViewById<RecyclerView>(R.id.recyclerCategories)
        val searchView = findViewById<SearchView>(R.id.searchView)
        recyclerCategories.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerCategories.layoutManager = layoutManager
        recyclerCategories.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (categoryList != null) {
//                    if (categoryList.contains(query)) {
                    categoryAdapter.filter(newText);
//                    } else {
//                        LogUtils.showToast(this, "No Match found")
//                    }
                }
                return false
            }
        })
    }

    override fun updateCategoriesUI(categories: MutableList<CategoryListData>?) {
        if (categories != null) {
            categoryList = categories
            categoryAdapter = BizCategoryRecyclerAdapter(this, this, categories)
            recyclerCategories.adapter = categoryAdapter
        } else {
            categoryList = Utils.readDataFromFile(this)
            categoryAdapter = BizCategoryRecyclerAdapter(this, this, categoryList)
            recyclerCategories.adapter = categoryAdapter
        }
    }

    override fun onBizCategoryItemClick(mItem: CategoryListData) {
//        LogUtils.showToast(this, "${mItem.categoryId} , ${mItem.categoryName} ${mItem.parentCategoryId}")
        myBizViewModel.addCategoryApi(this, this, mItem)
    }


    override fun updateCategory() {
        finish()
    }
}