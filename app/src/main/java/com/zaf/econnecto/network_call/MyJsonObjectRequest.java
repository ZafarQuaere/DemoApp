package com.zaf.econnecto.network_call;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zaf.econnecto.R;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.FileUtils;
import com.zaf.econnecto.utils.LogUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class MyJsonObjectRequest extends JsonObjectRequest {

    private final SharedPreferences preferences;
    private Context mContext;

    public MyJsonObjectRequest(Context context,int method, String url, JSONObject requestBody,
                               Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

        mContext = context;
        //if server is not working pick the default response from local storage.
        if (mContext.getResources().getBoolean(R.bool.read_from_local)) {
            //here pick file from assets
            String fileName = null;
            if (url.contains(AppConstant.URL_LOGIN)) {
                fileName = "login.txt";
            } else if (url.contains(AppConstant.URL_BIZ_LIST)) {
                fileName = "business_list.txt";
            } else if (url.contains(AppConstant.URL_BIZ_CATEGORY)) {
                fileName = "categories.txt";
            } else if (url.contains(AppConstant.URL_REGISTER)) {
                fileName = "register.txt";
            }


            try {
                listener.onResponse(new JSONObject(FileUtils.loadJSONFromAsset(mContext, fileName)));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.ERROR(e.getMessage());
            }
        } else {
            LogUtils.DEBUG(AppConstant.TAG + " url : " + url);
        }

        setRetryPolicy(new DefaultRetryPolicy((int)context.getResources().getInteger(R.integer.value_request_timeout),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        LogUtils.DEBUG(" parseNetworkResponse statusCode >> " + response.statusCode);
        if (mContext.getResources().getBoolean(R.bool.read_from_local)){
            //if reading from local then return null to handle the call
            return Response.success(null,HttpHeaderParser.parseCacheHeaders(response));
        }
        //LogUtils.DEBUG(" [raw json]: " + (new String(response.data)));
        String jsonString = null;
        try {
            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            if (jsonString == null || jsonString.length() < 3)
                return Response.error(new ParseError(new NullPointerException()));

            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
        return Response.error(new ParseError());
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

}
