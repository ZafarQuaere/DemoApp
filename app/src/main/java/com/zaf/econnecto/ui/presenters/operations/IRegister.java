package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.request_model.Register;

public interface IRegister {

    void doRegister();
    void onValidationError(String msg);
    void callApi(Register register);
}
