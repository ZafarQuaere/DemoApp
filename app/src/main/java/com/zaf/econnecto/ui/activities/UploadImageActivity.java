package com.zaf.econnecto.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.VolleyMultipartRequest;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppDialogLoader;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.PermissionUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnChooseImg;
    Button btnUploadImage;
    EditText editText2;
    ImageView imageBanner;
    ImageView imageBanner1;
    ImageView imageProfile;
    private Context mContext;
    private Uri selectedImageUri;
    private static int IMG_SELECT_GALLERY_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        mContext = this;
        initUI();
    }

    private void initUI() {
        btnChooseImg = (Button)findViewById(R.id.btnChooseImg);
        btnUploadImage = (Button)findViewById(R.id.btnUploadImage);
        editText2 = (EditText)findViewById(R.id.editText2);
        imageBanner = (ImageView)findViewById(R.id.imageBanner);
        imageBanner1 = (ImageView)findViewById(R.id.imageBanner1);
        imageProfile = (ImageView)findViewById(R.id.imageProfile);
        btnChooseImg.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUploadImage) {
            uploadBitmap(bitmap);
        } else if (v.getId() == R.id.btnChooseImg) {
            if (PermissionUtils.checkPermission(mContext)) {
                selectImgFromGallery();
            } else {
                PermissionUtils.requestPermission(UploadImageActivity.this);
            }
        }
    }


    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imgBytes = outputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }



    private void selectImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_SELECT_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();
            if (requestCode == IMG_SELECT_GALLERY_REQUEST) {
               bitmap = BitmapUtils.getBitmap(mContext, selectedImageUri);
                // imgProfile.setImageBitmap(bitmap);
                imageBanner.setImageURI(selectedImageUri);
                imageBanner1.setImageURI(selectedImageUri);
                imageProfile.setImageURI(selectedImageUri);

            } else {
                imageBanner.setImageURI(selectedImageUri);
                imageBanner1.setImageURI(selectedImageUri);
                imageProfile.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0){
                    boolean readAccpeted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readAccpeted /*&& writeAccepted*/){
                        selectImgFromGallery();
                    }else if (cameraPermission){
                       // captureFromCamera();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to of Read Permission to upload Banner and Profile",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE/*, WRITE_EXTERNAL_STORAGE*/},
                                                            PermissionUtils.STORAGE_PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }


    public void uploadBitmap(final Bitmap bitmapUpload/*, CircleImageView imgUserProfile*/) {
        AppDialogLoader loader = AppDialogLoader.getLoader(mContext);
        loader.show();
        LogUtils.DEBUG("Upload URL : " + AppConstant.URL_UPLOAD_USER_PROFILE_PIC);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConstant.URL_UPLOAD_USER_PROFILE_PIC,
                response -> {
                    String uploadResponse = new String(response.data);
                    LogUtils.DEBUG("Upload Profile Pic Response : " + uploadResponse);
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        int status = obj.optInt("status");
                        if (status == AppConstant.SUCCESS) {
                          // LogUtils.showToast(mContext,"Image uploaded successfully");

                        }
                        Toast.makeText(mContext.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loader.dismiss();
                },
                error -> {
                    Toast.makeText(mContext.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    LogUtils.ERROR("Upload profile pic Error " + error);
                    loader.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", "zafima20@gmail.com");
                LogUtils.DEBUG("user_email " + Utils.getUserEmail(mContext));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("user_image", new DataPart(imagename + ".png", BitmapUtils.getByteArrayFromBitmap(bitmapUpload)));
                return params;
            }

        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(mContext).add(volleyMultipartRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LogUtils.showToast(mContext,s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_upload_img) {
            startActivity(new Intent(this,UploadImageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
