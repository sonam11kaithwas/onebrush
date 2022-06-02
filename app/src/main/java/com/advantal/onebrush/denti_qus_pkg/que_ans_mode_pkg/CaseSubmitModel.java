package com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg;

import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientModel;
import com.advantal.onebrush.registration_pkg.add_case_pkg.LogRecordsList;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam on 23-04-2022 14:24.
 */
public class CaseSubmitModel {
    private final List<LogRecordsList> logRecordsList = new ArrayList<>();
    private Integer id;
    private Integer caseId;
    private Integer caseSequenceCustomer;
    private Integer customerId;
    private Integer forProfile;
    private Object casePayment;
    private Object caseRegistration;
    private String caseOpened;
    private String caseAnswered;
    private Object caseClosed;
    private String status;
    private ArrayList<XRayImageListModl> xRayImageList = new ArrayList<>();
    private Object cameraImageList;
    private Object cameraVideoList;
    private Object prescriptionImageList;
    private Object panLocatorImagesList;
    private String dentist;
    private Integer perscriptionID;
    private String paymentSystem;
    private Integer paymentEvidence;
    private String creationDate;
    private String lastUpdationDate;
    public AddPatientModel patient;


    public List<LogRecordsList> getLogRecordsList() {
        return logRecordsList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getCaseSequenceCustomer() {
        return caseSequenceCustomer;
    }

    public void setCaseSequenceCustomer(Integer caseSequenceCustomer) {
        this.caseSequenceCustomer = caseSequenceCustomer;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getForProfile() {
        return forProfile;
    }

    public void setForProfile(Integer forProfile) {
        this.forProfile = forProfile;
    }

    public Object getCasePayment() {
        return casePayment;
    }

    public void setCasePayment(Object casePayment) {
        this.casePayment = casePayment;
    }

    public Object getCaseRegistration() {
        return caseRegistration;
    }

    public void setCaseRegistration(Object caseRegistration) {
        this.caseRegistration = caseRegistration;
    }

    public String getCaseOpened() {
        return caseOpened;
    }

    public void setCaseOpened(String caseOpened) {
        this.caseOpened = caseOpened;
    }

    public String getCaseAnswered() {
        return caseAnswered;
    }

    public void setCaseAnswered(String caseAnswered) {
        this.caseAnswered = caseAnswered;
    }

    public Object getCaseClosed() {
        return caseClosed;
    }

    public void setCaseClosed(Object caseClosed) {
        this.caseClosed = caseClosed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<XRayImageListModl> getxRayImageList() {
        return xRayImageList;
    }

    public void setxRayImageList(ArrayList<XRayImageListModl> xRayImageList) {
        this.xRayImageList = xRayImageList;
    }

    public Object getCameraImageList() {
        return cameraImageList;
    }

    public void setCameraImageList(Object cameraImageList) {
        this.cameraImageList = cameraImageList;
    }

    public Object getCameraVideoList() {
        return cameraVideoList;
    }

    public void setCameraVideoList(Object cameraVideoList) {
        this.cameraVideoList = cameraVideoList;
    }

    public Object getPrescriptionImageList() {
        return prescriptionImageList;
    }

    public void setPrescriptionImageList(Object prescriptionImageList) {
        this.prescriptionImageList = prescriptionImageList;
    }

    public Object getPanLocatorImagesList() {
        return panLocatorImagesList;
    }

    public void setPanLocatorImagesList(Object panLocatorImagesList) {
        this.panLocatorImagesList = panLocatorImagesList;
    }

    public String getDentist() {
        return dentist;
    }

    public void setDentist(String dentist) {
        this.dentist = dentist;
    }

    public Integer getPerscriptionID() {
        return perscriptionID;
    }

    public void setPerscriptionID(Integer perscriptionID) {
        this.perscriptionID = perscriptionID;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public Integer getPaymentEvidence() {
        return paymentEvidence;
    }

    public void setPaymentEvidence(Integer paymentEvidence) {
        this.paymentEvidence = paymentEvidence;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdationDate() {
        return lastUpdationDate;
    }

    public void setLastUpdationDate(String lastUpdationDate) {
        this.lastUpdationDate = lastUpdationDate;
    }

    public AddPatientModel getPatient() {
        return patient;
    }

    public void setPatient(AddPatientModel patient) {
        this.patient = patient;
    }
}
