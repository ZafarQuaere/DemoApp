package com.zaf.econnecto.ui.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.VolleyMultipartRequest;
import com.zaf.econnecto.ui.presenters.operations.IEditImage;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EditImagePresenter extends BasePresenter {
    Context mContext;
    IEditImage iEditImage;
    private AppLoaderFragment loader;

    public EditImagePresenter(Context context, IEditImage iEditImg) {
        super(context);
        iEditImage = iEditImg;
        mContext = context;
        loader = AppLoaderFragment.getInstance(mContext);
    }

    public void uploadBitmap(final Bitmap bitmapUpload/*, CircleImageView imgUserProfile*/) {
        loader.show();
        LogUtils.DEBUG("Upload URL : " + AppConstant.URL_ADD_IMAGE);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConstant.URL_ADD_IMAGE,
                response -> {
                    String uploadResponse = new String(response.data);
                    LogUtils.DEBUG("Add Image Response : " + uploadResponse);
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        int status = obj.optInt("status");
                        if (status == AppConstant.SUCCESS) {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), obj.optJSONArray("message").optString(0));
                        } else {
                            LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), obj.optJSONArray("message").optString(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loader.dismiss();
                },
                error -> {
                    Toast.makeText(mContext.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    LogUtils.VolleyError(error);
                    LogUtils.ERROR("Add Image Error " + error.getMessage());

                    loader.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jwt_token", Utils.getAccessToken(mContext));
                params.put("owner_id", Utils.getUserID(mContext));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imageName = System.currentTimeMillis();
//                byte[] fileDataFromDrawable = BitmapUtils.getFileDataFromDrawable(bitmapUpload);
//                DataPart name = new DataPart("name", fileDataFromDrawable);
                params.put("img", new DataPart(imageName + ".png", BitmapUtils.getFileDataFromDrawable(bitmapUpload)));
                return params;
            }

        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest);
    }
}
