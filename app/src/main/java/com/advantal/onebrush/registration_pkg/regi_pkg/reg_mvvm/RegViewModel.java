package com.advantal.onebrush.registration_pkg.regi_pkg.reg_mvvm;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg.EmailExitsModel;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Sonam on 28-02-2022 14:32.
 */

public class RegViewModel extends ViewModel implements ApiCallCallBack {
    public final MutableLiveData<String> passwordErrorLableregis = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> surname = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> preferredLanguage = new MutableLiveData<>();
    public MutableLiveData<String> allowFirmWareUpdate = new MutableLiveData<>();
    public RegModel regModel;
    Context context;
    private MutableLiveData<String> emailExits = new MutableLiveData<>();
    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();
    private EmailExitsModel emailExitsModel;

    public RegViewModel(Context context, RegModel regModel) {
        this.regModel = regModel;
        this.context = context;
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public MutableLiveData<String> getEmailExits() {
        return emailExits;
    }

    public void setEmailExits(MutableLiveData<String> emailExits) {
        this.emailExits = emailExits;
    }

    public void emailExits(EmailExitsModel emailExitsModel) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.isEmailValid, "RegistrationFirstActivity", (new Gson().toJson(emailExitsModel)));
        this.emailExitsModel = emailExitsModel;
        String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(emailExitsModel));
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.isEmailValid);
        CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
        model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
        requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.isEmailValid);
    }


    public void emailExitsVerify() {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.isUser_account_exist, "RegistrationFirstActivity", (new Gson().toJson(emailExitsModel)));
        String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(emailExitsModel));
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.IsEmailExits);
        CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
        requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.isUser_account_exist);
    }

    @Override
    public void getApionComplete() {

    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.isEmailValid:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isEmailValid, "RegistrationFirstActivity", jsonObject.get("responseCode").getAsString()
                        );
                        emailExitsVerify();

                    } else if (jsonObject.get("responseCode").getAsString().equals("401") && jsonObject.has("message")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isEmailValid, "RegistrationFirstActivity",
                                jsonObject.get("message").getAsString()
                                        + " " + jsonObject.get("responseCode").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                    } else if (jsonObject.get("responseCode").getAsString().equals("500") && jsonObject.has("message")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isEmailValid, "RegistrationFirstActivity",
                                jsonObject.get("message").getAsString()
                                        + " " + jsonObject.get("responseCode").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }
                }
                break;
            case ApiCallCallBack.IsEmailExits:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isUser_account_exist, "RegistrationFirstActivity",
                                jsonObject.get("message").getAsString() + "  " +
                                        jsonObject.get("responseCode").getAsString() + "");
                        emailExits.postValue("");


                    } else if (jsonObject.get("responseCode").getAsString().equals("409") && jsonObject.has("message")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isUser_account_exist, "RegistrationFirstActivity", jsonObject.get("message").getAsString()
                                        + "  " + jsonObject.get("responseCode").getAsString()+ "");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("500") && jsonObject.has("message")) {

                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isUser_account_exist, "RegistrationFirstActivity",
                                jsonObject.get("message").getAsString() + "  " + jsonObject.get("responseCode").
                                        getAsString() + "");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.has("message")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.isUser_account_exist, "RegistrationFirstActivity",
                                jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }

                }
                Log.e("", "");
                break;
        }
    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                ,"" , "RegistrationFirstActivity", error);
        errorMsgBar.postValue(new ErrorModel(error, titleHeader));

    }
}
