package com.advantal.onebrush.sign_in_registr_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintCnfrmActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.regi_pkg.RegistrationFirstActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;

public class SignIn_RegistrationActivity extends AppCompatActivity {
//    private MyCountDownTimer countDownTimer;
//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        Log.e("Test", " ---user interaction...");
//
//        //Reset the timer on user interaction...
//        AppUtility.timeCounterReset(countDownTimer);
//    }
//
//    private void userIntreactionEvent() {
//        if (userAccountDetailsModel.getIdMethod() != null &&
//                (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                        userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
////                MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
////                MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
//                dashboardBinding.txtPinEntry.requestFocus();
//                dashboardBinding.pinViewLayout.setVisibility(View.VISIBLE);
//                dashboardBinding.layoutForView.setVisibility(View.GONE);
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_registration);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//        countDownTimer = new MyCountDownTimer(AppConstant.startTime, AppConstant.interval, new MyCountDownTimer.UserIntractionDisable() {
//            @Override
//            public void getUserPinWindow() {
//                userIntreactionEvent();
//            }
//        });
//        AppUtility.timeCounterReset(countDownTimer);
//        getWindow().setStatusBarColor(ContextCompat.getColor(SignIn_RegistrationActivity.this, R.color.background_color));
    }

    public void myButtonsCliekevents(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                //  this.finish();
                break;
            case R.id.sign_in_btn:
                //Intent intent2 = new Intent(this, RegistrationFirstActivity.class);
                Intent intent2 = new Intent(this, RegistrationFirstActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent2);
                this.finish();
                break;

        }
    }

}