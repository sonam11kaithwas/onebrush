package com.advantal.onebrush.setting_pkg.open_list_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;

import java.util.ArrayList;

/**
 * Created by Surbhi on 2022-04-18 03:33 PM.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyView> {
    private final ArrayList<ListModel> list;
    private final Context mContext;
    private final UserAccountSelected userAccountSelected;
    private int mSelectedItem = -1;

    public ListAdapter(ArrayList<ListModel> list, Context mContext, UserAccountSelected userAccountSelected) {
        this.list = list;
        this.mContext = mContext;
        this.userAccountSelected = userAccountSelected;
    }


    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_layout, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {

        final ListModel listModel = list.get(position);
        holder.textView1.setText(listModel.getUserNm());
        holder.textView2.setText(listModel.getStatus2());

        holder.radioButton.setChecked(false);
        holder.radioButton.setChecked(position == mSelectedItem);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem = position;
                if (userAccountSelected != null && list.size() > 0) {
                    userAccountSelected.setUserSeelcted(listModel.getUserId());
                }

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface UserAccountSelected {
        void setUserSeelcted(String selectedUsrId);
    }

    public class MyView extends RecyclerView.ViewHolder {
        private final TextView textView1;
        private final TextView textView2;
        private final RadioButton radioButton;

        public MyView(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.tv_ac_1);
            textView2 = itemView.findViewById(R.id.tv_ac_2);
            radioButton = itemView.findViewById(R.id.radio);


        }


    }

}
