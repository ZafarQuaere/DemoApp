package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.crop.CroppingHelper;
import com.zaf.econnecto.crop.UCrop;
import com.zaf.econnecto.crop.callback.BitmapCropCallback;
import com.zaf.econnecto.crop.view.CropImageView;
import com.zaf.econnecto.crop.view.GestureCropImageView;
import com.zaf.econnecto.crop.view.OverlayView;
import com.zaf.econnecto.crop.view.UCropView;
import com.zaf.econnecto.network_call.VolleyMultipartRequest;
import com.zaf.econnecto.network_call.response_model.my_business.MyBusinessData;
import com.zaf.econnecto.ui.presenters.MyBusinessPresenter;
import com.zaf.econnecto.ui.presenters.operations.IMyBusiness;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.BitmapUtils;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.zaf.econnecto.utils.AppConstant.ALL;
import static com.zaf.econnecto.utils.AppConstant.DEFAULT_COMPRESS_FORMAT;
import static com.zaf.econnecto.utils.AppConstant.DEFAULT_COMPRESS_QUALITY;
import static com.zaf.econnecto.utils.AppConstant.NONE;
import static com.zaf.econnecto.utils.AppConstant.ROTATE;
import static com.zaf.econnecto.utils.AppConstant.SCALE;


public class MyBusinessActivity extends BaseActivity<MyBusinessPresenter> implements IMyBusiness, View.OnClickListener {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 200;
    private static int IMG_PROFILE_RESULT = 1;
    private static int IMG_BANNER_RESULT = 2;
    private static int IMG_SELECTED_FOR;
    private Context mContext;
    private TextView textFollowers;
    private ImageButton imgBannerUpload;
    private ImageButton imgProfileUpload;
    private CircleImageView imgProfile;
    private ImageView imgBanner;
    private Uri selectedImageUri;
    private TextView textPhone;
    private AppLoaderFragment loader;
    private TextView textEmail;
    private TextView textWebsite;
    private TextView textShortDescription;
    private TextView textDetailDescription;
    private TextView textAddress;

    private LinearLayout crop_layout;
    private CroppingHelper mCroppingHelper;
    private Intent cropOptionintent;
    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private int[] mAllowedGestures;
    private ImageButton btCancelCrop;
    private ImageButton btApplyCrop;

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
        loader = AppLoaderFragment.getInstance(mContext);
        initUI();
     //   getPresenter().callMyBizApi();

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
        crop_layout = (LinearLayout) findViewById(R.id.crop_layout);
        crop_layout.setVisibility(View.GONE);
        imgBannerUpload.bringToFront();
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
        textAddress = (TextView) findViewById(R.id.textAddress);
        textPhone = (TextView) findViewById(R.id.textPhone);
        textEmail = (TextView) findViewById(R.id.textEmail);
        textWebsite = (TextView) findViewById(R.id.textWebsite);
        textShortDescription = (TextView) findViewById(R.id.textShortDescription);
        textDetailDescription = (TextView) findViewById(R.id.textDetailDescription);
        textFollowers.setText(bizDetails.getFollowersCount());
        textAddress.setText(bizDetails.getAddress());
        textPhone.setText(bizDetails.getPhone1());
        textEmail.setText(bizDetails.getBusinessEmail());
        textWebsite.setText(bizDetails.getWebsite());
        //textWebsite.setVisibility(bizDetails.getWebsite().isEmpty() || bizDetails.getWebsite()== null ? View.GONE : View.VISIBLE);
        textShortDescription.setText(bizDetails.getShortDescription());
        textDetailDescription.setText(bizDetails.getDetailedDescription());
        Picasso.get().load(bizDetails.getBusinessPic()).placeholder(R.drawable.avatar_male).into(imgProfile);
        Picasso.get().load(bizDetails.getBannerPic()).placeholder(R.drawable.gradient).into(imgBanner);
       // imgBannerUpload.bringToFront();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancelCrop:
                crop_layout.setVisibility(View.GONE);
                break;
            case R.id.btApplyCrop:
                //LogUtils.showToast(mContext,"Apply Crop");
                crop_layout.setVisibility(View.GONE);
                    cropsAndSaveImage();
                break;

