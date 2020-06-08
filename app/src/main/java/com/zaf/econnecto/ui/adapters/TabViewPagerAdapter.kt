package com.zaf.econnecto.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zaf.econnecto.utils.LogUtils
import java.util.*

class TabViewPagerAdapter(manager: FragmentManager?, messListData: ArrayList<String>?) : FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    private var data: ArrayList<String>? = messListData
    private var fragmentManager: FragmentManager? = manager


    override fun getItem(position: Int): Fragment {
        //We are doing this only for checking the total number of fragments in the fragment manager.
        val fragmentsList = fragmentManager!!.fragments
        var size = 0
        if (fragmentsList != null) {
            size = fragmentsList.size
            LogUtils.DEBUG("size of fragments : $size")
        }
        /*  Utils.DummyItem dummyItem = mDummyItems.get(position);
        Log.i(TAG, "********getItem position:" + position + " size:" + size + " title:" + dummyItem.getImageTitle() + " url:" + dummyItem.getImageUrl());

        //Create a new instance of the fragment and return it.
        SampleFragment sampleFragment = (SampleFragment) SampleFragment.getInstance(*/
        /*dummyItem.getImageUrl(), dummyItem.getImageTitle()*/ /*);
        //We will not pass the data through bundle because it will not gets updated by calling notifyDataSetChanged()  method. We will do it through getter and setter.
        sampleFragment.setDummyItem(dummyItem);
        return sampleFragment;*/return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
        // mFragmentList.add(data.getDetails().get(0).getMondayLunchDesc());
    }

    /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }*/

    /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }*/
    override fun getPageTitle(position: Int): CharSequence? {
        //  final String s = mFragmentTitleList.get(position);
        return mFragmentTitleList[position]
    }
}