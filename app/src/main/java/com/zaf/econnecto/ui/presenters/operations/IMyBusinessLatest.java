package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData;
import com.zaf.econnecto.network_call.response_model.my_business.BasicDetailsResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IMyBusinessLatest {

    void updateBannerImage(@NotNull List<? extends ViewImageData> data);
    void updateBasicDetails(BasicDetailsResponse basicDetailsResponse, boolean imageUpdate);
}
