package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin__confirm_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinEntryEditText;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinGenerateActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.concurrent.Callable;

public class PinConfirmActivity extends AppCompatActivity {
    UserAccountDetailsModel userAccountDetailsModel;
    RelativeLayout prentlay;
    private PinEntryEditText txt_pin_entry;
    private ScreenDetailsModel screenDetailsModel;
    private TextView txt_heading, pin_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_confirm);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        screenDetailsModel = OneBrushAppPrefrence.getSharedprefInstance().getScreenDetailsdData("CONFIRM_PIN");

        AppUtility.updateScreeState("PinConfirmActivity", userAccountDetailsModel);

        finidMyViews();
    }

    private void setOpenAppPin() {

        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                txt_heading.setText(screenDetailsModel.getTitle());
            } else {
                txt_heading.setText(getResources().getString(R.string.create_pin));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                pin_txt.setText(screenDetailsModel.getDiscription());
            } else {
                pin_txt.setText(getResources().getString(R.string.confirm_pin_txt));
            }
        }

    }

    private void finidMyViews() {
        txt_heading = findViewById(R.id.txt_heading);
        pin_txt = findViewById(R.id.pin_txt);
        txt_pin_entry = findViewById(R.id.txt_pin_entry);
        prentlay = findViewById(R.id.prentlay);
        txt_pin_entry.requestFocus();

        setOpenAppPin();


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

    private void showErrorMsg(String msg, String titleHead) {

        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });

    }

    private void openPingenerateState() {
        if (userAccountDetailsModel.getRegisteredPIN1().equals(txt_pin_entry.getText().toString())) {
            userAccountDetailsModel.setIdMethod(AppConstant.ID_MENTHOD_PIN);
            userAccountDetailsModel.setRegistrationTimePIN(AppUtility.getCurrentDateTime("dd.mm.yyyy KK:hh:ss"));
            userAccountDetailsModel.setRegisteredPIN2(txt_pin_entry.getText().toString());
            AppUtility.updateScreeState("PinConfirmActivity", userAccountDetailsModel);

            /*if (AppUtility.fingerPrintAvableOrNot(PinConfirmActivity.this)) {


                UserAccountDetailsModel tempUserModel
                        = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getBioMetricData(AppConstant.ID_MENTHOD_Bio);
                Intent intent;
                if (tempUserModel != null && !tempUserModel.getIdMethod().isEmpty()) {
                    intent = new Intent(PinConfirmActivity.this, DentinosticDashboardActivity.class);

                } else {
                    intent = new Intent(PinConfirmActivity.this, FingerPrintActivity.class);

                }
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            } else {
                Intent intent = new Intent(PinConfirmActivity.this, DentinosticDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            PinConfirmActivity.this.finish();*/
            AppUtility.alertDialog(this, getResources().getString(R.string.pin_safely_message), ""
                    , new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            if (AppUtility.fingerPrintAvableOrNot(PinConfirmActivity.this)) {

                                UserAccountDetailsModel tempUserModel
                                        = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getBioMetricData(AppConstant.ID_MENTHOD_Bio);
                                Intent intent;
                                if (tempUserModel != null && !tempUserModel.getIdMethod().isEmpty()) {
                                    intent = new Intent(PinConfirmActivity.this, DentinosticDashboardActivity.class);

                                } else {
                                    intent = new Intent(PinConfirmActivity.this, FingerPrintActivity.class);

                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(PinConfirmActivity.this, DentinosticDashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                            }
                            PinConfirmActivity.this.finish();
                            return null;
                        }
                    });


        } else {
            showErrorMsg(getResources().getString(R.string.pin_not_match), "");
            txt_pin_entry.setText("");
        }
    }

    public void pinViewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img:
                onBackPressed();
                break;
            case R.id.login_btn:
//                startLoginForUser();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        AppUtility.updateScreeState("PinGenerateActivity", userAccountDetailsModel);
        Intent intent = new Intent(this, PinGenerateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

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