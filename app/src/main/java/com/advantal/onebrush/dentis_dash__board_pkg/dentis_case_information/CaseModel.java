package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information;

/**
 * Created by Surbhi on 2022-03-28 12:00 PM.
 */
public class CaseModel {
    private String name = "";
    private int id;


    public CaseModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
