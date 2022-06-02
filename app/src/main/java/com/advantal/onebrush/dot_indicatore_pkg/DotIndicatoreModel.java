package com.advantal.onebrush.dot_indicatore_pkg;

/**
 * Created by Advantal on DotModel.
 */
public class DotIndicatoreModel {
    private String id;
    private String headerLable;
    private String subLable;
    private int icon;

    public DotIndicatoreModel(String id, String headerLable, String subLable) {
        this.id = id;
        this.headerLable = headerLable;
        this.subLable = subLable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeaderLable() {
        return headerLable;
    }

    public void setHeaderLable(String headerLable) {
        this.headerLable = headerLable;
    }

    public String getSubLable() {
        return subLable;
    }

    public void setSubLable(String subLable) {
        this.subLable = subLable;
    }
}
