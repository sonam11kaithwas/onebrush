package com.advantal.onebrush.registration_pkg.payment_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.RetrofitApiClient;
import com.advantal.onebrush.databinding.ActivityPaymentAcceptBinding;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.CaseSubmitModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.add_case_pkg.AddCaseReqModel;
import com.advantal.onebrush.registration_pkg.add_case_pkg.LogRecordsList;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;
import com.advantal.onebrush.registration_pkg.payment_pkg.payment_view_pkg.PaymentAcceptViewModel;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.File;
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

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PaymentAcceptActivity extends AppCompatActivity {
    AddCaseReqModel addCaseReqModel;
    private UserAccountDetailsModel userAccountDetailsModel;
    //    private RelativeLayout rl;
    private int caseId;
    private int forProfile = 0;
    private boolean LOADERLOADING = false;
    private MyCountDownTimer countDownTimer;
    //    private EditText txt_pin_entry;
//    private LinearLayout pin_view_layout;
//    private RelativeLayout layout_for_view, bio_layout;
//    private Button login_btn;
    private int counterForBio = 0;
    private PaymentAcceptViewModel paymentAcceptViewModel;
    private ActivityPaymentAcceptBinding binding;

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
//        setContentView(R.layout.activity_payment_accept);
        paymentAcceptViewModel = ViewModelProviders.of(this).get(PaymentAcceptViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_accept);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

        AppUtility.updateScreeState("PaymentAcceptActivity", userAccountDetailsModel);
        createCounterTimer();


//        Bundle bundlecaseid = getIntent().getExtras();
//        if (bundlecaseid != null) {
//            if (getIntent().hasExtra("forProfile")) {
//                forProfile = bundlecaseid.getInt("forProfile");
//            }
//        }
        forProfile = OneBrushAppPrefrence.getSharedprefInstance().getSelectedPatientId();

        caseId = OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId();
        initializeViews();
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

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void paymentClickListner(View view) {
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
                startPreviouscreen();

                break;
            case R.id.iv_cross:
                Log.e("", "test");
                this.finish();
                break;
            case R.id.next_state_button:
                if (AppUtility.isNetworkConnected()) {
                    AppUtility.showProgressBaForRelLay(binding.layoutForView, this);
                    LOADERLOADING = true;
//                    caseSubmit();

                    addCaseByUser(addCaseReqModel);
//
                } else {
                    showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));
                }
                break;
        }

    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());


        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file);
        return MultipartBody.Part.createFormData("xRayImages", file.getName(), requestFile);


    }

    private void caseSubmit() {
        try {
            List<AnswerLogDataModel> answerLogDataModelList =
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAnswerListByCaseId
                            (OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());

            ArrayList<LogRecordsList> logRecordsListArrayList = new ArrayList<>();
            MultipartBody.Part[] xRayImages = null;

            int index = 0;
            for (AnswerLogDataModel model : answerLogDataModelList) {
                index = 0;
                if (model.getTypeForAttchment() == 1) {
                    xRayImages = new MultipartBody.Part[model.getGalleryImageList().size()];
                    for (String str : model.getGalleryImageList()) {
                        Uri myUri = Uri.parse(str);
                        xRayImages[index] = (prepareFilePart("file", myUri));
                        index++;
                    }
                }

                logRecordsListArrayList.add(new LogRecordsList("1",
                        model.getCurrentPosition(), model.getNextPosition(), model.getPreviousPosition(),
                        model.getAnswer(), model.getQuestion(), "", model.getResultingCode(), model.getIsType()));
            }


            addCaseReqModel = new AddCaseReqModel(userAccountDetailsModel.getUuId(),
                    "Waiting for dentist answer", forProfile, ""
                    , "0", "", "0", logRecordsListArrayList
                    , xRayImages,
                    caseId + "", userAccountDetailsModel.getEmailAddress());


//            addCaseByUser(addCaseReqModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void addCaseByUser(AddCaseReqModel addCaseReqModel) {
        try {
            RequestBody uuId = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getUuId());
            RequestBody usrId = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getUuId());
            RequestBody status = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getStatus());
            RequestBody forProfile = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getForProfile() + "");
            RequestBody dentist = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getDentist());
            RequestBody perscriptionID = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getPerscriptionID());
            RequestBody paymentSystem = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getPaymentSystem());
            RequestBody paymentEvidence = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getPaymentEvidence());
            RequestBody recordCreationProfile = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getRecordCreationProfile());
            RequestBody caseId = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getCaseId());
            RequestBody recordCreationFunction = RequestBody.create(MultipartBody.FORM, addCaseReqModel.getRecordCreationFunction());


            RequestBody logRecordsList = null;
            if (addCaseReqModel.getLogRecordsList() != null) {
                Gson gson = new Gson();
                String s = gson.toJson(addCaseReqModel.getLogRecordsList());
                logRecordsList = RequestBody.create(MultipartBody.FORM, s);
            }


            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , "cases/add_case", "PaymentAcceptActivity", (new Gson().toJson(addCaseReqModel)));

            /*RetrofitApiClient.getservices().addCases(addCaseReqModel.getxRayImages()
                    , uuId, status, usrId, forProfile, dentist, perscriptionID, paymentSystem
                    , paymentEvidence, logRecordsList, caseId, recordCreationFunction, recordCreationProfile)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.e("", "");
                            AppUtility.hideProgressBar(PaymentAcceptActivity.this);
//                            if (response.get("responseCode").getAsString().equals("200")) {
//                            }
                            Toast.makeText(PaymentAcceptActivity.this, "Recode adde successfully", Toast.LENGTH_SHORT).show();
                            nextStateAfterCaseSubmit();

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            AppUtility.hideProgressBar(PaymentAcceptActivity.this);

                            Toasapit.makeText(PaymentAcceptActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });*/


            RetrofitApiClient.getservices().addCases(addCaseReqModel.getxRayImages()
                    , uuId, status, usrId, forProfile, dentist, perscriptionID, paymentSystem
                    , paymentEvidence, logRecordsList, caseId, recordCreationFunction, recordCreationProfile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppConstant.ERRORAPPREARMSG = "";
                            if (jsonObject.has("responseCode")) {
                                if (jsonObject.get("responseCode").getAsString().equals("200")) {

                                    try {
                                        if (jsonObject.has("message"))
                                            OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                                        String questDataStr = new Gson().toJson(jsonObject.get("data"));

                                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                                , "cases/add_case", "PaymentAcceptActivity",
                                                jsonObject.get("responseCode").getAsString() + "  " + questDataStr);


                                        CaseSubmitModel questionModelList = new Gson().fromJson(questDataStr, CaseSubmitModel.class);
                                        ArrayList<XRayImageListModl> urlList = new ArrayList<>();

                                        ArrayList<AnswerLogDataModel> answerLogDataModelList = new ArrayList<>();
                                        AnswerLogDataModel answerLogDataModel;
                                        int typeForAttchment = 0;
                                        for (LogRecordsList logmodel : questionModelList.getLogRecordsList()) {
                                            if (!urlList.isEmpty()) {
                                                urlList.clear();
                                            }

                                            answerLogDataModel = new AnswerLogDataModel(logmodel.getAnswer(),
                                                    logmodel.getQuestion(),
                                                    AppUtility.getCurrentDateTime("dd-MM-yyyy KK:mm:ss")
                                                    , "Request " + questionModelList.getCaseId(),

                                                    questionModelList.getStatus() != null && !logmodel.getStatus().isEmpty()
                                                            ? questionModelList.getStatus() : "Waiting for dentist answer",
                                                    logmodel.getCurrentPosition(), logmodel.getNextPosition()
                                                    , logmodel.getUserId(),
                                                    logmodel.getPreviousPosition(),
                                                    logmodel.getIsType().equals("CheckBox") ? 1 : 0, new ArrayList<>(), typeForAttchment,
                                                    1,
                                                    Integer.valueOf(questionModelList.getCaseId()), logmodel.getIsType(), logmodel.getResultingCode()

                                                    , questionModelList.getPatient() != null
                                                    && questionModelList.getPatient().getName() != null && !questionModelList.getPatient().getName().isEmpty()
                                                    && questionModelList.getPatient().getSurname() != null && !questionModelList.getPatient().getSurname()
                                                    .isEmpty()
                                                    ? questionModelList.getPatient().getName() + " " + questionModelList.getPatient().getSurname() : ""

                                            );
                                            answerLogDataModel.setStateForWindow("4");
                                            answerLogDataModel.setDentist(questionModelList.getDentist() != null
                                                    &&
                                                    !questionModelList.getDentist().isEmpty() ?
                                                    questionModelList.getDentist() : "Dr. Dentina");
                                            answerLogDataModelList.add(answerLogDataModel);


                                        }

                                        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                                                deleteAnswerByCaseId(questionModelList.getCaseId());

                                        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                                                insertAllAnswerSList(answerLogDataModelList);

                                        for (XRayImageListModl xRayImageListModl : questionModelList.getxRayImageList()) {
                                            typeForAttchment = 1;
                                            urlList.add(new XRayImageListModl(xRayImageListModl.getImgUrl(),
                                                    xRayImageListModl.getId(), questionModelList.getCaseId()));
                                        }

                                        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).xRayImageDao().
                                                insertAllXRaysList(urlList);


                                        nextStateAfterCaseSubmit();

                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                } else if (jsonObject.get("responseCode").getAsString().equals("409")) {
                                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                            , "cases/add_case", "PaymentAcceptActivity",
                                            jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());


                                    showErrorMsg(jsonObject.get("message").getAsString(), "");

                                } else if (jsonObject.get("responseCode").getAsString().equals("500")) {
                                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                            , "cases/add_case", "PaymentAcceptActivity",
                                            jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());


                                    showErrorMsg(jsonObject.get("message").getAsString(), "");

                                } else if (jsonObject.get("responseCode").getAsString().equals("403")) {
                                    showErrorMsg(jsonObject.get("message").getAsString(), "");

                                } else if (jsonObject.has("message")) {
                                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                            , "cases/add_case", "PaymentAcceptActivity",
                                            jsonObject.get("responseCode").getAsString() + "  " + jsonObject.get("message").getAsString());


                                    showErrorMsg(jsonObject.get("message").getAsString(), "");

                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                    , "cases/add_case", "PaymentAcceptActivity", e.getMessage());
                            AppUtility.hideProgressBar(PaymentAcceptActivity.this);
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                showErrorMsg(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                showErrorMsg(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                showErrorMsg(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                showErrorMsg(e.getMessage(), "");
                            }

                        }

                        @Override
                        public void onComplete() {
                            LOADERLOADING = false;
                            AppUtility.hideProgressBar(PaymentAcceptActivity.this);
                            Log.e("TAG onComplete------", "onComplete");
                        }
                    });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    private void nextStateAfterCaseSubmit() {
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().updateAnswerDataState(OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId(),
                "3");

//        int caseIdTemp = OneBrushAppPrefrence.getSharedprefInstance().getCaseId() + 1;
//        OneBrushAppPrefrence.getSharedprefInstance().setCaseId(caseIdTemp);
        Intent intent1 = new Intent(this, DonePaymentActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent1);
        finish();
    }

    private void startPreviouscreen() {
//        if (forProfile != 0) {
//            Intent intent1 = new Intent(this, PatientListActivity.class);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent1);
//            this.finish();
//        }

        Intent intent1 = new Intent(this, DentinosticDashboardActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent1);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE) {
            Log.e("", "");
            this.finish();
        } else if (!LOADERLOADING) {
            Intent intent1 = new Intent(this, DentinosticDashboardActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent1);
            finish();
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

    private void initializeViews() {
//        txt_pin_entry = findViewById(R.id.txt_pin_entry);
//        pin_view_layout = findViewById(R.id.pin_view_layout);
//        layout_for_view = findViewById(R.id.layout_for_view);
//        bio_layout = findViewById(R.id.bio_layout);
//        login_btn = findViewById(R.id.login_btn);
        caseSubmit();

    }

    private void showErrorMsg(String msg, String titleHead) {
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
//        AppUtility.isDisplaySnacker(rl, this,
//                msg);
    }

    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }
}