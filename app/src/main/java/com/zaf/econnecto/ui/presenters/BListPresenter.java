package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.biz_list.BizData;
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData;
import com.zaf.econnecto.ui.presenters.operations.IFragListing;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;


import org.json.JSONObject;

import java.util.ArrayList;

public class BListPresenter extends BaseFragmentPresenter {
    private Context mContext;
    private IFragListing mProductFrag;
    private AppLoaderFragment loader;
    private BizListData data;

    public BListPresenter(Context context, IFragListing productList) {
        super(context);
        mProductFrag = productList;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void callBListApi() {
        loader.show();
        String url = AppConstant.URL_BASE_MVP + AppConstant.URL_BIZ_LIST;
        if (Utils.isLoggedIn(mContext)){
            url = AppConstant.URL_BASE_MVP + AppConstant.URL_BIZ_LIST+Utils.getUserID(mContext);
        }
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::");
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("ProductList Response ::" + response.toString());
                int status = response.optInt("status");
                if (status == AppConstant.SUCCESS_501) {
                    data = ParseManager.getInstance().fromJSON(response.toString(), BizListData.class);
                    try {
                        mProductFrag.updateList(data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.ERROR(e.getMessage());
                    }
                }else {
                    mProductFrag.updateList(null);
                }
                loader.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.DEBUG("ProductList Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "ProductList");
    }

        public void filterList(String bizName) {
            ArrayList<BizData> filterData = new ArrayList<>();

            for (BizData bizData : data.getData()){
            if (bizData.getBusinessName().toLowerCase().trim().contains(bizName.toLowerCase())){
                filterData.add(bizData);
            }
        }
            mProductFrag.updateList(filterData);
    }

    public void clearSearch() {
        mProductFrag.updateList(data.getData());
    }

    /*public void checkProgress() {
        if (loader != null &&loader.isProgressVisible()){
            loader.dismiss();
        }
    }*/
}
