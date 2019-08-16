package com.zaf.econnecto.ui.presenters;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetailData;
import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetails;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.operations.IBizDetail;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.parser.ParseManager;


import org.json.JSONObject;

public class BizDetailPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IBizDetail mSellerAddress;

    public BizDetailPresenter(Context context, IBizDetail iBizDetail) {
        super(context);
        mSellerAddress = iBizDetail;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }


    public void callBizDetailApi(final String biz_id) {
        loader.show();

        String url = AppConstant.URL_BASE + AppConstant.URL_BIZ_DETAIL+biz_id;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" );
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("BizDetail Response ::" + response.toString());
                BizDetailData bizDetailData = ParseManager.getInstance().fromJSON(response.toString(),BizDetailData.class);
                try {
                    if (bizDetailData.getStatus().equals(AppConstant.SUCCESS)){
                        loader.dismiss();
                        BizDetails bizDetails = bizDetailData.getData().get(0);
                        mSellerAddress.updateUI(bizDetails);
                    }else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), bizDetailData.getMessage());
                    }
                    loader.dismiss();
                } catch (Exception e) {
                    loader.dismiss();
                    e.printStackTrace();
                    LogUtils.ERROR(e.getMessage());
                    LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.something_wrong_from_server_plz_try_again), new DialogButtonClick() {
                        @Override
                        public void onOkClick() {
                            ((Activity)mContext).onBackPressed();
                        }
                        @Override
                        public void onCancelClick() { }
                    });
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("BizDetail Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "BizDetail");
    }


}
