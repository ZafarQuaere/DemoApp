package com.zaf.econnecto.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.activities.RegisterActivity;
import com.zaf.econnecto.ui.interfaces.AgeSelectedListener;

import java.util.List;

public class AgeGroupRecyclerAdapter extends RecyclerView.Adapter<AgeGroupRecyclerAdapter.MyView> {
    int selectedPosition = -1;
    private List<String> list;
    private Context mContext;
    private AgeSelectedListener mListener;

    public AgeGroupRecyclerAdapter(Context context, List<String> horizontalList) {
        this.list = horizontalList;
        mContext = context;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        holder.textView.setText(list.get(position));
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor((mContext.getResources().getColor(R.color.colorPrimary)));
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        else {
            holder.itemView.setBackgroundColor((mContext.getResources().getColor(R.color.colorWhite)));
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                mListener.onAgeSelected(list.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListener(AgeSelectedListener ageSelectedListener) {
        mListener = ageSelectedListener;
    }

    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;

        public MyView(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textview1);
        }
    }

}