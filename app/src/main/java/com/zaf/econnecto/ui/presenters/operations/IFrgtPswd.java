package com.zaf.econnecto.ui.presenters.operations;

public interface IFrgtPswd {

    void startOTPActivity();

    void onValidationError(String msg);

    void callOtpApi(String mobile);
    void updateEmail(String email);
}
