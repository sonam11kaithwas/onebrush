package com.advantal.onebrush.setting_pkg.feedback_pkg.feedback_view_model_pkg;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.setting_pkg.feedback_pkg.feedback_model_pkg.FeedBackModel;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Surbhi joshi on 09-03-2022 11:48.
 */
public class FeedBackViewModel extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final FeedBackModel feedBackModel;
    private final MutableLiveData<String> feedback = new MutableLiveData<>();
    private MutableLiveData<ErrorModel> errorMsgBar = new MutableLiveData<>();

    public FeedBackViewModel(Context context, FeedBackModel feedBackModel) {
        this.context = context;
        this.feedBackModel = feedBackModel;
    }

    public MutableLiveData<ErrorModel> getErrorMsgBar() {
        return errorMsgBar;
    }

    public void setErrorMsgBar(MutableLiveData<ErrorModel> errorMsgBar) {
        this.errorMsgBar = errorMsgBar;
    }

    public MutableLiveData<String> getFeedback() {
        return feedback;
    }

    public void userfeedback(FeedBackModel feedBackModel) {
        if (context != null) {

            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , ApiServices.feed_back, "FeedBackActivity", (new Gson().toJson(feedBackModel)));

            String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(feedBackModel));
            ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.FeedBack);
            CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
            model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
            requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.feed_back);
        }
    }

    @Override
    public void getApionComplete() {
        errorMsgBar.postValue(new ErrorModel("", ""));
    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case ApiCallCallBack.FeedBack:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        feedback.postValue("");
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.feed_back, "FeedBackActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());

                        if (jsonObject.has("message"))
                            LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                    , ApiServices.feed_back, "FeedBackActivity",
                                    jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());

                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());


                    } else if (jsonObject.get("responseCode").getAsString().equals("400")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.feed_back, "FeedBackActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());

                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.feed_back, "FeedBackActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());

                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    } else if (jsonObject.get("responseCode").getAsString().equals("403")) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.feed_back, "FeedBackActivity",
                                jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());

                        errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));

                    }
                } else if (jsonObject.has("message")) {
                    errorMsgBar.postValue(new ErrorModel(jsonObject.get("message").getAsString(), ""));
                }
                break;

        }

    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                , ApiServices.feed_back, "FeedBackActivity",
                error);

        errorMsgBar.postValue(new ErrorModel(error, ""));

    }
}
