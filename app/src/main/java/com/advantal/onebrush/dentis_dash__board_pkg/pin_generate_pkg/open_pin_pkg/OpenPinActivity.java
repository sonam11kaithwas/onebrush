package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.open_pin_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinEntryEditText;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.concurrent.Callable;

public class OpenPinActivity extends AppCompatActivity {
    RelativeLayout prentlay;
    UserAccountDetailsModel userAccountDetailsModel;
    Button login_btn;
    private PinEntryEditText txt_pin_entry;
    private ScreenDetailsModel screenDetailsModel;
    private TextView txt_heading, pin_txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pin);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        screenDetailsModel = OneBrushAppPrefrence.getSharedprefInstance().getScreenDetailsdData("OPEN_PIN");

        finidMyViews();
    }

    private void setOpenAppPin() {
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                txt_heading.setText(screenDetailsModel.getTitle());
            } else {
                txt_heading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                pin_txt.setText(screenDetailsModel.getDiscription());
            } else {
                pin_txt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }

    }

    private void finidMyViews() {
        txt_heading = findViewById(R.id.txt_heading);
        pin_txt = findViewById(R.id.pin_txt);
        prentlay = findViewById(R.id.prentlay);

        txt_pin_entry = findViewById(R.id.txt_pin_entry);
        login_btn = findViewById(R.id.login_btn);
        txt_pin_entry.requestFocus();

        setOpenAppPin();

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
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
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void pinViewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img:
                onBackPressed();
                break;
            case R.id.login_btn:OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;
        }
    }

    private void showErrorMsg(String msg,String titleHead) {
//        AppUtility.isDisplaySnacker(prentlay, this,
//                msg);
        AppUtility.alertDialog(this, msg,titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onPause() {
//        if (userAccountDetailsModel.getIdMethod() != null && userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
    }

    private void openPingenerateState() {
        if (userAccountDetailsModel.getRegisteredPIN1().equals(txt_pin_entry.getText().toString())) {
            login_btn.setVisibility(View.GONE);
            Intent intent = new Intent(OpenPinActivity.this, AppUtility.getLastStateForLaunch(userAccountDetailsModel));
//            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            this.finish();
        } else {
            txt_pin_entry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2),"");
            login_btn.setVisibility(View.VISIBLE);

        }
    }
}