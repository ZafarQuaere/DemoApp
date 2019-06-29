package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.ui.presenters.operations.IAddBiz;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

public class AddBizPresenter extends BaseFragmentPresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IAddBiz iAddBiz;

    public AddBizPresenter(Context context, IAddBiz iAddBiz) {
        super(context);
        this.iAddBiz = iAddBiz;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void validateFields(String bizName, String shortDesc, String bizCategory, String detailDesc, String foundYear, String awards, String address, String phone1,
                               String phone2, String email, String website, String bizCharges, String amount) {

        if (bizName.equals("") || bizName.isEmpty()) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_business_name));
        } else if (shortDesc.equals("") || shortDesc.isEmpty()) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_a_short_description));
        } else if (bizCategory == null || bizCategory.equals(mContext.getString(R.string.Select_Category)) ) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_select_business_category));
        } else if (detailDesc.equals("") || detailDesc.isEmpty()) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_a_detail_description));
        } else if (foundYear.equals("") || foundYear.isEmpty()) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_foundation_year_of_business));
        } else if (address.equals("") || address.isEmpty()) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_address));
        } else if (phone1.equals("") || phone1.isEmpty() || phone1.length() < 10) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_valid_mobile_number));
        } else if (email.equals("") || email.isEmpty()) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_valid_email));
        }else if (!Utils.isValidEmail(email)) {
            iAddBiz.onValidationError(mContext.getString(R.string.please_enter_valid_email));
        } else {
            if (NetworkUtils.isNetworkEnabled(mContext)) {
                callAddBizApi(bizName, shortDesc,bizCategory,detailDesc,foundYear,awards,address,phone1,phone2,email,website,bizCharges,amount);
            } else {
                iAddBiz.onValidationError(mContext.getString(R.string.please_check_your_network_connection));
            }

        }

    }


    public void callAddBizApi(String bizName, String shortDesc, String bizCategory, String detailDesc, String foundYear, String awards, String address, String phone1,
                              String phone2, String email, String website, String bizCharges, final String amount) {
        loader.show();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("owner_email", Utils.getUserEmail(mContext));
            requestObject.put("business_name", bizName);
            requestObject.put("short_description", shortDesc);
            requestObject.put("business_category", bizCategory);
            requestObject.put("detailed_description", detailDesc);
            requestObject.put("year_founded", foundYear);
            requestObject.put("awards", awards);
            requestObject.put("address", address);
            requestObject.put("phone1", phone1);
            requestObject.put("phone2", phone2);
            requestObject.put("business_email", email);
            requestObject.put("website", website);
            requestObject.put("delivery_area", "delivery_area");
            requestObject.put("charges_unit", bizCharges);
            requestObject.put("charges_amount", amount);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        String url = AppConstant.URL_BASE + AppConstant.URL_ADD_BUSINESS;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" + requestObject.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("AddBusiness Response ::" + response.toString());
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        iAddBiz.addBusiness(mContext.getString(R.string.add_business_success_msg));
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }
                }
                loader.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                LogUtils.ERROR("AddBusiness Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "AddBusiness");
    }
}
