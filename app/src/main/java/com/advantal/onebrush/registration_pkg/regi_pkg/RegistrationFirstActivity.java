package com.advantal.onebrush.registration_pkg.regi_pkg;

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
import com.advantal.onebrush.databinding.ActivityRegistrationFirstBinding;
import com.advantal.onebrush.denti_qus_pkg.Dentinostic_Qus_Ans_Activity;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_next_pkg.Registr_NextActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.reg_mvvm.RegModel;
import com.advantal.onebrush.registration_pkg.regi_pkg.reg_mvvm.RegViewFactory;
import com.advantal.onebrush.registration_pkg.regi_pkg.reg_mvvm.RegViewModel;
import com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg.EmailExitsModel;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
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

public class RegistrationFirstActivity extends AppCompatActivity {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 15;
    public static boolean WEAK, MEDIUM, STRONG, VERY_STRONG;
    private final boolean CHECKSTATE = false;
    String lower = ".*[a-z].*";
    String upper = ".*[A-Z].*";
    String spacialChar = ".*[!@#$%^&+=].*";
    String whiteSpace = ".*\\\\S+$.*";
    String number = ".*[0-9].*";
    private UserAccountDetailsModel userAccountDetailsModel;
    private RegViewModel registrationViewModel;
    private ActivityRegistrationFirstBinding binding;
    private MyCountDownTimer countDownTimer;
    private String str = "rejected";
    private int counterForBio = 0;

