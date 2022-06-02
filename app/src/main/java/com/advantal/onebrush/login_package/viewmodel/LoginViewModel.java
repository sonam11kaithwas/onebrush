package com.advantal.onebrush.login_package.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.login_package.model_pkg.LoginModelClass;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.ValidationCtrlr;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.Callable;

public class LoginViewModel extends ViewModel implements ApiCallCallBack {

    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> passwordErrorLable = new MutableLiveData<>();
    private final MutableLiveData<ErrorModel> loginSuccessFully = new MutableLiveData<>();
    private final Context context;
    private final LoginModelClass user;
    private RelativeLayout relativeLayout;
    private MutableLiveData<String> text = new MutableLiveData<>();

    public LoginViewModel(Context context, LoginModelClass user) {
        this.user = user;
        this.context = context;
    }

    public MutableLiveData<ErrorModel> getLoginSuccessFully() {
        return loginSuccessFully;
    }

    public void onSubmitClick() {
        if (AppUtility.isNetworkConnected()) {
            user.setEmailAddress(email.getValue());
            user.setPassword(password.getValue());

            if (user.getEmailAddress() != null && ValidationCtrlr.getValidatInstance().emailValidator(user.getEmailAddress())) {
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    user.setEmailAddress(user.getEmailAddress().toLowerCase());
                    userLogin();

                } else {
                    showErrorMsg(context.getResources().getString(R.string.in_case_you_do_not_remember_the_password_you_can_ask_for_a_new_one), "");
                }


            } else {
                showErrorMsg(context.getResources().getString(R.string.wrong_email_address), "");
            }

        } else {
            showErrorMsg(context.getResources().getString(R.string.bad_internet_connection), context.getResources().getString(R.string.noInternet));
        }

    }

    private void userLogin() {
        if (context != null && relativeLayout != null) {
            AppUtility.showProgressBaForRelLay(relativeLayout, context);
        }

        user.setDeviceIp(AppUtility.getIPAddress(true));
        user.setDeviceId(AppConstant.deviceId);
        user.setDeviceToken("6584949476946759765");
        String stringObject = new Gson().toJson(user);
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.user_Login, "LoginActivity",stringObject);
        String encryptString = OneBrushKeyConstant.getEncryptStr(stringObject);
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.UserLogin);
        CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
        model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
        requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.user_Login);

    }

    private void showErrorMsg(String msg, String titleHead) {
        AppUtility.alertDialog(context, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        text.postValue("Error");
                        return null;
                    }
                });
    }

    public void setmyview(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;

    }

    public MutableLiveData<String> getText() {
        return text;
    }

    public void setText(MutableLiveData<String> text) {
        this.text = text;
    }

    @Override
    public void getApionComplete() {
        AppUtility.hideProgressBar(context);
    }


    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        try {
            AppUtility.hideProgressBar(context);
            switch (requestCode) {
                case ApiCallCallBack.UserLogin:
                    try {
                        if (jsonObject.has("responseCode")) {
                            if (jsonObject.get("responseCode").getAsString().equals("200")) {
                                CommonReqModel commonReqModel = new CommonReqModel(jsonObject.get("data").getAsString(), jsonObject.get("authId").getAsString());
                                String stringObject = OneBrushKeyConstant.getDecryptStr(commonReqModel.getData(), commonReqModel.getAuthId());

                                LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                        , ApiServices.user_Login, "LoginACtivity",
                                        jsonObject.get("responseCode").getAsString() + "  " +stringObject);

                                Log.e("Responce:", stringObject);

                                UserAccountDetailsModel model = new Gson().fromJson(stringObject, UserAccountDetailsModel.class);
                                model.setRegistrationCom("Done");
                                model.setPassword(user.getPassword() != null ? user.getPassword() : "");

//                                UserAccountDetailsModel detailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

//                                model.setLastAction(detailsModel.getLastAction());
//                                model.setLastScreen(detailsModel.getLastScreen());
//                                model.setIdMethod(detailsModel.getIdMethod());
//                                model.setRegisteredPIN1(detailsModel.getRegisteredPIN1());
//                                model.setRegisteredPIN2(detailsModel.getRegisteredPIN2());
//                                model.setRegistrationTimePIN(detailsModel.getRegistrationTimePIN());
//                                model.setRegistrationTimeBiom(detailsModel.getRegistrationTimeBiom());
//                                model.setUuId(model.getUuId());
//                                model.setRegistrationCom("Done");

//                                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateUuId(model.getUuId());
//                                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(model);

                                boolean uuIdNotExits = false;
                                String exitUser = "1";
                                List<UserAccountDetailsModel> userList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAllUserList();
                                for (UserAccountDetailsModel userModel : userList) {
                                    if (userModel.getUuId().equalsIgnoreCase(model.getUuId())) {
                                        uuIdNotExits = true;
                                        exitUser = "2";
                                        break;
                                    }
                                }

                                if (!uuIdNotExits) {
                                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().
                                            insertSinleUserAccountDetails(model);
                                    OneBrushAppPrefrence.getSharedprefInstance().setOLDSelectedUserId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
                                    OneBrushAppPrefrence.getSharedprefInstance().setSelectedUserId(model.getUuId());
                                }


                                if (!OneBrushAppPrefrence.getSharedprefInstance().getFirstRegistrationCompleted()) {
                                    OneBrushAppPrefrence.getSharedprefInstance().setFirstRegistrationCompleted(true);
                                }

//                                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().insertSinleUserAccountDetails(model);

                                loginSuccessFully.postValue(new ErrorModel(jsonObject.get("message").getAsString(), exitUser));
                            } else if (jsonObject.get("responseCode").getAsString().equals("404")) {
                                LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                        , ApiServices.user_Login, "LoginACtivity",
                                        jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());
                                if (jsonObject.has("message"))
                                    showErrorMsg(jsonObject.get("message").getAsString(), "");
                            } else if (jsonObject.has("message")) {
                                showErrorMsg(jsonObject.get("message").getAsString(), "");
                            } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                                LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                        , ApiServices.user_Login, "LoginACtivity",
                                        jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());
                                showErrorMsg(jsonObject.get("message").getAsString(), "");
                            }

                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                , ApiServices.user_Login, "LoginACtivity",
                error);
        showErrorMsg(error, "");
    }
}
