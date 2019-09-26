package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetailData;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness;
import com.zaf.econnecto.ui.presenters.operations.IMyBusiness;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MyBusinessPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IMyBusiness iMyBusiness;

    public MyBusinessPresenter(Context context, IMyBusiness iBizDetail) {
        super(context);
        iMyBusiness = iBizDetail;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }


    public void callMyBizApi() {
        loader.show();
        String url = AppConstant.URL_BASE + AppConstant.URL_MY_BUSINESS;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" );
        JSONObject object = new JSONObject();
        try {
            object.put("user_email",Utils.getUserEmail(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("MyBusiness Response ::" + response.toString());
                try {
                    LogUtils.DEBUG("AddBusiness Response ::" + response.toString());
                    if (response != null && !response.equals("")) {
                        int status = response.optInt("status");
                        if (status == AppConstant.SUCCESS) {
                            MyBusiness bizDetailData = ParseManager.getInstance().fromJSON(response.toString(),MyBusiness.class);
                           iMyBusiness.updateUI(bizDetailData.getData().get(0));

                        } else {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                        }
                    }
                    loader.dismiss();
                } catch (Exception e) {
                    loader.dismiss();
                    e.printStackTrace();
                    LogUtils.ERROR(e.getMessage());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("MyBusiness Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness");
    }

}
