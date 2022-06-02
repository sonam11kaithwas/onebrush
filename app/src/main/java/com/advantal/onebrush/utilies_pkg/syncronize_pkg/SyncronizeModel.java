package com.advantal.onebrush.utilies_pkg.syncronize_pkg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonam on 05-04-2022 15:41.
 */
public class SyncronizeModel implements Parcelable {
    private int masterFlag;
    private int recordsInDialogueControl;
    private int recordsInMultiControl;
    private String recordUpdate;
    private String refreshStatus;

    public SyncronizeModel() {
    }

    public static Creator<SyncronizeModel> getCREATOR() {
        return CREATOR;
    }

    protected SyncronizeModel(Parcel in) {
        masterFlag = in.readInt();
        recordsInDialogueControl = in.readInt();
        recordsInMultiControl = in.readInt();
        recordUpdate = in.readString();
        refreshStatus = in.readString();
    }

    public static final Creator<SyncronizeModel> CREATOR = new Creator<SyncronizeModel>() {
        @Override
        public SyncronizeModel createFromParcel(Parcel in) {
            return new SyncronizeModel(in);
        }

        @Override
        public SyncronizeModel[] newArray(int size) {
            return new SyncronizeModel[size];
        }
    };

    public int getMasterFlag() {
        return masterFlag;
    }

    public void setMasterFlag(int masterFlag) {
        this.masterFlag = masterFlag;
    }

    public int getRecordsInDialogueControl() {
        return recordsInDialogueControl;
    }

    public void setRecordsInDialogueControl(int recordsInDialogueControl) {
        this.recordsInDialogueControl = recordsInDialogueControl;
    }

    public int getRecordsInMultiControl() {
        return recordsInMultiControl;
    }

    public void setRecordsInMultiControl(int recordsInMultiControl) {
        this.recordsInMultiControl = recordsInMultiControl;
    }

    public String getRecordUpdate() {
        return recordUpdate;
    }

    public void setRecordUpdate(String recordUpdate) {
        this.recordUpdate = recordUpdate;
    }

    public String getRefreshStatus() {
        return refreshStatus;
    }

    public void setRefreshStatus(String refreshStatus) {
        this.refreshStatus = refreshStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(masterFlag);
        parcel.writeInt(recordsInDialogueControl);
        parcel.writeInt(recordsInMultiControl);
        parcel.writeString(recordUpdate);
        parcel.writeString(refreshStatus);
    }
}
