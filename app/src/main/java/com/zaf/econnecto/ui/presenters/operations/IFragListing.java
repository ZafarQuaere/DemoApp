package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.biz_list.BizData;

import java.util.List;

public interface IFragListing {


    void updateList(List<BizData> data);

   // void clearSearch();
}
