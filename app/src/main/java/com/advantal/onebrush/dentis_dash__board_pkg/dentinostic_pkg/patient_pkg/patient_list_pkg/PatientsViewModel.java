package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.patient_list_pkg;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.advantal.onebrush.BuildConfig;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_model.PatientModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.PatientDetailsModel;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Surbhi on 22-03-2022 10:34.
 */
public class PatientsViewModel extends AndroidViewModel implements ApiCallCallBack {
    private final MutableLiveData<String> patientListNotify = new MutableLiveData<>();

    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();

    public PatientsViewModel(@NonNull Application application) {
        super(application);

    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public void getNewPatients(PatientModel patientModel) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.get_Patient, "PatientListActivity", ApiServices.get_Patient + "/" + patientModel.getUuId());
        String stringObject = new Gson().toJson(patientModel);
        String encryptString = OneBrushKeyConstant.getEncryptStr(stringObject);
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetNewPatient);
        CommonReqModel model = new CommonReqModel(
                encryptString,
                OneBrushKeyConstant.authId);
//        requestor.getDataFromSerVer(patientModel.getUuId(), ApiServices.get_Patient);
        requestor.getDataFromSerVer(ApiServices.get_Patient + "/" + patientModel.getUuId());
    }

    public MutableLiveData<String> getPatientListNotify() {
        return patientListNotify;
    }

    @Override
    public void getApionComplete() {
        errorMsgBar.postValue(new ErrorModel("", ""));
    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.GetNewPatient:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        try {
                            patientListNotify.postValue("");
                            CommonReqModel commonReqModel = new CommonReqModel(jsonObject.get("data").getAsString(),
                                    jsonObject.get("authId").getAsString());
                            String stringObject = OneBrushKeyConstant.getDecryptStr(commonReqModel.getData(), commonReqModel.getAuthId());
                            PatientDetailsModel model = new Gson().fromJson(stringObject, PatientDetailsModel.class);
                            if (model != null && model.getPatientList() != null && model.getPatientList().size() > 0) {
                                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().delete();
                                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().insertMorePatientModel(model.getPatientList());
                            }

                            if (AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().
                                    getAllPatientById(model.getUuId()) == null) {
                                AddPatientModel addPatientModel = new AddPatientModel(0,
                                        model.getSalutation(),
                                        model.getName(),
                                        model.getSurname(),
                                        model.getDateOfBirth(),
                                        model.getStreetAndHousenumber(), model.getZipcode(),
                                        model.getCity(), model.getCountry(),
                                        model.getTelefon(), model.getUuId()
                                        , "2", model.getTelefon(), "android_v_" + BuildConfig.VERSION_NAME
                                        + "_add", "");

                                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().insertPatientModel(addPatientModel);
                            }
                            Log.e("Responce:", stringObject);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }

                    } else if (jsonObject.get("responseCode").getAsString().equals("404")) {
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("400")) {
//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }
                } else if (jsonObject.has("message")) {
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

//                    OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                }
                break;
        }

    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        errorMsgBar.postValue(new ErrorModel(error, ""));

    }
}
