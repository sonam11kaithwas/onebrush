package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityPinGenerateBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin__confirm_pkg.PinConfirmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_model_pkg.PinGenReqModel;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_mvvm_pkg.PinGenModelFactory;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_mvvm_pkg.PingenerateViewModel;
import com.advantal.onebrush.dentis_dash__board_pkg.terms_condition_pkg.Term_Condition_View_Activity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.concurrent.Callable;

public class PinGenerateActivity extends AppCompatActivity {
    UserAccountDetailsModel userAccountDetailsModel;
    private ScreenDetailsModel screenDetailsModel;
    private PingenerateViewModel pingenerateViewModel;
    private ActivityPinGenerateBinding activityPinGenerateBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activityPinGenerateBinding = DataBindingUtil.setContentView(this, R.layout.activity_pin_generate);

        screenDetailsModel = OneBrushAppPrefrence.getSharedprefInstance().getScreenDetailsdData("PIN");


        pingenerateViewModel = ViewModelProviders.of(this, new PinGenModelFactory(this,
                new PinGenReqModel())).get(PingenerateViewModel.class);
        activityPinGenerateBinding.setPingeneratemodel(pingenerateViewModel);

        activityPinGenerateBinding.txtPinEntry.requestFocus();
        setOpenAppPin();


        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());


        AppUtility.updateScreeState("PinGenerateActivity", userAccountDetailsModel);


        activityPinGenerateBinding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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
        activityPinGenerateBinding.txtPinEntry.setCursorVisible(true);
        activityPinGenerateBinding.txtPinEntry.setSelection(activityPinGenerateBinding.txtPinEntry.getText().length());


    }


    private void openPingenerateState() {
        UserAccountDetailsModel userList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao()
                .getExitPinData(activityPinGenerateBinding.txtPinEntry.getText().toString(), OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        if (userList != null && !userList.getRegisteredPIN1().isEmpty() &&
                userList.getRegisteredPIN1().equals(activityPinGenerateBinding.txtPinEntry.getText().toString())) {
            AppUtility.alertDialog(this, "Pin already  exit.", ""
                    , new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            activityPinGenerateBinding.txtPinEntry.setText("");
                            return null;
                        }
                    });
        } else {
            userAccountDetailsModel.setRegisteredPIN1(activityPinGenerateBinding.txtPinEntry.getText().toString());
            AppUtility.updateScreeState("PinGenerateActivity", userAccountDetailsModel);
            Intent intent = new Intent(this, PinConfirmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(intent);
            this.finish();
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

    private void setOpenAppPin() {

        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                activityPinGenerateBinding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                activityPinGenerateBinding.txtHeading.setText(getResources().getString(R.string.create_pin));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                activityPinGenerateBinding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                activityPinGenerateBinding.pinTxt.setText(getResources().getString(R.string.pin_txt));
            }
        }

    }

//    private void setViewsLable() {
//        activityPinGenerateBinding.pinTxt.setText(getResources().getString(R.string.confirm_pin_txt));
//    }

    @Override
    public void onBackPressed() {
        AppUtility.updateScreeState("Term_Condition_View_Activity", userAccountDetailsModel);

        Intent intent = new Intent(this, Term_Condition_View_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  AppUtility.updateScreeState("PinGenerateActivity", userAccountDetailsModel);

    }

    public void pinViewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cross_img:
                onBackPressed();
                break;
            case R.id.login_btn:
                OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;

        }
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }
}