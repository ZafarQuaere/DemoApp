package com.zaf.econnecto.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.fragments.CompletedFragment;
import com.zaf.econnecto.ui.fragments.NewOrderFragment;
import com.zaf.econnecto.ui.fragments.PendingsFragment;


public class OrdresPagerAdapter extends FragmentStatePagerAdapter {

    private int mTabCount;
    private Context mContext;

    public OrdresPagerAdapter(Context context, FragmentManager childFragmentManager, int tabCount) {
        super(childFragmentManager);
        mTabCount = tabCount;
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewOrderFragment tab1 = new NewOrderFragment();
                return tab1;
            case 1:
                PendingsFragment tab2 = new PendingsFragment();
                return tab2;
            case 2:
                CompletedFragment tab3 = new CompletedFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = mContext.getString(R.string.new_order);
        } else if (position == 1) {
            title = mContext.getString(R.string.pending);
        } else if (position == 2) {
            title = mContext.getString(R.string.completed);
        }
        return title;
    }
}
