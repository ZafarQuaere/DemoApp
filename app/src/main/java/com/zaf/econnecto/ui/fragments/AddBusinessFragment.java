package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.BaseFragmentPresenter;


public class AddBusinessFragment extends BaseFragment {

    private Context mContext;
    private ViewPager viewPager;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected BaseFragmentPresenter initPresenter() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_biz, container, false);
        mContext = getActivity();
        initUI(view);
        return view;
    }

    private void initUI(View view) {


    }


}
