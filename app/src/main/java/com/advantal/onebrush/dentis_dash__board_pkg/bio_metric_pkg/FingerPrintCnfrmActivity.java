package com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.an.biometric.BiometricCallback;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;


public class FingerPrintCnfrmActivity extends AppCompatActivity implements BiometricCallback {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private UserAccountDetailsModel userAccountDetailsModel;
    private RelativeLayout parent_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_cnfrm);

        parent_lay = findViewById(R.id.parent_lay);

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        if (userAccountDetailsModel.getIdMethod() != null && !userAccountDetailsModel.getIdMethod().isEmpty()
                && !userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)) {
            AppUtility.updateScreeState("FingerPrintCnfrmActivity", userAccountDetailsModel);
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(FingerPrintCnfrmActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                showErrorMsg("Pin Does not match");
                Log.e("Test", "onAuthenticationError");

//                OneBrushApp.getInstance().showToastmsg("Pin does not match");
                onBackPressed();
            }


            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Log.e("Test1", "onAuthenticationSucceeded");

                if (userAccountDetailsModel.getLastScreen() != null &&
                        userAccountDetailsModel.getLastScreen().equals("FingerPrintActivity")) {
                    AppUtility.updateScreeState("DentinosticDashboardActivity", userAccountDetailsModel);

                }
                if (userAccountDetailsModel.getIdMethod() == null || userAccountDetailsModel.getIdMethod().isEmpty()
                        || !userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)) {
                    userAccountDetailsModel.setIdMethod(AppConstant.ID_MENTHOD_Bio);
                    userAccountDetailsModel.setRegistrationTimeBiom(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
                    AppUtility.updateScreeState("DentinosticDashboardActivity", userAccountDetailsModel);

                    opnNextDashBoard();
                } else {//if (!AppConstant.LASTSTATEFORBIO.isEmpty())
//                    AppUtility.updateScreeState(AppConstant.LASTSTATEFORBIO, userAccountDetailsModel);
//                    userAccountDetailsModel.setLastScreen(AppConstant.LASTSTATEFORBIO);
//                    AppUtility.updateScreeState(AppConstant.LASTSTATEFORBIO, userAccountDetailsModel);
                    openDendistDashBoard();

                }
//                else {
//                    openDendistDashBoard();
//                }


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showErrorMsg("Authentication failed","");
                Log.e("Test1", "onAuthenticationFailed");

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)

                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app./ClickListener(view -> {
        biometricPrompt.authenticate(promptInfo);
        Log.e("", "");
    }

    private void showErrorMsg(String msg,String titleHead) {
//        AppUtility.isDisplaySnacker(parent_lay, this,
//                msg);

        AppUtility.alertDialog(this, msg,titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void openDendistDashBoard() {
        Intent intent = new Intent(FingerPrintCnfrmActivity.this, AppUtility.getLastStateForLaunch(userAccountDetailsModel));
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onSdkVersionNotSupported() {
        Log.e("Test1", "onSdkVersionNotSupported");

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Log.e("Test1", "onBiometricAuthenticationNotSupported");

    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Log.e("Test1", "onBiometricAuthenticationNotAvailable");

    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Log.e("Test1", "onBiometricAuthenticationPermissionNotGranted");

    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Log.e("Test1", "onBiometricAuthenticationInternalError");

    }

    @Override
    public void onAuthenticationFailed() {
        Log.e("Test1", "onAuthenticationFailed");

    }

    @Override
    public void onAuthenticationCancelled() {
        Log.e("Test1", "onAuthenticationCancelled");

    }

    @Override
    public void onAuthenticationSuccessful() {
        Log.e("Test1", "onAuthenticationSuccessful");

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Log.e("Test1", "onAuthenticationHelp");

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Log.e("Test1", "onAuthenticationError");

    }


    public void FingrPrintViewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img:
                onBackPressed();
                break;
            case R.id.no_unlock_tx:
                opnNextDashBoard();
                break;
            case R.id.term_nxt_btn:
                break;
        }
    }

    private void opnNextDashBoard() {
        Intent intent = new Intent(FingerPrintCnfrmActivity.this, DentinosticDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FingerPrintCnfrmActivity.this, FingerPrintActivity.class);
        startActivity(intent);
        this.finish();
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
        super.onDestroy();
    }


}