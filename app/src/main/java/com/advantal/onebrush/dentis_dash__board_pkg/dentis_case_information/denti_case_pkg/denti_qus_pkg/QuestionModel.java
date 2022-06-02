package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg;

/**
 * Created by Surbhi on 2022-03-29 10:47 AM.
 */
public class QuestionModel {

    private String question = "";
    private String answer = "";


    public QuestionModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
