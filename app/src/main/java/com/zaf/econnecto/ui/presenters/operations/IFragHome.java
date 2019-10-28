package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.home.CategoryData;

import java.util.List;

public interface IFragHome {
    void updateCategory(List<CategoryData> todaySalesData);
}
