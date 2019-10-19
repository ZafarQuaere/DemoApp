package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.biz_detail.BizDetails;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.BizDetailPresenter;
import com.zaf.econnecto.ui.presenters.operations.IBizDetail;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class BizDetailsActivity extends BaseActivity<BizDetailPresenter> implements IBizDetail, View.OnClickListener {

    private Context mContext;
    private String biz_uid;
    private BizDetails mBizDetailsData;
    private boolean isFollowing;
    private TextView textFollow;
    private TextView textFollowers;

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
        isFollowing = getIntent().getBooleanExtra(getResources().getString(R.string.is_following), false);
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
                //finish();
                onBackPressed();
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
        collapsingToolbar.setTitle(mBizDetailsData != null ? mBizDetailsData.getBusinessName() : getString(R.string.business_details));
        ImageView imgProfile = (ImageView) findViewById(R.id.imgProfile);
        ImageView imgBanner = (ImageView) findViewById(R.id.imgBanner);
        TextView textShortDescription = (TextView) findViewById(R.id.textShortDescription);
        TextView bizNameTitle = (TextView) findViewById(R.id.bizNameTitle);
        TextView bizDetailDescription = (TextView) findViewById(R.id.bizDetailDescription);
        textFollow = (TextView) findViewById(R.id.textFollow);
        textFollowers = (TextView) findViewById(R.id.textFollowers);
        TextView textEstablishedDate = (TextView) findViewById(R.id.textEstablishedDate);
        TextView textAddress = (TextView) findViewById(R.id.textAddress);
        TextView textPhone = (TextView) findViewById(R.id.textPhone);
        TextView textEmail = (TextView) findViewById(R.id.textEmail);
        TextView textWebsite = (TextView) findViewById(R.id.textWebsite);

        Picasso.get().load(mBizDetailsData.getBusinessPic()).placeholder(R.drawable.avatar_male).into(imgProfile);
        Picasso.get().load(mBizDetailsData.getBannerPic()).placeholder(R.drawable.gradient).into(imgBanner);
        textShortDescription.setText(mBizDetailsData.getShortDescription().trim());
        bizNameTitle.setText(mBizDetailsData.getBusinessName().trim());
        bizDetailDescription.setText(mBizDetailsData.getDetailedDescription().trim());
        textEstablishedDate.setText(mContext.getString(R.string.establish_date) + ": " + mBizDetailsData.getYearFounded());
        textFollowers.setText(mBizDetailsData.getFollowersCount() + " " + mContext.getString(R.string.followers));
        textAddress.setText(mBizDetailsData.getAddress());
        textPhone.setText(mBizDetailsData.getPhone1());
        textEmail.setText(mBizDetailsData.getBusinessEmail());
        textWebsite.setVisibility(mBizDetailsData.getWebsite().isEmpty() || mBizDetailsData.getWebsite() == null ? View.GONE : View.VISIBLE);
        textWebsite.setText(mBizDetailsData.getWebsite());
        textPhone.setOnClickListener(this);
        textFollow.setOnClickListener(this);

        if (isFollowing) {
            updateFollowingUI(textFollow);
        } else {
            updateUnfollowUI(textFollow);
        }


    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }

    public void updateUnfollowUI(TextView textFollow) {
        textFollow.setText(mContext.getString(R.string.follow));
        textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_follow));
    }

    public void updateFollowingUI(TextView textFollow) {
        textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_unfollow));
        textFollow.setText(mContext.getString(R.string.following));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textPhone:
                Utils.callPhone(mContext, mBizDetailsData.getPhone1());
                break;

            case R.id.textFollow:
                validateFollow();
                break;
        }
    }

    private void validateFollow() {
        if (Utils.isLoggedIn(mContext)) {
            if (textFollow.getText().equals(mContext.getString(R.string.follow))) {
                callFollowApi("follow", biz_uid);
                textFollowers.setText((Integer.parseInt(mBizDetailsData.getFollowersCount()))+ 1 + " " + mContext.getString(R.string.followers));
                AppConstant.NEW_FOLLOW = true;
               /* int followerCount = Integer.parseInt(mValues.get(position).getFollowersCount()) + 1;
                mValues.get(position).setFollowersCount(followerCount + "");
                mValues.get(position).setIsFollowing(1);*/
                updateFollowingUI(textFollow);
            } else {
                LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                        mContext.getString(R.string.do_you_want_to_unfollow) + " " + mBizDetailsData.getBusinessName() + " ?", new DialogButtonClick() {
                            @Override
                            public void onOkClick() {
                                callFollowApi("unfollow", biz_uid);
                                updateUnfollowUI(textFollow);
                                textFollowers.setText((Integer.parseInt(getFollowerCount(textFollowers)))- 1 + " " + mContext.getString(R.string.followers));
                                AppConstant.NEW_FOLLOW = true;
                               /* int followerCount = Integer.parseInt(mValues.get(position).getFollowersCount()) - 1;
                                mValues.get(position).setFollowersCount(followerCount + "");
                                mValues.get(position).setIsFollowing(0);*/
                            }

                            @Override
                            public void onCancelClick() {
                            }
                        });
            }
        } else {

            LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                    mContext.getString(R.string.you_need_to_login_first_to_follow_a_business), new DialogButtonClick() {
                        @Override
                        public void onOkClick() {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }

                        @Override
                        public void onCancelClick() {
                        }
                    });
        }
    }

    private String getFollowerCount(TextView textFollowers) {
        String followerCount = textFollowers.getText().toString().trim();
        String[] count = followerCount.split(" ");
        return count[0];
    }

    public void callFollowApi(final String action, String businessUid) {
        String url = AppConstant.URL_BASE + AppConstant.URL_FOLLOW;// + 3;

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("action", action);
            requestObject.put("user_email", Utils.getUserEmail(mContext));
            requestObject.put("business_uid", businessUid);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        LogUtils.DEBUG("URL : " + url + "\nRequest Body :: " + requestObject.toString());
        final MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Follow Response ::" + response.toString());

                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status != AppConstant.SUCCESS) {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.something_wrong_from_server_plz_try_again));
                        if (action.equals("follow")) {
                            textFollow.setText(mContext.getString(R.string.follow));
                            textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_follow));
                        } else {
                            textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_unfollow));
                            textFollow.setText(mContext.getString(R.string.following));
                        }
                    } else {

                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("Follow Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Follow");
    }
}
