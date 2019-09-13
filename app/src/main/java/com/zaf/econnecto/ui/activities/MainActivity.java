package com.zaf.econnecto.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.fragments.BizListFragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.MainPresenter;
import com.zaf.econnecto.ui.presenters.operations.IMain;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;


public class MainActivity extends BaseActivity<MainPresenter>
        implements IMain {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Context mContext;
    private FloatingActionButton fabAddBizness;
    private NavigationView navigationView;

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
        updateUI();
        initUI();

        //moveToHome();
        moveToBList();

    }

    private void updateUI() {
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView textVerifyEmail =  headerView.findViewById(R.id.textVerifyEmail);
        TextView textUserName =  headerView.findViewById(R.id.textUserName);
        if (Utils.isLoggedIn(mContext)) {
            textUserName.setText(Utils.getUserName(mContext));
            textVerifyEmail.setText(Utils.isEmailVerified(mContext) ? Utils.getUserEmail(mContext) : getString(R.string.verify_your_email));
        } else {
            textUserName.setText("");
            textVerifyEmail.setText("");
        }
    }

    private void moveToHome() {
        getPresenter().moveToFragment(BizCategoryFragment.class.getSimpleName());
    }

    @SuppressLint("RestrictedApi")
    private void moveToBList() {
        getPresenter().moveToFragment(BizListFragment.class.getSimpleName());
        fabAddBizness.setVisibility(View.VISIBLE);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @SuppressLint("RestrictedApi")
    private void initUI() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fabAddBizness = (FloatingActionButton) findViewById(R.id.fabAddBizness);
        fabAddBizness.setVisibility(View.GONE);

        //hide default drawer icon
        toggle.setDrawerIndicatorEnabled(false);
        ImageView imgDrawerIcon = (ImageView) findViewById(R.id.imgActionBarDrawerIcon);
        imgDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        updateUI();

    }

    private void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }


    public void onHomeClick(View view) {
        getPresenter().moveToFragment(BizCategoryFragment.class.getSimpleName());
        closeDrawer();

    }

    @SuppressLint("RestrictedApi")
    public void addBusinessClick(View view) {
        if (Utils.isLoggedIn(mContext)) {
            if (Utils.isEmailVerified(mContext)) {
                getPresenter().moveToFragment(AddBusinessFragment.class.getSimpleName());
                fabAddBizness.setVisibility(View.GONE);
            } else {
                LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                        mContext.getString(R.string.you_need_to_verify_your_email_to_add_a_business), new DialogButtonClick() {
                            @Override
                            public void onOkClick() {
                                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            }
                            @Override
                            public void onCancelClick() {
                            }
                        });
            }
        } else {
            LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                    mContext.getString(R.string.you_need_to_login_first_to_add_a_business), new DialogButtonClick() {
                        @Override
                        public void onOkClick() {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }
                        @Override
                        public void onCancelClick() {
                        }
                    });
        }
        closeDrawer();
    }


    @SuppressLint("RestrictedApi")
    public void onListingClick(View view) {
        getPresenter().moveToFragment(BizListFragment.class.getSimpleName());
        fabAddBizness.setVisibility(View.VISIBLE);
        closeDrawer();
    }

    public void onProfileClick(View view) {
        closeDrawer();
        // getPresenter().moveToFragment(FragmentProfile.class.getSimpleName());
        updateMyAccountUI(false);
        startActivity(new Intent(MainActivity.this, MyBusinessActivity.class));

    }

    public void verifyEmailClick(View view) {
        if (!Utils.isEmailVerified(mContext))
            showVerifyEmailDialog();
    }

    private void showVerifyEmailDialog() {
        LogUtils.showDialogDoubleButton(mContext, getString(R.string.cancel), getString(R.string.ok), getString(R.string.otp_is_sent_to_your_registered_email_plz_enter_otp_for_verification), new DialogButtonClick() {
            @Override
            public void onOkClick() {
                getPresenter().requestOtpApi();
            }

            @Override
            public void onCancelClick() {

            }
        });
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
        updateMyAccountUI(true);
    }

    private void updateMyAccountUI(boolean isExpand) {
        LinearLayout lytMyAccount = (LinearLayout) findViewById(R.id.lytMyAccount);
        ImageButton iconMyAccountExpand = (ImageButton) findViewById(R.id.iconMyAccountExpand);
        if (Utils.isLoggedIn(mContext) && isExpand) {
            lytMyAccount.setVisibility(lytMyAccount.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            iconMyAccountExpand.setBackground(lytMyAccount.getVisibility() == View.VISIBLE ? getResources().getDrawable(R.drawable.ic_minus) :
                    getResources().getDrawable(R.drawable.ic_plus));
        } else {
            lytMyAccount.setVisibility(View.GONE);
            iconMyAccountExpand.setBackground(getResources().getDrawable(R.drawable.ic_plus));
           // LogUtils.showToast(mContext, getString(R.string.please_login_first));
        }
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
        closeDrawer();
        LogUtils.showDialogDoubleButton(mContext, getString(R.string.cancel), getString(R.string.ok), getString(R.string.otp_is_sent_to_your_registered_email_plz_enter_same), new DialogButtonClick() {
            @Override
            public void onOkClick() {
                //getPresenter().requestOtpApi();
                getPresenter().startActivity(mContext);
            }

            @Override
            public void onCancelClick() {

            }
        });

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
        /*mContext.startActivity(new Intent(mContext, LoginActivity.class));
        ((Activity) mContext).finish();*/
        Utils.clearBackStackTillHomeFragment(mContext);
        getPresenter().moveToFragment(BizListFragment.class.getSimpleName());
        updateMyAccountUI(false);
        updateUI();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showAddBizFab(boolean show) {
        fabAddBizness.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateVerifyEmailUI() {
        TextView textVerifyEmail = (TextView) findViewById(R.id.textVerifyEmail);
        if (Utils.isEmailVerified(mContext)) {
            textVerifyEmail.setText(Utils.getUserEmail(mContext));
        }
    }

    @Override
    protected void onResume() {
        LogUtils.DEBUG("Main Activity OnResume ");
        super.onResume();

    }

    @Override
    protected void onRestart() {
        LogUtils.DEBUG("Main Activity OnRestart ");
        super.onRestart();
    }

    @Override
    protected void onResumeFragments() {
        LogUtils.DEBUG("Main Activity OnResumeFragments ");
        super.onResumeFragments();
    }

    @Override
    protected void onPostResume() {
        LogUtils.DEBUG("Main Activity OnPostResume ");
        super.onPostResume();
    }

}
