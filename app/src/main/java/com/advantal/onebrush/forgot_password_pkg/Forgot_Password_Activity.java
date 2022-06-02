package com.advantal.onebrush.forgot_password_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityForgotPasswordBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.forgot_password_pkg.forgot_model_pkg.ForgotModelClass;
import com.advantal.onebrush.forgot_password_pkg.forgot_viewmodel_pkg.ForgotViewModel;
import com.advantal.onebrush.forgot_password_pkg.forgot_viewmodel_pkg.ForgotViewModelFactory;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg.EmailExitsModel;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.ValidationCtrlr;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Forgot_Password_Activity extends AppCompatActivity {

    private ForgotViewModel forgotViewModel;
    private ActivityForgotPasswordBinding forgotPasswordBinding;
    private UserAccountDetailsModel userAccountDetailsModel;
    private MyCountDownTimer countDownTimer;
    private int counterForBio = 0;

    private final boolean CHECKSTATE = false;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Log.e("Test", " ---user interaction...");

        //Reset the timer on user interaction...
        if (countDownTimer != null)
            AppUtility.timeCounterReset(countDownTimer);
    }

    private void displayBiometricButton() {
        Log.e("MyTest", "displayBiometricButton");
        if (isBiometricCompatibleDevice()) {
            generateSecretKey();
        }

         try {
            getBiometricPromptHandler().authenticate(getBiometricPrompt(), new BiometricPrompt.CryptoObject(getCipher()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }

    private void generateSecretKey() {
        Log.e("MyTest", "generateSecretKey");

        KeyGenerator keyGenerator = null;
        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                AppConstant.KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(false)
                .build();
        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, AppConstant.ANDROID_KEY_STORE);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        if (keyGenerator != null) {
            try {
                keyGenerator.init(keyGenParameterSpec);
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            keyGenerator.generateKey();

        }
    }

    private void createCounterTimer() {
        OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        countDownTimer = new MyCountDownTimer(AppConstant.startTime, AppConstant.interval, new MyCountDownTimer.UserIntractionDisable() {
            @Override
            public void getUserPinWindow() {
                countDownTimer = null;

                userIntreactionEvent();
            }
        });
        AppUtility.timeCounterReset(countDownTimer);


    }

    private boolean isBiometricCompatibleDevice() {
        Log.e("MyTest", "isBiometricCompatibleDevice");
//        BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);

        return BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS;
    }


    private BiometricPrompt getBiometricPromptHandler() {
        Log.e("MyTest", "getBiometricPromptHandler");

        return new BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        Log.e("MyTest", "onAuthenticationError");
                        counterForBio++;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onTouchIdClick();
                            }
                        }, 200);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        Log.e("MyTest", "onAuthenticationSucceeded");

                        super.onAuthenticationSucceeded(result);
                        forgotPasswordBinding.pinViewLayout.setVisibility(View.GONE);
                        forgotPasswordBinding.layoutForView.setVisibility(View.VISIBLE);
                        forgotPasswordBinding.bioLayout.setVisibility(View.GONE);
                        Log.e("Test1", "onAuthenticationSucceeded");
                        counterForBio=0;
                        createCounterTimer();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Log.e("MyTest", "onAuthenticationFailed");
                        counterForBio++;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onTouchIdClick();
                            }
                        }, 200);

                    }
                }
        );

    }

    private void onTouchIdClick() {
        Log.e("MyTest", "onTouchIdClick");
        if (counterForBio <= 2 && isBiometricCompatibleDevice())  {
            displayBiometricButton();
        } else {
            pinViewStates();

        }

    }

    private void pinViewStates() {
        forgotPasswordBinding.txtPinEntry.requestFocus();
        forgotPasswordBinding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                forgotPasswordBinding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                forgotPasswordBinding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                forgotPasswordBinding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                forgotPasswordBinding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        forgotPasswordBinding.layoutForView.setVisibility(View.GONE);
        forgotPasswordBinding.bioLayout.setVisibility(View.GONE);
    }

    private BiometricPrompt.PromptInfo getBiometricPrompt() {
        Log.e("MyTest", "getBiometricPrompt");

//        return new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Biometric login for my app")
//                .setSubtitle("Login with your biometric credential")
//                .setNegativeButtonText("cancel")
//                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
//                .setConfirmationRequired(false)
//                .build();
//        Log.e("MyTest", "getBiometricPrompt");
//
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Login with your biometric credential")
                .setNegativeButtonText("Use account password")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setConfirmationRequired(false)
                .build();
    }

    private Cipher getCipher() {
        Log.e("MyTest", "getCipher");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + FORWARD_SLASH
                    + KeyProperties.BLOCK_MODE_CBC + FORWARD_SLASH
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            try {
                cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    private SecretKey getSecretKey() {
        Log.e("MyTest", "getSecretKey");

        KeyStore keyStore = null;
        Key secretKey = null;
        try {
            keyStore = KeyStore.getInstance(AppConstant.ANDROID_KEY_STORE);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        if (keyStore != null) {
            try {
                keyStore.load(null);
            } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                secretKey = keyStore.getKey(AppConstant.KEY_NAME, null);
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        }
        return (SecretKey) secretKey;
    }

    private void userIntreactionEvent() {
        if (userAccountDetailsModel.getIdMethod() != null &&
                (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
                        userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)) {
                counterForBio = 0;
                forgotPasswordBinding.pinViewLayout.setVisibility(View.GONE);
                forgotPasswordBinding.layoutForView.setVisibility(View.GONE);
                forgotPasswordBinding.bioLayout.setVisibility(View.VISIBLE);
                onTouchIdClick();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        forgotViewModel = ViewModelProviders.of(this, new ForgotViewModelFactory(this,
                new ForgotModelClass())).get(ForgotViewModel.class);
        forgotPasswordBinding.setForgotModel(forgotViewModel);
        setTheme(R.style.AppTheme_Cursor);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark


        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

        createCounterTimer();

        AppUtility.updateScreeState("Forgot_Password_Activity", userAccountDetailsModel);

        forgotViewModel.getEmailExits().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(Forgot_Password_Activity.this);
//                if (!s.getError().isEmpty()) {
//                    showErrorMsg(s.getError(),s.getErrorTitle());
//                }
                if (!s.isEmpty()) {
                    showErrorMsg(s,"");
                }
            }
        });
        forgotViewModel.getForgotPass().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(Forgot_Password_Activity.this);
                openDialog();

            }
        });

        forgotViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    forgotPasswordBinding.tvResetSubheader.setVisibility(View.GONE);
                    forgotPasswordBinding.tvInvalidText.setVisibility(View.VISIBLE);
                    forgotPasswordBinding.etEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error_background));
                    forgotPasswordBinding.etEmail.setHintTextColor(getColor(R.color.BrickRed));
