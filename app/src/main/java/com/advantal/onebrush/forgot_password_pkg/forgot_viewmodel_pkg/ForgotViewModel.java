package com.advantal.onebrush.forgot_password_pkg.forgot_viewmodel_pkg;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.R;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.CommonReqModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.forgot_password_pkg.forgot_model_pkg.ForgotModelClass;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg.EmailExitsModel;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ValidationCtrlr;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.secret_key_pkg.OneBrushKeyConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ForgotViewModel extends ViewModel implements ApiCallCallBack {
    private final Context context;
    private final MutableLiveData<String> text1 = new MutableLiveData<>();
    private final ForgotModelClass forgotModelClass;
    public MutableLiveData<String> email1 = new MutableLiveData<>();
    public MutableLiveData<String> forgotPass = new MutableLiveData<>();
    RelativeLayout relativeLayout;
    private MutableLiveData<String> emailExits = new MutableLiveData<>();


    public ForgotViewModel(Context context, ForgotModelClass forgotModelClass) {
        this.context = context;
        this.forgotModelClass = forgotModelClass;
    }

    public MutableLiveData<String> getForgotPass() {
        return forgotPass;
    }

    public void setForgotPass(MutableLiveData<String> forgotPass) {
        this.forgotPass = forgotPass;
    }

    public MutableLiveData<String> getEmailExits() {
        return emailExits;
    }

    public void setEmail1(MutableLiveData<String> emailExits) {
        this.emailExits = emailExits;
    }

    public void onSubmit() {
        forgotModelClass.setEmail1(email1.getValue());
        if (forgotModelClass.getEmail1() != null && ValidationCtrlr.getValidatInstance().emailValidator(forgotModelClass.getEmail1())) {

//            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            oprnDialog();
        } else {
            showError(context.getResources().getString(R.string.uuups_please_enter_a_valid_e_mail_adress));
        }


    }

    public void emailExits(EmailExitsModel emailExitsModel1) {
        if (context != null) {

            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , ApiServices.forgot_password, "Forgot_Password_Activity", (new Gson().toJson(emailExitsModel1)));

            String encryptString = OneBrushKeyConstant.getEncryptStr(new Gson().toJson(emailExitsModel1));
            ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.IsEmailForgot);
            CommonReqModel model = new CommonReqModel(encryptString, OneBrushKeyConstant.authId);
            model.setLanguageCode(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
            requestor.getLoadDataFromSerVer(AppUtility.getJsonObject(new Gson().toJson(model)), ApiServices.forgot_password);
        }
    }


    private void oprnDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.show_dialog);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.show();
        dialog.findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                dialog.dismiss();

            }
        });

    }

    private void showError(String s) {
        text1.postValue("Error");
//        AppUtility.isDisplaySnacker(relativeLayout, context,
//                s);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                text1.postValue("");


            }
        }, 3000);

    }

    private void showErrorMsg(Drawable drawable) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 3000);

    }


    public void setview(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;

    }

    public MutableLiveData<String> getText() {
        return text1;
    }

    @Override
    public void getApionComplete() {


    }

    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {

        switch (requestCode) {
            case ApiCallCallBack.IsEmailForgot:
                if (jsonObject.has("responseCode")) {
                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                        forgotPass.postValue(jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                        emailExits.postValue(jsonObject.get("message").getAsString());
                    } else {
                        emailExits.postValue(jsonObject.get("message").getAsString());

                    }

                }
                Log.e("", "");
                break;
        }

    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        emailExits.postValue(error);

    }
}
