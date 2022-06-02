package com.advantal.onebrush.registration_pkg.regi_next_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityRegisNextForBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_next_pkg.reg_mvvm.RegNextModel;
import com.advantal.onebrush.registration_pkg.regi_next_pkg.reg_mvvm.RegNextViewFactory;
import com.advantal.onebrush.registration_pkg.regi_next_pkg.reg_mvvm.RegNextViewModel;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.SaveContinueActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.date_piker_pkg.DateTimeCallBack;
import com.advantal.onebrush.utilies_pkg.date_piker_pkg.DateTimeDiloag;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

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

public class Registr_NextActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {//

    private final boolean CHECKSTATE = false;
    ActivityRegisNextForBinding binding;
    PDFView pdfView1, pdfView2;
    private UserAccountDetailsModel userAccountDetailsModel;
    private RegNextViewModel registrationViewModel;
    private AlertDialog alertDialog, alertDialog1;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private String forgender = "";
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
//        setContentView(R.layout.activity_regis_next_for);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regis_next_for);
        registrationViewModel = ViewModelProviders.of(this, new RegNextViewFactory(this,
                new RegNextModel())).get(RegNextViewModel.class);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

        createCounterTimer();

        AppUtility.updateScreeState("Registr_NextActivity", userAccountDetailsModel);

        binding.tvTextBirth.setText("2000-01-01");

        setALreadyFillValue();

        clickListners();


        binding.tvCheckboxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registr_NextActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_terms_con_layout, viewGroup, false);
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.getWindow().setLayout(600, 400); //Controlling width and height.

                dialogView.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                pdfView1 = dialogView.findViewById(R.id.pdfView1);
                pdfView1.fromAsset("onebrush_tnc.pdf")
                        .defaultPage(0)
                        .enableAnnotationRendering(true)
                        .scrollHandle(new DefaultScrollHandle(dialogView.getContext()))
                        .spacing(10).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
//                        Toast.makeText(Term_Condition_View_Activity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                })// in dp
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .load();
                Window window = alertDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog.show();


            }
        });
        binding.tvCheckboxText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registr_NextActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView1 = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_privacy_policy_layout, viewGroup, false);
                builder.setView(dialogView1);
                alertDialog1 = builder.create();
                alertDialog1.getWindow().setLayout(600, 400);//Controlling width and height.


                dialogView1.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                pdfView2 = dialogView1.findViewById(R.id.pdfView1);
                pdfView2.fromAsset("onebrush_privacypolicy.pdf")
                        .defaultPage(0)
                        .enableAnnotationRendering(true)
                        .scrollHandle(new DefaultScrollHandle(dialogView1.getContext()))
                        .spacing(15).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
//                        Toast.makeText(Term_Condition_View_Activity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                })// in dp
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .load();
                Window window = alertDialog1.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog1.show();


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

    private void clickListners() {
        binding.checkedBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.checked_box:
                break;
        }
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

    private void openNextRegistration() {
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(userAccountDetailsModel);
        Intent intent2 = new Intent(Registr_NextActivity.this, SaveContinueActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent2);
        this.finish();
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
            case R.id.rlayout_dateofbirth:
                getDatePickerDialog();
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.tv_text_bottom:
                Intent intent2 = new Intent(Registr_NextActivity.this, LoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                finish();
                break;
            case R.id.rlayout_gender:
            case R.id.tv_text_gender:
                openGenderDialog();
                break;
            case R.id.rlayout_language:
            case R.id.tv_text_language:
                openLanguageDialog();
                break;
            case R.id.btn_signup:
                updatDataForRegi();
                break;
        }
    }

    private void openLanguageDialog() {
        Dialog dialog2 = new Dialog(Registr_NextActivity.this);
        dialog2.setContentView(R.layout.custom_language_layout);
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RadioGroup radioGroup = dialog2.findViewById(R.id.radio_group);

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

                userAccountDetailsModel.setPreferredLanguage(langNm);
                OneBrushAppPrefrence.getSharedprefInstance().setPreferredLanguage(langNm);

            }
        });
        dialog2.show();

    }

    private void openGenderDialog() {
        Dialog dialog2 = new Dialog(Registr_NextActivity.this);
        dialog2.setContentView(R.layout.custom_gender_layout);
        dialog2.getWindow().setGravity(Gravity.BOTTOM);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RadioGroup radioGroup = dialog2.findViewById(R.id.radio_group);
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

    public void radioButtonClicked(View view) {
        boolean isSelected = ((AppCompatRadioButton) view).isChecked();
        switch (view.getId()) {

            case R.id.rb_left:
                if (isSelected) {
                    break;

                }

            case R.id.rb_right:
                if (isSelected) {

                    break;

                }


        }

    }

    private void setALreadyFillValue() {
        if (userAccountDetailsModel != null) {
            if (userAccountDetailsModel.getName() != null)
                binding.etName.setText(userAccountDetailsModel.getName());
            if (userAccountDetailsModel.getSurname() != null)
                binding.etSurname.setText(userAccountDetailsModel.getSurname());
            if (userAccountDetailsModel.getDateOfBirth() != null && !userAccountDetailsModel.getDateOfBirth().isEmpty())
                binding.tvTextBirth.setText(userAccountDetailsModel.getDateOfBirth());

            Log.e("", "");


            String gener = "";
            if (userAccountDetailsModel.getGender() != null && !userAccountDetailsModel.getGender().isEmpty()) {
                if (userAccountDetailsModel.getGender().equalsIgnoreCase("m")) {
                    gener = getResources().getString(R.string.male);

                } else if (userAccountDetailsModel.getGender().equalsIgnoreCase("f")) {
                    gener = getResources().getString(R.string.female);

                } else if (userAccountDetailsModel.getGender().equalsIgnoreCase("o")) {
                    gener = getResources().getString(R.string.other);
                }
                forgender = userAccountDetailsModel.getGender();

                binding.tvTextGender.setText(gener);
            } else {
                binding.tvTextGender.setText(R.string.male);
                forgender = "M";
            }


            String languagePref = getResources().getString(R.string.english);
            if (userAccountDetailsModel.getPreferredLanguage().equalsIgnoreCase("en")) {
                languagePref = getResources().getString(R.string.english);

            } else if (userAccountDetailsModel.getPreferredLanguage().equalsIgnoreCase("de")) {
                languagePref = getResources().getString(R.string.german);
            }

            binding.tvTextLanguage.setText(languagePref);

//            binding.checkedBox.setChecked(true);

        }
    }

    private void updatDataForRegi() {
        if (!binding.etName.getText().toString().trim().isEmpty()) {
            if (!binding.etSurname.getText().toString().trim().isEmpty()) {
                if (!binding.tvTextGender.getText().toString().trim().isEmpty()) {
                    if (!binding.tvTextLanguage.getText().toString().trim().isEmpty()) {
                        if (binding.checkedBox.isChecked()) {
                            userAccountDetailsModel.setSurname(binding.etSurname.getText().toString().trim());
                            userAccountDetailsModel.setName(binding.etName.getText().toString().trim());
                            userAccountDetailsModel.setDateOfBirth(binding.tvTextBirth.getText().toString().trim());
                            userAccountDetailsModel.setGender(forgender);
                            userAccountDetailsModel.setPreferredLanguage(OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());
                            openNextRegistration();
                        } else {
                            showErrorMsg(AppConstant.termscondition, "");
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


    @Override
    public void onClick(View view) {

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
    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            if (alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
            else if (alertDialog1 != null && alertDialog1.isShowing())
                alertDialog1.dismiss();

            else {
                Intent intent = new Intent(this, RegistrationFirstActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }


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

    }


    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }
}





