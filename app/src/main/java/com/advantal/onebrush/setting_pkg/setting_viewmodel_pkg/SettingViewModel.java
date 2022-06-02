package com.advantal.onebrush.setting_pkg.setting_viewmodel_pkg;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg.LogoutModel;
import com.advantal.onebrush.setting_pkg.SettingModel;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Surbhi joshi on 01-03-2022 18:43.
 */
public class SettingViewModel extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final SettingModel settingModel;
    private final MutableLiveData<String> logout = new MutableLiveData<>();
        private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();

    public SettingViewModel(Context context, SettingModel settingModel) {
        this.context = context;
        this.settingModel = settingModel;
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public MutableLiveData<String> getLogout() {
        return logout;
    }

    public void logout(LogoutModel logoutModel) {
        if (context != null) {
            String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(logoutModel));
            ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.Logout);
            CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
            model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
            requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.user_logout);
        }
    }

    @Override
    public void getApionComplete() {
        errorMsgBar.postValue(new ErrorModel("", ""));    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.Logout:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        if (jsonObject.has("message")) {
                            logout.postValue(jsonObject.get("message").getAsString());
                        } else {
                            logout.postValue("");
                        }

                    } else if (jsonObject.get("responseCode").getAsString().equals("400")) {
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                    } else if (jsonObject.has("message")) {
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                    }
                }
                break;

        }


    }

    @Override
     public void getApiErrorResponce(String error, String titleHeader) {

    }
}
