package com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_viewmodel_pkg;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_model_pkg.AccountModel;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Surbhi joshi on 09-03-2022 18:48.
 */
public class AccountViewModel extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final AccountModel accountModel;
    private final MutableLiveData<String> changepass = new MutableLiveData<>();
    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();

    public AccountViewModel(Context context, AccountModel accountModel) {
        this.context = context;
        this.accountModel = accountModel;
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public MutableLiveData<String> getChangepass() {
        return changepass;
    }

    public void userchangepass(AccountModel accountModel) {
        if (context != null) {

            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , ApiServices.change_password, "AccountSettingActivity", (new Gson().toJson(accountModel)));

            String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(accountModel));
            ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.ChangePassword);
            CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
            requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.change_password);
        }
    }

    @Override
    public void getApionComplete() {

    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.ChangePassword:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        changepass.postValue("");
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.change_password, "AccountSettingActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());
                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("responseCode").getAsString().equals("404")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.change_password, "AccountSettingActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("403")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.change_password, "AccountSettingActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());
                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }
                } else if (jsonObject.has("message")) {
                    OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                }
                break;

        }


    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                , ApiServices.change_password, "AccountSettingActivity",
                error);
    }
}
