package com.zaf.econnecto.crop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.zaf.econnecto.R;
import com.zaf.econnecto.utils.AppConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class CroppingHelper {
    private static final String TAG = CroppingHelper.class.getSimpleName();

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final String BEFORE_CROP_IMAGE = "BEFORE_CROP_IMAGE";
    private static final String AFTER_CROP_IMAGE = "AFTER_CROP_IMAGE";

    private Activity mActivity;
    private AlertDialog mAlertDialog;

    public CroppingHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Intent startCropping(Bitmap bitmap) {
        Uri uri = getImageUri(mActivity, bitmap);
        return startCrop(uri);
    }

    private Intent startCrop(@NonNull Uri uri) {
        String destinationFileName = AFTER_CROP_IMAGE;
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(mActivity.getCacheDir(), destinationFileName)));
        UCrop.Options options = new UCrop.Options();
        options.useSourceImageAspectRatio();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setHideBottomControls(false);
        options.setAllowedGestures(AppConstant.SCALE, AppConstant.ROTATE, AppConstant.ALL);
        options.setToolbarTitle("DSC 0237.JPG");
        uCrop.withOptions(options);
        return uCrop.getIntent(mActivity);
        //uCrop.start(mActivity);
    }

    public Uri getImageUri(Context inContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmap, BEFORE_CROP_IMAGE, null);
        return Uri.parse(path);
    }



    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(mActivity, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
          //  Toast.makeText(mActivity, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private String getString(int strResId) {
        return mActivity.getString(strResId);
    }

}
