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
import com.zaf.econnecto.network_call.response_model.home.DetailData;
import com.zaf.econnecto.ui.fragments.HomeFragment;

import java.util.List;

public class CategoryRecylcerAdapter extends RecyclerView.Adapter<CategoryRecylcerAdapter.ViewHolder> {

    private final List<DetailData> mValues;
    private final HomeFragment.OnCategoryItemClickListener mListener;
    private Context context;

    public CategoryRecylcerAdapter(Context mContext,List<DetailData> items, HomeFragment.OnCategoryItemClickListener listener) {
        context = mContext;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textName.setText(mValues.get(position).getFirstName());
        //holder.mContentView.setText(mValues.get(position).getLastName());

        Picasso.get().load(mValues.get(position).getAvatar()).placeholder(R.drawable.icon_drops).into(holder.imgItem);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCategoryItemClick(holder.mItem);
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
        final TextView textName;
       // final TextView mContentView;
        final ImageView imgItem;
        DetailData mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            textName = (TextView) view.findViewById(R.id.textName);
         //   mContentView = (TextView) view.findViewById(R.id.content);
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textName.getText() + "'";
        }
    }
}
