package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.biz_list.BizData;
import com.zaf.econnecto.ui.activities.OthersBusinessViewModel;
import com.zaf.econnecto.ui.activities.ViewBusinessActivity;
import com.zaf.econnecto.ui.adapters.BizListRecyclerAdapter;
import com.zaf.econnecto.ui.interfaces.DialogSingleButtonListener;
import com.zaf.econnecto.ui.presenters.BListPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragListing;
import com.zaf.econnecto.utils.ApiViewModel;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;

import java.util.List;


public class BizListFragment extends BaseFragment<BListPresenter> implements IFragListing {

    private RecyclerView recylcerProducts;
    private RecyclerView.LayoutManager layoutManager;
    private AppLoaderFragment loader;
    private Context mContext;
    private TextView emptyTextView;
    private ApiViewModel apiViewModel;
    private OthersBusinessViewModel otherBizVm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.DEBUG("OnResume AppConstant.NEW_FOLLOW  "+AppConstant.NEW_FOLLOW);
        if (AppConstant.NEW_FOLLOW || AppConstant.BIZNESS_ADDED) {
            callApi();
        }
    }


    @Override
    protected BListPresenter initPresenter() {
        return new BListPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biz_list, container, false);
        initUI(view);
        callApi();
        return view;
    }

    private void callApi() {
        if (NetworkUtils.isNetworkEnabled(mContext)) {
            apiViewModel.callBizListApi(getActivity(), this);
        } else {
            LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.retry), mContext.getString(R.string.please_check_your_network_connection), new DialogSingleButtonListener() {
                @Override
                public void okClick() {
                    if (NetworkUtils.isNetworkEnabled(mContext)) {
//                        getPresenter().callBListApi();
                        apiViewModel.callBizListApi(getActivity(), (IFragListing) mContext);
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enable_your_network_connection_and_launch_again));
                    }
                }
            });
        }
    }

    private void initUI(View view) {
        apiViewModel = ViewModelProviders.of(this).get(ApiViewModel.class);
        otherBizVm = ViewModelProviders.of(requireActivity()).get(OthersBusinessViewModel.class);
        // this approach is not working, need to update later.
        otherBizVm.getFollowStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                //not working need to check later
                callApi();
            }
        });
        recylcerProducts = (RecyclerView) view.findViewById(R.id.recyclerBusinessList);
        recylcerProducts.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recylcerProducts.setLayoutManager(layoutManager);

        recylcerProducts.setItemAnimator(new DefaultItemAnimator());
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);

        Utils.updateActionBar(mContext, BizListFragment.class.getSimpleName(), mContext.getString(R.string.business_list), null, null);
        /*recylcerProducts.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fabAddNew.hide();
                else if (dy < 0)
                    fabAddNew.show();
            }
        });*/
    }

    @Override
    public void updateList(List<BizData> data) {
        emptyTextView.setVisibility(data == null ? View.VISIBLE : View.GONE);
        recylcerProducts.setVisibility(data == null ? View.GONE : View.VISIBLE);
        if (data != null) {
            BizListRecyclerAdapter adapter = new BizListRecyclerAdapter(mContext, data, item -> {
                if (item != null) {
                    LogUtils.DEBUG("Selected Biz name: " + item.getBusinessName() + " Business id: " + item.getBusinessId() + " Owner id: " + item.getOwnerId());
                    Intent intent = new Intent(getActivity(), ViewBusinessActivity.class);
                    intent.putExtra(getString(R.string.key_biz_id), item.getBusinessId());
                    intent.putExtra(getString(R.string.key_owner_id), item.getOwnerId());
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
            recylcerProducts.setAdapter(adapter);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BizData item);
    }
}
