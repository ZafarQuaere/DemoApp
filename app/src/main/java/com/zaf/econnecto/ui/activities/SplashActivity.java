package com.zaf.econnecto.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zaf.econnecto.R;
import com.zaf.econnecto.utils.DateUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.version2.ui.HomeActivity;

import static com.zaf.econnecto.utils.AppConstant.SPLASH_TIME_OUT;


public class SplashActivity extends Activity {

    private Context mContext;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        image = findViewById(R.id.image);
        Glide.with(this).load(R.drawable.landing_gif).into(image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLoginExpired = DateUtils.isLoginExpired(mContext);
                if (isLoginExpired) {
                    Utils.setLoggedIn(mContext, false);
                    Utils.saveLoginData(mContext, null);
                }
                if (mContext.getResources().getBoolean(R.bool.run_version2_ui)) {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }
                /*Intent i = new Intent(SplashActivity.this, MyBusinessActivityLatest.class);
                startActivity(i);*/
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
