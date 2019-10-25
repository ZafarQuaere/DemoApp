package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData;

public interface IMyBusiness {

    void updateUI(MyBusinessData bizDetails);
    void onValidationError(String msg);

    void updateBizData(String address, String mobile, String email, String website);
}
