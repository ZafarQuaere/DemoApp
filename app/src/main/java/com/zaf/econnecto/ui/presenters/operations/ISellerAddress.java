package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.request_model.AddressData;

public interface ISellerAddress {

    void saveAddress();
    void onValidationError(String msg);
    void callApi(AddressData addressData);
}
