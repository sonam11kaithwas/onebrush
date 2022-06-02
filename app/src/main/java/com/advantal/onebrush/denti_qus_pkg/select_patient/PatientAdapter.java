package com.advantal.onebrush.denti_qus_pkg.select_patient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_model.PatientModel;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyView> {
    private final ArrayList<PatientModel> contactsList;
    private final Context mContext;


    public PatientAdapter(ArrayList<PatientModel> contactsList, Context mContext) {
        this.contactsList = contactsList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.add_patient_layout, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, @SuppressLint("RecyclerView") int position) {
        final PatientModel patientModel = contactsList.get(position);
     /*   holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSelectedItem = position;
                notifyDataSetChanged();
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return contactsList == null ? 0 : contactsList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
//        private final ImageView imageView;


        public MyView(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.iv_round);


        }


    }
}
