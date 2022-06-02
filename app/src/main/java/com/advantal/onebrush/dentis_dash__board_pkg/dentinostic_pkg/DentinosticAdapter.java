package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;

import java.util.ArrayList;

public class DentinosticAdapter extends RecyclerView.Adapter<DentinosticAdapter.MyHolder> {
    private  ArrayList<DentinosticModel> contactsList;
    private final Context mContext;
    private final CaseInfDetails caseInfDetails;


    public DentinosticAdapter(ArrayList<DentinosticModel> contactsList, Context mContext, CaseInfDetails caseInfDetails) {
        this.contactsList = contactsList;
        this.mContext = mContext;
        this.caseInfDetails = caseInfDetails;
    }

    public void updateCaseDataList(ArrayList<DentinosticModel> contactsList) {
        this.contactsList = contactsList;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recycler_view, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final DentinosticModel dentinosticModel = contactsList.get(position);
        holder.setContactName(dentinosticModel.getName());
        holder.setContactId(dentinosticModel.getStatusId());

        holder.layout_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (caseInfDetails != null) {
                    caseInfDetails.setCaseInfoDetails(dentinosticModel.getCaseId(), dentinosticModel.getStatusId());


                }


            }
        });

        if (dentinosticModel.getStatusId().equalsIgnoreCase("pending")) {
            holder.statusId.setTextColor(Color.parseColor("#EC6B33"));
        } else if (dentinosticModel.getStatusId().equalsIgnoreCase("Waiting for dentist answer")) {
            holder.statusId.setTextColor(Color.parseColor("#ffa600"));
//            holder.statusId.setTextColor(Color.parseColor("#ffa600"));
        }


    }

    @Override
    public int getItemCount() {
        return contactsList == null ? 0 : contactsList.size();
    }

    public interface CaseInfDetails {
        void setCaseInfoDetails(int caseId, String statusNm);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private final TextView statusName;
        private final TextView statusId;
        private final RelativeLayout layout_linear;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            statusName = itemView.findViewById(R.id.tv_text1);
            statusId = itemView.findViewById(R.id.tv_text2);
            layout_linear = itemView.findViewById(R.id.layout_linear);

        }


        public void setContactName(String name) {
            statusName.setText(name);
        }

        public void setContactId(String id) {
            statusId.setText(id);
        }
    }
}
