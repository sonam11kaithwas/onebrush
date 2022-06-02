package com.advantal.onebrush.registration_pkg.save_continue_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.Country_Model;

import java.util.List;

/**
 * Created by Surbhi on 2022-04-26 05:23 PM.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.myView> {

    private final List<Country_Model> list;
    private final Context mContext;
    private final SelectCountry selectCountry;

    public CountryAdapter(List<Country_Model> list, Context mContext, SelectCountry selectCountry) {
        this.list = list;
        this.mContext = mContext;
        this.selectCountry = selectCountry;
    }


    @NonNull
    @Override
    public myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.country_item_list, parent, false);
        return new myView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myView holder, int position) {
        final Country_Model country_model = list.get(position);
        holder.textView1.setText(country_model.getName());
        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCountry.setSeelctedCountry(country_model.getName(), country_model.getCountryCode()
//                        "+ "+country_model.getCountryCode()
                );

            }
        });


       /* holder.radioButton.setChecked(false);
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
        });*/
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface SelectCountry {
        void setSeelctedCountry(String selectedUsrId, String selected_country_code);
    }

    public class myView extends RecyclerView.ViewHolder {
        private final TextView textView1;

        public myView(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.tv_country);

        }
    }
}
