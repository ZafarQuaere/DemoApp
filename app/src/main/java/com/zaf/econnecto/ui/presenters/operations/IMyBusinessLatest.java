package com.zaf.econnecto.ui.presenters.operations;


import android.graphics.Bitmap;

import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData;
import com.zaf.econnecto.network_call.response_model.my_business.DealsBgData;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IMyBusinessLatest {

    void updateBannerImage(@NotNull List<? extends ViewImageData> data);
}
