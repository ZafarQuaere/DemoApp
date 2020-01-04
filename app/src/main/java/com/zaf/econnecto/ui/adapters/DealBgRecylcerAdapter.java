package com.zaf.econnecto.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.home.CategoryData;
import com.zaf.econnecto.network_call.response_model.my_business.DealsBgData;
import com.zaf.econnecto.ui.activities.MyBusinessActivity;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;

import java.util.List;

public class DealBgRecylcerAdapter extends RecyclerView.Adapter<DealBgRecylcerAdapter.ViewHolder> {

    private final List<DealsBgData> mValues;
    private final MyBusinessActivity.OnBgImageClicked mListener;
    private Context context;

    public DealBgRecylcerAdapter(Context mContext, List<DealsBgData> items, MyBusinessActivity.OnBgImageClicked listener) {
        context = mContext;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_add_deals_bg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.get().load(mValues.get(position).getImageLink()).placeholder(R.mipmap.ic_launcher).into(holder.imgItem);

        holder.mView.setOnClickListener(view -> {
            if (mListener != null)
                mListener.onBgImageClicked(holder.mItem);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView imgItem;
        DealsBgData mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgDealBg);
        }


    }
}
