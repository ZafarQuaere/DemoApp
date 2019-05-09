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
import com.zaf.econnecto.network_call.response_model.product_list.ProductList;
import com.zaf.econnecto.ui.fragments.ListingsFragment;
import com.zaf.econnecto.utils.AppConstant;

import java.util.List;

public class ProductRecylcerAdapter extends RecyclerView.Adapter<ProductRecylcerAdapter.ViewHolder> {

    private final List<ProductList> mValues;
    private final ListingsFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public ProductRecylcerAdapter(Context context, List<ProductList> items, ListingsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getPName());
        holder.mContentView.setText(mContext.getString(R.string.price)+": "+ AppConstant.RUPEES_SYMBOL+mValues.get(position).getPrice());
        Picasso.get().load(mValues.get(position).getImagePath()).into(holder.imgItem);

        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        final ImageView imgItem;
        ProductList mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
