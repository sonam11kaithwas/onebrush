package com.advantal.onebrush.dentis_dash__board_pkg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonam on 25-04-2022 14:53.
 */
public class ScreenDetailsModel implements Parcelable {
    public static final Creator<ScreenDetailsModel> CREATOR = new Creator<ScreenDetailsModel>() {
        @Override
        public ScreenDetailsModel createFromParcel(Parcel in) {
            return new ScreenDetailsModel(in);
        }

        @Override
        public ScreenDetailsModel[] newArray(int size) {
            return new ScreenDetailsModel[size];
        }
    };
    private String screenName;
    private String title;
    private String screenContent;
    private String creationDate;
    private String updationDate;
    private String discription;
    private int id;
    private int languageId;
    private int screenTypeId;

    public ScreenDetailsModel() {
    }

    protected ScreenDetailsModel(Parcel in) {
        screenName = in.readString();
        title = in.readString();
        screenContent = in.readString();
        creationDate = in.readString();
        updationDate = in.readString();
        discription = in.readString();
        id = in.readInt();
        languageId = in.readInt();
        screenTypeId = in.readInt();
    }

    public static Creator<ScreenDetailsModel> getCREATOR() {
        return CREATOR;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScreenContent() {
        return screenContent;
    }

    public void setScreenContent(String screenContent) {
        this.screenContent = screenContent;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(String updationDate) {
        this.updationDate = updationDate;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getScreenTypeId() {
        return screenTypeId;
    }

    public void setScreenTypeId(int screenTypeId) {
        this.screenTypeId = screenTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(screenName);
        parcel.writeString(title);
        parcel.writeString(screenContent);
        parcel.writeString(creationDate);
        parcel.writeString(updationDate);
        parcel.writeString(discription);
        parcel.writeInt(id);
        parcel.writeInt(languageId);
        parcel.writeInt(screenTypeId);
    }
}
