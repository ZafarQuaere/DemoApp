package com.zaf.econnecto.ui.presenters.operations;

public interface IFrgtPswd {
    void submitMobile();
    void onValidationError(String msg);
    void callSubmitMobileApi(String mobile);
}
