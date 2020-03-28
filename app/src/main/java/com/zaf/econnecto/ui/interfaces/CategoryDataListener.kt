package com.zaf.econnecto.ui.interfaces

import com.zaf.econnecto.model.CategoryListData

interface CategoryDataListener {
    fun onCategoryListLoaded(listData: MutableList<CategoryListData>?)
}