package com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam on 08-03-2022 12:08.
 */

@Entity(tableName = "questionModel", indices = {@Index(value = "id", unique = true)})
public class QuestionModel {

    @PrimaryKey
    @NonNull
    private int id;
    private int position;
    private String question;
    private String function;
    private String answerData;
    private int nextPosition;
    private int answerFlag;
    private String recordCreation;
    private String recordCreationFunction;
    private String recordCreationProfile;
    private String recordUpdate;
    private String recordUpdateFunction;

    public QuestionModel() {
    }


    public QuestionModel(int id, int position, String question,
                         String function, String answerData, int nextPosition,
                         int answerFlag, String recordCreation, String recordCreationFunction,
                         String recordCreationProfile, String recordUpdate, String recordUpdateFunction) {
        this.id = id;
        this.position = position;
        this.question = question;
        this.function = function;
        this.answerData = answerData;
        this.nextPosition = nextPosition;
        this.answerFlag = answerFlag;
        this.recordCreation = recordCreation;
        this.recordCreationFunction = recordCreationFunction;
        this.recordCreationProfile = recordCreationProfile;
        this.recordUpdate = recordUpdate;
        this.recordUpdateFunction = recordUpdateFunction;

    }


    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getAnswerData() {
        return answerData;
    }

    public void setAnswerData(String answerData) {
        this.answerData = answerData;
    }

    public Integer getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Integer nextPosition) {
        this.nextPosition = nextPosition;
    }

    public Integer getAnswerFlag() {
        return answerFlag;
    }

    public void setAnswerFlag(Integer answerFlag) {
        this.answerFlag = answerFlag;
    }

    public String getRecordCreation() {
        return recordCreation;
    }

    public void setRecordCreation(String recordCreation) {
        this.recordCreation = recordCreation;
    }

    public String getRecordCreationFunction() {
        return recordCreationFunction;
    }

    public void setRecordCreationFunction(String recordCreationFunction) {
        this.recordCreationFunction = recordCreationFunction;
    }

    public String getRecordCreationProfile() {
        return recordCreationProfile;
    }

    public void setRecordCreationProfile(String recordCreationProfile) {
        this.recordCreationProfile = recordCreationProfile;
    }

    public String getRecordUpdate() {
        return recordUpdate;
    }

    public void setRecordUpdate(String recordUpdate) {
        this.recordUpdate = recordUpdate;
    }

    public String getRecordUpdateFunction() {
        return recordUpdateFunction;
    }

    public void setRecordUpdateFunction(String recordUpdateFunction) {
        this.recordUpdateFunction = recordUpdateFunction;
    }


}