//                    forgotPasswordBinding.etEmail.setHintTextColor(getResources().getColor(R.color.BrickRed));


                } else {
                    forgotPasswordBinding.tvResetSubheader.setVisibility(View.VISIBLE);
                    forgotPasswordBinding.tvInvalidText.setVisibility(View.GONE);
                    forgotPasswordBinding.etEmail.setBackground(getResources().getDrawable(R.drawable.edittext_background_shape));
                    forgotPasswordBinding.etEmail.setHintTextColor(getColor(R.color.navy));


                }

            }
        });
        forgotViewModel.setview(forgotPasswordBinding.rlayout);

        forgotPasswordBinding.txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 4) {
                    openPingenerateState();
                }
            }
        });

    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(forgotPasswordBinding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            forgotPasswordBinding.layoutForView.setVisibility(View.VISIBLE);
            forgotPasswordBinding.pinViewLayout.setVisibility(View.GONE);
            forgotPasswordBinding.txtPinEntry.setText("");
            counterForBio=0;
            createCounterTimer();

        } else {
            forgotPasswordBinding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2),"");
            forgotPasswordBinding.loginBtn.setVisibility(View.VISIBLE);

        }
    }


    private void getForgot() {
        Intent intent = new Intent(Forgot_Password_Activity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }


    public void myClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.cross_img_for_pin:
                this.finish();
                break;

            case R.id.login_btn:OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;
            case R.id.back_button:
                onBackPressed();
                break;
              /*  Intent intent = new Intent(Forgot_Password_Activity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;*/
            case R.id.btn_reset:
//                openDialog();
                userForgotPass();
                break;


        }
    }

    private void userForgotPass() {
        if (ValidationCtrlr.getValidatInstance().emailValidator(forgotPasswordBinding.etEmail.getText().toString().trim())) {
            if (forgotPasswordBinding.etEmail.getText().toString().trim().equalsIgnoreCase(userAccountDetailsModel.getEmailAddress())){
            userAccountDetailsModel.setEmailAddress(forgotPasswordBinding.etEmail.getText().toString().trim());
            if (AppUtility.isNetworkConnected()) {
                AppUtility.showProgressBaForRelLay(forgotPasswordBinding.rlayout, this);
                forgotViewModel.emailExits(new EmailExitsModel(forgotPasswordBinding.etEmail.getText().toString().trim()));
            } else {
                showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));

            }

        } else {
            showErrorMsg(this.getResources().getString(R.string.email_not_exit),"");

        }
        } else {
            showErrorMsg(this.getResources().getString(R.string.wrong_email_address),"");

        }

    }

    private void showErrorMsg(String msg,String titleHead) {
        AppUtility.alertDialog(this, msg,titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }


    public void openDialog() {
        Dialog dialog = new Dialog(Forgot_Password_Activity.this);
        dialog.setContentView(R.layout.show_dialog);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.show();
        dialog.findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.updateScreeState("LoginActivity", userAccountDetailsModel);

                Intent intent = new Intent(Forgot_Password_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });

    }

    @Override
    protected void onResume() {
//        if (CHECKSTATE) {
//            if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                    userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                    && MyDigitalClockForScreenLock.getInstance().getMyTimerTime() >= 60) {
//                MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
//                MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//                if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                    startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
//                    forgotPasswordBinding.txtPinEntry.requestFocus();
//                    forgotPasswordBinding.pinViewLayout.setVisibility(View.VISIBLE);
//                    forgotPasswordBinding.layoutForView.setVisibility(View.GONE);
//                }
//                //   startActivity(new Intent(this, OpenPinActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        }
        super.onResume();

    }


    @Override
    protected void onPause() {
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
//            MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//            MyDigitalClockForScreenLock.getInstance().startTimerCounting();
//        }
//        CHECKSTATE = true;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onDestroy();
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
    }

    @Override
    public void onBackPressed() {
        if (forgotPasswordBinding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
        Intent intentlogin = new Intent(this, LoginActivity.class);
        intentlogin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentlogin);
        finish();
    }
    }
}