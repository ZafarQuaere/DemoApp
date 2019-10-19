package com.zaf.econnecto.ui.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

//import androidx.appcompat.app.AppCompatActivity;

import com.zaf.econnecto.ui.fragments.ProgressFragment;
import com.zaf.econnecto.ui.interfaces.BackPressHandler;
import com.zaf.econnecto.ui.presenters.BasePresenter;


public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    private P mPresenter;
    private ProgressFragment progressDialog;
    // private Unbinder mUnbinder;

    protected abstract P initPresenter();

    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mContext = this;

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onPostCreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();
        }
        super.onStop();
    }

    protected P getPresenter() {
        if (mPresenter == null) {
            throw new NullPointerException("No presenter available for this activity.");
        }
        return mPresenter;
    }


   /* public void openProgressDialog(){
        progressDialog = new ProgressFragment();
        progressDialog.setCancelable(false);
        progressDialog.showNow(((AppCompatActivity) mContext).getSupportFragmentManager(),"Progress");
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }*/


    /**
     * Function to replace a fragment into the container.
     *
     * @param container
     * @param clazz
     * @param args
     * @see FragmentTransaction#replace(int, Fragment, String)
     */
    protected void replaceFragment(int container, Class<? extends Fragment> clazz, Bundle args) {
        replaceFragment(container, clazz.getName(), args);
    }

    protected void replaceFragment(int container, String fragmentName, Bundle args) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setTransactionAnimations(fragmentTransaction);

        Fragment fragment = Fragment.instantiate(this, fragmentName, args);
        fragmentTransaction.replace(container, fragment, fragmentName);
        onPreCommitTransaction(fragmentTransaction);
        fragmentTransaction.commit();
    }

    /**
     * Function to add a fragment in the container.
     *
     * @param container
     * @param clazz
     * @param args
     * @see FragmentTransaction#add(int, Fragment, String)
     */
    protected void addFragment(int container, Class<? extends Fragment> clazz, Bundle args) {
        String fragmentName = clazz.getName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setTransactionAnimations(fragmentTransaction);

        Fragment fragment = Fragment.instantiate(this, fragmentName, args);
        fragmentTransaction.add(container, fragment, fragmentName);
        onPreCommitTransaction(fragmentTransaction);
        fragmentTransaction.commit();
    }

    /**
     * Function to detach a fragment from the screen.
     *
     * @param fragment
     * @see FragmentTransaction#detach(Fragment)
     */
    protected void detachFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Function to attach a fragment to the screen.
     *
     * @param fragment
     * @see FragmentTransaction#attach(Fragment)
     */
    protected void attachFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setTransactionAnimations(fragmentTransaction);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Override this function if you want to set up the animation of the transaction of the given
     * fragment
     *
     * @param transaction
     */
    protected void setTransactionAnimations(FragmentTransaction transaction) {
    }

    /**
     * Override this function if any other setting is required for this fragment transaction.
     * This function is called before {@link FragmentTransaction#commit()}.
     *
     * @param transaction
     */
    protected void onPreCommitTransaction(FragmentTransaction transaction) {
    }

    protected Fragment getFragment(int container) {
        return getSupportFragmentManager().findFragmentById(container);
    }

    protected Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    protected boolean isFragmentShowing(Fragment fragment, int container) {
        Fragment showing = getFragment(container);
        if (showing == null) {
            return false;
        }
        String tag = fragment.getTag();
        if (tag != null) {
            return tag.equals(showing.getTag());
        }
        return showing.equals(fragment);
    }

    @Override
    public void onBackPressed() {
        BackPressHandler handler = getBackPressHandler();
        if (handler != null && handler.onBackPressed()) {
            return;
        }
        // if there is only 1 fragment remaining and user hit the back button,
        // directly finish the current activity. Otherwise, let the system to handle it.
        // The reason is, if the activity has the fragment added to back stack,
        // the "back pressed" will pop the last fragment instead of finishing the activity.
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }


    @Nullable
    protected BackPressHandler getBackPressHandler() {
        return null;
    }


}
