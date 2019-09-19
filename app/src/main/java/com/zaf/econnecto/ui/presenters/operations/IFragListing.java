package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.biz_list.BizData;
import com.zaf.econnecto.network_call.response_model.home.DetailData;
import com.zaf.econnecto.network_call.response_model.home.SalesData;
import com.zaf.econnecto.network_call.response_model.product_list.MyProductsData;

import java.util.List;

public interface IFragListing {


    void updateList(List<BizData> data);
}
