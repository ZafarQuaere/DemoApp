package com.zaf.econnecto.ui.presenters.operations;


import android.graphics.Bitmap;

import com.zaf.econnecto.network_call.response_model.my_business.DealsBgData;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData;

import java.util.List;

public interface IMyBusiness {

    void updateUI(MyBusinessData bizDetails);
    void onValidationError(String msg);

    void updateBizData(String address, String mobile, String email, String website,String shortDesc,String detailDesc);

    void updateDealBackground(List<DealsBgData> dealsBgData);

    void updateImage(int imageType, Bitmap bitmapUpload);
}
