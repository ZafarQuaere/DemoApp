package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.zaf.econnecto.ui.fragments.ProgressFragment;

import java.util.ArrayList;
import java.util.List;


public abstract class BasePresenter {

    private final Context mContext;

    private final List<BasePresenter> mPresenters;

    protected Dialog mDialog;
    private ProgressFragment progressDialog;

    public BasePresenter(Context context) {
        mContext = context;
        mPresenters = new ArrayList<>();
    }

    /**
     * Override this function to provide implementation when this presenter is created. This
     * function is called with {@link Activity#onPostCreate(Bundle)} and {@link
     * Fragment#onActivityCreated(Bundle)}
     */
    public void onPostCreate() {
        for (BasePresenter presenter : mPresenters) {
            presenter.onPostCreate();
        }
    }

    /**
     * Override this function to provide implementation when this presenter starts. This function is
     * called with {@link Activity#onStart()} and {@link Fragment#onStart()}
     */
    public void onStart() {
        for (BasePresenter presenter : mPresenters) {
            presenter.onStart();
        }
    }

    /**
     * Override this function to provide implementation when this presenter stops. This function is
     * called with {@link Activity#onStop()} and {@link Fragment#onStop()}
     */
    public void onStop() {
        for (BasePresenter presenter : mPresenters) {
            presenter.onStop();
        }
        // dismissDialog();
        //hideProgressDialog();
    }

    protected Context getContext() {
        return mContext;
    }

    public <T extends BasePresenter> T addPresenter(T presenter) {
        mPresenters.add(presenter);
        return presenter;
    }

    // Dialog
   /* public Dialog showDialog(int messageRes, int titleRes) {
        return showDialog(messageRes, titleRes, null);
    }

    public Dialog showDialog(CharSequence messageStr, String titleStr) {
        return showDialog(messageStr, titleStr, null);
    }

    public Dialog showDialog(CharSequence messageStr, int title) {
        String titleStr = title > 0 ? mContext.getString(title) : null;
        return showDialog(messageStr, titleStr, null);
    }

    public Dialog showDialog(int message, int title,
                             DialogInterface.OnClickListener listener) {
        String msgStr = message > 0 ? mContext.getString(message) : null;
        String titleStr = title > 0 ? mContext.getString(title) : null;
        return showDialog(msgStr, titleStr, listener);
    }

    public Dialog showDialog(CharSequence message, String title,
                             DialogInterface.OnClickListener listener) {
        if (message == null) {
            return null;
        }
        AlertDialog.Builder builder = UIUtils.dialogBuilder(mContext, title, listener);
        builder.setMessage(message);
        builder.setCancelable(false);
        return showDialog(builder);
    }

    public Dialog showDialog(AlertDialog.Builder builder) {
        dismissDialog();
        mDialog = builder.show();
        return mDialog;
    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }*/

   /* public void openProgressDialog(){
        progressDialog = new ProgressFragment();
        progressDialog.setCancelable(false);
        progressDialog.showNow(((AppCompatActivity) mContext).getSupportFragmentManager(),"Progress");
    }

    public void hideProgressDialog(){
        if (progressDialog!= null) {
            progressDialog.dismiss();
        }
    }*/
}
