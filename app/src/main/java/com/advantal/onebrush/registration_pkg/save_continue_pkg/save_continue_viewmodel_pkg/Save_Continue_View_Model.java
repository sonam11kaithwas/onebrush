package com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_viewmodel_pkg;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.BuildConfig;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientModel;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.Country_Model;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.RegiReqModel;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.Save_Continue_Model;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Surbhi joshi on 24-02-2022 18:49.
 */

public class Save_Continue_View_Model extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final MutableLiveData<String> userRegisteredSuccessFully = new MutableLiveData<>();
    private final MutableLiveData<List<Country_Model>> country_list = new MutableLiveData<>();
    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();


    public Save_Continue_View_Model(Context context, Save_Continue_Model save_continue_model) {
        this.context = context;
    }

    public MutableLiveData<String> getUserRegisteredSuccessFully() {
        return userRegisteredSuccessFully;
    }

    public MutableLiveData<List<Country_Model>> getUserCountryList() {
        return country_list;
    }


    public void userRegistred(UserAccountDetailsModel userAccountDetailsModel) {

        if (context != null) {
            RegiReqModel regiReqModel = new RegiReqModel(userAccountDetailsModel.getGender(), userAccountDetailsModel.getAllowFirmWareUpdate()
                    , userAccountDetailsModel.getBrushName(), userAccountDetailsModel.getCity(), "12345678",
                    userAccountDetailsModel.getAgreedToTermsAndCond()
                    , 12, userAccountDetailsModel.getName(), userAccountDetailsModel.getAgreedToPersonalData(),
                    userAccountDetailsModel.getPassword()
                    , userAccountDetailsModel.getCountry(), OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage()
                    , userAccountDetailsModel.getStreetAndHousenumber()
                    , userAccountDetailsModel.getDateOfBirth(), userAccountDetailsModel.getSurname(), userAccountDetailsModel.getTelefon()
                    , userAccountDetailsModel.getAgreedToInformation(), userAccountDetailsModel.getZipcode(),
                    userAccountDetailsModel.getAllowPushCom()
                    , userAccountDetailsModel.getFirstDentinosticLaunch(), userAccountDetailsModel.getEmailAddress()
                    , "", userAccountDetailsModel.getFirstLaunch(), "android_v_" + BuildConfig.VERSION_NAME
                    + "_add", userAccountDetailsModel.getEmailAddress(), userAccountDetailsModel.getTelefonIsoCode());

            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , ApiServices.customer_user_register, "SaveContinueActivity", (new Gson().toJson(regiReqModel)));

            String stringObject = new Gson().toJson(regiReqModel);
            String encryptString = OneBrushKeyConstant.getEncryptStr(stringObject);
            CommonReqModel model = new CommonReqModel(
                    encryptString,
                    OneBrushKeyConstant.authId);
            /*add default Patient*/
            addDefaultPatient(regiReqModel, userAccountDetailsModel.getUuId());

            model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
            ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.UserReg);
            requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.customer_user_register);
        }

    }

    private void addDefaultPatient(RegiReqModel regiReqModel, String uuiD) {
        AddPatientModel addPatientModel = new AddPatientModel(0,
                regiReqModel.getSalutation(),
                regiReqModel.getName(),
                regiReqModel.getSurname(),
                regiReqModel.getDateOfBirth(),
                regiReqModel.getStreetAndHousenumber(), regiReqModel.getZipcode(),
                regiReqModel.getCity(), regiReqModel.getCountry(),
                regiReqModel.getTelefon(), uuiD
                , "2", regiReqModel.getTelefonIsoCode(), "android_v_" + BuildConfig.VERSION_NAME
                + "_add", "");

        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().insertPatientModel(addPatientModel);
    }

    public void getCountryCode() {
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetCountry);
        requestor.getDataFromSerVer(ApiServices.get_country);
    }

    @Override
    public void getApionComplete() {
    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.UserReg:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        try {
                            CommonReqModel commonReqModel = new CommonReqModel(jsonObject.get("data").getAsString(),
                                    jsonObject.get("authId").getAsString());
                            String stringObject = OneBrushKeyConstant.getDecryptStr(commonReqModel.getData(), commonReqModel.getAuthId());
                            LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                    , ApiServices.customer_user_register, "SaveContinueActivity",
                                    jsonObject.get("responseCode").getAsString() + "  " + stringObject);
                            Log.e("Responce:", stringObject);

                            UserAccountDetailsModel detailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance())
                                    .userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

                            UserAccountDetailsModel model = new Gson().fromJson(stringObject, UserAccountDetailsModel.class);
                            model.setLastAction(detailsModel.getLastAction());
                            model.setLastScreen(detailsModel.getLastScreen());
                            model.setIdMethod(detailsModel.getIdMethod());
                            model.setRegisteredPIN1(detailsModel.getRegisteredPIN1());
                            model.setRegisteredPIN2(detailsModel.getRegisteredPIN2());
                            model.setRegistrationTimePIN(detailsModel.getRegistrationTimePIN());
                            model.setRegistrationTimeBiom(detailsModel.getRegistrationTimeBiom());
                            model.setUuId(model.getUuId());
                            model.setRegistrationCom("Done");

                            model.setPassword(detailsModel.getPassword());

                            OneBrushAppPrefrence.getSharedprefInstance().setOLDSelectedUserId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

                            OneBrushAppPrefrence.getSharedprefInstance().setSelectedUserId(model.getUuId());


                            AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateUuId(model.getUuId());
                            AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(model);
                            userRegisteredSuccessFully.postValue("");

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    } else if (jsonObject.get("responseCode").getAsString().equals("409")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.customer_user_register, "SaveContinueActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));


                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.customer_user_register, "SaveContinueActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("403")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.customer_user_register, "SaveContinueActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
//
                    } else if (jsonObject.has("message")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.customer_user_register, "SaveContinueActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }


                }


                break;

            case ApiCallCallBack.GetCountry:
                if (jsonObject.has("data") && jsonObject.getAsJsonArray("data") != null) {
                    String questDataStr = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                    Type queDataListType = new TypeToken<List<Country_Model>>() {
                    }.getType();
                    List<Country_Model> questionModelList = new Gson().fromJson(questDataStr, queDataListType);
                    country_list.setValue(questionModelList);

                }
                break;
        }
    }


    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                ,"" , "SaveContinueActivity", error);
        errorMsgBar.postValue(new ErrorModel(error, titleHeader));
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

}
