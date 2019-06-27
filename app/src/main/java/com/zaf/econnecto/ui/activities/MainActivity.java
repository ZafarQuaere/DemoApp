package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.fragments.AddBusinessFragment;
import com.zaf.econnecto.ui.fragments.BListFragment;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.presenters.MainPresenter;
import com.zaf.econnecto.ui.presenters.operations.IMain;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class MainActivity extends BaseActivity<MainPresenter>
        implements IMain {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Context mContext;
    private TextView textUserEmail;

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        setUpToolbar();
        initUI();
        //moveToHome();
        moveToBList();

    }

    private void moveToHome() {
        getPresenter().moveToFragment(BizCategoryFragment.class.getSimpleName());
    }

    private void moveToBList() {
        getPresenter().moveToFragment(BListFragment.class.getSimpleName());
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initUI() {
//        textUserEmail = (TextView) findViewById(R.id.textUserEmail);
//        textUserEmail.setText(Utils.getUserEmail(mContext)!= null ?Utils.getUserEmail(mContext):"");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //hide default drawer icon
        toggle.setDrawerIndicatorEnabled(false);
        ImageView imgDrawerIcon = (ImageView) findViewById(R.id.imgActionBarDrawerIcon);
        imgDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
    }

    private void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }


    public void onHomeClick(View view) {
        getPresenter().moveToFragment(BizCategoryFragment.class.getSimpleName());
        closeDrawer();

    }

    public void addBusinessClick(View view) {
        getPresenter().moveToFragment(AddBusinessFragment.class.getSimpleName());
        closeDrawer();

    }


    public void onListingClick(View view) {
        getPresenter().moveToFragment(BListFragment.class.getSimpleName());
        closeDrawer();
    }

    public void onProfileClick(View view) {
        closeDrawer();
       // getPresenter().moveToFragment(FragmentProfile.class.getSimpleName());
        LogUtils.showToast(mContext, "Development under progress");

    }

    public void saveAddressClick(View view) {
        closeDrawer();
        startActivity(new Intent(MainActivity.this, BizDetailsActivity.class));
    }

    public void item3Click(View view) {
        closeDrawer();
        LogUtils.showToast(mContext, "Development under progress");
    }

    public void onLogoutClick(View view) {
        getPresenter().logoutUser();
        closeDrawer();
    }

    public void expandMyAccount(View view) {
        LinearLayout lytMyAccount = (LinearLayout) findViewById(R.id.lytMyAccount);
        ImageButton iconMyAccountExpand = (ImageButton) findViewById(R.id.iconMyAccountExpand);
        lytMyAccount.setVisibility(lytMyAccount.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        iconMyAccountExpand.setBackground(lytMyAccount.getVisibility() == View.VISIBLE ? getResources().getDrawable(R.drawable.ic_minus) : getResources().getDrawable(R.drawable.ic_plus));
    }

    public void closeDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            updateToolbar();
        }
        //getPresenter().updateToolbarTitle(mContext);
    }

    private void updateToolbar() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            //Utils.clearBackStackTillHomeFragment(MainActivity.this);
            try {
                getSupportFragmentManager().popBackStack();
                FragmentManager.BackStackEntry a = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2);//top
                Fragment baseFrag = (Fragment) getSupportFragmentManager().findFragmentByTag(a.getName());
                LogUtils.DEBUG("baseFrag Fragment : " + baseFrag.getClass().getSimpleName());
                getPresenter().updateActionBarTitleOnBackPress(mContext, baseFrag);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.ERROR(e.getMessage());
            }
        } else {
            super.onBackPressed();
        }
        if (count == 1) {
            finishAffinity();
        }

    }

    public void changePasswordClick(View view) {
        getPresenter().startActivity(mContext);
        closeDrawer();
        //LogUtils.showToast(mContext, "Development under progress");
    }

    public void onShareClick(View view) {
        Utils.shareApp(MainActivity.this);
    }

    public void helpNFaqClick(View view) {
        //getPresenter().moveToFragment(HelpNFaqFragment.class.getSimpleName());
        closeDrawer();
        LogUtils.showToast(mContext, "Development under progress");
    }


    @Override
    public void onLogoutCall() {
        Utils.clearBackStackTillHomeFragment(mContext);
        getPresenter().moveToFragment(BListFragment.class.getSimpleName());
    }
}
