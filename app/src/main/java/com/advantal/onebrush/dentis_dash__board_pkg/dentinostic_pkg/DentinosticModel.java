package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg;

public class DentinosticModel {

    private String name = "";
    private String statusId = "";
    private String caseDate = "";
    private int caseId;
    private int ansSubmitOrnot;


    public DentinosticModel(String name, String statusId, String caseDate, int caseId,int ansSubmitOrnot) {
        this.name = name;
        this.statusId = statusId;
        this.caseDate = caseDate;
        this.caseId = caseId;
        this.ansSubmitOrnot = ansSubmitOrnot;
    }

    public DentinosticModel() {
    }


    public int getAnsSubmitOrnot() {
        return ansSubmitOrnot;
    }

    public void setAnsSubmitOrnot(int ansSubmitOrnot) {
        this.ansSubmitOrnot = ansSubmitOrnot;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
