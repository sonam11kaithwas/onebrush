package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.advantal.onebrush.R;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintCnfrmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.CaseInformationActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;

public class ContactUsActivity extends AppCompatActivity {
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
//            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)){
//                startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                this.finish();}
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
        setContentView(R.layout.activity_contact_us);
//        countDownTimer = new MyCountDownTimer(AppConstant.startTime, AppConstant.interval, new MyCountDownTimer.UserIntractionDisable() {
//            @Override
//            public void getUserPinWindow() {
//                userIntreactionEvent();
//            }
//        });
//        AppUtility.timeCounterReset(countDownTimer);
    }

    public void onClickHandler(View view) {

        switch (view.getId()) {
            case R.id.back_btn_contact:
                onBackPressed();
                finish();
              /*  Intent i = new Intent(ContactUsActivity.this, CaseInformationActivity.class);
                startActivity(i);
                finish();
                break;*/
        }
    }
}