            case R.id.imgBannerUpload:
                IMG_SELECTED_FOR = IMG_BANNER_RESULT;
                if (checkPermission()) {
                    selectImgFromGallery();
                   // CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
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

    private void cropsAndSaveImage() {
        mGestureCropImageView.cropAndSaveImage(mCompressFormat, mCompressQuality, new BitmapCropCallback() {
            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    Bitmap resizedBmp = BitmapUtils.resizeBitmapBanner(bitmap);
                     uploadBitmap(resizedBmp, IMG_BANNER_RESULT);
                    //imgBanner.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                setResultError(t);
            }
        });
    }

    @Override
    public void updateBizData(String address, String mobile, String email, String website,String shortDesc,String detailDesc) {
        //TODO update UI for biz data, address,email, website,
        textAddress.setText(address);
        textPhone.setText(mobile);
        textEmail.setText(email);
        textWebsite.setText(website);
        textShortDescription.setText(shortDesc);
        textDetailDescription.setText(detailDesc);
    }

    @Override
    public void updateDealBackground(List<MyBusinessData> bizDetailData) {

    }

    private void selectImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_SELECTED_FOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();
            if (requestCode == IMG_PROFILE_RESULT) {
               /* CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Bitmap bitmap = result.getBitmap();*/
                Bitmap bitmap = BitmapUtils.getBitmap(mContext, data, selectedImageUri);
                Bitmap resizedBmp = BitmapUtils.resizeBitmapProfile(bitmap);
                uploadBitmap(resizedBmp, IMG_PROFILE_RESULT);

            } else if (requestCode == IMG_BANNER_RESULT) {
                /*CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Bitmap bitmap = result.getBitmap();*/
                Bitmap bitmap = BitmapUtils.getBitmap(mContext, data, selectedImageUri);
                crop_layout.setVisibility(View.VISIBLE);
                performCrop(bitmap);
               // Bitmap resizedBmp = resizeBitmapBanner(bitmap);
               // uploadBitmap(resizedBmp, IMG_BANNER_RESULT);
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void performCrop(Bitmap bitmap) {
        btCancelCrop = (ImageButton) findViewById(R.id.btCancelCrop);
        btApplyCrop = (ImageButton) findViewById(R.id.btApplyCrop);
        btCancelCrop.setOnClickListener(this);
        btApplyCrop.setOnClickListener(this);
        mCroppingHelper = new CroppingHelper(MyBusinessActivity.this);
        cropOptionintent = mCroppingHelper.startCropping(bitmap);
        setupUIforCrop(cropOptionintent);
    }

    private void setupUIforCrop(Intent intent) {
        setupViews(intent);
        setImageData(intent);
        setAllowedGestures(0);
    }

    private void setAllowedGestures(int tab) {
        mGestureCropImageView.setScaleEnabled(mAllowedGestures[tab] == ALL || mAllowedGestures[tab] == SCALE);
        mGestureCropImageView.setRotateEnabled(mAllowedGestures[tab] == ALL || mAllowedGestures[tab] == ROTATE);
    }

    private void setImageData(@NonNull Intent intent) {
        Uri inputUri = intent.getParcelableExtra(UCrop.EXTRA_INPUT_URI);
        Uri outputUri = intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
        processOptions(intent);

        if (inputUri != null && outputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri, outputUri);
            } catch (Exception e) {
                setResultError(e);
                finish();
            }
        } else {
            setResultError(new NullPointerException(getString(R.string.ucrop_error_input_data_is_absent)));
            finish();
        }
    }

    private void setResultError(Throwable throwable) {
        setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }

    @SuppressWarnings("deprecation")
    private void processOptions(@NonNull Intent intent) {
        // Bitmap compression options
        String compressionFormatName = intent.getStringExtra(UCrop.Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        mCompressFormat = (compressFormat == null) ? DEFAULT_COMPRESS_FORMAT : compressFormat;
        mCompressQuality = intent.getIntExtra(UCrop.Options.EXTRA_COMPRESSION_QUALITY, DEFAULT_COMPRESS_QUALITY);
        // Gestures options
        int[] allowedGestures = intent.getIntArrayExtra(UCrop.Options.EXTRA_ALLOWED_GESTURES);
        if (allowedGestures != null && allowedGestures.length == AppConstant.TABS_COUNT) {
            mAllowedGestures = allowedGestures;
        }
        // Crop image view options
        mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(UCrop.Options.EXTRA_MAX_BITMAP_SIZE, CropImageView.DEFAULT_MAX_BITMAP_SIZE));
        mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER, CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER));
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));
        // Overlay view options
        mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra(UCrop.Options.EXTRA_FREE_STYLE_CROP, false/*OverlayView.DEFAULT_FREESTYLE_CROP_MODE != OverlayView.FREESTYLE_CROP_MODE_DISABLE*/));
        mOverlayView.setDimmedColor(intent.getIntExtra(UCrop.Options.EXTRA_DIMMED_LAYER_COLOR, getResources().getColor(R.color.ucrop_color_default_dimmed)));
        mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra(UCrop.Options.EXTRA_CIRCLE_DIMMED_LAYER, OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER));
        mOverlayView.setShowCropFrame(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_FRAME, OverlayView.DEFAULT_SHOW_CROP_FRAME));
        mOverlayView.setCropFrameColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_COLOR, getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));
        mOverlayView.setShowCropGrid(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_GRID, OverlayView.DEFAULT_SHOW_CROP_GRID));
        mOverlayView.setCropGridRowCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_ROW_COUNT, OverlayView.DEFAULT_CROP_GRID_ROW_COUNT));
        mOverlayView.setCropGridColumnCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLUMN_COUNT, OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT));
        mOverlayView.setCropGridColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLOR, getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));

        // Result bitmap max size options
        int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
        int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);
        if (maxSizeX > 0 && maxSizeY > 0) {
            mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void setupViews(Intent intent) {
        initiateRootViews();
    }

    private void initiateRootViews() {
        mUCropView = findViewById(R.id.ucrop);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();
        findViewById(R.id.ucrop_frame).setBackgroundColor(getResources().getColor(R.color.ucrop_color_crop_background));
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
                if (grantResults.length > 0) {
                    boolean readAccpeted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readAccpeted /*&& writeAccepted*/) {
                        selectImgFromGallery();
                    } else if (cameraPermission) {
                        captureFromCamera();
                    } else {
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


    private void uploadBitmap(final Bitmap bitmapUpload, final int imageType) {
        loader.show();
        String uploadUrl = "";
        String upload_type = "";
        if (imageType == IMG_BANNER_RESULT) {
            uploadUrl = AppConstant.URL_UPLOAD_BUSINESS_BANNER_PIC;
            upload_type = "banner_image";
        } else {
            uploadUrl = AppConstant.URL_UPLOAD_BUSINESS_PROFILE_PIC;
            upload_type = "business_profile_image";
        }
        LogUtils.DEBUG("URL : " + uploadUrl);
        final String finalUpload_type = upload_type;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String s = new String(response.data);
                        LogUtils.DEBUG("Upload Pic Response : " + s);
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            int status = obj.optInt("status");
                            if (status == AppConstant.SUCCESS){
                                if (imageType == IMG_BANNER_RESULT){
                                    imgBanner.setImageBitmap(bitmapUpload);
                                }else {
                                    imgProfile.setImageBitmap(bitmapUpload);
                                }
                            }
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loader.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        LogUtils.ERROR("Upload profile pic Error " + error);
                        loader.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", Utils.getUserEmail(mContext));
                LogUtils.DEBUG("user_email "+Utils.getUserEmail(mContext));
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put(finalUpload_type, new DataPart(imagename + ".png", BitmapUtils.getFileDataFromDrawable(bitmapUpload)));
                LogUtils.DEBUG("image " + new DataPart(imagename + ".png", BitmapUtils.getFileDataFromDrawable(bitmapUpload)));
                return params;
            }

        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public void editBusinessClick(View view) {
       // startActivity(new Intent(this, MainActivity.class).putExtra("",""));
        getPresenter().showUpdateBizDialog();
    }

    public void addDealsExpandCollapse(View view) {
        LinearLayout lytAddDeals = (LinearLayout) findViewById(R.id.lytAddDeals);
        ImageView imgExpandAddDeals = (ImageView) findViewById(R.id.imgExpandAddDeals);
        lytAddDeals.setVisibility(lytAddDeals.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        imgExpandAddDeals.setBackground(lytAddDeals.getVisibility() == View.VISIBLE ? getResources().getDrawable(R.drawable.ic_expand_less) :
                getResources().getDrawable(R.drawable.ic_expand_more));
        
        final Switch switchAddDealType = findViewById(R.id.switchAddDealType);
        updateImageDealUI();
        
        switchAddDealType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchAddDealType.setText(getString(R.string.imageVersion));
                }else{
                    switchAddDealType.setText(getString(R.string.textVersion));
                    
                    updateTextDealUI();
                }
            }
        });
    }

    private void updateTextDealUI() {
    }

    private void updateImageDealUI() {
        
    }


    @IntDef({NONE, SCALE, ROTATE, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {

    }
}
