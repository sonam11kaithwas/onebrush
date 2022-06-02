package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityNewCaseBinding;
import com.advantal.onebrush.denti_qus_pkg.Dentinostic_Qus_Ans_Activity;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintCnfrmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_model_pkg.NewCaseModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_viewmodel_pkg.NewCaseModelFactory;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_viewmodel_pkg.NewCaseViewModel;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.open_pin_pkg.OpenPinActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

public class NewCaseActivity extends AppCompatActivity {
    private NewCaseViewModel newCaseViewModel;
    private ActivityNewCaseBinding newCaseBinding;
    private UserAccountDetailsModel userAccountDetailsModel;
    private String state = "Registration";
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newCaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_case);
        newCaseViewModel = ViewModelProviders.of(this, new NewCaseModelFactory(this,
                new NewCaseModel())).get(NewCaseViewModel.class);
        newCaseBinding.setNewCaseModel(newCaseViewModel);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("NewCaseActivity", userAccountDetailsModel);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("state"))
                state = bundle.getString("state");
            if (getIntent().hasExtra("position"))
                position = bundle.getInt("position");
        }

        if (state.equals("QueAns")) {
            newCaseBinding.tvCencle.setVisibility(View.INVISIBLE);
            newCaseBinding.backButton.setVisibility(View.INVISIBLE);
        } else {
            newCaseBinding.tvCencle.setVisibility(View.VISIBLE);
            newCaseBinding.backButton.setVisibility(View.VISIBLE);
        }
    }


    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_cross:
                this.finish();
                break;
            case R.id.back_button:
                if (newCaseBinding.tvCencle.getVisibility() == View.INVISIBLE) {
                    Intent intentBack = new Intent(NewCaseActivity.this, DentinosticDashboardActivity.class);
                    intentBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentBack);
                }
                break;
            case R.id.tv_cencle:
                Intent intentRegi = new Intent(this, RegistrationFirstActivity.class);
                intentRegi.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intentRegi);
                finish();
                break;
            case R.id.btn_start:
                Intent intent = null;
                if (state.equals("Registration")) {
                    intent = new Intent(NewCaseActivity.this, RegistrationFirstActivity.class);
                } else {
                    intent = new Intent(NewCaseActivity.this, Dentinostic_Qus_Ans_Activity.class);
                    intent.putExtra("position", position);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
                break;
        }
    }
//
//    @Override
//    protected void onResume() {
//        if (userAccountDetailsModel.getIdMethod() != null && userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                && MyDigitalClockForScreenLock.getInstance().getMyTimerTime() >= 60) {
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
//            MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN))
//                startActivity(new Intent(this, OpenPinActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
//        super.onResume();
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
//            MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//            MyDigitalClockForScreenLock.getInstance().startTimerCounting();
//        }
//
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
//    }


}