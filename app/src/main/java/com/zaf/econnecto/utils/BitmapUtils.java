package com.zaf.econnecto.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapUtils {

    public static final int URI_IMAGE = 50;
    public static final int BITMAP_IMAGE = 55;
    /**
     * Create file with current timestamp name
     *
     * @throws IOException
     */
    public static File createImageFile(Activity activity) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = null;
        try {
            mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mFile;
    }

    /**
     * Get real file path from URI
     */
    public static String getImagePath(Activity activity, Uri imageUri, Bitmap bitmap, int imageType) {
        String path = null;
        if (imageType == URI_IMAGE){
            path = getPathFromUri(activity,imageUri);
        } else {
 //           path = getPathFromUri(activity,getUriFromBitmap(activity,bitmap));
           path = getPathFromBitmap(activity,bitmap);
        }
        return path;
    }

    private static String getPathFromBitmap(Activity activity, Bitmap bitmap) {
        String[] FILE = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(getUriFromBitmap(activity,bitmap),FILE, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FILE[0]);
        String decodedImage = cursor.getString(columnIndex);
        cursor.close();
        return decodedImage;
    }

    private static String getPathFromUri(Activity activity, Uri imageUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = activity.getContentResolver().query(imageUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Uri getUriFromBitmap(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmap(Context mContext,  Uri selectedImageUri) {
      /*  String[] FILE = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(selectedImageUri,FILE, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FILE[0]);
        String decodedImage = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(decodedImage);*/
        Bitmap bitmap = null;
        try {
            /*BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(selectedImage), null, o);*/
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImageUri), null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap resizeBitmapProfile(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap,200,200,true);
    }

    public static Bitmap resizeBitmapBanner(Bitmap bitmap,int maxWidth,int maxHeight) {
       /* int maxWidth = 900;
        int maxHeight = 266;
        float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);*/
        //Bitmap mbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Bitmap mbitmap =   Bitmap.createScaledBitmap(bitmap,maxWidth,maxHeight,true);
        if (getByteArrayFromBitmap(bitmap).length > (500 * 1000)) {
            resizeBitmapBanner(mbitmap,maxWidth-50,maxHeight-20);
            // reduce size again
        } else {
            return mbitmap;
        }
        return mbitmap;
    }



    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        LogUtils.DEBUG("Image size is : "+(bytes.length)/1024);
//        LogUtils.DEBUG("Image size is : "+KotUtil.getReadableFileSize(bytes.length));
        return bytes;
    }


    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        //Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //  Bitmap mbitmap =   Bitmap.createScaledBitmap(bitmap,200,200,true);
        Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        //canvas.drawCircle(200, 200, 100, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return bitmap;
    }

    public static Bitmap getBitmapFromAssets(Context context, String fileName, int width, int height) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            istr = assetManager.open(fileName);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, width, height);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(istr, null, options);
        } catch (IOException e) { LogUtils.ERROR( "Exception: " + e.getMessage()); }
        return null;
    }

    /**
     * Getting bitmap from Gallery
     *
     * @return
     */
    public static Bitmap getBitmapFromGallery(Context context, Uri path, int width, int height) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(path, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Storing image to device gallery
     *
     * @param cr
     * @param source
     * @param title
     * @param description
     * @return
     */
    public static final String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        Uri url = null;
        String stringUrl = null;    /* value to be returned */
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }
                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }
        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @see MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();
        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();
        matrix.setScale(scaleX, scaleY);
        Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());
        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm,int maxWidth,int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Log.v("Pictures", "Width and height are " + width + "--" + height);
        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }
        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static void saveProfileImage(Context mContext, Bitmap bitmap) {
        new ImageSaver(mContext).setFileName("profileImage.png").setDirectoryName("profile").save(bitmap);
    }

    public static Bitmap getProfileBitmap(Context mContext){
        Bitmap bitmap = new ImageSaver(mContext).setFileName("profileImage.png").
                setDirectoryName("profile").load();
        return bitmap;
    }

    /**
     *
     * @param image : input bitmap.
     * @return converted file.
     */
    public String storeImage(Bitmap image, String filepath) {
        Bitmap image1= Bitmap.createBitmap(image);
        File pictureFile = new File(filepath);
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image1.compress(Bitmap.CompressFormat.JPEG,100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("File not found", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("Not able to access file", "Error accessing file: " + e.getMessage());
        }
        return pictureFile.toString();
    }


    public static Bitmap getBitmapFromUri(Context mContext,Uri path, int width, int height) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(path, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
       // return BitmapFactory.decodeFile(picturePath, options);
        return BitmapFactory.decodeFile(picturePath);
    }

}
