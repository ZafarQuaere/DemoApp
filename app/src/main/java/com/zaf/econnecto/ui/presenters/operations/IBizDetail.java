package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.request_model.AddressData;

public interface IBizDetail {

    void updateUI();
    void onValidationError(String msg);
}
