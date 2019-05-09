package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.adapters.OrdresPagerAdapter;
import com.zaf.econnecto.ui.presenters.BaseFragmentPresenter;


public class OrdersFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

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
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        mContext = getActivity();
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        //Creating our pager adapter
        OrdresPagerAdapter adapter = new OrdresPagerAdapter(getActivity(), this.getChildFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

}
