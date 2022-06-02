package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.ArrayList;

/**
 * Created by Surbhi on 2022-03-28 11:59 AM.
 */
public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.MyView> {
    private final Context mContext;
    private final ArrayList<CaseModel> patientList;
    private final GetPatient getPatient;


    public CaseAdapter(ArrayList<CaseModel> patientList, Context mContext, GetPatient getPatient) {
        this.patientList = patientList;
        this.mContext = mContext;
        this.getPatient = getPatient;
    }

    @NonNull
    @Override
    public CaseAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.patient_layout, parent, false);
        return new MyView(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CaseAdapter.MyView holder, @SuppressLint("RecyclerView") int position) {
        final CaseModel caseModel = patientList.get(position);
        holder.button1.setText(patientList.get(position).getName());
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPatient.setPatient(caseModel.getId());

            }
        });


    }

    @Override
    public int getItemCount() {
        return patientList == null ? 0 : patientList.size();
    }

    interface GetPatient {
        void setPatient(int id);
    }

    public class MyView extends RecyclerView.ViewHolder {
        private final AppCompatButton button1;

        public MyView(@NonNull View itemView) {
            super(itemView);
            button1 = itemView.findViewById(R.id.btn_diagnosis);


        }
    }

}
