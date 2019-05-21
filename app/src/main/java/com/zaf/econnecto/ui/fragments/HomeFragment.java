package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.home.DetailData;
import com.zaf.econnecto.network_call.response_model.home.Sales;
import com.zaf.econnecto.network_call.response_model.product_list.ProductList;
import com.zaf.econnecto.ui.adapters.CategoryRecylcerAdapter;
import com.zaf.econnecto.ui.presenters.HomePresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragHome;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import java.util.List;

public class HomeFragment extends BaseFragment<HomePresenter> implements IFragHome {

    private Context mContext;
    private View view;
    private RecyclerView recyclerCategory;
    private GridLayoutManager layoutManager;
    //private LinearLayoutManager layoutManager;
    private TextView emptyTextView;
    SliderLayout sliderLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected HomePresenter initPresenter() {
        return new HomePresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        initUI(view);
        getPresenter().callTodaySalesApi();
        return view;
    }

    private void initUI(View view) {
        recyclerCategory = (RecyclerView) view.findViewById(R.id.recyclerCategory);
        recyclerCategory.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(mContext,2);
       // layoutManager = new LinearLayoutManager(mContext);
        recyclerCategory.setLayoutManager(layoutManager);
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        sliderLayout = view.findViewById(R.id.imageSlider);
       // sliderLayout.setIndicatorAnimation(SliderLayout.setSliderTransformAnimation()); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        setSliderViews();

    }


    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(mContext);

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 2:
                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(mContext, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }

    @Override
    public void updateTodaySalesData(List<DetailData> data) {
        emptyTextView.setVisibility(data == null ? View.VISIBLE : View.GONE);
        recyclerCategory.setVisibility(data == null ? View.GONE : View.VISIBLE);

        CategoryRecylcerAdapter adapter = new CategoryRecylcerAdapter(mContext, data, new OnCategoryItemClickListener() {
            @Override
            public void onCategoryItemClick(DetailData item) {
                Utils.moveToFragment(mContext, new BListFragment(), BListFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, BListFragment.class.getSimpleName(),mContext.getString(R.string.business_list),null,null);
                //LogUtils.showToast(mContext,item.getFirstName());
            }
        });
        recyclerCategory.setAdapter(adapter);

    }

    @Override
    public void updateTotalSalesData(List<Sales> totalSalesData) {

    }

    public interface OnCategoryItemClickListener {
        void onCategoryItemClick(DetailData item);
    }
}
