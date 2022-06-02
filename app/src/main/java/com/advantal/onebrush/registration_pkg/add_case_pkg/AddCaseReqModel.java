package com.advantal.onebrush.registration_pkg.add_case_pkg;

import com.advantal.onebrush.BuildConfig;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by Sonam on 07-04-2022 12:10.
 */
public class AddCaseReqModel {
    /************  IOS param's  ***************/

    private final String uuId;
    private final String status;
    private final int forProfile;// 0 for self and id for patient
    private final String dentist;
    private final String perscriptionID;
    private final String paymentSystem;
    private final String paymentEvidence;
    private final String recordCreationFunction;
    //    private ArrayList<XRayImageListModl> xRayImageListModlArrayList;// = new ArrayList<>();
    private MultipartBody.Part[] xRayImages;//= new MultipartBody.Part[0];
//    List<MultipartBody.Part> xRayImages;
    private String caseId;
    private ArrayList<LogRecordsList> logRecordsListStr;// = new ArrayList<>();
    private ArrayList<AnswerLogDataModel> answerLogDataModels;// = new ArrayList<>();
//    private MultipartBody.Part[] cameraImages;// = new MultipartBody.Part[0];
//    private MultipartBody.Part[] panLocatorImages;// = new MultipartBody.Part[0];
//    private MultipartBody.Part[] prescriptionImages;//= new MultipartBody.Part[0];
//    private MultipartBody.Part[] cameraVideos;//= new MultipartBody.Part[0];
    private String recordCreationProfile;

    public AddCaseReqModel(String uuId, String status, int forProfile,
                           String dentist, String perscriptionID, String paymentSystem, String paymentEvidence
            , ArrayList<LogRecordsList> logRecordsList,
             MultipartBody.Part[] xRayImages,
//                           List<MultipartBody.Part> xRayImages,
//                           MultipartBody.Part[] cameraImages,
//                           MultipartBody.Part[] panLocatorImages, MultipartBody.Part[] prescriptionImages,
//                           MultipartBody.Part[] cameraVideos,
                           String caseId, String recordCreationProfile) {
        this.uuId = uuId;
        this.status = status;
        this.forProfile = forProfile;
        this.dentist = dentist;
        this.recordCreationProfile = recordCreationProfile;
        this.perscriptionID = perscriptionID;
        this.paymentSystem = paymentSystem;
        this.paymentEvidence = paymentEvidence;
        this.logRecordsListStr = logRecordsList;
        this.xRayImages = xRayImages;
//        this.cameraImages = cameraImages;
//        this.panLocatorImages = panLocatorImages;
//        this.prescriptionImages = prescriptionImages;
//        this.cameraVideos = cameraVideos;
        this.caseId = caseId;
        this.recordCreationFunction = "android_v_" + BuildConfig.VERSION_NAME + "_add";

    }

    public String getRecordCreationFunction() {
        return recordCreationFunction;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public ArrayList<LogRecordsList> getLogRecordsList() {
        return logRecordsListStr;
    }

    public void setLogRecordsList(ArrayList<LogRecordsList> logRecordsList) {
        this.logRecordsListStr = logRecordsList;
    }

    public String getUuId() {
        return uuId;
    }

    public ArrayList<AnswerLogDataModel> getAnswerLogDataModels() {
        return answerLogDataModels;
    }

    public void setAnswerLogDataModels(ArrayList<AnswerLogDataModel> answerLogDataModels) {
        this.answerLogDataModels = answerLogDataModels;
    }

    public String getRecordCreationProfile() {
        return recordCreationProfile;
    }

    public void setRecordCreationProfile(String recordCreationProfile) {
        this.recordCreationProfile = recordCreationProfile;
    }

    public String getStatus() {
        return status;
    }


    public ArrayList<LogRecordsList> getLogRecordsListStr() {
        return logRecordsListStr;
    }

    public void setLogRecordsListStr(ArrayList<LogRecordsList> logRecordsListStr) {
        this.logRecordsListStr = logRecordsListStr;
    }

//    public ArrayList<XRayImageListModl> getxRayImageListModlArrayList() {
//        return xRayImageListModlArrayList;
//    }
//
//    public void setxRayImageListModlArrayList(ArrayList<XRayImageListModl> xRayImageListModlArrayList) {
//        this.xRayImageListModlArrayList = xRayImageListModlArrayList;
//    }


    public String getDentist() {
        return dentist;
    }

    public String getPerscriptionID() {
        return perscriptionID;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public String getPaymentEvidence() {
        return paymentEvidence;
    }

    public MultipartBody.Part[] getxRayImages() {
        return xRayImages;
    }

    public void setxRayImages(MultipartBody.Part[] xRayImages) {
        this.xRayImages = xRayImages;
    }


    //
//    public MultipartBody.Part[] getCameraImages() {
//        return cameraImages;
//    }
//
//    public void setCameraImages(MultipartBody.Part[] cameraImages) {
//        this.cameraImages = cameraImages;
//    }
//
//    public MultipartBody.Part[] getPanLocatorImages() {
//        return panLocatorImages;
//    }
//
//    public void setPanLocatorImages(MultipartBody.Part[] panLocatorImages) {
//        this.panLocatorImages = panLocatorImages;
//    }
//
//    public MultipartBody.Part[] getPrescriptionImages() {
//        return prescriptionImages;
//    }
//
//    public void setPrescriptionImages(MultipartBody.Part[] prescriptionImages) {
//        this.prescriptionImages = prescriptionImages;
//    }
//
//    public MultipartBody.Part[] getCameraVideos() {
//        return cameraVideos;
//    }
//
//    public void setCameraVideos(MultipartBody.Part[] cameraVideos) {
//        this.cameraVideos = cameraVideos;
//    }

    public int getForProfile() {
        return forProfile;
    }
}
