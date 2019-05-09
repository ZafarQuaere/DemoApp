package com.zaf.econnecto.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.zaf.econnecto.R;


public class AppDialogLoader {
    private static AppDialogLoader loader = null;
    private static AppDialogLoader previousFragmentLoader = null;
    private static Context con;
    private ProgressDialog pDialog;

    private AppDialogLoader(Context context) {
        pDialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
        //pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        // pDialog.setMessage("Pocessing...");

        pDialog.setCancelable(false);
    }


    public static AppDialogLoader getLoader(Context context) {
        con = context;

       // if(loader == null)
        {
            loader = new AppDialogLoader(context);
            previousFragmentLoader = loader;
        }

        return loader;
    }


    public boolean CheckLoaderStatus() {

        if (pDialog.isShowing() ) {
            return true;
        } else {
            return false;
        }
    }

    public void show() {
        LogUtils.DEBUG("AppDialogLoader >> show() : " + pDialog);
        if (!pDialog.isShowing() && !((Activity) con).isFinishing())//use this condition to check, for any reason if the activity is destroyed then it will prevent from crash
        {
            pDialog.show();
        }
    }

    public void dismiss() {
        LogUtils.DEBUG("AppDialogLoader >> dismiss() > " + pDialog);
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
