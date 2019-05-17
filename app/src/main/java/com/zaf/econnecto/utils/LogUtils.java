package com.zaf.econnecto.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;


public class LogUtils {

    public static void showToast(Context context, String message) {
        if (context != null) {
            final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void DEBUG(String sb) {
        if (sb.length() > 4000) {
            int chunkCount = sb.length() / 4000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= sb.length()) {
                    Log.d(AppConstant.TAG, " >> " + sb.substring(4000 * i));
                } else {
                    Log.d(AppConstant.TAG, " >> " + sb.substring(4000 * i, max));
                }
            }
        } else {
            Log.d(AppConstant.TAG, " >> " + sb.toString());
        }
    }

    public static void ERROR(String message) {
        Log.e(AppConstant.TAG, " >> " + message);
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
                if (listener != null){
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

}
