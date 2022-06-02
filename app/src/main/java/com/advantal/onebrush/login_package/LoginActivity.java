package com.advantal.onebrush.login_package;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

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
import com.advantal.onebrush.databinding.ActivityLoginBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinGenerateActivity;
import com.advantal.onebrush.forgot_password_pkg.Forgot_Password_Activity;
import com.advantal.onebrush.login_package.model_pkg.LoginModelClass;
import com.advantal.onebrush.login_package.viewmodel.LoginViewModel;
import com.advantal.onebrush.login_package.viewmodel.UserViewModelFactory;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
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

public class LoginActivity extends AppCompatActivity {


    private ActivityLoginBinding activityLoginBinding;
    private UserAccountDetailsModel userAccountDetailsModel;
    private MyCountDownTimer countDownTimer;
    private int counterForBio = 0;
    private boolean SIGNUP = false;

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
                        activityLoginBinding.pinViewLayout.setVisibility(View.GONE);
                        activityLoginBinding.layoutForView.setVisibility(View.VISIBLE);
                        activityLoginBinding.bioLayout.setVisibility(View.GONE);
                        Log.e("Test1", "onAuthenticationSucceeded");
                        counterForBio = 0;
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
        if (counterForBio <= 2 && isBiometricCompatibleDevice()) {
            displayBiometricButton();
        } else {
            pinViewStates();

        }

    }

    private void pinViewStates() {
        activityLoginBinding.txtPinEntry.requestFocus();
        activityLoginBinding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                activityLoginBinding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                activityLoginBinding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                activityLoginBinding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                activityLoginBinding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        activityLoginBinding.layoutForView.setVisibility(View.GONE);
        activityLoginBinding.bioLayout.setVisibility(View.GONE);
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
                activityLoginBinding.pinViewLayout.setVisibility(View.GONE);
                activityLoginBinding.layoutForView.setVisibility(View.GONE);
                activityLoginBinding.bioLayout.setVisibility(View.VISIBLE);
                onTouchIdClick();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this,
                new LoginModelClass())).get(LoginViewModel.class);
        activityLoginBinding.setLoginModel(userViewModel);
        setTheme(R.style.AppTheme_Cursor);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        createCounterTimer();

        AppUtility.updateScreeState("LoginActivity", userAccountDetailsModel);
        AppUtility.setupUI(activityLoginBinding.rlayoutMain, this);


        Bundle bundlecaseid = getIntent().getExtras();
        if (bundlecaseid != null) {
            SIGNUP = getIntent().hasExtra("SIGNUP");
            OneBrushAppPrefrence.getSharedprefInstance().setsignIn(true);
        }
        if (OneBrushAppPrefrence.getSharedprefInstance().getsignIn()) {
            activityLoginBinding.tvAccount.setVisibility(View.VISIBLE);

        } else {
            activityLoginBinding.tvAccount.setVisibility(View.GONE);

        }


        userViewModel.getLoginSuccessFully().observeForever(new Observer<ErrorModel>() {
            @Override
            public void onChanged(ErrorModel s) {
                if (!s.getError().isEmpty()) {
//                    showErrorMsg(s);
                    OneBrushApp.getInstance().showToastmsg(s.getError());

                    if (s.getErrorTitle() != null && s.getErrorTitle().equals("2"))
                        startNewActionForUser();
                    else startNewActionForUserPin();

                }
            }
        });

        userViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("", "");
                if (!s.isEmpty()) {
                    activityLoginBinding.loginText1.setVisibility(View.GONE);
                    activityLoginBinding.loginText2.setVisibility(View.GONE);
                    activityLoginBinding.tvText1.setVisibility(View.VISIBLE);
                    activityLoginBinding.tvText2.setVisibility(View.VISIBLE);


                } else {
                    activityLoginBinding.loginText1.setVisibility(View.VISIBLE);
                    activityLoginBinding.loginText2.setVisibility(View.VISIBLE);
                    activityLoginBinding.tvText1.setVisibility(View.GONE);
                    activityLoginBinding.tvText2.setVisibility(View.GONE);
                }
            }
        });
        userViewModel.setmyview(activityLoginBinding.rlayoutMain);

        activityLoginBinding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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


        activityLoginBinding.editTextPass.addTextChangedListener(new TextWatcher() {
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
            }
        });


    }


    private void startNewActionForUser() {
        AppUtility.updateScreeState("DentinosticDashboardActivity", userAccountDetailsModel);
        Intent intent = new Intent(this, DentinosticDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }

    private void startNewActionForUserPin() {
        Intent intent = new Intent(this, PinGenerateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }


    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(activityLoginBinding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            activityLoginBinding.layoutForView.setVisibility(View.VISIBLE);
            activityLoginBinding.pinViewLayout.setVisibility(View.GONE);
            activityLoginBinding.txtPinEntry.setText("");
            counterForBio = 0;
            createCounterTimer();

        } else {
            activityLoginBinding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            activityLoginBinding.loginBtn.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onBackPressed() {
//        Intent intentRegi = new Intent(this, RegistrationFirstActivity.class);
//        intentRegi.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(intentRegi);
        finish();
    }

    private void startLoginForUser() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(intent);
//        this.finish();
        AppUtility.setupUI(activityLoginBinding.layoutForView,this);
        activityLoginBinding.layoutForView.setVisibility(View.VISIBLE);
        activityLoginBinding.pinViewLayout.setVisibility(View.GONE);
        activityLoginBinding.txtPinEntry.setText("");
        counterForBio = 0;
        createCounterTimer();

    }

    public void myClickListner(View view) {
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
            case R.id.tv_forgot:
                Intent intent2 = new Intent(LoginActivity.this, Forgot_Password_Activity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                finish();
                break;
            case R.id.tv_account:
                Intent intent3 = new Intent(LoginActivity.this, RegistrationFirstActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
                finish();


        }
    }

    @Override
    protected void onResume() {

        super.onResume();

    }


    private void showErrorMsg(String msg, String titleHead) {
//        AppUtility.isDisplaySnacker(activityLoginBinding.rlayoutMain, this,
//                msg);
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onDestroy();

    }


    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }
}


