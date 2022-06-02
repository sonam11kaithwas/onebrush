package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityQuestionsBinding;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.CaseInformationActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class QuestionsActivity extends AppCompatActivity {
    private final boolean CHECKSTATE = false;
    RecyclerView recyclerView;
    List<AnswerLogDataModel> submitAnswerList = new ArrayList<>();
    TextView tv_name, tv_date, tv_status;
    private QuestionAdapter questionAdapter;
    private ActivityQuestionsBinding binding;
    private QuesViewModel quesViewModel;
    private UserAccountDetailsModel userAccountDetailsModel;
    private List<AnswerLogDataModel> allQuesAnswerList = new ArrayList<>();
    private AnswerLogDataModel answerLogDataModel;
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
                        Log.e("Test1", "onAuthenticationSucceeded");counterForBio=0;
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
        quesViewModel = ViewModelProviders.of(this)
                .get(QuesViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions);
        binding.setLifecycleOwner(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());


        createCounterTimer();

        submitAnswerList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAnswerListByCaseId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());
        AppUtility.updateScreeState("QuestionsActivity", userAccountDetailsModel);

        allQuesAnswerList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                getAllQuesAnswerListByCaseId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());

        if (allQuesAnswerList != null && allQuesAnswerList.size() > 1) {
            answerLogDataModel = allQuesAnswerList.get(allQuesAnswerList.size() - 1);
            binding.tvPatientName.setText(answerLogDataModel.getPatientNm() != null &&
                    !answerLogDataModel.getPatientNm().isEmpty() ? answerLogDataModel.getPatientNm() : userAccountDetailsModel.getName() + " " + userAccountDetailsModel.getSurname());
            binding.tvPatientRegis.setText(answerLogDataModel.getCreatedDate());
            if (answerLogDataModel.getCaseId() == 1) {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " st request");

            } else if (answerLogDataModel.getCaseId() == 2) {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " nd request");
            } else if (answerLogDataModel.getCaseId() == 3) {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " rd request");
            } else {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " th request");
            }
            binding.tvPatientStatus.setText(answerLogDataModel.getStatus());
            binding.tvPatientStatus.setTextColor(Color.parseColor("#ffa600"));

            binding.tvPatientDentist.setText(answerLogDataModel.getDentist() != null && !answerLogDataModel.getDentist().isEmpty() ? answerLogDataModel.getDentist() : "Dr. Dentina");

        }


        recyclerView = findViewById(R.id.recycler_view_ques);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        questionAdapter = new QuestionAdapter(this, submitAnswerList);
        recyclerView.setAdapter(questionAdapter);


        questionAdapter.notifyDataSetChanged();
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

    private void showErrorMsg(String msg, String titleHead) {
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(binding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            binding.layoutForView.setVisibility(View.VISIBLE);
            binding.pinViewLayout.setVisibility(View.GONE);
            binding.txtPinEntry.setText("");
            counterForBio=0;
            createCounterTimer();

        } else {
            binding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            binding.loginBtn.setVisibility(View.VISIBLE);

        }
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void onClickQus(View view) {
        switch (view.getId()) {
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.back_btn_qus:
                QuestionsActivity.this.onBackPressed();
                break;

            case R.id.iv_cross:
                this.finish();
                break;

            case R.id.btn_download:
                openDownloadDialog();
                break;
            case R.id.login_btn:OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;
        }
    }

    private void openDownloadDialog() {
        Dialog dialog = new Dialog(QuestionsActivity.this);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        dialog.show();

    }

    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            Intent i = new Intent(QuestionsActivity.this, CaseInformationActivity.class);
            startActivity(i);
            //Do whatever custom stuff you wish here
            super.onBackPressed();
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
}