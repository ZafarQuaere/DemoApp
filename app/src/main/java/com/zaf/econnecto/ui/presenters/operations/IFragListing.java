package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.product_list.MyProductsData;

public interface IFragListing {

    void onResponseSuccess();
    void onResponseFailure(String msg);
    void editItem();

    void updateList(MyProductsData data);
}
