package com.advantal.onebrush.setting_pkg.open_list_pkg;

/**
 * Created by Surbhi on 2022-04-18 03:34 PM.
 */
public class ListModel {

    private String userNm;
    private String status2;
    private String userId;


    public ListModel(String userNm, String userId) {
        this.userNm = userNm;
        this.userId = userId;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
