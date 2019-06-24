package com.zaf.econnecto.ui.presenters;

import android.content.Context;


import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.operations.IOtp;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpPresenter extends BasePresenter {
    private Context mContext;
    private IOtp iOtp;

    public OtpPresenter(Context context, IOtp iOtp) {
        super(context);
        this.iOtp = iOtp;
        mContext = context;
    }

    public void validateOtp(String otp) {
        if (otp.equals("") || otp.isEmpty()) {
            iOtp.onValidationError(mContext.getString(R.string.please_enter_otp));
        } else if (otp.length() < 4) {
            iOtp.onValidationError(mContext.getString(R.string.enter_valid_otp));
        } else {
            validateServerOtp(otp);
        }
    }

    public void validateServerOtp(String otp) {
        String otpData = Utils.getOTPData(mContext);
      //  LogUtils.DEBUG("OTP DATA : "+otpData);
        try {
            JSONObject jObj = new JSONObject(otpData);
            String otpText = jObj.getString("sms_text");
            String serverOtp = otpText.substring(0, 4);
            LogUtils.DEBUG("OTP :: "+serverOtp);
            if (serverOtp.equals(otp)){
                iOtp.submitOtp();
            }else {
                iOtp.onValidationError(mContext.getString(R.string.enter_valid_otp));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
    }
}
