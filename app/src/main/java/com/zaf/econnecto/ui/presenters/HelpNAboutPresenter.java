package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.zaf.econnecto.ui.presenters.operations.IHelpAbout;
import com.zaf.econnecto.utils.AppLoaderFragment;


public class HelpNAboutPresenter extends BaseFragmentPresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IHelpAbout iFrgtPswd;

    public HelpNAboutPresenter(Context context, IHelpAbout iFrgtPswd) {
        super(context);
        this.iFrgtPswd = iFrgtPswd;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }


}
