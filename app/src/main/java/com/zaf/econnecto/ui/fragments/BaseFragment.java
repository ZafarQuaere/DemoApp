package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaf.econnecto.ui.presenters.BaseFragmentPresenter;


public abstract class BaseFragment<P extends BaseFragmentPresenter> extends Fragment {

    private ProgressFragment progressDialog ;
    private P mPresenter;
    // private Unbinder mUnbinder;

    protected abstract P initPresenter();
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView view = new TextView(getActivity());
        view.setText("AquaHey");
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mContext = getActivity();

    }



    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onStop() {
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

    public void openDialog(){
        progressDialog = new ProgressFragment();
        progressDialog.setCancelable(false);
        progressDialog.showNow(getFragmentManager(),"Progress");
    }

    public void hideDialog(){
        progressDialog.dismiss();
    }
}
