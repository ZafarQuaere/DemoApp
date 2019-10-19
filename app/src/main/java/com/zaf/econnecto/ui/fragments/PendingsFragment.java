package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.biz_list.BizListData;
import com.zaf.econnecto.network_call.response_model.biz_list.OrderList;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.parser.ParseManager;


public class PendingsFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private RecyclerView recyclerPendings;
    private LinearLayoutManager layoutManager;
    private TextView emptyTextView;
    private BizListData orderData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendings, container, false);

        String pendingOrderData = "";// Utils.getPendingOrderData(mContext);
        LogUtils.DEBUG("pendingOrderData >>>> " + pendingOrderData);
        orderData = ParseManager.getInstance().fromJSON(pendingOrderData, BizListData.class);
        initUI(view);

        return view;
    }

    private void initUI(View view) {
        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        recyclerPendings = (RecyclerView) view.findViewById(R.id.recyclerPendings);
        recyclerPendings.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerPendings.setLayoutManager(layoutManager);
        recyclerPendings.setItemAnimator(new DefaultItemAnimator());

        if (orderData.getStatus().equals(AppConstant.SUCCESS)) {
            emptyTextView.setVisibility(View.GONE);
            recyclerPendings.setVisibility(View.VISIBLE);
            updateList();
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerPendings.setVisibility(View.GONE);
        }
    }

    private void updateList() {
       /* OrdersRecylcerAdapter adapter = new OrdersRecylcerAdapter((ArrayList<OrderList>) orderData.getData(), new OnListFragmentInteractionListener() {

            @Override
            public void onListFragmentInteraction(OrderList item) {
                LogUtils.showToast(mContext, item.getAmount());
            }
        });
        recyclerPendings.setAdapter(adapter);*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
