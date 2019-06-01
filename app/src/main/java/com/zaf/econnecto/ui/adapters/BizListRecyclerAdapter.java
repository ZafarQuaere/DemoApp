package com.zaf.econnecto.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.biz_list.BizData;
import com.zaf.econnecto.network_call.response_model.home.DetailData;
import com.zaf.econnecto.ui.fragments.BListFragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.utils.LogUtils;

import java.util.Calendar;
import java.util.List;

public class BizListRecyclerAdapter extends RecyclerView.Adapter<BizListRecyclerAdapter.ViewHolder> {

    private final List<BizData> mValues;
    private final BListFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public BizListRecyclerAdapter(Context context, List<BizData> items, BListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.biz_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getBusinessName());
        holder.mContentView.setText(mContext.getString(R.string.establish_date)+": "+mValues.get(position).getYearFounded());
        Picasso.get().load(mValues.get(position).getBusinessPic()).placeholder(R.drawable.avatar_male).into(holder.imgItem);

        holder.textFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textFollow.getText().equals("Follow")){
                    holder.textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_unfollow));
                    holder.textFollow.setText("Following");
                }else {
                    LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                            mContext.getString(R.string.do_you_want_to_unfollow )+" "+mValues.get(position).getBusinessName()+" ?", new DialogButtonClick() {
                                @Override
                                public void onOkClick() {
                                    holder.textFollow.setText("Follow");
                                    holder.textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_follow));
                                }
                                @Override
                                public void onCancelClick() { }
                            });

                }
            }
        });


        holder.textViewBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final TextView textFollow;
        final TextView textViewBusiness;
        final ImageView imgItem;
        BizData mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.textName);
            mContentView = (TextView) view.findViewById(R.id.content);
            textFollow = (TextView) view.findViewById(R.id.textFollow);
            textViewBusiness = (TextView) view.findViewById(R.id.textViewBusiness);
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
