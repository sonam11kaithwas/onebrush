package com.advantal.onebrush.setting_pkg;

/**
 * Created by Surbhi joshi on 01-03-2022 17:13.
 */
public class SettingModel {
    private int img;
    private String text;

    public SettingModel(int img, String text) {
        this.img = img;
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
