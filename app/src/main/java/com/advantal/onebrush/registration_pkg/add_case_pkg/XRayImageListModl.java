package com.advantal.onebrush.registration_pkg.add_case_pkg;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam on 22-04-2022 18:55.
 */
@Entity(tableName = "xRayImageListModl", indices = {@Index(value = "id", unique = true)})
public class XRayImageListModl {

    private String imgUrl;
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String creationDate;
    private int caseId;

    public XRayImageListModl(String imgUrl, int id, int caseId) {
        this.imgUrl = imgUrl;
        this.caseId = caseId;
        this.id = id;
    }

    public XRayImageListModl() {
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
