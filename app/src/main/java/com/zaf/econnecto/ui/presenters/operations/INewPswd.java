package com.zaf.econnecto.ui.presenters.operations;

public interface INewPswd {

    void changePswd();
    void onValidationError(String msg);
    void callApi(String password);
}
