package com.zaf.econnecto.ui.presenters.operations;


public interface IBaseDataOperation {

    void onPreRequest(String message);

    void onRequestFinish();
}
