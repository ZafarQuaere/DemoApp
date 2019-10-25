package com.zaf.econnecto.ui.presenters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
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



    public void showUpdateBizDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.dialogUpDownAnimation);
        dialog.setContentView(R.layout.dialog_update_biz);
        final TextInputEditText editAddress = (TextInputEditText) dialog.findViewById(R.id.editAddress);
        final TextInputEditText editMobile = (TextInputEditText) dialog.findViewById(R.id.editMobile);
        final TextInputEditText editEmail = (TextInputEditText) dialog.findViewById(R.id.editEmail);
        final TextInputEditText editBizWebsite = (TextInputEditText) dialog.findViewById(R.id.editBizWebsite);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setText(mContext.getString(R.string.cancel));
        btnOk.setText(mContext.getString(R.string.update));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callUpdateBizApi(editAddress.getText().toString().trim(),editMobile.getText().toString().trim(),
                        editEmail.getText().toString().trim(),editBizWebsite.getText().toString().trim());

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callUpdateBizApi(final String address, final String mobile, final String email, final String website) {
        loader.show();
        String url = AppConstant.URL_BASE + "update_business";

        JSONObject object = new JSONObject();
        try {
            object.put("address",address);
            object.put("mobile",mobile);
            object.put("user_email",email);
            object.put("website",website);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" +object.toString());
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("UpdateBusiness Response ::" + response.toString());
                try {
                    if (response != null && !response.equals("")) {
                        int status = response.optInt("status");
                        if (status == AppConstant.SUCCESS) {
                            iMyBusiness.updateBizData(address,mobile,email,website);
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
                LogUtils.DEBUG("UpdateBusiness Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "UpdateBusiness");
    }
}
