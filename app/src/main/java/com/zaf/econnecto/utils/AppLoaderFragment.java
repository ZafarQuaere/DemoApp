package com.zaf.econnecto.utils;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.zaf.econnecto.ui.fragments.ProgressFragment;


public class AppLoaderFragment {

    private static ProgressFragment progressFrag = null;
    private static AppLoaderFragment loader = null;
    private static Context mContext;

    private AppLoaderFragment() {
        progressFrag = new ProgressFragment();
        progressFrag.setCancelable(false);

    }

    public static AppLoaderFragment getInstance(Context context) {
        mContext = context;
        if (loader == null) {
            return new AppLoaderFragment();
        }
        return loader;
    }

    public void show() {
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().add(progressFrag, "Progress").commitAllowingStateLoss();
        //progressFrag.showNow(((AppCompatActivity) mContext).getSupportFragmentManager(),"Progress");
        // progressFrag.showNow(fragmentManager,"");
    }

    public void dismiss() {
        progressFrag.dismiss();
    }

    public boolean isProgressVisible(){
     return progressFrag.isVisible();
    }
}
