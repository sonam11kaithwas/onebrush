package com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam on 09-03-2022 11:37.
 */
@Entity(tableName = "multiControlQueModel", indices = {@Index(value = "mcId", unique = true)})
public class MultiControlQueModel implements Parcelable {
    public static final Creator<MultiControlQueModel> CREATOR = new Creator<MultiControlQueModel>() {
        @Override
        public MultiControlQueModel createFromParcel(Parcel in) {
            return new MultiControlQueModel(in);
        }

        @Override
        public MultiControlQueModel[] newArray(int size) {
            return new MultiControlQueModel[size];
        }
    };
    @PrimaryKey
    @NonNull

    private int mcId;
    private String resultingCode;
    private int nextPosition;
    private String recordCreationDate;
    private String recordCreationFunction;
    private String recordCreationProfile;
    private String recordUpdateDate;
    private String recordUpdateFunction;
    private String recordUpdateProfile;
    private int multiControlKey;

    public MultiControlQueModel() {
    }

    public MultiControlQueModel(int mcId, int multiControlKey, String resultingCode, int nextPosition,
                                String recordCreationDate, String recordCreationFunction,
                                String recordCreationProfile, String recordUpdateDate, String recordUpdateFunction,
                                String recordUpdateProfile) {
        this.mcId = mcId;
        this.resultingCode = resultingCode;
        this.nextPosition = nextPosition;
        this.recordCreationDate = recordCreationDate;
        this.recordCreationFunction = recordCreationFunction;
        this.recordCreationProfile = recordCreationProfile;
        this.recordUpdateDate = recordUpdateDate;
        this.recordUpdateFunction = recordUpdateFunction;
        this.recordUpdateProfile = recordUpdateProfile;
        this.multiControlKey = multiControlKey;
    }

    protected MultiControlQueModel(Parcel in) {
        mcId = in.readInt();
        resultingCode = in.readString();
        nextPosition = in.readInt();
        recordCreationDate = in.readString();
        recordCreationFunction = in.readString();
        recordCreationProfile = in.readString();
        recordUpdateDate = in.readString();
        recordUpdateFunction = in.readString();
        recordUpdateProfile = in.readString();
        multiControlKey = in.readInt();
    }

    public static Creator<MultiControlQueModel> getCREATOR() {
        return CREATOR;
    }

    public int getMcId() {
        return mcId;
    }

    public void setMcId(int mcId) {
        this.mcId = mcId;
    }

    public String getResultingCode() {
        return resultingCode;
    }

    public void setResultingCode(String resultingCode) {
        this.resultingCode = resultingCode;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public String getRecordCreationDate() {
        return recordCreationDate;
    }

    public void setRecordCreationDate(String recordCreationDate) {
        this.recordCreationDate = recordCreationDate;
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

    public String getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(String recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }

    public String getRecordUpdateFunction() {
        return recordUpdateFunction;
    }

    public void setRecordUpdateFunction(String recordUpdateFunction) {
        this.recordUpdateFunction = recordUpdateFunction;
    }

    public String getRecordUpdateProfile() {
        return recordUpdateProfile;
    }

    public void setRecordUpdateProfile(String recordUpdateProfile) {
        this.recordUpdateProfile = recordUpdateProfile;
    }

    public int getMultiControlKey() {
        return multiControlKey;
    }

    public void setMultiControlKey(int multiControlKey) {
        this.multiControlKey = multiControlKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mcId);
        parcel.writeString(resultingCode);
        parcel.writeInt(nextPosition);
        parcel.writeString(recordCreationDate);
        parcel.writeString(recordCreationFunction);
        parcel.writeString(recordCreationProfile);
        parcel.writeString(recordUpdateDate);
        parcel.writeString(recordUpdateFunction);
        parcel.writeString(recordUpdateProfile);
        parcel.writeInt(multiControlKey);
    }
}
