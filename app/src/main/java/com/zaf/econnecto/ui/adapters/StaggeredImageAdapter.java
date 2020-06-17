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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StaggeredImageAdapter extends RecyclerView.Adapter<StaggeredImageAdapter.ViewHolder> {

    private  List<ViewImageData> mValues;
    private Context context;

    public StaggeredImageAdapter(Context mContext, List<ViewImageData> items) {
        context = mContext;
        mValues = items;
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
        holder.textName.setText(mValues.get(position).getImgId().toString());
        //holder.textEstd.setText(mValues.get(position).getLastName());

        Picasso.get().load(mValues.get(position).getImageLink()).placeholder(R.drawable.default_biz_profile_pic).into(holder.imgItem);


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + textName.getText() + "'";
        }
    }
}
