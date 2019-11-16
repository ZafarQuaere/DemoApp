package com.zaf.econnecto.crop.callback;

import android.graphics.Bitmap;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zaf.econnecto.crop.model.ExifInfo;


public interface BitmapLoadCallback {

    void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath);

    void onFailure(@NonNull Exception bitmapWorkerException);

}