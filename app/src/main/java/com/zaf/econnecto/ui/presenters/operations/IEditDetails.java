package com.zaf.econnecto.ui.presenters.operations;



public interface IEditDetails {

    void onValidationError(String msg);
    void updateBusinessDetails(String msg);
    void updateAddressDetails(String msg);
    void updateContactDetails(String msg);
}
