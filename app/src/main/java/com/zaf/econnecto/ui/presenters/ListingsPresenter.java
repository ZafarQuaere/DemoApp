package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.product_list.MyProductsData;
import com.zaf.econnecto.ui.presenters.operations.IFragListing;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;


import org.json.JSONObject;

public class ListingsPresenter extends BaseFragmentPresenter {
    private Context mContext;
    private IFragListing mProductFrag;
    private AppLoaderFragment loader;

    public ListingsPresenter(Context context, IFragListing productList) {
        super(context);
        mProductFrag = productList;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void callItemsApi() {
        loader.show();
        String url = AppConstant.URL_BASE + AppConstant.URL_PRODUCT_LIST + Utils.getDealerId(mContext);
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::");
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("ProductList Response ::" + response.toString());
                int status = response.optInt("status");
                if (status == AppConstant.SUCCESS) {
                    try {
                        MyProductsData productsData = ParseManager.getInstance().fromJSON(response.toString(), MyProductsData.class);
                        LogUtils.DEBUG("ProductList Response ::" + productsData.getData().toString());
                        mProductFrag.updateList(productsData);

                    } catch (Exception e) {
                        e.printStackTrace();
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

}
