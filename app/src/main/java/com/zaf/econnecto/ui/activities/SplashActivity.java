package com.zaf.econnecto.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.zaf.econnecto.R;
import com.zaf.econnecto.version2.ui.HomeActivity;

import static com.zaf.econnecto.utils.AppConstant.SPLASH_TIME_OUT;


public class SplashActivity extends Activity {

    private ProgressBar progress;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setIndeterminate(false);
        progress.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
                if(mContext.getResources().getBoolean(R.bool.run_version2_ui)){
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(SplashActivity.this, AddBusinessActivity.class);
                    startActivity(i);
                }
               /* Intent i = new Intent(SplashActivity.this, MyBusinessActivity.class);
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
