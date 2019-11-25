package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.home.BizCategoryData;
import com.zaf.econnecto.ui.presenters.operations.IFragHome;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.parser.ParseManager;


import org.json.JSONObject;

public class BizCategoryPresenter extends BaseFragmentPresenter {
    private Context mContext;
    private IFragHome mIFragHome;
    private AppLoaderFragment loader;

    public BizCategoryPresenter(Context context, IFragHome iFragHome) {
        super(context);
        mIFragHome = iFragHome;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }


    public void callCategoryApi() {
        loader.show();
        String url = AppConstant.URL_BIZ_CATEGORY;// + AppConstant.listUrl+ 2;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" );
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Biz Category Response ::" + response.toString());
                BizCategoryData data = ParseManager.getInstance().fromJSON(response.toString(), BizCategoryData.class);
                if (data.getStatus().equals(AppConstant.SUCCESS)){
                    mIFragHome.updateCategory(data.getData());
                }else {
                    mIFragHome.updateCategory(data.getData());
                }

              loader.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("Biz Category Error ::" + error.getMessage());
                loader.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Biz Category");
    }



}