    public static boolean calculate(String password) {
        int score = 0;
        // boolean indicating if password has an upper case
        boolean upper = false;
        // boolean indicating if password has a lower case
        boolean lower = false;
        // boolean indicating if password has at least one digit
        boolean digit = false;
        // boolean indicating if password has a leat one special char
        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!specialChar && !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
            } else {
                if (!digit && Character.isDigit(c)) {
                    score++;
                    digit = true;
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                        } else {
                            lower = true;
                        }

                        if (upper && lower) {
                            score++;
                        }
                    }
                }
            }
        }

        int length = password.length();

        if (length > MAX_LENGTH) {
            score++;
        } else if (length < MIN_LENGTH) {
            score = 0;
        }

        // return enum following the score
        switch (score) {
            case 0:
                return WEAK;
            case 1:
                return MEDIUM;
            case 2:
                return STRONG;
            case 3:
                return VERY_STRONG;
            default:
        }

        return VERY_STRONG;
    }

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
                        binding.pinViewLayout.setVisibility(View.GONE);
                        binding.layoutForView.setVisibility(View.VISIBLE);
                        binding.bioLayout.setVisibility(View.GONE);
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
        if (counterForBio <= 2 && isBiometricCompatibleDevice())  {
            displayBiometricButton();
        } else {
            pinViewStates();

        }

    }

    private void pinViewStates() {
        binding.txtPinEntry.requestFocus();
        binding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                binding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                binding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                binding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                binding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        binding.layoutForView.setVisibility(View.GONE);
        binding.bioLayout.setVisibility(View.GONE);
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
                binding.pinViewLayout.setVisibility(View.GONE);
                binding.layoutForView.setVisibility(View.GONE);
                binding.bioLayout.setVisibility(View.VISIBLE);
                onTouchIdClick();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_first);
        registrationViewModel = ViewModelProviders.of(this, new RegViewFactory(this,
                new RegModel())).get(RegViewModel.class);

        binding.setFirstregis(registrationViewModel);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        createCounterTimer();

        AppUtility.updateScreeState("RegistrationFirstActivity", userAccountDetailsModel);

        setALreadyFillValue();

        registrationViewModel.getEmailExits().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(RegistrationFirstActivity.this);
                openNextRegistration();
            }
        });

        registrationViewModel.getErrorMsgBar().observeForever(new Observer<ErrorModel>() {
            @Override
            public void onChanged(ErrorModel s) {
                AppUtility.hideProgressBar(RegistrationFirstActivity.this);

                if (!s.getError().isEmpty()) {

                    showErrorMsg(s.getError(), s.getErrorTitle());

                }
            }
        });
        binding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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

        binding.editTextPass.addTextChangedListener(new TextWatcher() {
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
                binding.viewDashLayout.setVisibility(View.VISIBLE);
                binding.passLable.setVisibility(View.VISIBLE);
                validation(String.valueOf(s));
       /*         if (s.toString().length() == 0) {
                    binding.viewDashLayout.setVisibility(View.GONE);
                    binding.passLable.setVisibility(View.GONE);
                    binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
                    binding.viewSec.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.grey));
                    *//*s.toString().length() >= 1 && s.toString().length() <= 6*//*
                } else if (ValidationCtrlr.getValidatInstance().passawardaValid(s.toString())) {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewSec.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setText(getResources().getString(R.string.week));
                    binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.BrickRed));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);

                    *//*s.toString().length() >= 7 &&*//*

                } else if (ValidationCtrlr.getValidatInstance().passawardaValid(s.toString())) {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setText(getResources().getString(R.string.so_so));
                    binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.yellow));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);

                    *//*s.toString().length() >= 8 && s.toString().length() < 12 &&*//*
                } else if (ValidationCtrlr.getValidatInstance().passawardaValid(s.toString())) {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.light_green));
                    binding.viewSec.setBackgroundColor(getColor(R.color.light_green));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.light_green));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setText(getResources().getString(R.string.good));
                    binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.light_green));

                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                    *//*s.toString().length() >= 10 && s.toString().length() < 11 &&*//*
                } else if (ValidationCtrlr.getValidatInstance().passawardaValid(s.toString())) {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.light_green1));
                    binding.viewSec.setBackgroundColor(getColor(R.color.light_green1));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.light_green1));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.light_green1));
                    binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setText(getResources().getString(R.string.verygood));
                    binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.light_green1));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                } else {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewFifth.setBackgroundColor(getColor(R.color.dark_green));
                    binding.passLable.setText(getResources().getString(R.string.great));
                    binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.dark_green));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }*/


            }


        });


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


    private void setALreadyFillValue() {
        if (userAccountDetailsModel != null) {
            if (userAccountDetailsModel.getEmailAddress() != null)
                binding.editTextEmail.setText(userAccountDetailsModel.getEmailAddress());
            if (userAccountDetailsModel.getPassword() != null)
                binding.editTextPass.setText(userAccountDetailsModel.getPassword());
        }
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            if (AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAllQuesAnswerList().size() != 0) {
                Intent intent1 = new Intent(RegistrationFirstActivity.this, Dentinostic_Qus_Ans_Activity.class);
                intent1.putExtra("caseid", OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
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
            case R.id.btn_reg:
                userRgisterStepFrst();
                break;

            case R.id.sign_in_txt:
                Intent loginintent = new Intent(RegistrationFirstActivity.this, LoginActivity.class);
                loginintent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                loginintent.putExtra("SIGNUP", "SIGNUP");
                startActivity(loginintent);
                finish();
                break;
            case R.id.btn_login:
                Intent intent = new Intent(RegistrationFirstActivity.this, Registr_NextActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.back_button:
                Intent intent1 = new Intent(RegistrationFirstActivity.this, Dentinostic_Qus_Ans_Activity.class);
                intent1.putExtra("caseid", OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
                finish();
                break;

        }
    }

    private void userRgisterStepFrst() {
        if (ValidationCtrlr.getValidatInstance().emailValidator(binding.editTextEmail.getText().toString().trim())) {//!binding.editTextPass.getText().toString().trim().isEmpty()
            if (!binding.editTextPass.getText().toString().trim().isEmpty()) {
                if (!str.isEmpty() && !str.equalsIgnoreCase("rejected")) {
                    userAccountDetailsModel.setEmailAddress(binding.editTextEmail.getText().toString().trim());
                    userAccountDetailsModel.setPassword(binding.editTextPass.getText().toString().trim());
                    if (AppUtility.isNetworkConnected()) {
                        AppUtility.showProgressBaForRelLay(binding.rlayoutMain, this);
                        registrationViewModel.emailExits(new EmailExitsModel(binding.editTextEmail.getText().toString().trim().toLowerCase()));
                    } else {
                        showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));

                    }

                } else {
                    showErrorMsg(this.getResources().getString(R.string.in_case_you_do_not_remember_the_password_you_can_ask_for_a_new_one), "");
                }

            } else {
                showErrorMsg(this.getResources().getString(R.string.plz_enter_password), "");
            }
        } else {
            showErrorMsg(this.getResources().getString(R.string.enter_email_address), "");
        }
    }

    private void showErrorMsg(String msg, String titleHead) {
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void openNextRegistration() {
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(userAccountDetailsModel);
        Intent intent = new Intent(RegistrationFirstActivity.this, Registr_NextActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        finish();
    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(binding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            binding.layoutForView.setVisibility(View.VISIBLE);
            binding.pinViewLayout.setVisibility(View.GONE);
            binding.txtPinEntry.setText("");
            counterForBio = 0;
            createCounterTimer();

        } else {
            binding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            binding.loginBtn.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onResume() {

        super.onResume();

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
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
//        AppUtility.updateScreeState("DentinosticDashboardActivity", userAccountDetailsModel);
    }

    //    private void calculatePasswordStrength(String str) {
//        // Now, we need to define a PasswordStrength enum
//        // with a calculate static method returning the password strength
//        PasswordStrength passwordStrength = PasswordStrength.calculate(str);
//
//        if (passwordStrength.color == 0) {
//            Toast.makeText(this, "weak", Toast.LENGTH_SHORT).show();
//            binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
//
//
//        } else if (passwordStrength.color == 1) {
//            Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
//            binding.viewFirst.setBackgroundColor(getResources().getColor(R.color.yellow));
//
//        } else if (passwordStrength.color == 2) {
//            Toast.makeText(this, "very good", Toast.LENGTH_SHORT).show();
//            binding.viewFirst.setBackgroundColor(getResources().getColor(R.color.yellow));
//
//        } else if (passwordStrength.color == 3) {
//            Toast.makeText(this, "great", Toast.LENGTH_SHORT).show();
//            binding.viewFirst.setBackgroundColor(getResources().getColor(R.color.yellow));
//
//        }
//    }
    private void validation(String txt) {
        int count = 0;
        if (txt.length() >= 12) {
            count = count + 2;
        }

        if ((txt.length() >= 6) && (txt.length() < 12)) {
            count = count + 1;
        }

        if (txt.matches(lower)) {
            count = count + 1;
        }

        if (txt.matches(upper)) {
            count = count + 1;
        }

        if (txt.matches(spacialChar)) {
            count = count + 2;
        }

        if (txt.matches(number)) {
            count = count + 2;
        }

        if (count == 0) {
            binding.viewDashLayout.setVisibility(View.GONE);
            binding.passLable.setVisibility(View.GONE);
            binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.grey));
            str = "rejected";

//            Toast.makeText(RegistrationFirstActivity.this, "Default "+count, Toast.LENGTH_SHORT).show();
        } else if (count <= 3 && count > 0) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.week));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.BrickRed));
            str = "rejected";
