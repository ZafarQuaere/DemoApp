package com.yalantis.ucrop.task;

import android.util.Log;

import java.io.IOException;

public class NklCrop {

    // Used to load the 'ucrop' library.
    /*static {
        Log.d("SFM", "Before...");
        System.loadLibrary("ucrop");
        Log.d("SFM", "After");
    }*/

    public boolean cropImage(String inputPath, String outputPath,
                             int left, int top, int width, int height,
                             float angle, float resizeScale,
                             int format, int quality,
                             int exifDegrees, int exifTranslation) throws IOException, OutOfMemoryError {
        Log.d("SFM", "CropImage() method called...");

        return BitmapCropTask.cropCImg(inputPath, outputPath,
                left, top, width, height,
                angle, resizeScale,
                format, quality,
                exifDegrees, exifTranslation);
    }


   // @SuppressWarnings("JniMissingFunction")
    /*public static native boolean cropCImg(String inputPath, String outputPath,
                                          int left, int top, int width, int height,
                                          float angle, float resizeScale,
                                          int format, int quality,
                                          int exifDegrees, int exifTranslation);*/

}
