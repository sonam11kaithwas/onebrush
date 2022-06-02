package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.patient_list_pkg;

/**
 * Created by Sonam on 22-03-2022 10:19.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientModel;

import java.util.ArrayList;
import java.util.List;

public class PatientListsAdpter extends RecyclerView.Adapter<PatientListsAdpter.MyView> {
    private final Context mContext;
    private final GetSelectedPatient getSelectedPatient;
    private List<AddPatientModel> patientModelArrayList;
    private int mSelectedItem = 0;

    public PatientListsAdpter(List<AddPatientModel> patientModelArrayList, Context mContext, GetSelectedPatient getSelectedPatient) {
        this.patientModelArrayList = patientModelArrayList;
        this.mContext = mContext;
        this.getSelectedPatient = getSelectedPatient;
    }

    public void updatePatientList(List<AddPatientModel> patientModelArrayList) {
        this.patientModelArrayList = patientModelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.add_patient_layout, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        final AddPatientModel patientModel = patientModelArrayList.get(position);
        holder.radio_patient.setChecked(position == mSelectedItem);
        holder.patient_nm.setText(patientModel.getName() + ", " + patientModel.getSurname());


        if (position == mSelectedItem) {
            holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_check_circle_24));

        } else {
            holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_radio));
        }
//        holder.radio_patient.setButtonDrawable(R.drawable.custom_checkbox);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem = position;
                if (getSelectedPatient != null)
                    getSelectedPatient.setSelectedPatient(patientModel.getPatientId());
                notifyDataSetChanged();
            }
        });


//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSelectedItem = position;
//
//                notifyDataSetChanged();
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return patientModelArrayList.size();
    }

    public interface GetSelectedPatient {
        void setSelectedPatient(int nextpostion);
    }

    public class MyView extends RecyclerView.ViewHolder {
        private final RadioButton radio_patient;
        TextView patient_nm;
        ImageView img;
        RelativeLayout layout;

        public MyView(@NonNull View itemView) {
            super(itemView);
            radio_patient = itemView.findViewById(R.id.radio_patient);
            patient_nm = itemView.findViewById(R.id.patient_nm);
            img = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.layout);
//            radio_patient.setButtonDrawable(R.drawable.custom_checkbox);
        }


    }
}

