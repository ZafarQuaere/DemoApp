package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetails;

public interface IBizDetail {

    void updateUI(BizDetails bizDetails);
    void onValidationError(String msg);
}
