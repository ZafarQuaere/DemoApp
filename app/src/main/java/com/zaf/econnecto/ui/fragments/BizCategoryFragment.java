package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.home.CategoryData;
import com.zaf.econnecto.ui.adapters.CategoryRecylcerAdapter;
import com.zaf.econnecto.ui.adapters.SliderAdapterExample;
import com.zaf.econnecto.ui.presenters.BizCategoryPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragHome;
import com.zaf.econnecto.utils.Utils;
import com.zaf.imageslider.IndicatorAnimations;
import com.zaf.imageslider.IndicatorView.draw.controller.DrawController;
import com.zaf.imageslider.SliderAnimations;
import com.zaf.imageslider.SliderView;

import java.util.List;

public class BizCategoryFragment extends BaseFragment<BizCategoryPresenter> implements IFragHome {

    private Context mContext;
    private View view;
    private RecyclerView recyclerCategory;
    private GridLayoutManager layoutManager;
    //private LinearLayoutManager layoutManager;
    private TextView emptyTextView;
    SliderView sliderLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected BizCategoryPresenter initPresenter() {
        return new BizCategoryPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_biz_category, container, false);
        mContext = getActivity();
        initUI(view);
        getPresenter().callCategoryApi();
        return view;
    }

    private void initUI(View view) {

        recyclerCategory = (RecyclerView) view.findViewById(R.id.recyclerCategory);
        recyclerCategory.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(mContext,2);
        recyclerCategory.setLayoutManager(layoutManager);
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        sliderLayout = view.findViewById(R.id.imageSlider);

        setSliderViews();

    }


    private void setSliderViews() {

        SliderAdapterExample sliderAdapter = new SliderAdapterExample(mContext);
        sliderAdapter.setCount(3);
        sliderLayout.setSliderAdapter(sliderAdapter);

        sliderLayout.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderLayout.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderLayout.setIndicatorSelectedColor(Color.BLUE);
        sliderLayout.setIndicatorUnselectedColor(Color.LTGRAY);
        sliderLayout.setIndicatorVisibility(false);
        sliderLayout.startAutoCycle();

        sliderLayout.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderLayout.setCurrentPagePosition(position);
            }
        });

        /*for (int i = 0; i < 3; i++) {

            SliderAdapterExample sliderView = new SliderAdapterExample(mContext);

            switch (i) {
                case 0:
                   // sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    sliderView.setImageDrawable(R.drawable.slider1);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.slider2);
                   // sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.slider3);
                   // sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
               *//* case 3:
                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;*//*
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
           // sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                   // Toast.makeText(mContext, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }*/
    }

    @Override
    public void updateCategory(List<CategoryData> data) {
        emptyTextView.setVisibility(data == null ? View.VISIBLE : View.GONE);
        recyclerCategory.setVisibility(data == null ? View.GONE : View.VISIBLE);

        CategoryRecylcerAdapter adapter = new CategoryRecylcerAdapter(mContext, data, new OnCategoryItemClickListener() {
            @Override
            public void onCategoryItemClick(CategoryData item) {
                Utils.moveToFragment(mContext, new BizListFragment(), BizListFragment.class.getSimpleName(), null);
                Utils.updateActionBar(mContext, BizListFragment.class.getSimpleName(),mContext.getString(R.string.business_list),null,null);
            }
        });
        recyclerCategory.setAdapter(adapter);

    }


    public interface OnCategoryItemClickListener {
        void onCategoryItemClick(CategoryData item);
    }
}
