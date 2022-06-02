package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.dentinostic_viewmodel_pkg;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.CaseSubmitModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticModel;
import com.advantal.onebrush.registration_pkg.add_case_pkg.LogRecordsList;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.MyDateUtils;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DentinosticViewModel extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final DentinosticModel dentinosticModel;
    private final MutableLiveData<String> caseListNotify = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> statusId = new MutableLiveData<>();


    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();
    private MutableLiveData<String> isUserVerifed = new MutableLiveData<>();


    public DentinosticViewModel(Context context, DentinosticModel dentinosticModel) {
        this.context = context;
        this.dentinosticModel = dentinosticModel;
    }

    public MutableLiveData<String> getIsUserVerifed() {
        return isUserVerifed;
    }

    public void setIsUserVerifed(MutableLiveData<String> isUserVerifed) {
        this.isUserVerifed = isUserVerifed;
    }

    public MutableLiveData<String> getCaseListNotify() {
        return caseListNotify;
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public void getCaseDataFromServer(String uuId) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity", "cases/get_all_cases_by_uuid" + uuId);

        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetAllCases);
        requestor.getCaseDataList(uuId);
    }

    @Override
    public void getApionComplete() {
        errorMsgBar.postValue(new ErrorModel("", ""));
    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {

            case ApiCallCallBack.GetAllCases:
                if (jsonObject.has("responseCode")) {

                    try {
                        if (jsonObject.get("responseCode").getAsString().equals("200")) {
                            List<Integer> myTempCaseList = new ArrayList<>();
                            if (jsonObject.has("data") && jsonObject.getAsJsonArray("data").size() > 0) {
                                if (jsonObject.has("responseCode")) {
                                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                                        try {
                                            String questDataStr = new Gson().toJson(jsonObject.get("data"));
                                            LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                                    , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity",
                                                    jsonObject.get("responseCode").getAsString() + "  " + questDataStr);

                                            Type listType = new TypeToken<List<CaseSubmitModel>>() {
                                            }.getType();
                                            List<CaseSubmitModel> caseQuestionModelList = new Gson().fromJson(questDataStr, listType);

                                            for (CaseSubmitModel caseSubmitModel : caseQuestionModelList) {
                                                ArrayList<XRayImageListModl> urlList = new ArrayList<>();

                                                if (caseSubmitModel.getCaseId() != null) {
                                                    myTempCaseList.add(caseSubmitModel.getCaseId());

                                                    ArrayList<AnswerLogDataModel> answerLogDataModelList = new ArrayList<>();
                                                    AnswerLogDataModel answerLogDataModel;
                                                    int typeForAttchment = 0;
                                                    for (LogRecordsList logmodel : caseSubmitModel.getLogRecordsList()) {
                                                        if (!urlList.isEmpty()) {
                                                            urlList.clear();
                                                        }

                                                        answerLogDataModel = new AnswerLogDataModel(logmodel.getAnswer(),
                                                                logmodel.getQuestion(), AppUtility.getCurrentDateTime("dd-MM-yyyy KK:mm:ss")
                                                                , "Request " + caseSubmitModel.getCaseId(),
                                                                caseSubmitModel.getStatus() != null && !caseSubmitModel.getStatus().isEmpty() ?
                                                                        caseSubmitModel.getStatus() :
                                                                        "Waiting for dentist answer",

                                                                logmodel.getCurrentPosition(), logmodel.getNextPosition()
//                                                                , logmodel.getUserId(),
                                                                , OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId(),
                                                                logmodel.getPreviousPosition(),
                                                                logmodel.getIsType() != null && logmodel.getIsType().equals("CheckBox") ? 1 : 0,
                                                                new ArrayList<>(), typeForAttchment,
                                                                1,
                                                                Integer.valueOf(caseSubmitModel.getCaseId()),
                                                                logmodel.getIsType(), logmodel.getResultingCode()
                                                                , caseSubmitModel.getPatient() != null
                                                                && caseSubmitModel.getPatient().getName() != null && !caseSubmitModel.getPatient().getName().isEmpty()
                                                                && caseSubmitModel.getPatient().getSurname() != null && !caseSubmitModel.getPatient().getSurname()
                                                                .isEmpty()
                                                                ? caseSubmitModel.getPatient().getName() + " " + caseSubmitModel.getPatient().getSurname() : ""

                                                        );
                                                        answerLogDataModel.setStateForWindow("4");
                                                        answerLogDataModel.setDentist(caseSubmitModel.getDentist() != null &&
                                                                !caseSubmitModel.getDentist().isEmpty() ?
                                                                caseSubmitModel.getDentist() : "Dr. Dentina");
                                                        answerLogDataModelList.add(answerLogDataModel);


                                                        if (caseSubmitModel.getCreationDate() != null
                                                                && !caseSubmitModel.getCreationDate().isEmpty()) {
                                                            String str = MyDateUtils.convertStringToDateTime(
                                                                    (caseSubmitModel.getCreationDate()));
                                                            answerLogDataModel.setCreatedDate(str);
                                                        }
                                                    }


                                                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                                                            deleteAnswerByCaseId(caseSubmitModel.getCaseId());

                                                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                                                            insertAllAnswerSList(answerLogDataModelList);


                                                    for (XRayImageListModl xRayImageListModl : caseSubmitModel.getxRayImageList()) {
                                                        typeForAttchment = 1;
                                                        urlList.add(new XRayImageListModl(xRayImageListModl.getImgUrl(),
                                                                xRayImageListModl.getId(), caseSubmitModel.getCaseId()));
                                                    }

                                                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).xRayImageDao().
                                                            insertAllXRaysList(urlList);


                                                }


                                            }

                                            if ((!OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId().
                                                    equals(OneBrushAppPrefrence.getSharedprefInstance().getOLDSelectedUserId())) &&
                                                    caseQuestionModelList != null && caseQuestionModelList.size() >= 0
                                            ) {
                                                int highestNumber = Collections.max(myTempCaseList);
                                                if (OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId()<=highestNumber)
                                                OneBrushAppPrefrence.getSharedprefInstance().
                                                        setCaseId(highestNumber);
                                            }
                                            caseListNotify.postValue("");

                                        } catch (JsonSyntaxException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (jsonObject.get("responseCode").getAsString().equals("404")) {
                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity",
                            jsonObject.get("responseCode").getAsString() + "  ");
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity",
                            jsonObject.get("responseCode").getAsString() + "  ");
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                } else if (jsonObject.get("responseCode").getAsString().equals("400")) {
                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity",
                            jsonObject.get("responseCode").getAsString() + "  ");
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                } else {
                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity",
                            jsonObject.get("message").getAsString() + "  ");
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                }

                break;

        }

    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                , "cases/get_all_cases_by_uuid", "DentinosticDashboardActivity",
                error);

        errorMsgBar.postValue(new ErrorModel(error, ""));

    }
}



