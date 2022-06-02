package com.advantal.onebrush.registration_pkg.add_case_pkg;

/**
 * Created by Sonam on 07-04-2022 12:32.
 */
public class LogRecordsList {
    private final String userId;
    private final int currentPosition;
    private final int nextPosition;
    private final int previousPosition;
    private final String answer;
    private final String question;
    private final String status;
    private String resultingCode;
    private String isType;
    private String createdDate;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getStatus() {
        return status;
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

    public LogRecordsList(String userId, int currentPosition, int nextPosition,
                          int previousPosition, String answer, String question, String status
            , String resultingCode, String isType) {// int uuId,
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


}
