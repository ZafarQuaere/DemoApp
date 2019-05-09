package com.zaf.econnecto.ui.presenters.operations;



public interface IAddSeller {

    void addSeller(String mobile);
    void onValidationError(String msg);
    void callApi(String dealerName, String dealerMobile);
}
