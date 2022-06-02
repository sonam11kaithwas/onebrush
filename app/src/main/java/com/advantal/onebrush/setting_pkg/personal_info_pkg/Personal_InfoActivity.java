package com.advantal.onebrush.setting_pkg.personal_info_pkg;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityPersonalInfoBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.setting_pkg.SettingsActivity;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_model_pkg.PersonalInfoModel;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_viewmodel_pkg.PersonalInfoFactory;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_viewmodel_pkg.PersonalInfoViewModel;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.date_piker_pkg.DateTimeCallBack;
import com.advantal.onebrush.utilies_pkg.date_piker_pkg.DateTimeDiloag;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.an.biometric.BuildConfig;

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

public class Personal_InfoActivity extends AppCompatActivity {
    private ActivityPersonalInfoBinding binding;

    private PersonalInfoViewModel personalInfoViewModel;
    private UserAccountDetailsModel userAccountDetailsModel;
    private String forgender = "", forLang = "";
    private MyCountDownTimer countDownTimer;
    private int counterForBio = 0;

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
        if (counterForBio <= 2 && isBiometricCompatibleDevice()) {
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
//        setContentView(R.layout.activity_personal_info);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// set status text dark

        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info);

        personalInfoViewModel = ViewModelProviders.of(this, new PersonalInfoFactory(this,
                new PersonalInfoModel("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""))).get(PersonalInfoViewModel.class);
        binding.setPersonalinfomodel(personalInfoViewModel);

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("Personal_InfoActivity", userAccountDetailsModel);


        createCounterTimer();

        binding.tvTextBirth.setText("2000-01-01");


        personalInfoViewModel.getUserProfileUpdateSuccessFully().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(Personal_InfoActivity.this);

                Intent intent = new Intent(Personal_InfoActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
            }
        });

        personalInfoViewModel.getErrorMsgBar().observeForever(new Observer<ErrorModel>() {
            @Override
            public void onChanged(ErrorModel s) {
                AppUtility.hideProgressBar(Personal_InfoActivity.this);

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

        setDataInFields();
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

    private void setDataInFields() {
        binding.etName.setText(userAccountDetailsModel.getName() != null ? userAccountDetailsModel.getName() : "");
        binding.etLastname.setText(userAccountDetailsModel.getSurname() != null ? userAccountDetailsModel.getSurname() : "");
//        String str = AppUtility.getDateByFormat(userAccountDetailsModel.getDateOfBirth() != null ? userAccountDetailsModel.getDateOfBirth() : "");
        binding.tvTextBirth.setText(userAccountDetailsModel.getDateOfBirth() != null ? userAccountDetailsModel.getDateOfBirth() : "");

        String gener = "";
        if (userAccountDetailsModel.getGender() != null && !userAccountDetailsModel.getGender().isEmpty()) {
            if (userAccountDetailsModel.getGender().equalsIgnoreCase("m")) {
                gener = getResources().getString(R.string.male);
//                forgender = "M";

            } else if (userAccountDetailsModel.getGender().equalsIgnoreCase("f")) {
                gener = getResources().getString(R.string.female);
//                forgender = "F";

            } else if (userAccountDetailsModel.getGender().equalsIgnoreCase("o")) {
                gener = getResources().getString(R.string.other);
//                forgender = "O";

            }
            forgender = userAccountDetailsModel.getGender();

            binding.tvTextGender.setText(gener);
        }


        String en = "";
        if (userAccountDetailsModel.getPreferredLanguage() != null && !userAccountDetailsModel.getPreferredLanguage().isEmpty()) {
            if (userAccountDetailsModel.getPreferredLanguage().equalsIgnoreCase("en")) {
                en = getResources().getString(R.string.english);
                forLang = "en";

            } else if (userAccountDetailsModel.getPreferredLanguage().equalsIgnoreCase("de")) {
                en = getResources().getString(R.string.german);
                forLang = "de";
            } else {
                forLang = OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage();
                if (forLang.equalsIgnoreCase("de")) {
                    en = getResources().getString(R.string.german);
                } else if (forLang.equalsIgnoreCase("en")) {
                    en = getResources().getString(R.string.english);
                }
                binding.tvTextLanguage.setText(en);
            }

        } else {
            forLang = OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage();
            if (forLang.equalsIgnoreCase("de")) {
                en = getResources().getString(R.string.german);
            } else if (forLang.equalsIgnoreCase("en")) {
                en = getResources().getString(R.string.english);
            }
        }
        binding.tvTextLanguage.setText(en);
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            Intent intent = new Intent(Personal_InfoActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(intent);
            finish();
        }
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void onClickInfo(View view) {
        switch (view.getId()) {
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.cross_img_for_pin:
                this.finish();
                break;
            case R.id.login_btn:
                OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;
            case R.id.back_button:
                Intent intent = new Intent(Personal_InfoActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
                break;
            case R.id.rlayout_dateofbirth:
                getDatePickerDialog();
                break;
            case R.id.rlayout_gender:
            case R.id.tv_text_gender:
                openGenderDialog();
//                binding.spinner.performClick();
                break;
            case R.id.rlayout_language:
            case R.id.tv_text_language:
//                binding.spinner1.performClick();
                openLanguageDialog();

                break;
            case R.id.btn_personal_info:
                updatDataPersonalInfo();
                break;


        }

    }

    private void openGenderDialog() {
        Dialog dialog2 = new Dialog(Personal_InfoActivity.this);
        dialog2.setContentView(R.layout.custom_gender_layout);
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RadioGroup radioGroup = dialog2.findViewById(R.id.radio_group);
        RadioButton radioButton1, radioButton2, radioButton3;

        radioButton1 = dialog2.findViewById(R.id.radio_btn1);
        radioButton2 = dialog2.findViewById(R.id.radio_btn2);
        radioButton3 = dialog2.findViewById(R.id.radio_btn3);


        if (!binding.tvTextGender.getText().toString().isEmpty()) {
            if (binding.tvTextGender.getText().toString().equals(getResources().getString(R.string.male))) {
                radioButton1.setChecked(true);
                forgender = "M";

            } else if (binding.tvTextGender.getText().toString().equals(getResources().getString(R.string.female))) {
                radioButton2.setChecked(true);
                forgender = "F";

            } else if (binding.tvTextGender.getText().toString().equals(getResources().getString(R.string.other))) {
                radioButton3.setChecked(true);
                forgender = "O";

            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.radio_btn1 == i) {
                    binding.tvTextGender.setText(R.string.male);
                    forgender = "M";

                    dialog2.dismiss();


                } else if (R.id.radio_btn2 == i) {
                    binding.tvTextGender.setText(R.string.female);
                    forgender = "F";

                    dialog2.dismiss();


                } else if (R.id.radio_btn3 == i) {
                    binding.tvTextGender.setText(R.string.other);
                    forgender = "O";

                    dialog2.dismiss();


                }


            }
        });
        dialog2.show();
    }

    private void openLanguageDialog() {
        Dialog dialog2 = new Dialog(Personal_InfoActivity.this);
        dialog2.setContentView(R.layout.custom_language_layout);
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RadioGroup radioGroup = dialog2.findViewById(R.id.radio_group);

        RadioButton radioButton1, radioButton2, radioButton3;

        radioButton1 = dialog2.findViewById(R.id.radio_btn1);
        radioButton2 = dialog2.findViewById(R.id.radio_btn2);
        if (!binding.tvTextLanguage.getText().toString().isEmpty()) {
            if (binding.tvTextLanguage.getText().toString().equals(getResources().getString(R.string.english))) {
                radioButton1.setChecked(true);
            } else if (binding.tvTextLanguage.getText().toString().equals(getResources().getString(R.string.german))) {
                radioButton2.setChecked(true);
            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.radio_btn1 == i) {
                    binding.tvTextLanguage.setText(R.string.english);
                    dialog2.dismiss();


                } else if (R.id.radio_btn2 == i) {
                    binding.tvTextLanguage.setText(R.string.german);
                    dialog2.dismiss();


                }
                String langNm = "en";

                if (binding.tvTextLanguage.getText().toString().equalsIgnoreCase(getResources().getString(R.string.english))) {
                    langNm = "en";
                } else {
                    langNm = "de";
                }
                forLang = langNm;
                userAccountDetailsModel.setPreferredLanguage(langNm);
                OneBrushAppPrefrence.getSharedprefInstance().setPreferredLanguage(langNm);

            }
        });
        dialog2.show();

    }


    private void updatDataPersonalInfo() {

        if (!binding.etName.getText().toString().trim().isEmpty()) {
            if (!binding.etLastname.getText().toString().trim().isEmpty()) {
                if (!binding.tvTextGender.getText().toString().trim().isEmpty()) {
                    if (!binding.tvTextLanguage.getText().toString().trim().isEmpty()) {
                        userAccountDetailsModel.setSurname(binding.etLastname.getText().toString().trim());
                        userAccountDetailsModel.setName(binding.etName.getText().toString().trim());
                        userAccountDetailsModel.setDateOfBirth(binding.tvTextBirth.getText().toString().trim());
                        userAccountDetailsModel.setGender(forgender);

                        PersonalInfoModel personalInfoModel = new PersonalInfoModel(userAccountDetailsModel.getUuId(),
                                "D"
                                , userAccountDetailsModel.getBrushName() != null ? userAccountDetailsModel.getBrushName() : "Onebrush",
                                userAccountDetailsModel.getSalutation() != null ? userAccountDetailsModel.getSalutation() : ""
                                , binding.etName.getText().toString().trim()
                                , binding.etLastname.getText().toString().trim()
                                , forLang,
                                forgender,
                                binding.tvTextBirth.getText().toString().trim(),
                                userAccountDetailsModel.getStreetAndHousenumber()
                                        != null ? userAccountDetailsModel.getStreetAndHousenumber() : "",
                                userAccountDetailsModel.getZipcode() != null ? userAccountDetailsModel.getZipcode() : "",
                                userAccountDetailsModel.getCity() != null ? userAccountDetailsModel.getCity() : "",
                                userAccountDetailsModel.getCountry() != null ? userAccountDetailsModel.getCountry() : "",
                                userAccountDetailsModel.getTelefon() != null ? userAccountDetailsModel.getTelefon() : "",
                                userAccountDetailsModel.getAllowPushCom() != null ? userAccountDetailsModel.getAllowPushCom() : "",

                                "android_v_" + BuildConfig.VERSION_NAME
                                        + "_update", "");

                        if (AppUtility.isNetworkConnected()) {
                            AppUtility.showProgressBaForRelLay(binding.layout, this);
                            personalInfoViewModel.profileUpdate(personalInfoModel);
                        } else {
                            showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));

                        }

                    } else {
                        showErrorMsg(AppConstant.langcondition, "");

                    }

                } else {
                    showErrorMsg(AppConstant.gendercondition, "");
                }

            } else {
                showErrorMsg(AppConstant.serName, "");
            }
        } else showErrorMsg(AppConstant.name, "");

    }

    private void getDatePickerDialog() {
        DialogFragment datePicker = new DateTimeDiloag(new DateTimeCallBack() {
            @Override
            public void getDateTimeFromPicker(String currentDateString) {
                binding.tvTextBirth.setText(currentDateString);
            }

        }, true);
        datePicker.show(getSupportFragmentManager(), "TAG");

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

    }

}