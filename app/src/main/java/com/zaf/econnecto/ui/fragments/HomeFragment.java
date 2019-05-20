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
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.home.DetailData;
import com.zaf.econnecto.network_call.response_model.home.Sales;
import com.zaf.econnecto.network_call.response_model.product_list.ProductList;
import com.zaf.econnecto.ui.adapters.CategoryRecylcerAdapter;
import com.zaf.econnecto.ui.presenters.HomePresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragHome;
import com.zaf.econnecto.utils.LogUtils;

import java.util.List;

public class HomeFragment extends BaseFragment<HomePresenter> implements IFragHome {

    private Context mContext;
    private View view;
    private RecyclerView recyclerCategory;
    private GridLayoutManager layoutManager;
    //private LinearLayoutManager layoutManager;
    private TextView emptyTextView;

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

    }


    @Override
    public void updateTodaySalesData(List<DetailData> data) {
        emptyTextView.setVisibility(data == null ? View.VISIBLE : View.GONE);
        recyclerCategory.setVisibility(data == null ? View.GONE : View.VISIBLE);

        CategoryRecylcerAdapter adapter = new CategoryRecylcerAdapter(mContext, data, new OnCategoryItemClickListener() {
            @Override
            public void onCategoryItemClick(DetailData item) {
                LogUtils.showToast(mContext,item.getFirstName());
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
