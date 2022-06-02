package com.advantal.onebrush.denti_qus_pkg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.ArrayList;

/**
 * Created by Surbhi joshi on 28-02-2022 17:15.
 */
public class DentiQusAdapter extends RecyclerView.Adapter<DentiQusAdapter.MyHolder> {
    private final Context mContext;
    private final GetNextPosition getNextPosition;
    private ArrayList<QuestionModel> questionModelArrayList;
    private int mSelectedItem = -1;
    private boolean checked = false;

    public DentiQusAdapter(ArrayList<QuestionModel> questionModelArrayList, Context mContext, GetNextPosition getNextPosition) {
        this.questionModelArrayList = questionModelArrayList;
        this.mContext = mContext;
        this.getNextPosition = getNextPosition;
    }

    public void setQuestIonList(ArrayList<QuestionModel> questionModelArrayList) {
        checked=false;
        this.questionModelArrayList = questionModelArrayList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.select_qus_ans, parent, false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        QuestionModel dentiQusModel = questionModelArrayList.get(position);
        holder.mText.setText(dentiQusModel.getAnswerData());
        holder.mRadio.setChecked(false);
        holder.mRadio.setChecked(position == mSelectedItem);


        try {
            if (!checked) {
                AnswerLogDataModel answerLogDataModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                        getAnswerDataBy(dentiQusModel.getPosition(), OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());
                if (answerLogDataModel != null && answerLogDataModel.getAnswer() != null) {
                    if (answerLogDataModel.getAnswer().equalsIgnoreCase(holder.mText.getText().toString())) {
                        holder.mRadio.setChecked(false);

                        mSelectedItem = position;
                        holder.mRadio.setChecked(position == mSelectedItem);

                        if (getNextPosition != null && questionModelArrayList.size() > 0) {
                            getNextPosition.setNextpostion(questionModelArrayList.get(mSelectedItem).getNextPosition(), holder.mText.getText().toString());
                        }

//                    notifyDataSetChanged();
                    }
                }
            }
       /*     if (((Dentinostic_Qus_Ans_Activity) mContext).getTempAnswer().equals(holder.mText.getText().toString())) {
                holder.mRadio.setChecked(false);
                holder.mRadio.setChecked(true);

                getNextPosition.setNextpostion(dentiQusModel.getNextPosition(), holder.mText.getText().toString());

                ((Dentinostic_Qus_Ans_Activity) mContext).setTempAnswer("");
            } else if (((Dentinostic_Qus_Ans_Activity) mContext).getDeleteTempAnswer().equals(dentiQusModel.getAnswerData() + "")) {
                holder.mRadio.setChecked(false);
                holder.mRadio.setChecked(true);

                getNextPosition.setNextpostion(dentiQusModel.getNextPosition(), holder.mText.getText().toString());
                ((Dentinostic_Qus_Ans_Activity) mContext).setDeleteTempAnswer("");
            }*/

        } catch (Exception e) {
            e.printStackTrace();

        }

        holder.mRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked = true;
                holder.mRadio.setChecked(false);

                mSelectedItem = position;

                if (getNextPosition != null && questionModelArrayList.size() > 0) {
                    getNextPosition.setNextpostion(questionModelArrayList.get(mSelectedItem).getNextPosition(), holder.mText.getText().toString());
                    Log.e("text", holder.mText.getText().toString());
                }
                ((Dentinostic_Qus_Ans_Activity) mContext).setTempAnswer("");

                notifyDataSetChanged();
            }
        });

    }

    public void clearList() {
        mSelectedItem = -1;
        questionModelArrayList.clear();
        notifyItemRangeRemoved(0, questionModelArrayList.size());
    }

    @Override
    public int getItemCount() {
        return questionModelArrayList == null ? 0 : questionModelArrayList.size();
    }

    interface GetNextPosition {
        void setNextpostion(int nextpostion, String answer);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public RadioButton mRadio;
        public TextView mText;
        public LinearLayout layout;

        public MyHolder(final View inflate) {
            super(inflate);
            mText = inflate.findViewById(R.id.text);
            mRadio = inflate.findViewById(R.id.radio);
            layout = inflate.findViewById(R.id.layout);

        }
    }

}
