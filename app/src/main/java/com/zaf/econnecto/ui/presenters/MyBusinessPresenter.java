package com.zaf.econnecto.ui.presenters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerView;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.VolleyMultipartRequest;
import com.zaf.econnecto.network_call.response_model.my_business.AddDealsBg;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusiness;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData;
import com.zaf.econnecto.ui.presenters.operations.IMyBusiness;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.CustomFlag;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.zaf.econnecto.ui.activities.MyBusinessActivity.IMG_BANNER_RESULT;
import static com.zaf.econnecto.ui.activities.MyBusinessActivity.IMG_HOT_DEALS;

public class MyBusinessPresenter extends BasePresenter {
    private final AppLoaderFragment loader;
    private Context mContext;
    private IMyBusiness iMyBusiness;
    private MyBusiness bizDetailData;
    private AddDealsBg addDealsBg;

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
                            bizDetailData = ParseManager.getInstance().fromJSON(response.toString(),MyBusiness.class);
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
        final TextInputEditText editSD = (TextInputEditText) dialog.findViewById(R.id.editShortDescription);
        final TextInputEditText editDD = (TextInputEditText) dialog.findViewById(R.id.editLongDescription);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setText(mContext.getString(R.string.cancel));
        btnOk.setText(mContext.getString(R.string.update));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                validateFields(editAddress.getText().toString().trim(),editMobile.getText().toString().trim(),
                        editEmail.getText().toString().trim(),editBizWebsite.getText().toString().trim(),
                        editSD.getText().toString().trim(),editDD.getText().toString().trim());

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

    private void validateFields(String address, String mobile, String email, String website, String shortDesc, String detailDesc) {
        MyBusinessData myBusinessData = bizDetailData.getData().get(0);
        if (address.isEmpty()){
            address = myBusinessData.getAddress();
        } if (mobile.isEmpty()){
            mobile = myBusinessData.getPhone1();
        } if (email.isEmpty()){
            email = myBusinessData.getBusinessEmail();
        } if (website.isEmpty()){
            website = myBusinessData.getWebsite();
        } if (shortDesc.isEmpty()){
            shortDesc = myBusinessData.getShortDescription();
        } if (detailDesc.isEmpty()){
            detailDesc = myBusinessData.getDetailedDescription();
        }

        callUpdateBizApi(address,mobile,email,website,shortDesc,detailDesc,myBusinessData);
    }

    private void callUpdateBizApi(final String address, final String mobile, final String email, final String website, final String shortDesc, final String detailDesc, MyBusinessData myBusinessData) {
        loader.show();
        String url = AppConstant.URL_BASE+AppConstant.URL_UPDATE_BUSINESS;

        JSONObject object = new JSONObject();
        try {
            object.put("owner_email",Utils.getUserEmail(mContext));
            object.put("short_description",shortDesc);
            object.put("business_category",myBusinessData.getBusinessCategory());
            object.put("detailed_description",detailDesc);
            object.put("year_founded",myBusinessData.getYearFounded());
            object.put("awards","");
            object.put("address",address);
            object.put("phone1", mobile);
            object.put("phone2", myBusinessData.getPhone2());
            object.put("business_email", email);
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
                            iMyBusiness.updateBizData(address,mobile,email,website,shortDesc,detailDesc);
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

    public void callDealBgApi(){
        String url = AppConstant.URL_DEAL_BACKGROUND;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" );

        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.GET, url, null, response -> {
            LogUtils.DEBUG("Deal Background Response ::" + response.toString());
            try {
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        addDealsBg = ParseManager.getInstance().fromJSON(response.toString(), AddDealsBg.class);
                        iMyBusiness.updateDealBackground(addDealsBg.getData());

                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), response.optString("message"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.ERROR(e.getMessage());
            }

        }, error -> LogUtils.DEBUG("MyBusiness Error ::" + error.getMessage()));
        AppController.getInstance().addToRequestQueue(objectRequest, "MyBusiness");
    }


    public void uploadBitmap(final Bitmap bitmapUpload, final int imageType,final String isImage,final String categoryId) {
        loader.show();
        String uploadUrl = "";
        String upload_type = "";
        if (imageType == IMG_BANNER_RESULT) {
            uploadUrl = AppConstant.URL_UPLOAD_BUSINESS_BANNER_PIC;
            upload_type = "banner_image";
        }else if (imageType == IMG_HOT_DEALS) {
            uploadUrl = AppConstant.URL_ADD_DEAL;
            upload_type = "deal_image";
        }
        else {
            uploadUrl = AppConstant.URL_UPLOAD_BUSINESS_PROFILE_PIC;
            upload_type = "business_profile_image";
        }
        LogUtils.DEBUG("URL : " + uploadUrl);
        final String finalUpload_type = upload_type;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl,
                response -> {
                    String s = new String(response.data);
                    LogUtils.DEBUG("Upload Pic Response : " + s);
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        int status = obj.optInt("status");
                        if (status == AppConstant.SUCCESS){
                            if (imageType == IMG_BANNER_RESULT){
                                iMyBusiness.updateImage(imageType,bitmapUpload);
                            }else {
                                iMyBusiness.updateImage(imageType,bitmapUpload);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loader.dismiss();
                },
                error -> {
                    LogUtils.showToast(mContext,error.getMessage());
                    LogUtils.ERROR("Upload profile pic Error " + error);
                    loader.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (imageType == IMG_HOT_DEALS) {
                    params.put("owner_email", Utils.getUserEmail(mContext));
                    params.put("category_id", categoryId);
                    params.put("is_image", isImage);
                } else {
                    params.put("user_email", Utils.getUserEmail(mContext));
                }
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put(finalUpload_type, new DataPart(imagename + ".png", BitmapUtils.getFileDataFromDrawable(bitmapUpload)));

                return params;
            }

        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding the request to volley
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest);
    }


    public void submitHotDealsApi() { loader.show();
        String url = AppConstant.URL_ADD_DEAL ;
        LogUtils.DEBUG("URL : " + url + "\nRequest Body ::" );
        JSONObject object = new JSONObject();
        try {
            object.put("owner_email",Utils.getUserEmail(mContext));
            object.put("category_id","5");
            object.put("is_image",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, object, response -> {
            LogUtils.DEBUG("Submit HotDeals Response ::" + response.toString());
            try {
                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status == AppConstant.SUCCESS) {
                        bizDetailData = ParseManager.getInstance().fromJSON(response.toString(),MyBusiness.class);
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

        }, error -> {
            loader.dismiss();
            LogUtils.DEBUG("SubmitHotDeals Error ::" + error.getMessage());
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "SubmitHotDeals");
    }

    public void showColorPickerDialog(RelativeLayout rlyBG) {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("ColorPicker Dialog");
        builder.setPreferenceName("MyColorPickerDialog");

        builder.setPositiveButton(mContext.getString(R.string.confirm), envelope -> rlyBG.setBackgroundColor(envelope.getColor()));
        builder.setNegativeButton(mContext.getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());

        ColorPickerView colorPickerView = builder.getColorPickerView();
        colorPickerView.setPaletteDrawable(mContext.getResources().getDrawable(R.drawable.palettebar));
        colorPickerView.setFlagView(new CustomFlag(mContext, R.layout.layout_color_flag));
        builder.show();

    }
}
