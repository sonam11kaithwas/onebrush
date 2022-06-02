package com.advantal.onebrush.denti_qus_pkg;

import android.content.Context;

/**
 * Created by Surbhi joshi on 28-02-2022 17:14.
 */
public class DentiQusModel {
    private Context context;

    private String question;
    private String description;
    private int img;
    private String text;
    private String checkbox;
    private String radiobtn;

    public DentiQusModel(String question, String description, String text, String checkbox, String radiobtn) {
        this.question = question;
        this.description = description;
        this.text = text;
        this.checkbox = checkbox;
        this.radiobtn = radiobtn;
    }

    public String getRadiobtn() {
        return radiobtn;
    }

    public void setRadiobtn(String radiobtn) {
        this.radiobtn = radiobtn;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    /*public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }*/

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }
}
