package com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityDentiWlcmBinding;
import com.advantal.onebrush.denti_qus_pkg.Dentinostic_Qus_Ans_Activity;
import com.advantal.onebrush.denti_qus_pkg.frst_qus_pkg.FirstQuestIonActivity;
import com.advantal.onebrush.denti_qus_pkg.open_camera_pkg.Denti_Camera_Activity;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintCnfrmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.NewCaseActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.PatientListActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.CaseInformationActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.case_picture_pkg.PictureActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.QuestionsActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.diagnostic_pkg.DiagnosisActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.invoice_pkg.InvoiceActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.open_bio_pkg.OpenBioACtivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinGenerateActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.open_pin_pkg.OpenPinActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin__confirm_pkg.PinConfirmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.terms_condition_pkg.Term_Condition_View_Activity;
import com.advantal.onebrush.forgot_password_pkg.Forgot_Password_Activity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.payment_pkg.ContinueWithPaymentActivity;
import com.advantal.onebrush.registration_pkg.payment_pkg.DonePaymentActivity;
import com.advantal.onebrush.registration_pkg.payment_pkg.PaymentAcceptActivity;
import com.advantal.onebrush.registration_pkg.regi_next_pkg.Registr_NextActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.registration_pkg.save_continue_pkg.SaveContinueActivity;
import com.advantal.onebrush.setting_pkg.SettingsActivity;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.AccountSettingActivity;
import com.advantal.onebrush.setting_pkg.feedback_pkg.FeedBackActivity;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.Personal_InfoActivity;
import com.advantal.onebrush.setting_pkg.privacy_policy_pkg.PrivacyPolicyActivity;
import com.advantal.onebrush.setting_pkg.terms_cond_pkg.TermsConditionActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.an.biometric.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DentiWclScrActivity extends AppCompatActivity {
    private UserAccountDetailsModel userAccountDetailsModel;
    private ActivityDentiWlcmBinding binding;
    private DentiWclScrAdpter dotIndicatorAdpter;
    private ImageView[] dots;
    private int selectedPosition = 1;
    private String com_approval = "01 Welcome";
    private DentiWclScrViewModel dentiWclScrViewModel;
    private List<WelcomeCarouselModel> welcomeCarouselModelList = new ArrayList<>();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dentiWclScrViewModel = ViewModelProviders.of(this)
                .get(DentiWclScrViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_denti_wlcm);
        binding.setLifecycleOwner(this);
        binding.setDendines(dentiWclScrViewModel);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (AppUtility.isNetworkConnected()) {
            dentiWclScrViewModel.getallScreenDetail();
            dentiWclScrViewModel.getDentiSyncroNization();
        } else {
            showErrorMsg(OneBrushApp.getInstance().getResources().getString(R.string.bad_internet_connection), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
        }
        AppConstant.deviceId = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        Log.e("deviceId---", OneBrushAppPrefrence.getSharedprefInstance().getLastLaunchTime());
        Log.e("Authid---", BuildConfig.VERSION_NAME + "");

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().
                getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());


        if (userAccountDetailsModel == null) {
            userAccountDetailsModel = new UserAccountDetailsModel("1", "", "",
                    "", "", 1, "",
                    "Onebrush", "", "", "", "",
                    AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss"),
                    AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss"), ""
                    , AppUtility.getCurrentDateTime("yyyy-MM-dd"),
                    AppUtility.getCurrentDateTime("yyyy-MM-dd"),
//                    AppUtility.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"),
//                    AppUtility.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"), ""
//                    , AppUtility.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"),
//                    AppUtility.getCurrentDateTime("yyyy-MM-dd hh:mm:ss"),
                    1, "", "", "", "",
                    "", "", "", "D", "",
                    "", com_approval, "", "", "", "",
                    "", "", "", "", "", "");

            AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().insertSinleUserAccountDetails(userAccountDetailsModel);
        }
        fetchDataFromDbCrausal();


        dentiWclScrViewModel.getWelCarousllist().observeForever(new Observer<List<WelcomeCarouselModel>>() {
            @Override
            public void onChanged(List<WelcomeCarouselModel> welcomeCarouselModels) {

                welcomeCarouselModelList = welcomeCarouselModels;
                finfViews();

            }
        });

    }


    private void finfViews() {

        dotIndicatorAdpter = new DentiWclScrAdpter(DentiWclScrActivity.this, welcomeCarouselModelList);
        binding.viewPager.setAdapter(dotIndicatorAdpter);

        dots = new ImageView[dotIndicatorAdpter.getCount()];


        dots();

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedViewSet(position);
            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position + 1;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("state", "" + state + "");
            }
        });

    }


    private void fetchDataFromDbCrausal() {
        Log.e("sona", userAccountDetailsModel.getLastScreen());
        if (userAccountDetailsModel != null && userAccountDetailsModel.getLastScreen() != null) {
            if (userAccountDetailsModel.getLastScreen().equals(com_approval) || userAccountDetailsModel.getLastScreen().equals(
                    "DentiWclScrActivity")) {
                dentiWclScrViewModel.getCrausalDataFromServer();
            } else {


                switch (userAccountDetailsModel.getLastScreen()) {
                    case "PinConfirmActivity":
                        startLastStateScreen(PinConfirmActivity.class);
                        break;
                    case "TermsConditionActivity":
                        startLastStateScreen(TermsConditionActivity.class);
                        break;
                    case "PrivacyPolicyActivity":
                        startLastStateScreen(PrivacyPolicyActivity.class);
                        break;

                    case "OpenPinActivity":
                        startLastStateScreen(OpenPinActivity.class);
                        break;

                    case "ContinueWithPaymentActivity":
                        startLastStateScreen(ContinueWithPaymentActivity.class);
                        break;
                    case "DonePaymentActivity":
                        startLastStateScreen(DonePaymentActivity.class);
                        break;
                    case "Forgot_Password_Activity":
                        startLastStateScreen(Forgot_Password_Activity.class);
                        break;
                    case "PaymentAcceptActivity":
                        startLastStateScreen(PaymentAcceptActivity.class);
                        break;
                    case "Term_Condition_View_Activity":
                        startLastStateScreen(Term_Condition_View_Activity.class);
                        break;

                    case "TermConditionActivity":
                        startLastStateScreen(TermsConditionActivity.class);
                        break;
                    case "PinGenerateActivity":
//                        openPinScreen(false);
                        startLastStateScreen(PinGenerateActivity.class);

                        break;
                    case "FingerPrintActivity":
                        startLastStateScreen(FingerPrintActivity.class);
                        break;
                    case "FingerPrintCnfrmActivity":
                        startLastStateScreen(FingerPrintCnfrmActivity.class);
                        break;
                    case "DentinosticDashboardActivity":
                        startLastStateScreen(DentinosticDashboardActivity.class);
                        break;
                    case "NewCaseActivity":
                        startLastStateScreen(NewCaseActivity.class);
                        break;
                    case "RegistrationFirstActivity":
                        startLastStateScreen(RegistrationFirstActivity.class);
                        break;
                    case "Registr_NextActivity":
                        startLastStateScreen(Registr_NextActivity.class);
                        break;
                    case "SaveContinueActivity":
                        startLastStateScreen(SaveContinueActivity.class);
                        break;
                    case "LoginActivity":
                        startLastStateScreen(LoginActivity.class);
                        break;
                    case "Personal_InfoActivity":
                        startLastStateScreen(Personal_InfoActivity.class);
                        break;
                    case "Dentinostic_Qus_Ans_Activity":
                        startLastStateScreen(Dentinostic_Qus_Ans_Activity.class);
                        break;
                    case "AddPatientActivity":
                        startLastStateScreen(AddPatientActivity.class);
                        break;
                    case "CaseInformationActivity":
                        startLastStateScreen(CaseInformationActivity.class);
                        break;
                    case "PatientListActivity":
                        startLastStateScreen(PatientListActivity.class);
                        break;
                    case "Denti_Camera_Activity":
                        startLastStateScreen(Denti_Camera_Activity.class);
                        break;
                    case "DiagnosisActivity":
                        startLastStateScreen(DiagnosisActivity.class);
                        break;
                    case "InvoiceActivity":
                        startLastStateScreen(InvoiceActivity.class);
                        break;
                    case "QuestionsActivity":
                        startLastStateScreen(QuestionsActivity.class);
                        break;
                    case "PictureActivity":
                        startLastStateScreen(PictureActivity.class);
                        break;
                    case "FirstQuestIonActivity":
                        startLastStateScreen(FirstQuestIonActivity.class);
                        break;
                    case "AccountSettingActivity":
                        startLastStateScreen(AccountSettingActivity.class);
                        break;
                    case "FeedBackActivity":
                        startLastStateScreen(FeedBackActivity.class);
                        break;
                    case "SettingsActivity":
                        startLastStateScreen(SettingsActivity.class);
                        break;


                    default:
                        startLastStateScreen(DentiWclScrActivity.class);

                }
            }
        } else {
            openDialog();
        }
    }

    private boolean isBiometricCompatibleDevice() {
        return BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void startLastStateScreen(Class c) {
        com_approval = userAccountDetailsModel.getLastScreen();

        Intent intent = null;
        if (userAccountDetailsModel.getIdMethod() != null && !userAccountDetailsModel.getIdMethod().isEmpty()) {
            if (!AppUtility.getCompareTwoTime(userAccountDetailsModel.getLastAction())) {
                intent = new Intent(this, c);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                Intent intent1 = new Intent(DentiWclScrActivity.this, OpenPinActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
                finish();

            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)) {
                if (isBiometricCompatibleDevice()) {
//                intent = new Intent(this, FingerPrintCnfrmActivity.class);
                    intent = new Intent(this, OpenBioACtivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent1 = new Intent(DentiWclScrActivity.this, OpenPinActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent1);
                    finish();

                }
            } else {
                intent = new Intent(this, c);
                startActivity(intent);
                finish();
            }
        } else {
            intent = new Intent(this, c);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }


    }


    public void dots() {
        for (int i = 0; i < dotIndicatorAdpter.getCount(); i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(DentiWclScrActivity.this,
                    R.drawable.tab_deselector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);

            binding.dotLayout.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(DentiWclScrActivity
                        .this,
                R.drawable.selected_dot));
        if (userAccountDetailsModel != null && userAccountDetailsModel.getLastScreen() != null
                && userAccountDetailsModel.getLastScreen().equals(com_approval))
            openDialog();

    }

    private void selectedViewSet(int pos) {
        for (int i = 0; i < dotIndicatorAdpter.getCount(); i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(DentiWclScrActivity.this,
                    R.drawable.tab_deselector));
        }
        dots[pos].setImageDrawable(ContextCompat.getDrawable(DentiWclScrActivity.this,
                R.drawable.selected_dot));


        if (pos == dotIndicatorAdpter.getCount() - 1) {
            binding.bioNxtBtn.setText(getResources().getString(R.string.start_registration));
        } else {
            binding.bioNxtBtn.setText(getResources().getString(R.string.next));
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void viewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img:
                onBackPressed();
                break;
            case R.id.bio_nxt_btn:
                Log.e("", "");
                if (selectedPosition >= dotIndicatorAdpter.getCount()) {
                    stateForTerms();
                } else {
                    selectedViewSet(selectedPosition);
                    binding.viewPager.setCurrentItem(selectedPosition);
                }
                break;
        }
    }

    private void stateForTerms() {
        Intent intent = new Intent(this, Term_Condition_View_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }


    private void openDialog() {
        Dialog dialog = new Dialog(DentiWclScrActivity.this);
        dialog.setContentView(R.layout.open_biometric_dialog);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.wcl_dialog_bg);
        dialog.show();
        dialog.setCancelable(false);
        SwitchCompat switchCompat = dialog.findViewById(R.id.noti_on_off_switch);

        dialog.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String push = switchCompat.isChecked() ? "Yes" : "No";
                userAccountDetailsModel.setAllowPushCom(AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss"));// hh:mm:ss
                if (switchCompat.isChecked())
                    com_approval = "DentiWclScrActivity";
                AppUtility.updateScreeState(com_approval, userAccountDetailsModel);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //    AppUtility.updateScreeState("DentiWclScrActivity", userAccountDetailsModel);
    }


}

