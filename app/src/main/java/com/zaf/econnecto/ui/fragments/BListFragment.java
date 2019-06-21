package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.zaf.econnecto.network_call.response_model.biz_list.BizData;
import com.zaf.econnecto.ui.activities.BizDetailsActivity;
import com.zaf.econnecto.ui.adapters.BizListRecyclerAdapter;
import com.zaf.econnecto.ui.presenters.BListPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragListing;
import com.zaf.econnecto.utils.AppLoaderFragment;

import java.util.List;


public class BListFragment extends BaseFragment<BListPresenter> implements IFragListing {

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
    protected BListPresenter initPresenter() {
        return new BListPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);
        initUI(view);

        getPresenter().callBListApi();

        return view;
    }

    private void initUI(View view) {
       /* FloatingActionButton fabAddBizness = (FloatingActionButton) view.getRootView().findViewById(R.id.fabAddBizness);
        fabAddBizness.setVisibility(View.VISIBLE);*/

        recylcerProducts = (RecyclerView) view.findViewById(R.id.recyclerBusinessList);
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
    public void updateList(List<BizData> data) {
        emptyTextView.setVisibility(data == null? View.VISIBLE:View.GONE);
        recylcerProducts.setVisibility(data == null? View.GONE:View.VISIBLE);
        if (data != null){
            BizListRecyclerAdapter adapter = new BizListRecyclerAdapter(mContext,data, new OnListFragmentInteractionListener() {
                @Override
                public void onListFragmentInteraction(BizData item) {
                    if (item != null)
                         startActivity(new Intent(getActivity(), BizDetailsActivity.class).putExtra(getString(R.string.key_biz_id),item.getBusinessUid()));
                }
            });
            recylcerProducts.setAdapter(adapter);
        }
    }



    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BizData item);
    }
}
