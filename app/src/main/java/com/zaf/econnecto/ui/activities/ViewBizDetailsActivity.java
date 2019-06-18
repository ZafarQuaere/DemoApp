package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;


import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.request_model.AddressData;
import com.zaf.econnecto.ui.presenters.BizDetailPresenter;
import com.zaf.econnecto.ui.presenters.operations.IBizDetail;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class ViewBizDetailsActivity extends BaseActivity<BizDetailPresenter> implements IBizDetail {

    private Context mContext;
    private String biz_uid;

    @Override
    protected BizDetailPresenter initPresenter() {
        return new BizDetailPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_biz_details);
        mContext = this;
        biz_uid = getIntent().getStringExtra(getResources().getString(R.string.key_biz_id));
        initUI();

        getPresenter().callBizDetailApi(biz_uid);
       //Utils.updateActionBar(this,new ViewBizDetailsActivity().getClass().getSimpleName(),getString(R.string.biz_details), null,null);


    }

    private void initUI() {
        final Toolbar toolbar = findViewById(R.id.toolbarBd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.business_details));
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
    public void updateUI() {
//        startActivity(new Intent(ViewBizDetailsActivity.this, MainActivity.class));
//        finish();
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }


}
