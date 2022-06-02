package com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam on 08-03-2022 12:09.
 */
@Entity(tableName = "answerLogDataModel", indices = {@Index(value = "id", unique = true)})
public class AnswerLogDataModel implements Parcelable {
    public static final Creator<AnswerLogDataModel> CREATOR = new Creator<AnswerLogDataModel>() {
        @Override
        public AnswerLogDataModel createFromParcel(Parcel in) {
            return new AnswerLogDataModel(in);
        }

        @Override
        public AnswerLogDataModel[] newArray(int size) {
            return new AnswerLogDataModel[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String answer;
    private String createdDate;
    private String question;
    private String date;
    private String requestName;
    private String status;
    private int currentPosition;
    private int typeForAttchment;
    private int nextPosition;
    private String userId;
    private int previousPosition;
    private int checkBoxType;
    private int caseId;
    private String anserComplete;
    private String dentist;
    private String resultingCode;
    private String isType;
    private String stateForWindow;
    private int ansSubmitOrnot;
    @TypeConverters(AnswerImageCoverter.class)
    private List<String> galleryImageList = new ArrayList<>();

    private String patientNm;
    public AnswerLogDataModel() {
    }

    public AnswerLogDataModel(String userId, int currentPosition, int nextPosition,
                              int previousPosition, String answer, String question, String status
            , String resultingCode, String isType) {
        this.userId = userId;
        this.currentPosition = currentPosition;
        this.nextPosition = nextPosition;
        this.previousPosition = previousPosition;
        this.answer = answer;
        this.question = question;
        this.status = status;
        this.resultingCode = resultingCode;
        this.isType = isType;
    }

    public AnswerLogDataModel(String answer, String question, String date,
                              String requestName, String status, int currentPosition,
                              int nextPosition, String userId, int previousPosition, int checkBoxType, List<String> galleryImageList
            , int typeForAttchment, int ansSubmitOrnot, int caseId, String isType, String resultingCode,
                              String patientNm) {
        this.answer = answer;
        this.question = question;
        this.date = date;
        this.requestName = requestName;
        this.status = status;
        this.currentPosition = currentPosition;
        this.nextPosition = nextPosition;
        this.userId = userId;
        this.previousPosition = previousPosition;
        this.checkBoxType = checkBoxType;
        this.galleryImageList = galleryImageList;
        this.typeForAttchment = typeForAttchment;
        this.caseId = caseId;
        this.ansSubmitOrnot = ansSubmitOrnot;
//        this.function = function;
        this.resultingCode = resultingCode;
        this.isType = isType;
        this.patientNm=patientNm;
    }

    protected AnswerLogDataModel(Parcel in) {
        id = in.readInt();
        answer = in.readString();
        question = in.readString();
        createdDate = in.readString();
        date = in.readString();
        requestName = in.readString();
        status = in.readString();
        patientNm = in.readString();
        currentPosition = in.readInt();
        typeForAttchment = in.readInt();
        nextPosition = in.readInt();
        userId = in.readString();
        previousPosition = in.readInt();
        checkBoxType = in.readInt();
        caseId = in.readInt();
        anserComplete = in.readString();
        stateForWindow = in.readString();
        dentist = in.readString();
        resultingCode = in.readString();
        isType = in.readString();
        ansSubmitOrnot = in.readInt();
        galleryImageList = in.createStringArrayList();
    }

    public static Creator<AnswerLogDataModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(answer);
        dest.writeString(question);
        dest.writeString(date);
        dest.writeString(requestName);
        dest.writeString(createdDate);
        dest.writeString(dentist);
        dest.writeString(patientNm);
        dest.writeString(status);
        dest.writeInt(currentPosition);
        dest.writeInt(typeForAttchment);
        dest.writeInt(nextPosition);
        dest.writeString(userId);
//        dest.writeString(caseSubmitState);
        dest.writeInt(previousPosition);
        dest.writeInt(checkBoxType);
        dest.writeInt(caseId);
        dest.writeString(anserComplete);
        dest.writeString(stateForWindow);
//        dest.writeString(function);
        dest.writeString(resultingCode);
        dest.writeString(isType);
        dest.writeInt(ansSubmitOrnot);
        dest.writeStringList(galleryImageList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientNm() {
        return patientNm;
    }

    public void setPatientNm(String patientNm) {
        this.patientNm = patientNm;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getTypeForAttchment() {
        return typeForAttchment;
    }

    public void setTypeForAttchment(int typeForAttchment) {
        this.typeForAttchment = typeForAttchment;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }

    public int getCheckBoxType() {
        return checkBoxType;
    }

    public void setCheckBoxType(int checkBoxType) {
        this.checkBoxType = checkBoxType;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getAnserComplete() {
        return anserComplete;
    }

    public void setAnserComplete(String anserComplete) {
        this.anserComplete = anserComplete;
    }

    public String getResultingCode() {
        return resultingCode;
    }

    public void setResultingCode(String resultingCode) {
        this.resultingCode = resultingCode;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public int getAnsSubmitOrnot() {
        return ansSubmitOrnot;
    }

    public void setAnsSubmitOrnot(int ansSubmitOrnot) {
        this.ansSubmitOrnot = ansSubmitOrnot;
    }

    public List<String> getGalleryImageList() {
        return galleryImageList;
    }

    public void setGalleryImageList(List<String> galleryImageList) {
        this.galleryImageList = galleryImageList;
    }

    public String getStateForWindow() {
        return stateForWindow;
    }

    public void setStateForWindow(String stateForWindow) {
        this.stateForWindow = stateForWindow;
    }

    public String getDentist() {
        return dentist;
    }

    public void setDentist(String dentist) {
        this.dentist = dentist;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
