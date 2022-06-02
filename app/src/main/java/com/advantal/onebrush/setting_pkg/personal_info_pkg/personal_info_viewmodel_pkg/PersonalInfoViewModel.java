package com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_viewmodel_pkg;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_model_pkg.PersonalInfoModel;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Surbhi joshi on 03-03-2022 17:58.
 */
public class PersonalInfoViewModel extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final PersonalInfoModel personalInfoModel;
    private MutableLiveData<String> userProfileUpdateSuccessFully = new MutableLiveData<>();
    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();

    public PersonalInfoViewModel(Context context, PersonalInfoModel personalInfoModel) {
        this.context = context;
        this.personalInfoModel = personalInfoModel;
    }

    public MutableLiveData<String> getUserProfileUpdateSuccessFully() {
        return userProfileUpdateSuccessFully;
    }

    public void setUserProfileUpdateSuccessFully(MutableLiveData<String> userProfileUpdateSuccessFully) {
        this.userProfileUpdateSuccessFully = userProfileUpdateSuccessFully;
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }


    public void profileUpdate(PersonalInfoModel personalInfoModel) {
        if (context != null) {
            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , ApiServices.user_profile_update, "Personal_InfoActivity", (new Gson().toJson(personalInfoModel)));
            String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(personalInfoModel));
            ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.ProfileUpdate);
            CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
            model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
            requestor.updateUserData(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.user_profile_update);
        }
    }

    @Override
    public void getApionComplete() {
    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.ProfileUpdate:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {

                        CommonReqModel commonReqModel = new CommonReqModel(jsonObject.get("data").getAsString(), jsonObject.get("authId").getAsString());
                        String stringObject = OneBrushKeyConstant.getDecryptStr(commonReqModel.getData(), commonReqModel.getAuthId());
                        UserAccountDetailsModel detailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
                        Log.e("Responce:", stringObject);
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.user_profile_update, "Personal_InfoActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + stringObject);
                        UserAccountDetailsModel model = new Gson().fromJson(stringObject, UserAccountDetailsModel.class);


                        detailsModel.setName(model.getName());
                        detailsModel.setSurname(model.getSurname());
                        detailsModel.setDateOfBirth(model.getDateOfBirth());
                        detailsModel.setGender(model.getGender());
                        detailsModel.setPreferredLanguage(model.getPreferredLanguage());
                        OneBrushAppPrefrence.getSharedprefInstance().setPreferredLanguage(model.getPreferredLanguage());
                        detailsModel.setGender(model.getGender());

//                        model.setLastAction(detailsModel.getLastAction());
//                        model.setLastScreen(detailsModel.getLastScreen());
//                        model.setIdMethod(detailsModel.getIdMethod());
//                        model.setRegisteredPIN1(detailsModel.getRegisteredPIN1());
//                        model.setRegisteredPIN2(detailsModel.getRegisteredPIN2());
//                        model.setRegistrationTimePIN(detailsModel.getRegistrationTimePIN());
//                        model.setRegistrationTimeBiom(detailsModel.getRegistrationTimeBiom());
//                        model.setUuId(detailsModel.getUuId());
//                        model.setRegistrationCom("Done");


                        if (jsonObject.has("message")) {
                            OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        }

//                        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateUuId(model.getUuId());
                        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(detailsModel);
                        userProfileUpdateSuccessFully.postValue(jsonObject.get("message").getAsString());

                    } else if (jsonObject.get("responseCode").getAsString().equals("404")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.user_profile_update, "Personal_InfoActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.user_profile_update, "Personal_InfoActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("403")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.user_profile_update, "Personal_InfoActivity",
                                jsonObject.get("responseCode").getAsString() + "  ");
                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }
                } else if (jsonObject.has("message")) {
                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , ApiServices.user_profile_update, "Personal_InfoActivity",
                            jsonObject.get("message").getAsString() + "  ");
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                }
                break;
        }

    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                ,ApiServices.user_profile_update , "Personal_InfoActivity", error);
        errorMsgBar.postValue(new ErrorModel(error, ""));
    }
}
