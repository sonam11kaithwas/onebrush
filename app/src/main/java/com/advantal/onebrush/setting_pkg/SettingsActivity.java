package com.advantal.onebrush.setting_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.BuildConfig;
import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivitySettingsBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.DentiWclScrActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.regi_model_pkg.LogoutModel;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.AccountSettingActivity;
import com.advantal.onebrush.setting_pkg.feedback_pkg.FeedBackActivity;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.setting_pkg.open_list_pkg.ListAdapter;
import com.advantal.onebrush.setting_pkg.open_list_pkg.ListModel;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.Personal_InfoActivity;
import com.advantal.onebrush.setting_pkg.privacy_policy_pkg.PrivacyPolicyActivity;
import com.advantal.onebrush.setting_pkg.setting_viewmodel_pkg.SettingFactory;
import com.advantal.onebrush.setting_pkg.setting_viewmodel_pkg.SettingViewModel;
import com.advantal.onebrush.setting_pkg.terms_cond_pkg.TermsConditionActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.ErrorModel;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SettingsActivity extends AppCompatActivity {
    private final ArrayList<SettingModel> contactsList = new ArrayList<>();
    private final boolean CHECKSTATE = false;
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private SettingAdapter settingAdapter;
    private ListAdapter listAdapter;
    private SettingViewModel settingViewModel;
    private ActivitySettingsBinding settingsBinding;
    private UserAccountDetailsModel userAccountDetailsModel;
    private CheckBox checkBoxA, checkBoxB;
    private Dialog accountDialog;
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
                        settingsBinding.pinViewLayout.setVisibility(View.GONE);
                        settingsBinding.layoutForView.setVisibility(View.VISIBLE);
                        settingsBinding.bioLayout.setVisibility(View.GONE);
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

    private void onTouchIdClick() {
        Log.e("MyTest", "onTouchIdClick");
        if (counterForBio <= 2 && isBiometricCompatibleDevice())  {
            displayBiometricButton();
        } else {
            pinViewStates();

        }

    }

    private void pinViewStates() {
        settingsBinding.txtPinEntry.requestFocus();
        settingsBinding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                settingsBinding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                settingsBinding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                settingsBinding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                settingsBinding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        settingsBinding.layoutForView.setVisibility(View.GONE);
        settingsBinding.bioLayout.setVisibility(View.GONE);
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
                settingsBinding.pinViewLayout.setVisibility(View.GONE);
                settingsBinding.layoutForView.setVisibility(View.GONE);
                settingsBinding.bioLayout.setVisibility(View.VISIBLE);
                onTouchIdClick();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);


        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        settingViewModel = ViewModelProviders.of(this, new SettingFactory(this,
                new SettingModel(R.drawable.next, ""))).get(SettingViewModel.class);
        settingsBinding.setDentinosticModel(settingViewModel);
        String versionName = BuildConfig.VERSION_NAME;
        settingsBinding.tvVersion.setText("App Version " + " " + versionName);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// set status text dark
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

        createCounterTimer();


        AppUtility.updateScreeState("SettingsActivity", userAccountDetailsModel);
        settingViewModel.getLogout().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(SettingsActivity.this);
                if (!s.isEmpty()) {
                    showErrorMsg(s, "");
                }


                userlogout();

            }
        });


        settingViewModel.getErrorMsgBar().observeForever(new Observer<ErrorModel>() {
            @Override
            public void onChanged(ErrorModel s) {
                AppUtility.hideProgressBar(SettingsActivity.this);
                if (!s.getError().isEmpty()) {
                    showErrorMsg(s.getErrorTitle(), s.getErrorTitle());
                }
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        settingsBinding.recycler.setLayoutManager(layoutManager);
        settingAdapter = new SettingAdapter(contactsList, this,
                new SettingAdapter.AdapterCallBackListener() {
                    @Override
                    public void onClickListner(int position) {
                        if (position == 0) {
                            Intent pIntent=new Intent(SettingsActivity.this,Personal_InfoActivity.class);
                            startActivity(pIntent);
                            finish();

                        } else if (position == 1) {
                            Intent pIntent=new Intent(SettingsActivity.this,AccountSettingActivity.class);
                            startActivity(pIntent);
                            finish();


                        } else if (position == 2) {
                            Intent pIntent=new Intent(SettingsActivity.this,PrivacyPolicyActivity.class);
                            startActivity(pIntent);
                            finish();


                        } else if (position == 3) {
                            Intent pIntent=new Intent(SettingsActivity.this,TermsConditionActivity.class);
                            startActivity(pIntent);
                            finish();
                        } else if (position == 4) {
                            Intent pIntent=new Intent(SettingsActivity.this,FeedBackActivity.class);
                            startActivity(pIntent);
                            finish();
                        } else if (position == 5) {

                            sendLogFileOnserver();
                        }

                    }
                });
        settingsBinding.recycler.setAdapter(settingAdapter);
        contactsList.add(new SettingModel(R.drawable.ic_next, "Personal Information"));
        contactsList.add(new SettingModel(R.drawable.ic_next, "Account settings"));
        contactsList.add(new SettingModel(R.drawable.ic_next, "Privacy Policy"));
        contactsList.add(new SettingModel(R.drawable.ic_next, "Terms & Conditions"));
        contactsList.add(new SettingModel(R.drawable.ic_next, "Feedback"));
        contactsList.add(new SettingModel(R.drawable.ic_next, "Send Log Data"));
        settingAdapter.notifyDataSetChanged();

        settingsBinding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void sendLogFileOnserver() {
/*
        StringBuffer sb = new StringBuffer();
        String extStorageDirectory = Environment
                .getExternalStorageDirectory().getAbsolutePath();
        File folder = new File(extStorageDirectory, "OneBrushFile");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(folder, "en.pdf");
        try {
            if (file.exists()) {
                sb = getOldContentOfFile(file);
            } else {
                file.createNewFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        verifyStoragePermissions();


//        File logFile = new File("sdcard/log.file");
//
//
//        try {
//            //BufferedWriter for performance, true to set append to file flag
//            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//            buf.append("text");
//            buf.newLine();
//            buf.close();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        if (AppUtility.isNetworkConnected()) {

            LogFileController.getLogFileInstance().setContext(this);
            LogFileController.getLogFileInstance().createFile(settingsBinding.layout);
        } else {
            showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));
        }
    }

    public String getStringPdf(Uri filepath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }

    private StringBuffer getOldContentOfFile(File file) {

        StringBuffer text = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
//                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text;
    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(settingsBinding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            settingsBinding.layoutForView.setVisibility(View.VISIBLE);
            settingsBinding.pinViewLayout.setVisibility(View.GONE);
            settingsBinding.txtPinEntry.setText("");
            counterForBio = 0;
            createCounterTimer();

        } else {
            settingsBinding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            settingsBinding.loginBtn.setVisibility(View.VISIBLE);

        }
    }
//    ABC123456!

    private void userlogout() {

//        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(userAccountDetailsModel);
        AppUtility.updateScreeState("DentiWclScrActivity", userAccountDetailsModel);

        Intent intent = new Intent(SettingsActivity.this, DentiWclScrActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        finish();
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
        if (settingsBinding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            Intent intent = new Intent(SettingsActivity.this, DentinosticDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(intent);
            this.finish();
        }
    }

    public void onClickSettings(View view) {
        switch (view.getId()) {
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.back_button:
//                OneBrushAppPrefrence.getSharedprefInstance().setSelectedUserId("1");

                Intent intent = new Intent(SettingsActivity.this, DentinosticDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                this.finish();
                break;
            case R.id.iv_add_account:
//                Intent intent1 = new Intent(SettingsActivity.this, RegistrationFirstActivity.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent1);
                break;
            case R.id.llayout:
                opennewAcDialog();
                break;
            case R.id.btn_logout:
                openbtnDialog();
                break;
        }
    }

    private void userLogoutByServer() {
        AppUtility.showProgressBaForRelLay(settingsBinding.layout, this);
        settingViewModel.logout(new LogoutModel(userAccountDetailsModel.getUuId(), AppConstant.deviceId));

    }

    private void openbtnDialog() {
        Dialog dialog3 = new Dialog(SettingsActivity.this);
        dialog3.setContentView(R.layout.btn_layout);
        dialog3.getWindow().setGravity(Gravity.CENTER);
        dialog3.setCancelable(false);
        dialog3.getWindow().setBackgroundDrawableResource(R.drawable.cancel_btn_bg);
        dialog3.show();

        dialog3.findViewById(R.id.tv_cencle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();

            }
        });


        dialog3.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogoutByServer();
            }
        });
    }

    private void opennewAcDialog() {

        accountDialog = new Dialog(SettingsActivity.this);
        accountDialog.setContentView(R.layout.new_ac_layout);
        accountDialog.getWindow().setGravity(Gravity.BOTTOM);
        accountDialog.getWindow().setBackgroundDrawableResource(R.drawable.new_ac_bg);
        RecyclerView recyclerView = accountDialog.findViewById(R.id.recycler_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ListModel> addList = featcUserListFromDB();


        listAdapter = new ListAdapter(addList, this, new ListAdapter.UserAccountSelected() {
            @Override
            public void setUserSeelcted(String selectedUsrId) {
                OneBrushAppPrefrence.getSharedprefInstance().setSelectedUserId(selectedUsrId);
                Intent intent = new Intent(SettingsActivity.this, DentinosticDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
            }
        });
        recyclerView.setAdapter(listAdapter);


        accountDialog.show();
    }


    private ArrayList<ListModel> featcUserListFromDB() {
        ArrayList<ListModel> userDataList = new ArrayList<>();
        userDataList.add(new ListModel(userAccountDetailsModel.getName() + " " + userAccountDetailsModel.getSurname(),
                userAccountDetailsModel.getUuId()));

        return userDataList;
//                      AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll( OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());;
    }


    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void onCheckboxClicked(View view) {
        switch (view.getId()) {
            case R.id.cross_img_for_pin:
                this.finish();
                break;
            case R.id.login_btn:OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;


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