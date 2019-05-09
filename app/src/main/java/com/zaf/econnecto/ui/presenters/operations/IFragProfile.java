package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.login.Data;

public interface IFragProfile {

    void updateUI(Data data);
    void onValidationError(String msg);
    void callApi();
}
