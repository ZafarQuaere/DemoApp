package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetails;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData;
import com.zaf.econnecto.ui.presenters.MyBusinessPresenter;
import com.zaf.econnecto.ui.presenters.operations.IMyBusiness;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
//import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MyBusinessActivity extends BaseActivity<MyBusinessPresenter> implements IMyBusiness, View.OnClickListener {

    private Context mContext;
    private TextView textFollowers;
    private ImageButton imgBannerUpload;
    private ImageButton imgProfileUpload;
    private CircleImageView imgProfile;
    private ImageView imgBanner;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 200;
    private static int IMG_PROFILE_RESULT = 1;
    private static int IMG_BANNER_RESULT = 2;
    private static int IMG_SELECTED_FOR ;
    private Uri selectedImageUri;
    private TextView textPhone;

    @Override
    protected MyBusinessPresenter initPresenter() {
        return new MyBusinessPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business);
        mContext = this;
        initUI();
        getPresenter().callMyBizApi();
        //Utils.updateActionBar(this,new BizDetailsActivity().getClass().getSimpleName(),getString(R.string.biz_details), null,null);


    }

    private void initUI() {
        final Toolbar toolbar = findViewById(R.id.toolbarBd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                onBackPressed();
            }
        });

        imgBannerUpload = (ImageButton) findViewById(R.id.imgBannerUpload);
        imgProfileUpload = (ImageButton) findViewById(R.id.imgProfileUpload);


        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgBanner = (ImageView) findViewById(R.id.imgBanner);


        imgBannerUpload.setOnClickListener(this);
        imgProfileUpload.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }


    @Override
    public void updateUI(MyBusinessData bizDetails) {
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(bizDetails != null ? bizDetails.getBusinessName() : getString(R.string.business_details));
        textFollowers = (TextView) findViewById(R.id.textFollowers);
        TextView textAddress = (TextView) findViewById(R.id.textAddress);
        textPhone = (TextView) findViewById(R.id.textPhone);
        TextView textEmail = (TextView) findViewById(R.id.textEmail);
        TextView textWebsite = (TextView) findViewById(R.id.textWebsite);
        TextView textShortDescription = (TextView) findViewById(R.id.textShortDescription);
        TextView textDetailDescription = (TextView) findViewById(R.id.textDetailDescription);
        textFollowers.setText(bizDetails.getFollowersCount());
        textAddress.setText(bizDetails.getAddress());
        textPhone.setText(bizDetails.getPhone1());
        textEmail.setText(bizDetails.getBusinessEmail());
        textWebsite.setText(bizDetails.getWebsite());
        textShortDescription.setText(bizDetails.getShortDescription());
        textDetailDescription.setText(bizDetails.getDetailedDescription());
        Picasso.get().load(bizDetails.getBusinessPic()).placeholder(R.drawable.avatar_male).into(imgProfile);
        Picasso.get().load(bizDetails.getBannerPic()).placeholder(R.drawable.gradient).into(imgBanner);
        imgBannerUpload.bringToFront();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textPhone:
               // Utils.callPhone(mContext, .getPhone1());
                break;


            case R.id.imgBannerUpload:
                IMG_SELECTED_FOR = IMG_BANNER_RESULT;
                if (checkPermission()) {
                    selectImgFromGallery();
                } else {
                    requestPermission();
                }

                break;

            case R.id.imgProfileUpload:
                IMG_SELECTED_FOR = IMG_PROFILE_RESULT;
                if (checkPermission()) {
                    selectImgFromGallery();
                } else {
                    requestPermission();
                }

                break;
        }
    }

    private void selectImgFromGallery() {
        Intent  intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_SELECTED_FOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();
            if (requestCode == IMG_PROFILE_RESULT ){
                Bitmap bitmap = getBitmap(data);
                imgProfile.setImageBitmap(getCircledBitmap(bitmap));
                    //imgProfile.setImageURI(selectedImageUri);
            }else {
                imgBanner.setImageURI(selectedImageUri);
            }
        }
    }

    private Bitmap getBitmap(Intent data) {
        String[] FILE = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImageUri,
                FILE, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FILE[0]);
        String decodedImage = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeFile(decodedImage);
        return bitmap;
    }

   public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, CAMERA}, STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0){
                        boolean readAccpeted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        if (readAccpeted /*&& writeAccepted*/){
                            selectImgFromGallery();
                        }else if (cameraPermission){
                            captureFromCamera();
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
                                                                STORAGE_PERMISSION_REQUEST_CODE);
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

    private void captureFromCamera() {

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
