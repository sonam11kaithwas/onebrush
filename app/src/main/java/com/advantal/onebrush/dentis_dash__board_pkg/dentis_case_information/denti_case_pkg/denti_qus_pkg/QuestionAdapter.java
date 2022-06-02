package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surbhi on 2022-03-29 10:47 AM.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyHolder> {
    private final Context mContext;
    private final List<AnswerLogDataModel> submitAnswerList ;

    public QuestionAdapter(Context mContext, List<AnswerLogDataModel> submitAnswerList) {
        this.mContext = mContext;
        this.submitAnswerList = submitAnswerList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.question_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final AnswerLogDataModel answerLogDataModel = submitAnswerList.get(position);
        holder.tv_ques.setText(answerLogDataModel.getQuestion());
        holder.tv_ans.setText(answerLogDataModel.getAnswer());


    }

    @Override
    public int getItemCount() {
        return submitAnswerList == null ? 0 : submitAnswerList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tv_ques;
        private final TextView tv_ans;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_ques = itemView.findViewById(R.id.tv_ques);
            tv_ans = itemView.findViewById(R.id.tv_ans);
        }
    }
}
