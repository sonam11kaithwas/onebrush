package com.advantal.onebrush.setting_pkg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.AccountSettingActivity;
import com.advantal.onebrush.setting_pkg.feedback_pkg.FeedBackActivity;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.Personal_InfoActivity;
import com.advantal.onebrush.setting_pkg.privacy_policy_pkg.PrivacyPolicyActivity;
import com.advantal.onebrush.setting_pkg.terms_cond_pkg.TermsConditionActivity;

import java.util.ArrayList;

/**
 * Created by Surbhi joshi on 01-03-2022 17:15.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.myViewHolder> {
    private final ArrayList<SettingModel> list;
    private final Context mContext;
    private AdapterCallBackListener listener;

    public SettingAdapter(ArrayList<SettingModel> list, Context mContext,AdapterCallBackListener listener) {
        this.list = list;
        this.mContext = mContext;
        this.listener=listener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.setting_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SettingModel dentinosticModel = list.get(position);
        holder.setTextView(dentinosticModel.getText());
        holder.setImg(dentinosticModel.getImg());
        holder.linear_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (position == 0) {
//                    mContext.startActivity(new Intent(mContext, Personal_InfoActivity.class));
//
//
//                } else if (position == 1) {
//                    mContext.startActivity(new Intent(mContext, AccountSettingActivity.class));
//
//
//                } else if (position == 2) {
//                    mContext.startActivity(new Intent(mContext, PrivacyPolicyActivity.class));
//
//                } else if (position == 3) {
//                    mContext.startActivity(new Intent(mContext, TermsConditionActivity.class));
//
//                } else if (position == 4) {
//                    mContext.startActivity(new Intent(mContext, FeedBackActivity.class));
//
//                } else if (position == 5) {
//
//                    if (listener!=null)
//                        listener.onClickListner(position);
//                }

                if (listener!=null)
                    listener.onClickListner(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }



    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView img;
        private final LinearLayout linear_setting;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_pinfo);
            img = itemView.findViewById(R.id.iv_next);
            linear_setting = itemView.findViewById(R.id.linear_setting);
        }

        public void setTextView(String text) {
            textView.setText(text);
        }

        public void setImg(int image) {
            img.setImageResource(image);
        }

    }

    public interface AdapterCallBackListener {

        void onClickListner(int position);


    }
}
