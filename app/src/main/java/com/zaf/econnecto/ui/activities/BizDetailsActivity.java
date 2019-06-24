package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetails;
import com.zaf.econnecto.ui.presenters.BizDetailPresenter;
import com.zaf.econnecto.ui.presenters.operations.IBizDetail;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class BizDetailsActivity extends BaseActivity<BizDetailPresenter> implements IBizDetail {

    private Context mContext;
    private String biz_uid;
    private BizDetails mBizDetailsData;

    @Override
    protected BizDetailPresenter initPresenter() {
        return new BizDetailPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ToDo have to make app bar transparent.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_details);
        mContext = this;
        biz_uid = getIntent().getStringExtra(getResources().getString(R.string.key_biz_id));
        initUI();

        getPresenter().callBizDetailApi(biz_uid);
       //Utils.updateActionBar(this,new BizDetailsActivity().getClass().getSimpleName(),getString(R.string.biz_details), null,null);


    }

    private void initUI() {
        final Toolbar toolbar = findViewById(R.id.toolbarBd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Todo have to make app bar transparent
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }




    @Override
    public void updateUI(BizDetails bizDetails) {
            mBizDetailsData = bizDetails;
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mBizDetailsData != null ? mBizDetailsData.getBusinessName(): getString(R.string.business_details));
        ImageView imgProfile = (ImageView) findViewById(R.id.imgProfile);
        ImageView imgBanner = (ImageView) findViewById(R.id.imgBanner);
        TextView textShortDescription = (TextView)findViewById(R.id.textShortDescription);
        TextView bizNameTitle = (TextView)findViewById(R.id.bizNameTitle);
        TextView bizDetailDescription = (TextView)findViewById(R.id.bizDetailDescription);
        TextView textFollow = (TextView)findViewById(R.id.textFollow);
        TextView textFollowers = (TextView)findViewById(R.id.textFollowers);
        TextView textEstablishedDate = (TextView)findViewById(R.id.textEstablishedDate);
        TextView textAddress = (TextView)findViewById(R.id.textAddress);
        TextView textPhone = (TextView)findViewById(R.id.textPhone);
        TextView textEmail = (TextView)findViewById(R.id.textEmail);
        TextView textWebsite = (TextView)findViewById(R.id.textWebsite);

        Picasso.get().load(mBizDetailsData.getBusinessPic()).placeholder(R.drawable.avatar_male).into(imgProfile);
        Picasso.get().load(mBizDetailsData.getBannerPic()).placeholder(R.drawable.avatar_male).into(imgBanner);
        textShortDescription.setText(mBizDetailsData.getShortDescription().trim());
        bizNameTitle.setText(mBizDetailsData.getBusinessName().trim());
        bizDetailDescription.setText(mBizDetailsData.getDetailedDescription().trim());
        textEstablishedDate.setText(mContext.getString(R.string.establish_date)+": "+mBizDetailsData.getYearFounded());
        textFollowers.setText(mBizDetailsData.getFollowersCount()+" "+mContext.getString(R.string.followers));
        textAddress.setText(mBizDetailsData.getAddress());
        textPhone.setText(mBizDetailsData.getPhone1());
        textEmail.setText(mBizDetailsData.getBusinessEmail());
        textWebsite.setVisibility(mBizDetailsData.getWebsite().isEmpty() || mBizDetailsData.getWebsite()==null?View.GONE:View.VISIBLE);
        textWebsite.setText(mBizDetailsData.getWebsite());
        textPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.callPhone(mContext,mBizDetailsData.getPhone1());
            }
        });
      //  textFollow.setText(mBizDetailsData.getDetailedDescription().trim());


    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }


}
