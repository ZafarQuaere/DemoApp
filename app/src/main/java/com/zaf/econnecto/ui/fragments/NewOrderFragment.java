package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData;
import com.zaf.econnecto.network_call.response_model.biz_list.OrderList;
import com.zaf.econnecto.ui.adapters.OrdersRecylcerAdapter;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;

import java.util.ArrayList;

public class NewOrderFragment extends Fragment {

    private Context mContext;
    private View view;
    private BizListData orderData;
    private TextView emptyTextView;
    private RecyclerView recyclerNewOrders;
    private LinearLayoutManager layoutManager;
    private PendingsFragment.OnListFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_orders, container, false);

        String newOrderData = Utils.getNewOrderData(mContext);
        LogUtils.DEBUG("newOrderData >>>> " + newOrderData);
        orderData = ParseManager.getInstance().fromJSON(newOrderData, BizListData.class);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        recyclerNewOrders = (RecyclerView) view.findViewById(R.id.recyclerNewOrders);
        recyclerNewOrders.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerNewOrders.setLayoutManager(layoutManager);
        recyclerNewOrders.setItemAnimator(new DefaultItemAnimator());

        if (orderData.getStatus().equals(AppConstant.SUCCESS)) {
            emptyTextView.setVisibility(View.GONE);
            recyclerNewOrders.setVisibility(View.VISIBLE);
            updateList();
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerNewOrders.setVisibility(View.GONE);
        }
    }

    private void updateList() {
       /* OrdersRecylcerAdapter adapter = new OrdersRecylcerAdapter((ArrayList<OrderList>) orderData.getData(), new PendingsFragment.OnListFragmentInteractionListener() {

            @Override
            public void onListFragmentInteraction(OrderList item) {
                LogUtils.showToast(mContext, item.getAmount());
            }
        });
        recyclerNewOrders.setAdapter(adapter);*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PendingsFragment.OnListFragmentInteractionListener) {
            mListener = (PendingsFragment.OnListFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
