package com.yalantis.ucrop.task;


import java.io.IOException;


public class BitmapCropTask {

    static {
        System.loadLibrary("ucrop");
    }

    @SuppressWarnings("JniMissingFunction")
    native public static boolean
    cropCImg(String inputPath, String outputPath,
             int left, int top, int width, int height,
             float angle, float resizeScale,
             int format, int quality,
             int exifDegrees, int exifTranslation) throws IOException, OutOfMemoryError;
}
