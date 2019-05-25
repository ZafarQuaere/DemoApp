package com.zaf.econnecto.ui.presenters.operations;



public interface IAddBiz {

    void addBusiness(String mobile);
    void onValidationError(String msg);
    void callApi(String dealerName, String dealerMobile);
}
