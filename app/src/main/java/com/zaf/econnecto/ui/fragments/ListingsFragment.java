package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.product_list.MyProductsData;
import com.zaf.econnecto.network_call.response_model.product_list.ProductList;
import com.zaf.econnecto.ui.adapters.ProductRecylcerAdapter;
import com.zaf.econnecto.ui.presenters.ListingsPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragListing;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppLoaderFragment;


public class ListingsFragment extends BaseFragment<ListingsPresenter> implements IFragListing {

    private RecyclerView recylcerProducts;
    private RecyclerView.LayoutManager layoutManager;
    private AppLoaderFragment loader;
    private Context mContext;
    private TextView emptyTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Override
    protected ListingsPresenter initPresenter() {
        return new ListingsPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);
        initUI(view);

        getPresenter().callItemsApi();

        return view;
    }

    private void initUI(View view) {
        recylcerProducts = (RecyclerView) view.findViewById(R.id.recylcerProducts);
        recylcerProducts.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recylcerProducts.setLayoutManager(layoutManager);
        recylcerProducts.setItemAnimator(new DefaultItemAnimator());
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);

    }


    @Override
    public void onResponseSuccess() {

    }

    @Override
    public void onResponseFailure(String msg) {

    }

    @Override
    public void editItem() {

    }

    @Override
    public void updateList(MyProductsData data) {
        emptyTextView.setVisibility(data == null? View.VISIBLE:View.GONE);
        recylcerProducts.setVisibility(data == null? View.GONE:View.VISIBLE);
        if (data != null && data.getStatus().equals(AppConstant.SUCCESS)) {
            ProductRecylcerAdapter adapter = new ProductRecylcerAdapter(mContext,data.getData(), new OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(ProductList item) {
                   // LogUtils.showToast(mContext, item.getPName());
                }
            });
            recylcerProducts.setAdapter(adapter);
        }else {

        }

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductList item);
    }
}
