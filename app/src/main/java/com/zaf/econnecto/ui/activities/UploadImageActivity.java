package com.zaf.econnecto.ui.activities;

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
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.PermissionUtils;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnChooseImg;
    Button btnUploadImage;
    EditText editText2;
    ImageView imageView;
    private Context mContext;
    private Uri selectedImageUri;
    private static int IMG_SELECT_GALLERY_REQUEST = 1;

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
        imageView = (ImageView)findViewById(R.id.imageView);
        btnChooseImg.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnUploadImage){
            if (editText2.getText().toString().trim().isEmpty()){
                LogUtils.showToast(mContext,"Please enter image name");
            }else {
                uploadImage();
            }
        }else if (v.getId() == R.id.btnChooseImg){
            if (PermissionUtils.checkPermission(mContext)) {
                selectImgFromGallery();
            } else {
                PermissionUtils.requestPermission(UploadImageActivity.this);
            }
        }
    }

    private void uploadImage() {

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
        if (resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();
            if (requestCode == IMG_SELECT_GALLERY_REQUEST ){
                Bitmap bitmap = BitmapUtils.getBitmap(mContext,data,selectedImageUri);
               // imgProfile.setImageBitmap(bitmap);
                imageView.setImageURI(selectedImageUri);
            }else {
                imageView.setImageURI(selectedImageUri);
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

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
