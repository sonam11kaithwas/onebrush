package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityAddPatientBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.PatientListActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.CountryAdapter;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.Country_Model;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.ValidationCtrlr;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AddPatientActivity extends AppCompatActivity {
    private final boolean CHECKSTATE = false;
    ActivityAddPatientBinding binding;
    List<Country_Model> list;
    private AddPatientViewModel addPatientViewModel;
    private UserAccountDetailsModel userAccountDetailsModel;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private MyCountDownTimer countDownTimer;
    private Dialog countryDialog;
    private CountryAdapter countryAdapter;
    private int counterForBio = 0;

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
//        setContentView(R.layout.activity_add_patient);
        setTheme(R.style.AppTheme_Cursor);
        addPatientViewModel = ViewModelProviders.of(this)
                .get(AddPatientViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_patient);
        binding.setLifecycleOwner(this);
        list = new ArrayList<>();
        addPatientViewModel.getCountryCode();


//        binding.setAppointment(appointmentViewModel);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        createCounterTimer();

        AppUtility.updateScreeState("AddPatientActivity", userAccountDetailsModel);

        binding.tvTextBirth.setText("2000-01-01");
        binding.tvTextSalution.setText(R.string.mr);


        addPatientViewModel.getAddPatientSuccessFully().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(AddPatientActivity.this);

                OneBrushAppPrefrence.getSharedprefInstance().setpatientId(OneBrushAppPrefrence.getSharedprefInstance().getpatient() + 1);
                startNewActionForUser();
            }
        });
        addPatientViewModel.getUserCountryList().observeForever(new Observer<List<Country_Model>>() {
            @Override
            public void onChanged(List<Country_Model> country_models) {
                list = country_models;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().equals("Germany")) {
                        binding.etCountry.setText(list.get(i).getName());
                    }
                    if (list.get(i).getCountryCode().equals("+49")) {
                        binding.tvTextPhone.setText(list.get(i).getCountryCode());
                    }

                }

            }
        });


        addPatientViewModel.getErrorMsgBar().observeForever(new Observer<ErrorModel>() {
            @Override
            public void onChanged(ErrorModel s) {
                AppUtility.hideProgressBar(AddPatientActivity.this);

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
    }


    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void onClicklistner(View view) {
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
            case R.id.rlayout_salution:
            case R.id.tv_text_salution:
                openSaluationDialog();

//                binding.spinnerSalution.performClick();
                break;
            case R.id.btn_add_patient:
                addNewPatients();
                break;
            case R.id.cross_button:
                this.finish();
                break;
            case R.id.rlayout_dateofbirth:
                getDatePickerDialog();
                break;
            case R.id.et_country:
                opennewCountryDialog();
                break;
        }
    }

    private void opennewCountryDialog() {
        countryDialog = new Dialog(AddPatientActivity.this);
        countryDialog.setContentView(R.layout.show_country);
        countryDialog.getWindow().setGravity(Gravity.BOTTOM);
        countryDialog.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RecyclerView recyclerView = countryDialog.findViewById(R.id.recycler_list);
        countryAdapter = new CountryAdapter(list, this, new CountryAdapter.SelectCountry() {
            @Override
            public void setSeelctedCountry(String selectedUsrId, String country_code) {
                binding.etCountry.setText(selectedUsrId);
                binding.tvTextPhone.setText(country_code);
                countryDialog.dismiss();


            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        ArrayList<ListModel> addList = featcUserListFromDB();


        recyclerView.setAdapter(countryAdapter);


        countryDialog.show();
    }

    private void openSaluationDialog() {

        Dialog dialog2 = new Dialog(AddPatientActivity.this);
        dialog2.setContentView(R.layout.custom_saluation_layout);
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RadioGroup radioGroup = dialog2.findViewById(R.id.radio_group);

        radioButton1 = dialog2.findViewById(R.id.radio_btn1);
        radioButton2 = dialog2.findViewById(R.id.radio_btn2);


        if (!binding.tvTextSalution.getText().toString().isEmpty()) {
            if (binding.tvTextSalution.getText().toString().equals(getResources().getString(R.string.mr))) {
                radioButton1.setChecked(true);
            } else if (binding.tvTextSalution.getText().toString().equals(getResources().getString(R.string.mis))) {
                radioButton2.setChecked(true);
            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.radio_btn1 == i) {
                    binding.tvTextSalution.setText(R.string.mr);
                    dialog2.dismiss();


                } else if (R.id.radio_btn2 == i) {
                    binding.tvTextSalution.setText(R.string.mis);
                    dialog2.dismiss();


                }


            }
        });
        dialog2.show();


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

    private void startNewActionForUser() {
        Intent intent = new Intent(this, PatientListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            startNewActionForUser();
        }
    }

    private void addNewPatients() {
        String msg = ValidationCtrlr.getValidatInstance().phoneNumValidator(binding.etPhone.getText().toString().trim());

        if (!binding.etName.getText().toString().trim().isEmpty()) {
            if (!binding.etStreet.getText().toString().trim().isEmpty()) {
                if (!binding.etZipcode.getText().toString().trim().isEmpty()) {
                    if (!binding.etCity.getText().toString().trim().isEmpty()) {
                        if (!binding.etCountry.getText().toString().trim().isEmpty()) {
                            if (msg.isEmpty()) {
                                AddPatientModel addPatientModel = new AddPatientModel(OneBrushAppPrefrence.getSharedprefInstance().getpatient(),
                                        binding.tvTextSalution.getText().toString().trim(),
                                        binding.etName.getText().toString().trim(),
                                        binding.etSurname.getText().toString().trim()
                                        , binding.tvTextBirth.getText().toString().trim(),
                                        binding.etStreet.getText().toString().trim(), binding.etZipcode.getText().toString().trim(),
                                        binding.etCity.getText().toString().trim(), binding.etCountry.getText().toString().trim(),
                                        binding.etPhone.getText().toString().trim(),
                                        userAccountDetailsModel.getUuId()
                                        , "2", binding.tvTextPhone.getText().toString().trim(),
                                        "android_v_" + BuildConfig.VERSION_NAME
                                                + "_add", userAccountDetailsModel.getEmailAddress());


                                if (AppUtility.isNetworkConnected()) {
                                    AppUtility.showProgressBaForRelLay(binding.relativeLayout, this);

                                    addPatientViewModel.addNewPatients(addPatientModel);
                                } else {
                                    showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));

                                }
                            } else {
                                showErrorMsg(msg, "");
                            }

                        } else showErrorMsg(AppConstant.country, "");
                    } else showErrorMsg(AppConstant.city, "");

                } else {
                    showErrorMsg(AppConstant.zipCode, "");
                }
            } else {
                showErrorMsg(AppConstant.street, "");
            }
        } else {
            showErrorMsg(AppConstant.name, "");
        }
    }

    private void showErrorMsg(String msg, String titleHead) {
//        AppUtility.isDisplaySnacker(binding.relativeLayout, this,
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
    protected void onResume() {
//        if (CHECKSTATE) {
//
//            if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                    userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                    && MyDigitalClockForScreenLock.getInstance().getMyTimerTime() >= 60) {
//                MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
//                MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//                if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                    startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
//                    binding.txtPinEntry.requestFocus();
//                    binding.pinViewLayout.setVisibility(View.VISIBLE);
//                    binding.layoutForView.setVisibility(View.GONE);
//                }
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
    }

    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }

}