//            Toast.makeText(RegistrationFirstActivity.this, "Weak "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 4) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
            binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.so_so));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.yellow));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "So-So "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 5 || count == 6) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.good));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.light_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 7) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFourth.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.verygood));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.light_green1));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Very Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 8) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFifth.setBackgroundColor(getColor(R.color.dark_green));
            binding.passLable.setText(getResources().getString(R.string.great));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.dark_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Great "+count, Toast.LENGTH_SHORT).show();
        }
    }


/*
    private void validation(String txt) {
        if (txt.length() == 0) {
            binding.viewDashLayout.setVisibility(View.GONE);
            binding.passLable.setVisibility(View.GONE);
            binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.grey));
            str = "rejected";


        } else if (txt.matches(lower) && txt.matches(upper) && txt.matches(number) && txt.matches(spacialChar) && txt.length() >= 12) {
//            Toast.makeText(RegistrationFirstActivity.this, "Great", Toast.LENGTH_SHORT).show();
            binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFifth.setBackgroundColor(getColor(R.color.dark_green));
            binding.passLable.setText(getResources().getString(R.string.great));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.dark_green));
            str = "accept";


        } else if (txt.matches(lower) && txt.matches(upper) && txt.matches(number) && txt.matches(spacialChar) && txt.length() >= 6) {
//            Toast.makeText(RegistrationFirstActivity.this, "very Good", Toast.LENGTH_SHORT).show();
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFourth.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.verygood));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.light_green1));
            str = "accept";


        } else if ((txt.matches(lower) && txt.matches(upper) && txt.matches(number) && txt.length() >= 6) ||
                txt.matches(lower) && txt.matches(upper) && txt.matches(spacialChar) && txt.length() >= 6) {
            //        Toast.makeText(RegistrationFirstActivity.this, "Good", Toast.LENGTH_SHORT).show();
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.good));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.light_green));
            str = "accept";


        } else if (txt.matches(lower) && txt.matches(number) && txt.length() >= 6) {
//            Toast.makeText(RegistrationFirstActivity.this, "SO SO", Toast.LENGTH_SHORT).show();
            binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
            binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.so_so));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.yellow));
            str = "accept";


        } else if ((txt.matches(lower)) || txt.matches(upper) || txt.length() > 6) {
            //  Toast.makeText(RegistrationFirstActivity.this, "week", Toast.LENGTH_SHORT).show();
            binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.week));
            binding.passLable.setTextColor(ContextCompat.getColor(RegistrationFirstActivity.this, R.color.BrickRed));
            str = "rejected";


        }

    }
*/


}