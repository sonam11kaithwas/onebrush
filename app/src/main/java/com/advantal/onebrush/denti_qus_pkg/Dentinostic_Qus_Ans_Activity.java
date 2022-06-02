package com.advantal.onebrush.denti_qus_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityDentinosticQusAnsBinding;
import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.ImageAdapter;
import com.advantal.onebrush.denti_qus_pkg.denti_viewmodel_pkg.DentiQusFactory;
import com.advantal.onebrush.denti_qus_pkg.denti_viewmodel_pkg.DentiQusViewModel;
import com.advantal.onebrush.denti_qus_pkg.frst_qus_pkg.FirstQuestIonActivity;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.MultiControlQueModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.PatientListActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.CompressImageInBack;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Dentinostic_Qus_Ans_Activity extends AppCompatActivity {
    private final List<QuestionModel> tempQuestionModelList = new ArrayList<>();
    private final ArrayList<QuestionModel> radioOptionsList = new ArrayList<>();
    private final StringBuilder tempSb = new StringBuilder();
    private final StringBuilder saveSbInDb = new StringBuilder();
    private final ArrayList<String> resultingCode = new ArrayList<>();
    private final ArrayList<String> tempResultingCode = new ArrayList<>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final ArrayList<String> galleryImageList = new ArrayList<>();

    int nextPosition = 0;
    ArrayList<Integer> positionList = new ArrayList<>();
    int progressCounter = 0;
    int positionListCount;
    int totalvalue;
    int ansSubmitOrnot = 0;
    String function;
    private boolean TYPECHECKBOX = false;//, LOADERLOADING = false;
    private ActivityDentinosticQusAnsBinding qusAnsBinding;
    private UserAccountDetailsModel userAccountDetailsModel;
    private List<QuestionModel> questionModelList = new ArrayList<>();
    private boolean test = false;
    private QuestionModel model;
    private DentiQusAdapter adapter;
    private int checkBoxcount = 0;
    private int checkBoxType = 0;
    private ImageAdapter imageAdapter;
    private String answer = "start";
    private int currentPosition, previousPosition;
    private int deletePreviousPosition, deletecaseid;
    private String tempAnswer = "", deleteTempAnswer = "";
    private int caseid;
    private boolean OLDDATASAVE = false, FORBEXTANSWER = false, LOADERSTATE = false;
    private List<AnswerLogDataModel> allQuesAnswerList = new ArrayList<>();
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
                        qusAnsBinding.pinViewLayout.setVisibility(View.GONE);
                        qusAnsBinding.layoutForView.setVisibility(View.VISIBLE);
                        qusAnsBinding.bioLayout.setVisibility(View.GONE);
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
        qusAnsBinding.txtPinEntry.requestFocus();
        qusAnsBinding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                qusAnsBinding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                qusAnsBinding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                qusAnsBinding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                qusAnsBinding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        qusAnsBinding.layoutForView.setVisibility(View.GONE);
        qusAnsBinding.bioLayout.setVisibility(View.GONE);
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
                qusAnsBinding.pinViewLayout.setVisibility(View.GONE);
                qusAnsBinding.layoutForView.setVisibility(View.GONE);
                qusAnsBinding.bioLayout.setVisibility(View.VISIBLE);
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
                if (!LOADERSTATE)
                    userIntreactionEvent();
            }
        });
        AppUtility.timeCounterReset(countDownTimer);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        qusAnsBinding = DataBindingUtil.setContentView(this, R.layout.activity_dentinostic_qus_ans);
        DentiQusViewModel dentiQusViewModel = ViewModelProviders.of(this, new DentiQusFactory(this
                , new DentiQusModel("", "", "", "", ""))).get(DentiQusViewModel.class);

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        createCounterTimer();


        AppUtility.updateScreeState("Dentinostic_Qus_Ans_Activity", userAccountDetailsModel);

        initalizaViews();


        int pIndex = 0;
        for (QuestionModel questionModel : AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().getAllQuesList()) {
            if (pIndex != questionModel.getPosition())
                positionList.add(questionModel.getPosition());
            pIndex = questionModel.getPosition();
        }

        positionListCount = positionList.size();
        totalvalue = positionListCount / 15;//. == 5

        Bundle bundlecaseid = getIntent().getExtras();
        if (bundlecaseid != null) {
            if (getIntent().hasExtra("caseid")) {
                caseid = bundlecaseid.getInt("caseid");
            }
            if (getIntent().hasExtra("FORBEXTANSWER")) {
                FORBEXTANSWER = true;
            }
        } else {
            caseid = OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId();
        }

        allQuesAnswerList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                getAllQuesAnswerListByCaseId(caseid);

        if (allQuesAnswerList != null && allQuesAnswerList.size() > 1) {
            OLDDATASAVE = true;
            AnswerLogDataModel logDataModel = null;
            if (FORBEXTANSWER) {
                logDataModel = allQuesAnswerList.get(1);
            } else {
                logDataModel = allQuesAnswerList.get(allQuesAnswerList.size() - 1);
            }

            nextPosition = logDataModel.getCurrentPosition();
            previousPosition = logDataModel.getPreviousPosition();
            tempAnswer = "";
            tempAnswer = logDataModel.getAnswer();
            if (logDataModel != null && logDataModel.getGalleryImageList() != null && logDataModel.getGalleryImageList().size() > 0) {
                galleryImageList.addAll(logDataModel.getGalleryImageList());
            } else {
                galleryImageList.clear();
            }
            tempSb.setLength(0);

            tempSb.append(tempAnswer);
        }

        if (!OLDDATASAVE) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (getIntent().hasExtra("position")) {
                    nextPosition = bundle.getInt("position");
                    test = true;
                    answer = "start";
                }
            }

            questionModelList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().getAllQuesList();


            if (test) {
                qusAnsBinding.tvQus.setText(questionModelList.get(0).getQuestion());
                currentPosition = questionModelList.get(0).getPosition();
                function = questionModelList.get(0).getFunction();
                this.model = questionModelList.get(0);
                addAnswers();
                tempQuestionModelList.add(questionModelList.get(0));
                questionModelList.remove(0);
            }


            if (questionModelList != null && questionModelList.size() > 0) {
                if (!test) {
                    questionModelList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().
                            getAllQuesListByPos(questionModelList.get(0).getPosition());
                    for (QuestionModel model : questionModelList) {
                        if (model.getNextPosition() != null && model.getNextPosition() != 0) {
                            nextPosition = model.getNextPosition();
                            currentPosition = model.getPosition();
                            this.model = model;
                            break;
                        }
                    }
                    Intent intent = new Intent(this, FirstQuestIonActivity.class);
                    intent.putExtra("position", nextPosition);
                    intent.putExtra("caseid", caseid);
                    intent.putExtra("currentPos", currentPosition);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(intent);
                    this.finish();

//                    qusAnsBinding.ibUpload.setVisibility(View.VISIBLE);
//                    getImageFromGallery();
                } else {
                    feactNextPosData();
                }
            }
        } else {
            feactNextPosData();
        }
        qusAnsBinding.progressBarSec2.setProgress(1);

        qusAnsBinding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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


        qusAnsBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                answer = i + "";
                switch (i) {
                    case 1:
                        qusAnsBinding.ivLightBlue.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivDarkBlue.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivLightPurple.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkPurple.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        qusAnsBinding.ivLightPurple.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkPurple.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivLightBlue.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkBlue.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivLightSky.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkSky.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        qusAnsBinding.ivLightSky.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivDarkSky.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivLightPurple.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkPurple.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivSky.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivSky1.setVisibility(View.INVISIBLE);


                        break;

                    case 4:
                        qusAnsBinding.ivSky.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivSky1.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivLightSky.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkSky.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivPista.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkPista.setVisibility(View.INVISIBLE);

                        break;
                    case 5:
                        qusAnsBinding.ivPista.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivDarkPista.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivSky.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivSky1.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivLightGrey.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkGrey.setVisibility(View.INVISIBLE);


                        break;
                    case 6:
                        qusAnsBinding.ivLightGrey.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivDarkGrey.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivPista.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkPista.setVisibility(View.INVISIBLE);
                        qusAnsBinding.lightPink.setVisibility(View.VISIBLE);
                        qusAnsBinding.darkPink.setVisibility(View.INVISIBLE);

                        break;

                    case 7:
                        qusAnsBinding.lightPink.setVisibility(View.INVISIBLE);
                        qusAnsBinding.darkPink.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivLightGrey.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivDarkGrey.setVisibility(View.INVISIBLE);
                        qusAnsBinding.lightRed.setVisibility(View.VISIBLE);
                        qusAnsBinding.darkRed.setVisibility(View.INVISIBLE);

                        break;

                    case 8:
                        qusAnsBinding.lightRed.setVisibility(View.INVISIBLE);
                        qusAnsBinding.darkRed.setVisibility(View.VISIBLE);
                        qusAnsBinding.lightPink.setVisibility(View.VISIBLE);
                        qusAnsBinding.darkPink.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivRed.setVisibility(View.VISIBLE);
                        qusAnsBinding.ivRed1.setVisibility(View.INVISIBLE);
                        break;
                    case 9:
                        qusAnsBinding.ivRed.setVisibility(View.INVISIBLE);
                        qusAnsBinding.ivRed1.setVisibility(View.VISIBLE);
                        qusAnsBinding.lightRed.setVisibility(View.VISIBLE);
                        qusAnsBinding.darkRed.setVisibility(View.INVISIBLE);

                        break;


                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getTempAnswer() {
        return tempAnswer;
    }

    public void setTempAnswer(String tempAnswer) {
        this.tempAnswer = tempAnswer;
    }

    public String getDeleteTempAnswer() {
        return deleteTempAnswer;
    }

    public void setDeleteTempAnswer(String deleteTempAnswer) {
        this.deleteTempAnswer = deleteTempAnswer;
    }

    private void initalizaViews() {
        qusAnsBinding.radioRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        qusAnsBinding.radioRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new DentiQusAdapter(radioOptionsList, this, new DentiQusAdapter.GetNextPosition() {
            @Override
            public void setNextpostion(int nextpostion, String ans) {
                nextPosition = nextpostion;
                answer = ans;
            }
        });

        qusAnsBinding.radioRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        qusAnsBinding.radioRecyclerView.setItemAnimator(new DefaultItemAnimator());

        qusAnsBinding.radioRecyclerView.setAdapter(adapter);


        imageAdapter = new ImageAdapter(galleryImageList, new ImageAdapter.RemoveDataForGallery() {
            @Override
            public void setGalaryRmvPos(int pos, String url) {
                try {
                    if (galleryImageList.size() >= pos)
                        galleryImageList.remove(pos);
                    /***********VERY IMPORTANT**********/
//                    removeFromList(url);
                    imageAdapter.updateGalleryDataList(galleryImageList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        qusAnsBinding.galleryrecyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        qusAnsBinding.galleryrecyclerview.setAdapter(imageAdapter);

    }

    private void feactNextPosData() {
        clearExitsDataAndViews();

        questionModelList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().getAllQuesListByPos(nextPosition);
        nextPosition = 0;
        for (QuestionModel model : questionModelList) {
            if (model.getQuestion() != null && !model.getQuestion().isEmpty()) {
                resultingCode.clear();
                qusAnsBinding.tvQus.setText(questionModelList.get(0).getQuestion());
                currentPosition = model.getPosition();
                this.model = model;
            } else if (model.getFunction() != null && !model.getFunction().isEmpty()) {
//                this.model = model;
                qusAnsBinding.nextStateButton.setText(getResources().getString(R.string.next));
                openViewForStates(model);
            }
        }
    }

    private void clearExitsDataAndViews() {
        if (adapter.getItemCount() > 0) {
            adapter.clearList();
        }

        if (qusAnsBinding.linearForButton.getChildCount() > 0) {
            qusAnsBinding.linearForButton.removeAllViews();
        }

        if (qusAnsBinding.linearCheck.getChildCount() > 0) {
            qusAnsBinding.linearCheck.removeAllViews();
        }
        qusAnsBinding.typeInput.setText("");

        qusAnsBinding.linearCheck.setVisibility(View.GONE);
        qusAnsBinding.typeInputLable.setVisibility(View.GONE);
        qusAnsBinding.radioRecyclerView.setVisibility(View.GONE);
        qusAnsBinding.textType.setVisibility(View.GONE);
        qusAnsBinding.textType.setText("");
        qusAnsBinding.pictureType.setVisibility(View.GONE);
        qusAnsBinding.galleryrecyclerview.setVisibility(View.GONE);
        qusAnsBinding.ibUpload.setVisibility(View.GONE);
        qusAnsBinding.linearForButton.setVisibility(View.GONE);

        qusAnsBinding.nextStateButton.setVisibility(View.VISIBLE);


        galleryImageList.clear();

        AppUtility.setupUI(qusAnsBinding.layout, this);


//        TYPECHECKBOX = false;
        //        resultingCode.clear();

        /************For DB*************/
//        answer = "";
    }


    private void openViewForStates(QuestionModel questionModel) {
        qusAnsBinding.layoutGraph.setVisibility(View.GONE);
        qusAnsBinding.layoutQusAns.setVisibility(View.VISIBLE);
        checkBoxType = 0;
        switch (questionModel.getFunction()) {
            case "Graph":
                this.function = questionModel.getFunction();
                model.getFunction();
                this.model = questionModel;
                loadGraph();
                break;
            case "Picture":
                this.function = questionModel.getFunction();
                this.model = questionModel;
                loadImage();
                break;
            case "RadioButton":
                this.function = questionModel.getFunction();
                this.model = questionModel;

                setRadioButtons();
                break;
            case "Button":
                this.model = questionModel;

                qusAnsBinding.nextStateButton.setVisibility(View.GONE);
                creatDynamicButton(questionModel);


                break;
            case "CheckBox":
                this.function = questionModel.getFunction();
                this.model = questionModel;

                setCheckBoxOption();
                break;
            case "InputField":
                this.function = questionModel.getFunction();
                this.model = questionModel;

                userInputForQue();
                break;
            case "Fileload":
                this.function = questionModel.getFunction();
                this.model = questionModel;
                qusAnsBinding.ibUpload.setVisibility(View.VISIBLE);
                takeImageFromGalleryandRemove();
                break;
            case "Text":
                this.function = questionModel.getFunction();
                this.model = questionModel;

                setLable();
                break;
            case "PainLocatorAttribute":
                this.function = questionModel.getFunction();
                this.model = questionModel;

                painLocatorLoad();
                break;
            case "ShowLastImage":
                this.function = questionModel.getFunction();
                Log.e("", "");
                this.model = questionModel;

                loadImage();
                break;
            case "PainLocator":
                this.function = questionModel.getFunction();
                this.model = questionModel;

                nextPosition = model.getNextPosition();
                break;
        }
    }

    private void creatDynamicButton(QuestionModel questionModel) {
        qusAnsBinding.linearForButton.setVisibility(View.VISIBLE);
        Button btn = new Button(this);
        btn.setText(questionModel.getAnswerData());
        btn.setBackgroundResource(R.drawable.next_button_shape);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.steagalmedium);
        btn.setTypeface(typeface);
        btn.setAllCaps(false);
        try {
//            btn.setEnabled(false);
            btn.setSaveEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        btn.seten(null);
        btn.setTag(questionModel);
        btn.setTextColor(Color.parseColor("#ffffff"));
        btn.setTypeface(btn.getTypeface(), Typeface.NORMAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);

        btn.setTextSize((int) getResources().getDimension(R.dimen._5sdp));

        btn.setLayoutParams(params);
        qusAnsBinding.linearForButton.addView(btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionModel selectedOptionModel = ((QuestionModel) view.getTag());
                nextPosition = selectedOptionModel.getNextPosition();
                currentPosition = selectedOptionModel.getPosition();
                function = selectedOptionModel.getFunction();
                answer = selectedOptionModel.getAnswerData();

                boolean NOTFOUND = addAnswers();


                if (NOTFOUND) {
                    updateProgressBar();

                    if (nextPosition != 0) {
//                        if (!TYPECHECKBOX) {
                        if (!function.equalsIgnoreCase("CheckBox")) {
                            if (nextPosition != 0) {
                                feactNextPosData();
                            } else if (model.getFunction().equals("Button") && model.getAnswerData().equals("End")) {
                                qusAnsBinding.nextStateButton.setText(model.getFunction());
                                Intent intent;
                                if (userAccountDetailsModel.getRegistrationCom() != null && !userAccountDetailsModel.getRegistrationCom().equals("")) {
                                    startNewActionForUser();
                                } else {
                                    intent = new Intent(Dentinostic_Qus_Ans_Activity.this, RegistrationFirstActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        } else {
                            featchDataFromMultiQues();
                        }
                    } else {
//                        if (!TYPECHECKBOX) {
                        if (!function.equalsIgnoreCase("CheckBox")) {
                            if (nextPosition != 0) {
                                feactNextPosData();
                            } else if (model.getFunction().equals("Button") && model.getAnswerData().equals("End")) {
                                Intent intent;
                                if (userAccountDetailsModel.getRegistrationCom() != null && !userAccountDetailsModel.getRegistrationCom().equals("")) {
                                    startNewActionForUser();
                                } else {
                                    intent = new Intent(Dentinostic_Qus_Ans_Activity.this, RegistrationFirstActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if (nextPosition == 0) {
                                showErrorMsg("Please fill answer", "");
                            }
                        } else {
                            featchDataFromMultiQues();
                        }
                    }

                }

//                feactNextPosData();

            }
        });
    }

    private void loadGraph() {
        nextPosition = model.getNextPosition();
        qusAnsBinding.layoutGraph.setVisibility(View.VISIBLE);
        qusAnsBinding.layoutQusAns.setVisibility(View.GONE);
        qusAnsBinding.tvHeaderGraph.setText(qusAnsBinding.tvQus.getText().toString().trim());
        nextPosition = model.getNextPosition();
    }

    private void takeImageFromGalleryandRemove() {
        /***********VERY IMPORTANT**********/

        if (imageAdapter != null && galleryImageList.size() != 0) {
            imageAdapter.updateGalleryDataList(galleryImageList);
        } else if (galleryImageList != null && galleryImageList.size() != 0) {
            imageAdapter.updateGalleryDataList(galleryImageList);
        } else {
            AnswerLogDataModel answerLogDataModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                    getExitAnswer(model.getPosition(), caseid);
            if (answerLogDataModel != null && answerLogDataModel.getGalleryImageList() != null && answerLogDataModel.getGalleryImageList().size() != 0)
                galleryImageList.addAll(answerLogDataModel.getGalleryImageList());
//            setUriArrayList(galleryImageList);
            imageAdapter.updateGalleryDataList(galleryImageList);
        }
        nextPosition = model.getNextPosition();
        qusAnsBinding.galleryrecyclerview.setVisibility(View.VISIBLE);
        qusAnsBinding.ibUpload.setVisibility(View.VISIBLE);
    }


    private void painLocatorLoad() {
        qusAnsBinding.layoutGraph.setVisibility(View.VISIBLE);
        qusAnsBinding.layoutQusAns.setVisibility(View.GONE);
        qusAnsBinding.tvHeaderGraph.setText(qusAnsBinding.tvQus.getText().toString().trim());
        nextPosition = model.getNextPosition();
    }


    private void loadImage() {
        qusAnsBinding.pictureType.setVisibility(View.VISIBLE);
    }

    private void updateProgressBar() {
        int indexofarr = progressCounter;
        int pcounter = qusAnsBinding.progressBarSec2.getProgress() + 5;
        {
            if (indexofarr <= 1 * totalvalue) {
                qusAnsBinding.progressBarSec2.setProgress(pcounter);
            } else if (indexofarr <= 2 * totalvalue) {
                qusAnsBinding.progressBarSec2.setProgress(pcounter);
            } else if (indexofarr <= 3 * totalvalue) {
                qusAnsBinding.progressBarSec2.setProgress(pcounter);
            } else if (indexofarr <= 4 * totalvalue) {
                qusAnsBinding.progressBarSec2.setProgress(pcounter);
            } else if (indexofarr <= 5 * totalvalue) {
                qusAnsBinding.progressBarSec2.setProgress(pcounter);
            } else if (indexofarr <= 6 * totalvalue) {//
                qusAnsBinding.progressBarSec2.setProgress(pcounter);
            } else if (indexofarr <= 7 * totalvalue) {//
                qusAnsBinding.progressBarSec2.setProgress(pcounter);

            } else if (indexofarr <= 8 * totalvalue) {//
                qusAnsBinding.progressBarSec2.setProgress(pcounter);

            } else if (indexofarr <= 9 * totalvalue) {//
                qusAnsBinding.progressBarSec2.setProgress(pcounter);

            } else if (indexofarr <= 10 * totalvalue) {//
                qusAnsBinding.progressBarSec2.setProgress(pcounter);

            } else if (indexofarr <= 11 * totalvalue) {
                qusAnsBinding.progressBarSec2.setProgress(pcounter);

            } else {
                qusAnsBinding.progressBarSec2.setProgressDrawable(ContextCompat.getDrawable(Dentinostic_Qus_Ans_Activity.this,
                        R.drawable.progress_colo_bg));
            }
            progressCounter++;
        }

    }


    private void takeDocument() {
    }

    private void setLable() {
        if (qusAnsBinding.textType.getVisibility() == View.GONE)
            qusAnsBinding.textType.setVisibility(View.VISIBLE);


        qusAnsBinding.textType.setText(model.getAnswerData());

        nextPosition = model.getNextPosition();
    }

    private void setRadioButtons() {
        if (qusAnsBinding.radioRecyclerView.getVisibility() == View.GONE)
            qusAnsBinding.radioRecyclerView.setVisibility(View.VISIBLE);


        radioOptionsList.add(model);
        adapter.setQuestIonList(radioOptionsList);
    }


    private void userInputForQue() {
        if (qusAnsBinding.typeInputLable.getVisibility() == View.GONE)
            qusAnsBinding.typeInputLable.setVisibility(View.VISIBLE);

        AnswerLogDataModel answerLogDataModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAnswerDataBy(model.getPosition(), caseid);
        if (answerLogDataModel != null && answerLogDataModel.getAnswer() != null) {
            qusAnsBinding.typeInput.setText(answerLogDataModel.getAnswer());
        }

        answer = "";
        qusAnsBinding.typeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                answer = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nextPosition = model.getNextPosition();
    }

    private void startNewActionForUser() {
        Intent intent = new Intent(this, PatientListActivity.class);
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

    public void stateClikLstner(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.cross_img_for_pin:
                this.finish();
                break;
            case R.id.back_button:

                onBackPressed();
                break;
            case R.id.iv_cross:
                openMyExitDialog();
                break;
            case R.id.ib_upload:
//                if (galleryImageList.size() == 10) {
//                    imageDialogFOrLarge();
//                } else {

                requestPermission();
//                }

//                openPhoneGallery();
                break;
            case R.id.next_state_button:

                String ATTCHMENANS = "Please fill answer";
                boolean inputAns = false;
                if (function.equalsIgnoreCase("InputField")) {
                    if (qusAnsBinding.typeInput.getText().toString().trim().isEmpty()) {
                        inputAns = true;
                        ATTCHMENANS = "Please fill answer";
                    } else {
                        answer = qusAnsBinding.typeInput.getText().toString().trim();
                        inputAns = false;
                    }
                } else if (function.equalsIgnoreCase("Fileload")) {
                    inputAns = galleryImageList.size() <= 0;
                    ATTCHMENANS = "Please upload X-Ray photos";
                }


                if (!inputAns) {
                    boolean NOTFOUND = addAnswers();

                    if (NOTFOUND) {
                        updateProgressBar();

                        if (nextPosition != 0) {
//                        if (!TYPECHECKBOX) {
                            if (!function.equalsIgnoreCase("CheckBox")) {
                                if (nextPosition != 0) {
                                    feactNextPosData();
                                } else if (model.getFunction().equals("Button") && model.getAnswerData().equals("End")) {
                                    qusAnsBinding.nextStateButton.setText(model.getFunction());
                                    Intent intent;
                                    if (userAccountDetailsModel.getRegistrationCom() != null && !userAccountDetailsModel.getRegistrationCom().equals("")) {
                                        startNewActionForUser();
                                    } else {
                                        intent = new Intent(this, RegistrationFirstActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            } else {
                                featchDataFromMultiQues();
                            }
                        } else {
//                        if (!TYPECHECKBOX) {
                            if (!function.equalsIgnoreCase("CheckBox")) {
                                if (nextPosition != 0) {
                                    feactNextPosData();
                                } else if (model.getFunction().equals("Button") && model.getAnswerData().equals("End")) {
                                    Intent intent;
                                    if (userAccountDetailsModel.getRegistrationCom() != null && !userAccountDetailsModel.getRegistrationCom().equals("")) {
                                        startNewActionForUser();
                                    } else {
                                        intent = new Intent(this, RegistrationFirstActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else if (nextPosition == 0) {
                                    showErrorMsg("Please fill answer", "");
                                }
                            } else {
                                featchDataFromMultiQues();
                            }
                        }

                    }

                } else {
                    showErrorMsg(ATTCHMENANS, "");

                }
                break;
        }
    }

    private void imageDialogFOrLarge() {
        try {

            TextView dialog_msg;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();// (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
            alertDialog.setView(customLayout);
            alertDialog.setCancelable(false);

            dialog_msg = customLayout.findViewById(R.id.dia_msg);

            dialog_msg.setText("you have upload only 10 images.");

            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            alertDialog.show();
        } catch (Exception e) {


            e.printStackTrace();
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


    @Override
    public void onBackPressed() {

        if (qusAnsBinding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else if (!LOADERSTATE) {
            updateProgressBarMinus();
            AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().updateSubmitAnswer(caseid, 0);


            try {
                if (previousPosition != 10) {
                    AnswerLogDataModel answerLogDataModel = null;

                    if (previousPosition != nextPosition)//80 !=80
                    {

                        answerLogDataModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao()
                                .getAnswerDataBy(previousPosition, caseid);
                    } else {
                        answerLogDataModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao()
                                .getAnswer(previousPosition, caseid);
                    }
                    deletePreviousPosition = currentPosition;

                    deleteTempAnswer = answer;
                    List<String> myList = new ArrayList<String>(Arrays.asList(answerLogDataModel.getResultingCode()));

                    tempResultingCode.addAll(myList);


                    questionModelList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().
                            getAllQuesListByPos(answerLogDataModel.getCurrentPosition());


                    clearExitsDataAndViews();
                    previousPosition = answerLogDataModel.getPreviousPosition();
                    nextPosition = answerLogDataModel.getNextPosition();
                    tempAnswer = "";
                    tempAnswer = answerLogDataModel.getAnswer();
                    tempSb.setLength(0);
                    tempSb.append(tempAnswer);

                    checkBoxcount = 0;


                    for (QuestionModel model : questionModelList) {
                        if (model.getQuestion() != null && !model.getQuestion().isEmpty()) {
                            qusAnsBinding.tvQus.setText(questionModelList.get(0).getQuestion());

                            currentPosition = model.getPosition();
                            this.model = model;
                            resultingCode.clear();

                        } else if (model.getFunction() != null && !model.getFunction().isEmpty()) {
                            qusAnsBinding.nextStateButton.setText(getResources().getString(R.string.next));
                            openViewForStates(model);
                        }
                    }

                } else {
                    Intent intent = new Intent(this, FirstQuestIonActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("caseid", caseid);
                    intent.putExtra("currentPos", 10);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(intent);
                    this.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean addAnswers() {
        int typeForAttchment = 0;
        MultiControlQueModel list;
        boolean NOTFOUND = true;
        StringBuilder sb = new StringBuilder();


        //        if (TYPECHECKBOX) {
        if (function.equalsIgnoreCase("CheckBox")) {
//            answer = tempSb.toString();
            answer = saveSbInDb.toString();
            try {
                while (resultingCode.size() != 10 && resultingCode.size() <= 10) {
                    resultingCode.add("0");
                }
                for (String u : resultingCode) {
                    sb.append(u);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            list = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao()
                    .getAllMulQuesListByResultingCode(
                            nextPosition,
                            sb.toString());


            if (list == null) {
                list = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().getAllMulQuesListByResultingCode(
                        nextPosition
                        , "0000000000");
            }

            if (list != null) {
                NOTFOUND = true;
                nextPosition = list.getNextPosition();
            } else {
                NOTFOUND = false;
            }


        } else {
            NOTFOUND = true;
            list = new MultiControlQueModel();
        }

        if (list != null) {
            if (galleryImageList.size() > 0) {
                typeForAttchment = 1;
            }

            if (model.getFunction().equalsIgnoreCase("Button") && model.getAnswerData().equalsIgnoreCase("End")) {
                ansSubmitOrnot = 1;
                AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().updateSubmitAnswer(caseid, ansSubmitOrnot);
            } else {
                ansSubmitOrnot = 0;
            }


            AnswerLogDataModel answerLogDataModel = new AnswerLogDataModel(answer, qusAnsBinding.tvQus.getText().toString().trim(),
                    AppUtility.getCurrentDateTime("dd-MM-yyyy KK:mm:ss"), "Request " +
                    caseid + "", "Pending",
                    currentPosition, nextPosition, userAccountDetailsModel.getUuId()
                    , previousPosition, checkBoxType, galleryImageList, typeForAttchment, ansSubmitOrnot, caseid
                    , function,
                    sb.toString()
                    , "");


            answerLogDataModel.setAnserComplete(AppUtility.getCurrentDateTime("dd.MM.yyyy"));

            AnswerLogDataModel exitAnswerModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                    getExitAnswer(currentPosition, caseid);


            if (previousPosition != nextPosition) {
                if (exitAnswerModel != null) {
                    answerLogDataModel.setId(exitAnswerModel.getId());
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().updateAnswer(answerLogDataModel);

                } else {
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().insertAnswersData(answerLogDataModel);
                }
            }


            AnswerLogDataModel deleteExitAns = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAnswerDataBy(nextPosition, caseid);
            if (deleteExitAns == null) {
                AnswerLogDataModel getAnswer = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAnswer(nextPosition, caseid);

                if (getAnswer != null)
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().removeByCur(getAnswer.getId(), caseid);
            }


            AnswerLogDataModel forAnswer = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().getAnswerDataBy
                    (nextPosition, caseid);

            if (forAnswer != null && forAnswer.getAnswer() != null) {
                tempAnswer = "";
                tempAnswer = forAnswer.getAnswer();
                tempSb.setLength(0);
                tempSb.append(tempAnswer);
            }


            previousPosition = model.getPosition();
            saveSbInDb.setLength(0);
            saveSbInDb.toString();
        } else {
            showErrorMsg("Invalid selection", "");
        }

        return NOTFOUND;
    }


    private void featchDataFromMultiQues() {
        try {
            while (resultingCode.size() != 10) {
                resultingCode.add("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (String u : resultingCode) {
            sb.append(u);
        }
        MultiControlQueModel list = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao()
                .getAllMulQuesListByResultingCode(
                        nextPosition,
                        sb.toString());

        if (list == null) {
            list = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().getAllMulQuesListByResultingCode(
                    nextPosition
                    , "0000000000");
        }
        if (list != null) {
            nextPosition = list.getNextPosition();
            resultingCode.clear();
            TYPECHECKBOX = false;
            checkBoxcount = 0;
        } else {
            resultingCode.clear();
            checkBoxcount = 0;

            questionModelList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().getAllQuesListByNxtPos(nextPosition);
        }
        feactNextPosData();
    }


    private void setCheckBoxOption() {
        if (qusAnsBinding.linearCheck.getVisibility() == View.GONE)
            qusAnsBinding.linearCheck.setVisibility(View.VISIBLE);

        final CheckBox checkBox = new CheckBox(this);
        checkBox.setText(model.getAnswerData());
        checkBox.setTag(checkBoxcount);
        resultingCode.add("0");
        TYPECHECKBOX = true;
        checkBoxType = 1;
        TextViewCompat.setTextAppearance(checkBox, R.style.CustomCheckbox02);
        checkBox.setTypeface(ResourcesCompat.getFont(this, R.font.steagalmedium));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        checkBox.setCompoundDrawablePadding(5);

        final float scale = this.getResources().getDisplayMetrics().density;
        checkBox.setPadding(checkBox.getPaddingLeft() + (int) (5.0f * scale + 0.5f),
                checkBox.getPaddingTop(),
                checkBox.getPaddingRight(),
                checkBox.getPaddingBottom());


        params.leftMargin = (int) getResources().getDimension(R.dimen._8sdp);
        params.topMargin = (int) getResources().getDimension(R.dimen._8sdp)
                + (int) (5.0f * scale + 0.5f);
        params.rightMargin = (int) getResources().getDimension(R.dimen._8sdp);
        params.bottomMargin = (int) getResources().getDimension(R.dimen._8sdp)
                + (int) (5.0f * scale + 0.5f);
        checkBox.setLayoutParams(params);

        params.gravity = Gravity.CENTER;

        CardView cardview = addCardView();
        cardview.addView(checkBox);


        qusAnsBinding.linearCheck.addView(cardview);


        String[] lines = tempSb.toString().split(",");
        for (String s : lines) {
            if (checkBox.getText().toString().equalsIgnoreCase(s.trim())) {
                checkBox.setChecked(true);
                nextPosition = model.getNextPosition();
                saveSbInDb.append(checkBox.getText().toString());
                saveSbInDb.append(",");
                resultingCode.set(checkBoxcount, "1");
                break;
            }
        }

        checkBoxcount++;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                if (compoundButton.isChecked()) {
                    resultingCode.set(pos, "1");
                    nextPosition = model.getNextPosition();
                    answer = checkBox.getText().toString();
                    if (tempSb.length() > 0) {
                        tempSb.append(",");
                    }

                    saveSbInDb.append(answer);
                    saveSbInDb.append(",");

                    tempSb.append(answer);

                } else {
//                    nextPosition = 0;
                    resultingCode.set(pos, "0");
                    answer = "";
                    int i = tempSb.indexOf(checkBox.getText().toString());
                    if (i != -1) {
                        tempSb.delete(i, i + checkBox.getText().toString().length());
                    }
                    int j = saveSbInDb.indexOf(checkBox.getText().toString());
                    if (j != -1) {
                        saveSbInDb.delete(j, j + checkBox.getText().toString().length());
                    }
                }

            }
        });


    }


    private LinearLayout.LayoutParams addLayoutMarging() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int radiuslrtp = (int) getResources().getDimension(R.dimen._5sdp);

        params.setMargins(radiuslrtp, radiuslrtp, radiuslrtp, radiuslrtp);
        return params;
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
    protected void onStop() {
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onDestroy();
    }

    private CardView addCardView() {
        CardView cardview = new CardView(this);
        ViewGroup.LayoutParams layoutparams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        cardview.setLayoutParams(layoutparams);
        cardview.setCardElevation(1f);
        cardview.setElevation(1);
//        cardview.setBackgroundDrawable(R.drawable.card_view_brdr);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius((int) getResources().getDimension(R.dimen._10sdp));
        gd.setColor(Color.WHITE);

        cardview.setBackground(gd);

        int radiuslrtp = (int) getResources().getDimension(R.dimen._5sdp);

        cardview.setPadding(radiuslrtp, radiuslrtp, radiuslrtp, radiuslrtp);


        cardview.setCardBackgroundColor(ContextCompat.getColor(OneBrushApp.getInstance(),
                R.color.white));
        cardview.setLayoutParams(addLayoutMarging());


        float radius = getResources().getDimension(R.dimen._10sdp);

        cardview.setRadius(radius);

        return cardview;
    }

    private void openMyExitDialog() {
        try {

            TextView dialog_msg;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();// (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
            alertDialog.setView(customLayout);
            alertDialog.setCancelable(false);

            dialog_msg = customLayout.findViewById(R.id.dia_msg);

            dialog_msg.setText((getResources().getString(R.string.are_you_want_to_save_data)));

            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
//                        function.call();
                        addAnswers();
                        Intent i = new Intent(Dentinostic_Qus_Ans_Activity.this, DentinosticDashboardActivity.class);
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent i = new Intent(Dentinostic_Qus_Ans_Activity.this, DentinosticDashboardActivity.class);
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    }
                }
            });

            alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().deleteAnsListByCaseId(caseid);
                    Intent i = new Intent(Dentinostic_Qus_Ans_Activity.this, DentinosticDashboardActivity.class);
                    startActivity(i);
                    finish();
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            Intent i = new Intent(Dentinostic_Qus_Ans_Activity.this, DentinosticDashboardActivity.class);
            startActivity(i);
            finish();

            e.printStackTrace();
        }
    }

    private void updateProgressBarMinus() {
        int indexofarr = progressCounter;
        progressCounter--;

        int pcounter = qusAnsBinding.progressBarSec2.getProgress() - 5;

        if (indexofarr <= 1 * totalvalue) {
            qusAnsBinding.progressBarSec2.setProgress(pcounter);
        } else if (indexofarr <= 2 * totalvalue) {
            qusAnsBinding.progressBarSec2.setProgress(pcounter);
        } else if (indexofarr <= 3 * totalvalue) {
            qusAnsBinding.progressBarSec2.setProgress(pcounter);
        } else if (indexofarr <= 4 * totalvalue) {
            qusAnsBinding.progressBarSec2.setProgress(pcounter);
        } else if (indexofarr <= 5 * totalvalue) {
            qusAnsBinding.progressBarSec2.setProgress(pcounter);
        } else if (indexofarr <= 6 * totalvalue) {//
            qusAnsBinding.progressBarSec2.setProgress(pcounter);
        } else if (indexofarr <= 7 * totalvalue) {//
            qusAnsBinding.progressBarSec2.setProgress(pcounter);

        } else if (indexofarr <= 8 * totalvalue) {//
            qusAnsBinding.progressBarSec2.setProgress(pcounter);

        } else if (indexofarr <= 9 * totalvalue) {//
            qusAnsBinding.progressBarSec2.setProgress(pcounter);

        } else if (indexofarr <= 10 * totalvalue) {//
            qusAnsBinding.progressBarSec2.setProgress(pcounter);

        } else if (indexofarr <= 11 * totalvalue) {//
            qusAnsBinding.progressBarSec2.setProgress(pcounter);

        } else {
            qusAnsBinding.progressBarSec2.setProgressDrawable(ContextCompat.getDrawable(Dentinostic_Qus_Ans_Activity.this,
                    R.drawable.progress_colo_bg));
        }
    }


    private void openPingenerateState() {
        if (userAccountDetailsModel.getRegisteredPIN1().equals(qusAnsBinding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);

            qusAnsBinding.txtPinEntry.setText("");

            counterForBio = 0;
            createCounterTimer();

            qusAnsBinding.layoutForView.setVisibility(View.VISIBLE);
            qusAnsBinding.pinViewLayout.setVisibility(View.GONE);
            qusAnsBinding.loginBtn.setVisibility(View.GONE);

        } else {
            qusAnsBinding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            qusAnsBinding.loginBtn.setVisibility(View.VISIBLE);

        }
    }

    // this is all you need to grant your application external storage permision
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission---", "Ask");

            ActivityCompat.requestPermissions(Dentinostic_Qus_Ans_Activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);

        } else {
            openPhoneGallery();

            Log.e("Permission---", "Allow");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission---", "Allowed");

                    openPhoneGallery();
                } else {
                    Log.e("Permission---", "Deney");

                    Toast.makeText(Dentinostic_Qus_Ans_Activity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void openPhoneGallery() {
        AppUtility.timeCounterStop(countDownTimer);
        countDownTimer = null;
        LOADERSTATE = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        createCounterTimer();
        if (resultCode != RESULT_CANCELED) {
            Log.e("", "");
            switch (requestCode) {
                case 1:
                    if (requestCode == 1 && resultCode == RESULT_OK
                            && null != data) {
                        // Get the Image from data

                        if (data.getData() != null) {
                            ArrayList<Uri> uriArrayList = new ArrayList<>();
                            Uri mImageUri = data.getData();

                            uriArrayList.add(mImageUri);
                            new CompressImageInBack(Dentinostic_Qus_Ans_Activity.this, new CompressImageInBack.OnImageCompressed() {
                                @Override
                                public void onImageCompressed(ArrayList<String> uriArrayList) {
//                                public void onImageCompressed(String uriArrayList) {
//                                    galleryImageList.add(uriArrayList);
                                    galleryImageList.addAll(uriArrayList);
                                    imageAdapter.updateGalleryDataList(galleryImageList);
                                    LOADERSTATE = false;

                                }
                            }, uriArrayList, qusAnsBinding.layout).compressImageInBckg();

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                ArrayList<Uri> uriArrayList = new ArrayList<>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri mImageUri = item.getUri();

                                    uriArrayList.add(mImageUri);

                                }


                                new CompressImageInBack(Dentinostic_Qus_Ans_Activity.this,
                                        new CompressImageInBack.OnImageCompressed() {
                                            @Override
                                            public void onImageCompressed(ArrayList<String> uriArrayList) {
//                                            public void onImageCompressed(String uriArrayList) {

                                                galleryImageList.addAll(uriArrayList);
                                                imageAdapter.updateGalleryDataList(galleryImageList);
                                                LOADERSTATE = false;

                                            }
                                        }, uriArrayList, qusAnsBinding.layout).compressImageInBckg();
                            }
                        }
                    } else {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }

//                    LOADERLOADING = false;

                    break;
            }
        } else {
            Log.e("", "");
            LOADERSTATE = false;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }


}

