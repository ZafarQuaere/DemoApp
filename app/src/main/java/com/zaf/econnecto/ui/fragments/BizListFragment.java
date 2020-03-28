package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import com.zaf.econnecto.ui.activities.AddBusinessActivity;
import com.zaf.econnecto.ui.activities.BizDetailsActivity;
import com.zaf.econnecto.ui.adapters.BizListRecyclerAdapter;
import com.zaf.econnecto.ui.interfaces.ActionBarItemClick;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.BListPresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragListing;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppLoaderFragment;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.NetworkUtils;
import com.zaf.econnecto.utils.Utils;

import java.util.List;


public class BizListFragment extends BaseFragment<BListPresenter> implements IFragListing, ActionBarItemClick {

    private RecyclerView recylcerProducts;
    private RecyclerView.LayoutManager layoutManager;
    private AppLoaderFragment loader;
    private Context mContext;
    private TextView emptyTextView;
    private ActionBarItemClick searchListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        searchListener = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.DEBUG("OnResume AppConstant.NEW_FOLLOW  "+AppConstant.NEW_FOLLOW);
        if (AppConstant.NEW_FOLLOW){
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
            getPresenter().callBListApi();
        } else {
            LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.retry), mContext.getString(R.string.please_check_your_network_connection), new DialogButtonClick() {
                @Override
                public void onOkClick() {
                    if (NetworkUtils.isNetworkEnabled(mContext)) {
                        getPresenter().callBListApi();
                    } else {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.please_enable_your_network_connection_and_launch_again));
                    }
                }
                @Override
                public void onCancelClick() {
                }
            });
        }
    }

    private void initUI(View view) {
        recylcerProducts = (RecyclerView) view.findViewById(R.id.recyclerBusinessList);
        recylcerProducts.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recylcerProducts.setLayoutManager(layoutManager);

        recylcerProducts.setItemAnimator(new DefaultItemAnimator());
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);

        Utils.updateActionBar(mContext, BizListFragment.class.getSimpleName(), mContext.getString(R.string.business_list), null, this);
        view.findViewById(R.id.btnAddBizns).setOnClickListener(view1 -> {
            getActivity().startActivity(new Intent(getActivity(), AddBusinessActivity.class));
        });
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
        emptyTextView.setVisibility(data == null? View.VISIBLE:View.GONE);
        recylcerProducts.setVisibility(data == null? View.GONE:View.VISIBLE);
        if (data != null){
            BizListRecyclerAdapter adapter = new BizListRecyclerAdapter(mContext,data, item -> {
                if (item != null) {
                    Intent intent = new Intent(getActivity(),BizDetailsActivity.class);
                    intent.putExtra(getString(R.string.key_biz_id), item.getBusinessUid());
                    intent.putExtra(getString(R.string.is_following),item.getIsFollowing()==1);
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
            recylcerProducts.setAdapter(adapter);
        }
    }

    @Override
    public void clearSearch() {
        getPresenter().clearSearch();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());
    }

    private void filter(String string) {
       getPresenter().filterList(string);
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BizData item);
    }
}
