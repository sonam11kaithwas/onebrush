package com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin__confirm_pkg.PinConfirmActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.concurrent.Callable;

public class FingerPrintActivity extends AppCompatActivity {
    private UserAccountDetailsModel userAccountDetailsModel;
    private RelativeLayout prentlay, layout_for_view;
    private LinearLayout pin_view_layout;
    private ScreenDetailsModel screenDetailsModel;
    private TextView txt_heading, pin_txt;
    private TextView txt_heading_for_pin, pin_txt_for_pin;
    private EditText txt_pin_entry;
    private Button login_btn;
    private int counterForBio = 0;
    private MyCountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("FingerPrintActivity", userAccountDetailsModel);

        screenDetailsModel = OneBrushAppPrefrence.getSharedprefInstance().getScreenDetailsdData("BIO");
        txt_heading = findViewById(R.id.txt_heading);
        pin_txt = findViewById(R.id.pin_txt);
        prentlay = findViewById(R.id.prentlay);

        txt_heading_for_pin = findViewById(R.id.txt_heading_for_pin);
        pin_txt_for_pin = findViewById(R.id.pin_txt_for_pin);
        txt_pin_entry = findViewById(R.id.txt_pin_entry);
        login_btn = findViewById(R.id.login_btn);
        layout_for_view = findViewById(R.id.layout_for_view);
        pin_view_layout = findViewById(R.id.pin_view_layout);


        txt_pin_entry.addTextChangedListener(new TextWatcher() {
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
        createCounterTimer();

        setOpenAppPin();

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
    public void onUserInteraction() {
        super.onUserInteraction();
        if (countDownTimer != null)
            AppUtility.timeCounterReset(countDownTimer);
    }
    private void userIntreactionEvent() {
        if (userAccountDetailsModel.getIdMethod() != null &&
                (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
                        userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {

            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
        }
    }

    private void pinViewStates() {
        txt_pin_entry.requestFocus();
        pin_view_layout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                txt_heading_for_pin.setText(screenDetailsModel.getTitle());
            } else {
                txt_heading_for_pin.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                pin_txt_for_pin.setText(screenDetailsModel.getDiscription());
            } else {
                pin_txt_for_pin.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        layout_for_view.setVisibility(View.GONE);

    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(txt_pin_entry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            layout_for_view.setVisibility(View.VISIBLE);
            pin_view_layout.setVisibility(View.GONE);
            txt_pin_entry.setText("");
            counterForBio = 0;
            createCounterTimer();

        } else {
            txt_pin_entry.setText("");
            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            login_btn.setVisibility(View.VISIBLE);

        }
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }

    private void setOpenAppPin() {

        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                txt_heading.setText(screenDetailsModel.getTitle());
            } else {
                txt_heading.setText(getResources().getString(R.string.bio_unlok));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                pin_txt.setText(screenDetailsModel.getDiscription());
            } else {
                pin_txt.setText(getResources().getString(R.string.do_you_want));
            }
        }

    }

    public void FingrPrintViewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img_for_pin:
                this.finish();
                break;
            case R.id.login_btn:
                startLoginForUser();
                break;
            case R.id.back_button:
                onBackPressed();
                this.finish();
                break;
            case R.id.cross_img:
                onBackPressed();
                break;
            case R.id.no_unlock_tx:
                openDendistDashBoard();
                break;
            case R.id.yes_btn:
//                if (!AppConstant.LASTSTATEFORBIO.isEmpty()) {
//                    AppUtility.updateScreeState(AppConstant.LASTSTATEFORBIO, userAccountDetailsModel);
//                }
                Intent intent = new Intent(FingerPrintActivity.this, FingerPrintCnfrmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    private void showErrorMsg(String msg, String titleHead) {
//        AppUtility.isDisplaySnacker(prentlay, this,
//                msg);
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    private void openDendistDashBoard() {
        Intent intent = null;

//        if (!AppConstant.LASTSTATEFORBIO.isEmpty()) {
//            userAccountDetailsModel.setLastScreen(userAccountDetailsModel.getLastScreen());
//            AppUtility.updateScreeState(AppConstant.LASTSTATEFORBIO, userAccountDetailsModel);
//
//            intent = new Intent(this, OpenPinActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//            this.finish();
//        } else
        {
            intent = new Intent(this, DentinosticDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            this.finish();
        }

//
////        Intent intent=null;
//        if (!userAccountDetailsModel.getLastScreen().equals("FingerPrintActivity")) {
//            intent = new Intent(this, AppUtility.getLastStateForLaunch(userAccountDetailsModel));
//
//        } else {
//            intent = new Intent(this, DentinosticDashboardActivity.class);
//
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(intent);
//        this.finish();
    }

    @Override
    public void onBackPressed() {

        Intent intent2 = new Intent(this, PinConfirmActivity.class);
//                if (userAccountDetailsModel.getStateForPin() != null && !userAccountDetailsModel.getStateForPin().equals("")) {


//                }
        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent2);

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
//
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
//                (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN))) {
//
//            pin_txt.requestFocus();
//            dashboardBinding.pinViewLayout.setVisibility(View.VISIBLE);
//            dashboardBinding.layoutForView.setVisibility(View.GONE);
//
//        }
//    }
}