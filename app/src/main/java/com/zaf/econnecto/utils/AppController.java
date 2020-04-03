package com.zaf.econnecto.utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zaf.econnecto.utils.validator.Validator;
import com.zaf.econnecto.utils.validator.ValidatorImpl;


public class AppController extends Application {

    public static AppController mInstance;
    private Validator mValidator;
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // create validator
        mValidator = new ValidatorImpl();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Validator getValidator() {
        return mInstance.mValidator;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
           // LogUtils.DEBUG("Appcontroller >> getRequestQueue() >> New volley request");
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
       // LogUtils.DEBUG("Appcontroller >> getRequestQueue() >> added request to queue");
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
