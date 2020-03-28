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
import com.zaf.econnecto.model.CategoryListData;
import com.zaf.econnecto.network_call.response_model.home.CategoryData;
import com.zaf.econnecto.ui.fragments.BizCategoryFragment;
import com.zaf.econnecto.ui.fragments.add_business.AddBizScreen1Fragment;

import java.util.List;

public class CategoryDialogRecylcerAdapter extends RecyclerView.Adapter<CategoryDialogRecylcerAdapter.ViewHolder> {

    private final List<CategoryListData> mCategoryList;
    private final AddBizScreen1Fragment.OnCategoryItemClickListener mListener;
    private Context context;

    public CategoryDialogRecylcerAdapter(Context mContext, List<CategoryListData> items, AddBizScreen1Fragment.OnCategoryItemClickListener listener) {
        context = mContext;
        mCategoryList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mCategoryList.get(position);
        holder.textName.setText(mCategoryList.get(position).getCategoryName());
       // Picasso.get().load(mCategoryList.get(position).getCategoryImage()).placeholder(R.mipmap.ic_launcher).into(holder.imgItem);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onCategoryItemClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView textName;
        CategoryListData mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            textName = view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textName.getText() + "'";
        }
    }
}
