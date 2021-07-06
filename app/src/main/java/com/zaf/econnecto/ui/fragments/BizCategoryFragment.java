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
