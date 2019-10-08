package com.zaf.econnecto.ui.presenters.operations;

import android.graphics.Bitmap;

public interface IMain {

   void onLogoutCall();

    void showAddBizFab(boolean show);

    void updateVerifyEmailUI();

    void updateProfilePic(Bitmap bitmap);
}
