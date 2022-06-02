package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityDentinosticDashboardBinding;
import com.advantal.onebrush.denti_qus_pkg.Dentinostic_Qus_Ans_Activity;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.dentinostic_viewmodel_pkg.DentinosticViewModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.dentinostic_viewmodel_pkg.DentinosticViewModelFactory;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.CaseInformationActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.payment_pkg.ContinueWithPaymentActivity;
import com.advantal.onebrush.registration_pkg.payment_pkg.DonePaymentActivity;
import com.advantal.onebrush.registration_pkg.payment_pkg.PaymentAcceptActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.setting_pkg.SettingsActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class DentinosticDashboardActivity extends AppCompatActivity {

    private final ArrayList<DentinosticModel> contactsList = new ArrayList<>();
    private final ArrayList<Integer> caseIdList = new ArrayList<>();
    private DentinosticAdapter dentinosticAdapter;
    private DentinosticViewModel dentinosticViewModel;
    private ActivityDentinosticDashboardBinding dashboardBinding;
    private MyCountDownTimer countDownTimer;
    private UserAccountDetailsModel userAccountDetailsModel;
    private int counterForBio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        createCounterTimer();


        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dentinostic_dashboard);
        dentinosticViewModel = ViewModelProviders.of(this, new DentinosticViewModelFactory(this
                , new DentinosticModel())).get(DentinosticViewModel.class);


        dashboardBinding.setDentinosticModel(dentinosticViewModel);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark


        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("DentinosticDashboardActivity", userAccountDetailsModel);


        featchCaseDataList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dashboardBinding.recyclerView.setLayoutManager(layoutManager);


        dentinosticAdapter = new DentinosticAdapter(contactsList, this, new DentinosticAdapter.CaseInfDetails() {
            @Override
            public void setCaseInfoDetails(int caseId, String statusNm) {
                AnswerLogDataModel answerLogDataModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getcAnswerByCaseId(caseId);
                if (answerLogDataModel != null && answerLogDataModel.getAnsSubmitOrnot() == 0) {
                    Intent intent2 = new Intent(DentinosticDashboardActivity.this, Dentinostic_Qus_Ans_Activity.class);
                    intent2.putExtra("caseid", caseId);
                    OneBrushAppPrefrence.getSharedprefInstance().setSelectedCaseId(caseId);
                    startActivity(intent2);
                } else {
                    OneBrushAppPrefrence.getSharedprefInstance().setSelectedCaseId(caseId);
                    Intent intent;
                    if (userAccountDetailsModel.getRegistrationCom() != null
                            && !userAccountDetailsModel.getRegistrationCom().equals("")) {
                        if (answerLogDataModel != null && answerLogDataModel.getStateForWindow() != null) {
                            if (answerLogDataModel.getStateForWindow().equals("1")) {

                                intent = new Intent(DentinosticDashboardActivity.this, ContinueWithPaymentActivity.class);
                            } else if (answerLogDataModel.getStateForWindow().equals("2")) {
                                intent = new Intent(DentinosticDashboardActivity.this, PaymentAcceptActivity.class);
                            } else if (answerLogDataModel.getStateForWindow().equals("3")) {
                                intent = new Intent(DentinosticDashboardActivity.this, DonePaymentActivity.class);

                            } else if (answerLogDataModel.getStateForWindow().equals("4")) {
                                intent = new Intent(DentinosticDashboardActivity.this, CaseInformationActivity.class);

                            } else {
                                intent = new Intent(DentinosticDashboardActivity.this, Dentinostic_Qus_Ans_Activity.class);

                            }

                        } else {
                            intent = new Intent(DentinosticDashboardActivity.this, Dentinostic_Qus_Ans_Activity.class);
                        }
                        intent.putExtra("caseid", caseId);
                    } else {
                        intent = new Intent(DentinosticDashboardActivity.this, RegistrationFirstActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    }
                    startActivity(intent);

                }
                finish();

            }
        });

        dashboardBinding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Your body here";
                String shareSub = "Your subject here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        dashboardBinding.recyclerView.setAdapter(dentinosticAdapter);


        fetacCaseDataFromDB();


        dentinosticAdapter.notifyDataSetChanged();


        dentinosticViewModel.getErrorMsgBar().observeForever(new Observer<ErrorModel>() {
            @Override
            public void onChanged(ErrorModel s) {
                AppUtility.hideProgressBar(DentinosticDashboardActivity.this);

                if (!s.getError().isEmpty()) {
                    showErrorMsg(s.getError(), s.getErrorTitle());
                }


            }
        });


        dentinosticViewModel.getCaseListNotify().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(DentinosticDashboardActivity.this);
                fetacCaseDataFromDB();
                dentinosticAdapter.updateCaseDataList(contactsList);
            }
        });

        dentinosticViewModel.getIsUserVerifed().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(DentinosticDashboardActivity.this);

                Intent intent1 = new Intent(DentinosticDashboardActivity.this, SettingsActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
                finish();
            }
        });


        dashboardBinding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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


    private void fetacCaseDataFromDB() {
        List<AnswerLogDataModel> list = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAllQuesAnswerListBySelectedUsr(
                OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId()
        );
        int previousPos = 0;
        for (AnswerLogDataModel model : list) {
            Log.e("c", model.getCaseId() + "");
//            if (model.getCaseId() == previousPos) {
//            } else
            if (model.getCaseId() != previousPos) {
                previousPos = model.getCaseId();
                if (!caseIdList.contains(model.getCaseId())) {
                    caseIdList.add(model.getCaseId());
                    contactsList.add(new DentinosticModel(model.getRequestName(), model.getStatus(), model.getDate(),
                            model.getCaseId(), model.getAnsSubmitOrnot()));
                }

            }
        }
    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(dashboardBinding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            dashboardBinding.layoutForView.setVisibility(View.VISIBLE);
            createCounterTimer();
            dashboardBinding.pinViewLayout.setVisibility(View.GONE);
            dashboardBinding.bioLayout.setVisibility(View.GONE);
            dashboardBinding.txtPinEntry.setText("");
            counterForBio = 0;
            createCounterTimer();

        } else {
            dashboardBinding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            dashboardBinding.loginBtn.setVisibility(View.VISIBLE);

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


    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }


    public void onSubmitEvent(View view) {
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
            case R.id.btn_refresh:
                if (AppUtility.isNetworkConnected()) {
                    if (userAccountDetailsModel.getRegistrationCom() != null
                            && !userAccountDetailsModel.getRegistrationCom().equals(""))
                        AppUtility.showProgressBaForRelLay(dashboardBinding.layout, this);
                    featchCaseDataList();
                } else {
                    showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));
                }
                break;
            case R.id.btn_dashboard:
                if (AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().getAllQuesList().size() != 0) {
                    if (userAccountDetailsModel.getRegistrationCom() == null
                            || userAccountDetailsModel.getRegistrationCom().equals("")
                            && AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao()
                            .getAllQuesAnswerList().size() == 0) {
                        Intent intent = null;
                        OneBrushAppPrefrence.getSharedprefInstance().setCaseId(OneBrushAppPrefrence.getSharedprefInstance().getCaseId() + 1);
                        OneBrushAppPrefrence.getSharedprefInstance().setSelectedCaseId(OneBrushAppPrefrence.getSharedprefInstance().getCaseId());

                        intent = new Intent(DentinosticDashboardActivity.this, Dentinostic_Qus_Ans_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        this.finish();
                    } else if (!OneBrushAppPrefrence.getSharedprefInstance().getFirstRegistrationCompleted()) {
                        newCaseVerify();
                    } else {
                        Intent intent = null;
                        OneBrushAppPrefrence.getSharedprefInstance().setCaseId((OneBrushAppPrefrence.getSharedprefInstance().getCaseId() + 1));
                        OneBrushAppPrefrence.getSharedprefInstance().setSelectedCaseId(OneBrushAppPrefrence.getSharedprefInstance().getCaseId());

                        intent = new Intent(DentinosticDashboardActivity.this, Dentinostic_Qus_Ans_Activity.class);
                        intent.putExtra("caseid", (OneBrushAppPrefrence.getSharedprefInstance().getCaseId() ));

                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        this.finish();
                    }
                } else {
                    serverFeatchIssue();
                }
                break;
            case R.id.iv_settings:
                if (userAccountDetailsModel.getRegistrationCom() != null
                        && !userAccountDetailsModel.getRegistrationCom().equals("")) {

                    Intent intent1 = new Intent(DentinosticDashboardActivity.this, SettingsActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent1);
                    this.finish();

                } else {
                    showErrorMsg("Please Completed your Registration process first.", "");
                }
                break;
        }
    }


    private void featchCaseDataList() {
        if (userAccountDetailsModel.getRegistrationCom() != null
                && !userAccountDetailsModel.getRegistrationCom().equals("")) {
            dentinosticViewModel.getCaseDataFromServer(userAccountDetailsModel.getUuId());
        }

    }

    private void serverFeatchIssue() {
        if (!AppUtility.isNetworkConnected()) {
            showErrorMsg(OneBrushApp.getInstance().getResources().getString(R.string.bad_internet_connection), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

        } else if (!AppConstant.ERRORAPPREARMSG.isEmpty()) {

            AppUtility.alertDialog(this,
                    //OneBrushApp.getInstance().getResources().getString(R.string.web_adrs),
                    //OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error),
                    AppConstant.ERRORAPPREARMSG,
                    OneBrushApp.getInstance().getResources().getString(R.string.noInternet)
                    , new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return null;
                        }
                    });
        } else {
            if (AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().getAllQuesList().size() == 0) {
                showErrorMsg("Data not featch from server please reload.", "");

            }

        }

    }

    private void newCaseVerify() {
//        AppUtility.alertDialog(this,
//                //OneBrushApp.getInstance().getResources().getString(R.string.web_adrs),
//                //OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error),
//                AppConstant.ERRORAPPREARMSG,
//                OneBrushApp.getInstance().getResources().getString(R.string.noInternet)
//                , new Callable<Boolean>() {
//                    @Override
//                    public Boolean call() throws Exception {
//                        return null;
//                    }
//                });
        AppUtility.alertDialog(this, "Please Completed your Registration process first by selecting diagnostic request."
                , ""
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
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


    @Override
    public void onBackPressed() {
//        this.finish();

        AppUtility.alertDialogForTwoEvent(this, "Are you sure want to exit", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                DentinosticDashboardActivity.this.finish();
                return null;
            }
        }, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                finish();
                return null;
            }
        });
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Log.e("Test", " ---user interaction...");

        //Reset the timer on user interaction...
        if (countDownTimer != null)
            AppUtility.timeCounterReset(countDownTimer);
    }

    private void userIntreactionEvent() {
        if (userAccountDetailsModel.getIdMethod() != null &&
                (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
                        userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)) {
                counterForBio = 0;
                dashboardBinding.pinViewLayout.setVisibility(View.GONE);
                dashboardBinding.layoutForView.setVisibility(View.GONE);
                dashboardBinding.bioLayout.setVisibility(View.VISIBLE);
                onTouchIdClick();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
        }
    }

    private void pinViewStates() {
        dashboardBinding.txtPinEntry.requestFocus();
        dashboardBinding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                dashboardBinding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                dashboardBinding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                dashboardBinding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                dashboardBinding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        dashboardBinding.layoutForView.setVisibility(View.GONE);
        dashboardBinding.bioLayout.setVisibility(View.GONE);
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

    private void onTouchIdClick() {
        Log.e("MyTest", "onTouchIdClick");
        if (counterForBio <= 2 && isBiometricCompatibleDevice()) {
            displayBiometricButton();
        } else {
            pinViewStates();

        }

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
                        dashboardBinding.pinViewLayout.setVisibility(View.GONE);
                        dashboardBinding.layoutForView.setVisibility(View.VISIBLE);
                        dashboardBinding.bioLayout.setVisibility(View.GONE);
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

}