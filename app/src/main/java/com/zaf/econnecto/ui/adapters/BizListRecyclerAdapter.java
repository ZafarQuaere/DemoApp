package com.zaf.econnecto.ui.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.MyJsonObjectRequest;
import com.zaf.econnecto.network_call.response_model.biz_list.BizData;
import com.zaf.econnecto.ui.activities.LoginActivity;
import com.zaf.econnecto.ui.fragments.BizListFragment;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.utils.AppConstant;
import com.zaf.econnecto.utils.AppController;
import com.zaf.econnecto.utils.LogUtils;
import com.zaf.econnecto.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BizListRecyclerAdapter extends RecyclerView.Adapter<BizListRecyclerAdapter.ViewHolder> {

    private final List<BizData> mValues;
    private final BizListFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private int lastPosition = -1;

    public BizListRecyclerAdapter(Context context, List<BizData> items, BizListFragment.OnListFragmentInteractionListener listener) {
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
        holder.textEstd.setText(mContext.getString(R.string.establish_year) + ": " + mValues.get(position).getYearEstablished());
        holder.textFollowers.setText(mValues.get(position).getFollowersCount() + " " + mContext.getString(R.string.followers));

        //TODO default following ui updates have to do.
        //TODO in case of user not logged in isFollowing is not coming in response
        if (mValues.get(position).getIsFollowing() == AppConstant.FOLLOWING) {
            updateFollowingUI(holder.textFollow);
        } else {
            updateUnfollowUI(holder.textFollow);
        }
        Picasso.get().load(mValues.get(position).getBizProfilePic()).placeholder(R.drawable.avatar_male).into(holder.imgItem);

        //scroll animation
        //setFadeAnimation(holder.itemView,position);

        holder.textFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLoggedIn(mContext)) {
                    if (holder.textFollow.getText().equals(mContext.getString(R.string.follow))) {
                        callFollowApi(holder, "follow", mValues.get(position).getBusinessId());
                        holder.textFollowers.setText((Integer.parseInt(mValues.get(position).getFollowersCount()) + 1) + " " + mContext.getString(R.string.followers));
                        int followerCount = Integer.parseInt(mValues.get(position).getFollowersCount()) + 1;
                        mValues.get(position).setFollowersCount(followerCount + "");
                        mValues.get(position).setIsFollowing(1);
                        updateFollowingUI(holder.textFollow);
                    } else {
                        LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                                mContext.getString(R.string.do_you_want_to_unfollow) + " " + mValues.get(position).getBusinessName() + " ?", new DialogButtonClick() {
                                    @Override
                                    public void onOkClick() {
                                        callFollowApi(holder, "unfollow", mValues.get(position).getBusinessId());
                                        updateUnfollowUI(holder.textFollow);
                                        holder.textFollowers.setText((Integer.parseInt(mValues.get(position).getFollowersCount()) - 1) + " " + mContext.getString(R.string.followers));
                                        int followerCount = Integer.parseInt(mValues.get(position).getFollowersCount()) - 1;
                                        mValues.get(position).setFollowersCount(followerCount + "");
                                        mValues.get(position).setIsFollowing(0);
                                    }

                                    @Override
                                    public void onCancelClick() {
                                    }
                                });
                    }
                } else {
                    LogUtils.showDialogDoubleButton(mContext, mContext.getString(R.string.cancel), mContext.getString(R.string.ok),
                            mContext.getString(R.string.you_need_to_login_first_to_follow_a_business) , new DialogButtonClick() {
                                @Override
                                public void onOkClick() {
                                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                }

                                @Override
                                public void onCancelClick() { }
                            });
                }

            }
        });


       /* holder.textViewBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    public void updateUnfollowUI(TextView textFollow) {
        textFollow.setText(mContext.getString(R.string.follow));
        textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_follow));
    }

    public void updateFollowingUI(TextView textFollow) {
        textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_unfollow));
        textFollow.setText(mContext.getString(R.string.following));

    }

    public void setFadeAnimation(View view, int position) {
      /*  AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);*/

        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }


    private void callFollowApi(final ViewHolder holder, final String action, String businessUid) {
        String url = AppConstant.URL_BASE + AppConstant.URL_FOLLOW;// + 3;

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("action", action);
            requestObject.put("user_email", Utils.getUserEmail(mContext));
            requestObject.put("business_uid", businessUid);

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.ERROR(e.getMessage());
        }
        LogUtils.DEBUG("URL : " + url + "\nRequest Body :: " + requestObject.toString());
        final MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(mContext, Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.DEBUG("Follow Response ::" + response.toString());

                if (response != null && !response.equals("")) {
                    int status = response.optInt("status");
                    if (status != AppConstant.SUCCESS) {
                        LogUtils.showErrorDialog(mContext, mContext.getString(R.string.ok), mContext.getString(R.string.something_wrong_from_server_plz_try_again));
                        if (action.equals("follow")) {
                            holder.textFollow.setText(mContext.getString(R.string.follow));
                            holder.textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_follow));
                        } else {
                            holder.textFollow.setBackground(mContext.getResources().getDrawable(R.drawable.btn_unfollow));
                            holder.textFollow.setText(mContext.getString(R.string.following));
                        }
                    } else {

                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.DEBUG("Follow Error ::" + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "Follow");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView textEstd;
        final TextView textFollow;
        final TextView textViewBusiness;
        final TextView textFollowers;
        final ImageView imgItem;
        BizData mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.textName);
            textEstd = (TextView) view.findViewById(R.id.textEstd);
            textFollow = (TextView) view.findViewById(R.id.textFollow);
            textViewBusiness = (TextView) view.findViewById(R.id.textViewBusiness);
            textFollowers = (TextView) view.findViewById(R.id.textFollowers);
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                     //   mListener.onListFragmentInteraction(mItem);
                    }
                }
            });
        }


        @Override
        public String toString() {
            return super.toString() + " '" + textEstd.getText() + "'";
        }
    }
}
