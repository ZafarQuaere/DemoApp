package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.zaf.econnecto.ui.fragments.ProgressFragment;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseFragmentPresenter {

    private final Context mContext;

    private final List<BaseFragmentPresenter> mPresenters;

    protected Dialog mDialog;
    private ProgressFragment progressDialog;

    public BaseFragmentPresenter(Context context) {
        mContext = context;
        mPresenters = new ArrayList<>();
    }

    /**
     * Override this function to provide implementation when this presenter is created. This
     * function is called with {@link Activity#onPostCreate(Bundle)} and {@link
     * Fragment#onActivityCreated(Bundle)}
     */
    public void onPostCreate() {
        for (BaseFragmentPresenter presenter : mPresenters) {
            presenter.onPostCreate();
        }
    }

    /**
     * Override this function to provide implementation when this presenter starts. This function is
     * called with {@link Activity#onStart()} and {@link Fragment#onStart()}
     */
    public void onStart() {
        for (BaseFragmentPresenter presenter : mPresenters) {
            presenter.onStart();
        }
    }

    /**
     * Override this function to provide implementation when this presenter stops. This function is
     * called with {@link Activity#onStop()} and {@link Fragment#onStop()}
     */
    public void onStop() {
        for (BaseFragmentPresenter presenter : mPresenters) {
            presenter.onStop();
        }
        //dismissDialog();
    }

    protected Context getContext() {
        return mContext;
    }

    public <T extends BaseFragmentPresenter> T addPresenter(T presenter) {
        mPresenters.add(presenter);
        return presenter;
    }

  /*  public void openProgressDialog(){
        progressDialog = new ProgressFragment();
        progressDialog.setCancelable(false);
//        if (progressDialog.isAdded()){
            progressDialog.showNow(((AppCompatActivity) mContext).getSupportFragmentManager(),"Progress");
//        }else {
//            return;
//        }

    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }
*/
}
