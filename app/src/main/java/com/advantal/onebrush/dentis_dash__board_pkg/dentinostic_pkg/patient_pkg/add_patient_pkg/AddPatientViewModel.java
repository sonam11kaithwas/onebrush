package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.Country_Model;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam on 21-03-2022 17:08.
 */
public class AddPatientViewModel extends AndroidViewModel implements ApiCallCallBack {

    private final MutableLiveData<String> addPatientSuccessFully = new MutableLiveData<>();
    private final MutableLiveData<List<Country_Model>> country_list = new MutableLiveData<>();
    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();


    public AddPatientViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public MutableLiveData<List<Country_Model>> getUserCountryList() {
        return country_list;
    }

    @Override
    public void getApionComplete() {
        errorMsgBar.postValue(new ErrorModel("", ""));
    }

    public MutableLiveData<String> getAddPatientSuccessFully() {
        return addPatientSuccessFully;
    }

    public void addNewPatients(AddPatientModel addPatientModel) {
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().insertPatientModel(addPatientModel);

        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.save_Patient, "AddPatientActivity", (new Gson().toJson(addPatientModel)));

        String stringObject = new Gson().toJson(addPatientModel);
        String encryptString = OneBrushKeyConstant.getEncryptStr(stringObject);
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.AddNewPatient);
        CommonReqModel model = new CommonReqModel(
                encryptString,
                OneBrushKeyConstant.authId);
        requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.save_Patient);
    }

    public void getCountryCode() {
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetCountry);
        requestor.getDataFromSerVer(ApiServices.get_country);
    }


    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.AddNewPatient:

                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        try {
                            CommonReqModel commonReqModel = new CommonReqModel(jsonObject.get("data").getAsString(), jsonObject.get("authId").getAsString());
                            String stringObject = OneBrushKeyConstant.getDecryptStr(commonReqModel.getData(), commonReqModel.getAuthId());
                            AddPatientModel patientModel = new Gson().fromJson(stringObject, AddPatientModel.class);



                            Log.e("Responce:", stringObject);

//                            AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().deletePatientData(patientModel);
                            AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().insertPatientModel(patientModel);
                            errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));


                            addPatientSuccessFully.postValue("");
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    } else if (jsonObject.get("responseCode").getAsString().equals("409")) {
//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("responseCode").getAsString().equals("403")) {
//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.has("message")) {
//                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }

                }
                Log.e("", "");
                break;

            case ApiCallCallBack.GetCountry:
                if (jsonObject.has("data") && jsonObject.getAsJsonArray("data") != null) {
                    String questDataStr = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                    Type queDataListType = new TypeToken<List<Country_Model>>() {
                    }.getType();
                    List<Country_Model> questionModelList = new Gson().fromJson(questDataStr, queDataListType);
                    country_list.setValue(questionModelList);
//                    Log.d("valueoflist", String.valueOf(jsonObject.getAsJsonArray("data")));

                }
                break;
        }

    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        errorMsgBar.postValue(new ErrorModel("", ""));

    }
    // }
}
