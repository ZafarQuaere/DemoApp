package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.orders.OrderData;
import com.zaf.econnecto.network_call.response_model.orders.OrderList;
import com.zaf.econnecto.ui.adapters.OrdersRecylcerAdapter;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;
import com.zaf.econnecto.utils.parser.ParseManager;

import java.util.ArrayList;


public class CompletedFragment extends Fragment {

    private PendingsFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private RecyclerView recyclerCompleted;
    private LinearLayoutManager layoutManager;
    private TextView emptyTextView;
    private OrderData orderData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        String completedOrderData = Utils.getCompleteOrderData(mContext);
        LogUtils.DEBUG("completedOrderData >>>> " + completedOrderData);
        orderData = ParseManager.getInstance().fromJSON(completedOrderData, OrderData.class);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        recyclerCompleted = (RecyclerView) view.findViewById(R.id.recyclerCompleted);
        recyclerCompleted.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerCompleted.setLayoutManager(layoutManager);
        recyclerCompleted.setItemAnimator(new DefaultItemAnimator());

        if (orderData.getStatus().equals(AppConstant.SUCCESS)) {
            emptyTextView.setVisibility(View.GONE);
            recyclerCompleted.setVisibility(View.VISIBLE);
            updateList();
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerCompleted.setVisibility(View.GONE);
        }
    }

    private void updateList() {
        OrdersRecylcerAdapter adapter = new OrdersRecylcerAdapter((ArrayList<OrderList>) orderData.getData(), new PendingsFragment.OnListFragmentInteractionListener() {

            @Override
            public void onListFragmentInteraction(OrderList item) {
                LogUtils.showToast(mContext, item.getAmount());
            }
        });
        recyclerCompleted.setAdapter(adapter);
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(OrderList item);
    }
}
