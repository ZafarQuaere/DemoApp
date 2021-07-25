package com.zaf.econnecto.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zaf.econnecto.BuildConfig;
import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.interfaces.AddPhotoDialogListener;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener;

import static com.zaf.econnecto.utils.AppConstant.TAG;


public class LogUtils {

    public static void showToast(Context context, String message) {
        if (context != null) {
            final Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void DEBUG(String sb) {
        //To print the log on debug mode only
        if (BuildConfig.DEBUG) {
            if (sb.length() > 4000) {
                int chunkCount = sb.length() / 4000;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= sb.length()) {
                        Log.d(TAG, " >> " + sb.substring(4000 * i));
                    } else {
                        Log.d(TAG, " >> " + sb.substring(4000 * i, max));
                    }
                }
            } else {
                Log.d(TAG, " >> " + sb.toString());
            }
        }
    }

    /**
     * @param message
     */
    public static void ERROR(String message) {
        Log.e(TAG, " >> " + message);
    }

    public static void showSnackBar(Context context, ViewGroup layout, String msg) {
        Snackbar snackbar = Snackbar.make(layout, msg, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();
    }

    public static void showErrorDialog(Context ctx, String btnText, String message) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_single_button);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        TextView textMessage = (TextView) dialog.findViewById(R.id.text_message);
        textMessage.setText(message);
        dialogButton.setText(btnText);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDialogDoubleButton(Context ctx, String btnCancelTxt, String btnOkTxt, String message, final DialogButtonClick listener) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_double_button);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView textMessage = (TextView) dialog.findViewById(R.id.text_message);
        textMessage.setText(message);
        btnCancel.setText(btnCancelTxt);
        btnOk.setText(btnOkTxt);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onOkClick();
                }
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

    public static void showDialogSingleActionButton(Context ctx, String btnOkTxt, String message, final DialogSingleButtonListener listener) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_single_button);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        TextView textMessage = (TextView) dialog.findViewById(R.id.text_message);
        textMessage.setText(message);

        btnOk.setText(btnOkTxt);
        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) {
                listener.okClick();
            }
        });
        dialog.show();
    }

    public static void showAddPhotoDialog(Context ctx, AddPhotoDialogListener listener) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_photo);
        TextView textTakePhoto = dialog.findViewById(R.id.textTakePhoto);
        TextView textSelectGallery = dialog.findViewById(R.id.textSelectGallery);
        textTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.takePhoto();
                }
            }
        });
        textSelectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.selectFromGallery();
                }
            }
        });
        dialog.show();
    }

    public static void VolleyError(VolleyError error) {
        if (error instanceof TimeoutError) {
            //For example your timeout is 3 seconds but the operation takes longer
            Log.e(TAG, "TimeoutError: " + (error.getMessage()));
        } else if (error instanceof ServerError) {
            Log.e(TAG, "ServerError: " + (error.getMessage()));
        } else if (error instanceof NetworkError) {
            Log.e(TAG, "NetworkError: " + (error.getMessage()));
        } else if (error instanceof ParseError) {
            //for cant convert data
            Log.e(TAG, "ParseError: " + (error.getMessage()));
        } else if (error instanceof AuthFailureError) {
            //for cant convert data
            Log.e(TAG, "AuthFailureError: " + (error.getMessage()));
        } else {
            //other error
        }
        //error.networkResponse.statusCode

        //error.getCause() instanceof JsonSyntaxException

        Log.e(TAG, "NullPointerException: " + (error.getCause() instanceof NullPointerException));

                /*if(error.getCause() instanceof UnknownHostException ||
                    error.getCause() instanceof EOFException ) {
                    errorMsg = resources.getString(R.string.net_error_connect_network);
                } else {
                    if(error.getCause().toString().contains("Network is unreachable")) {
                        errorMsg = resources.getString(R.string.net_error_no_network);
                    } else {
                        errorMsg = resources.getString(R.string.net_error_connect_network);
                    }
                }*/

    }
}
