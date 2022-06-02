package com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonam on 24-02-2022 15:23.
 */

public class WelcomeCarouselModel implements Parcelable {

    private Integer caroselId;
    private String imgUrl;
    private String title;
    private String descriptions;
    private String creationDate;
    private Boolean isDeleted;
    private String lastUpdationDate;
    private String title_font_size;
    private String title_font_color;
    private String title_font_style;
    private String descriptions_font_size;
    private String descriptions_font_color;
    private String descriptions_font_style;

    public WelcomeCarouselModel() {
    }

    public WelcomeCarouselModel(String imgUrl, String title, String descriptions) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.descriptions = descriptions;
    }

    protected WelcomeCarouselModel(Parcel in) {
        if (in.readByte() == 0) {
            caroselId = null;
        } else {
            caroselId = in.readInt();
        }
        imgUrl = in.readString();
        title = in.readString();
        descriptions = in.readString();
        creationDate = in.readString();
        byte tmpIsDeleted = in.readByte();
        isDeleted = tmpIsDeleted == 0 ? null : tmpIsDeleted == 1;
        lastUpdationDate = in.readString();
        title_font_size = in.readString();
        title_font_color = in.readString();
        title_font_style = in.readString();
        descriptions_font_size = in.readString();
        descriptions_font_color = in.readString();
        descriptions_font_style = in.readString();
    }

    public static final Creator<WelcomeCarouselModel> CREATOR = new Creator<WelcomeCarouselModel>() {
        @Override
        public WelcomeCarouselModel createFromParcel(Parcel in) {
            return new WelcomeCarouselModel(in);
        }

        @Override
        public WelcomeCarouselModel[] newArray(int size) {
            return new WelcomeCarouselModel[size];
        }
    };

    public String getTitle_font_size() {
        return title_font_size;
    }

    public void setTitle_font_size(String title_font_size) {
        this.title_font_size = title_font_size;
    }

    public String getTitle_font_color() {
        return title_font_color;
    }

    public void setTitle_font_color(String title_font_color) {
        this.title_font_color = title_font_color;
    }

    public String getTitle_font_style() {
        return title_font_style;
    }

    public void setTitle_font_style(String title_font_style) {
        this.title_font_style = title_font_style;
    }

    public String getDescriptions_font_size() {
        return descriptions_font_size;
    }

    public void setDescriptions_font_size(String descriptions_font_size) {
        this.descriptions_font_size = descriptions_font_size;
    }

    public String getDescriptions_font_color() {
        return descriptions_font_color;
    }

    public void setDescriptions_font_color(String descriptions_font_color) {
        this.descriptions_font_color = descriptions_font_color;
    }

    public String getDescriptions_font_style() {
        return descriptions_font_style;
    }

    public void setDescriptions_font_style(String descriptions_font_style) {
        this.descriptions_font_style = descriptions_font_style;
    }

    public Integer getCaroselId() {
        return caroselId;
    }

    public void setCaroselId(Integer caroselId) {
        this.caroselId = caroselId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getLastUpdationDate() {
        return lastUpdationDate;
    }

    public void setLastUpdationDate(String lastUpdationDate) {
        this.lastUpdationDate = lastUpdationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        if (caroselId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(caroselId);
        }
        parcel.writeString(imgUrl);
        parcel.writeString(title);
        parcel.writeString(descriptions);
        parcel.writeString(creationDate);
        parcel.writeByte((byte) (isDeleted == null ? 0 : isDeleted ? 1 : 2));
        parcel.writeString(lastUpdationDate);
        parcel.writeString(title_font_size);
        parcel.writeString(title_font_color);
        parcel.writeString(title_font_style);
        parcel.writeString(descriptions_font_size);
        parcel.writeString(descriptions_font_color);
        parcel.writeString(descriptions_font_style);
    }


}
