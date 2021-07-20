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
import com.zaf.econnecto.network_call.response_model.img_data.ViewImageData;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private final List<ViewImageData> mValues;
    private final BizCategoryFragment.OnCategoryItemClickListener mListener;
    private Context context;

    public PhotosAdapter(Context mContext, List<ViewImageData> items, BizCategoryFragment.OnCategoryItemClickListener listener) {
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
        holder.textName.setText(mValues.get(position).getImgId());
        //holder.textEstd.setText(mValues.get(position).getLastName());

        Picasso.get().load(mValues.get(position).getImageLink()).placeholder(R.drawable.default_biz_profile_pic).into(holder.imgItem);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
//                    mListener.onCategoryItemClick(holder.mItem);
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
       // final TextView textEstd;
        final ImageView imgItem;
        ViewImageData mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            textName = (TextView) view.findViewById(R.id.textName);
         //   textEstd = (TextView) view.findViewById(R.id.content);
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textName.getText() + "'";
        }
    }
}
