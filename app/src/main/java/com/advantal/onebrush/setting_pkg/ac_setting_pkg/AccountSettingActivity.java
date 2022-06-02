package com.advantal.onebrush.setting_pkg.ac_setting_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityAccountSettingBinding;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.setting_pkg.SettingsActivity;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_model_pkg.AccountModel;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_viewmodel_pkg.AccountFactory;
import com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_viewmodel_pkg.AccountViewModel;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.ValidationCtrlr;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AccountSettingActivity extends AppCompatActivity {
    private final boolean CHECKSTATE = false;
    String lower = ".*[a-z].*";
    String upper = ".*[A-Z].*";
    String spacialChar = ".*[!@#$%^&+=].*";
    String whiteSpace = ".*\\\\S+$.*";
    String number = ".*[0-9].*";
    private String str = "rejected";
    private AccountViewModel accountViewModel;
    private ActivityAccountSettingBinding binding;
    private UserAccountDetailsModel userAccountDetailsModel;
    private MyCountDownTimer countDownTimer;
    private int counterForBio = 0;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account_setting);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_setting);

        accountViewModel = ViewModelProviders.of(this, new AccountFactory(this,
                new AccountModel("", "", ""))).get(AccountViewModel.class);
        binding.setAccountModel(accountViewModel);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("AccountSettingActivity", userAccountDetailsModel);
        createCounterTimer();
        accountViewModel.getChangepass().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AppUtility.hideProgressBar(AccountSettingActivity.this);
//                OneBrushApp.getInstance().showToastmsg(s);
                userchangepass();
                Log.e("", "");

            }
        });

        binding.txtPinEntry.addTextChangedListener(new TextWatcher() {
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


/*
        binding.etOldPass.addTextChangedListener(new TextWatcher() {
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
                binding.viewDashLayout.setVisibility(View.VISIBLE);
                binding.passLable.setVisibility(View.VISIBLE);
                validation(String.valueOf(s));

              */
/*  if (s.toString().length() == 0) {
                    binding.viewDashLayout.setVisibility(View.GONE);
                    binding.passLable.setVisibility(View.GONE);
                    binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
                    binding.viewSec.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.grey));

                } else if (s.toString().length() < 4) {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewSec.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));

                    binding.passLable.setText(getResources().getString(R.string.very_week));
                    binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.navy));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                } else if (s.toString().length() < 6) {

                    binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewSec.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setText(getResources().getString(R.string.week));
                    binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.BrickRed));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);


//                    for (Drawable drawable : activityLoginBinding.passLable.getCompoundDrawables()) {
//                        if (drawable != null) {
//                            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(activityLoginBinding.passLable
//                                    .getContext(), R.color.navy), PorterDuff.Mode.SRC_IN));
//                        }
//                    }
                } else if (s.toString().length() < 8) {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable.setText(getResources().getString(R.string.so_so));
                    binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.yellow));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                } else {
                    binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
                    binding.passLable.setText(getResources().getString(R.string.great));
                    binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.dark_green));
                    binding.passLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }*//*


            }
        });
*/
        binding.etNewPass.addTextChangedListener(new TextWatcher() {
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
                binding.viewDashLayout1.setVisibility(View.VISIBLE);
                binding.passLable1.setVisibility(View.VISIBLE);
                validationNewPass(String.valueOf(s));

        /*        if (s.toString().length() == 0) {
                    binding.viewDashLayout1.setVisibility(View.GONE);
                    binding.passLable1.setVisibility(View.GONE);
                    binding.viewFirst1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewSec1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth1.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable1.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.grey));

                } else if (s.toString().length() < 4) {
                    binding.viewFirst1.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewSec1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth1.setBackgroundColor(getColor(R.color.grey));

                    binding.passLable1.setText(getResources().getString(R.string.very_week));
                    binding.passLable1.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.navy));
                    binding.passLable1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);

                } else if (s.toString().length() < 6) {

                    binding.viewFirst1.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewSec1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd1.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth1.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable1.setText(getResources().getString(R.string.week));
                    binding.passLable1.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.BrickRed));
                    binding.passLable1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);

                } else if (s.toString().length() < 8) {
                    binding.viewFirst1.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewSec1.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewThrd1.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewFourth1.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable1.setText(getResources().getString(R.string.so_so));
                    binding.passLable1.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.yellow));
                    binding.passLable1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                } else {
                    binding.viewFirst1.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewSec1.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewThrd1.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewFourth1.setBackgroundColor(getColor(R.color.dark_green));
                    binding.passLable1.setText(getResources().getString(R.string.great));
                    binding.passLable1.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.dark_green));
                    binding.passLable1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }*/

            }
        });
        binding.etConfirmPass.addTextChangedListener(new TextWatcher() {
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
                binding.viewDashLayout2.setVisibility(View.VISIBLE);
                binding.passLable2.setVisibility(View.VISIBLE);
                validationConPass(String.valueOf(s));

               /* if (s.toString().length() == 0) {
                    binding.viewDashLayout2.setVisibility(View.GONE);
                    binding.passLable2.setVisibility(View.GONE);
                    binding.viewFirst2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewSec2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth2.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable2.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.grey));

                } else if (s.toString().length() < 4) {
                    binding.viewFirst2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewSec2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth2.setBackgroundColor(getColor(R.color.grey));

                    binding.passLable2.setText(getResources().getString(R.string.very_week));
                    binding.passLable2.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.navy));
                    binding.passLable2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                } else if (s.toString().length() < 6) {

                    binding.viewFirst2.setBackgroundColor(getColor(R.color.BrickRed));
                    binding.viewSec2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewThrd2.setBackgroundColor(getColor(R.color.grey));
                    binding.viewFourth2.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable2.setText(getResources().getString(R.string.week));
                    binding.passLable2.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.BrickRed));
                    binding.passLable2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);

                } else if (s.toString().length() < 8) {
                    binding.viewFirst2.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewSec2.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewThrd2.setBackgroundColor(getColor(R.color.yellow));
                    binding.viewFourth2.setBackgroundColor(getColor(R.color.grey));
                    binding.passLable2.setText(getResources().getString(R.string.so_so));
                    binding.passLable2.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.yellow));
                    binding.passLable2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.snackbar_img, 0);
                } else {
                    binding.viewFirst2.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewSec2.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewThrd2.setBackgroundColor(getColor(R.color.dark_green));
                    binding.viewFourth2.setBackgroundColor(getColor(R.color.dark_green));
                    binding.passLable2.setText(getResources().getString(R.string.great));
                    binding.passLable2.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.dark_green));
                    binding.passLable2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }*/

            }
        });


    }

    private void validationConPass(String txt) {
        int count = 0;
        if (txt.length() >= 12) {
            count = count + 2;
        }

        if ((txt.length() >= 6) && (txt.length() < 12)) {
            count = count + 1;
        }

        if (txt.matches(lower)) {
            count = count + 1;
        }

        if (txt.matches(upper)) {
            count = count + 1;
        }

        if (txt.matches(spacialChar)) {
            count = count + 2;
        }

        if (txt.matches(number)) {
            count = count + 2;
        }

        if (count == 0) {
            binding.viewDashLayout.setVisibility(View.GONE);
            binding.passLable.setVisibility(View.GONE);
            binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.grey));
            str = "rejected";

//            Toast.makeText(RegistrationFirstActivity.this, "Default "+count, Toast.LENGTH_SHORT).show();
        } else if (count <= 3 && count > 0) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.week));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.BrickRed));
            str = "rejected";
//            Toast.makeText(RegistrationFirstActivity.this, "Weak "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 4) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
            binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.so_so));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.yellow));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "So-So "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 5 || count == 6) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.good));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.light_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 7) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFourth.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.verygood));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.light_green1));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Very Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 8) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFifth.setBackgroundColor(getColor(R.color.dark_green));
            binding.passLable.setText(getResources().getString(R.string.great));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.dark_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Great "+count, Toast.LENGTH_SHORT).show();
        }
    }

    private void validationNewPass(String txt) {

        int count = 0;
        if (txt.length() >= 12) {
            count = count + 2;
        }

        if ((txt.length() >= 6) && (txt.length() < 12)) {
            count = count + 1;
        }

        if (txt.matches(lower)) {
            count = count + 1;
        }

        if (txt.matches(upper)) {
            count = count + 1;
        }

        if (txt.matches(spacialChar)) {
            count = count + 2;
        }

        if (txt.matches(number)) {
            count = count + 2;
        }

        if (count == 0) {
            binding.viewDashLayout.setVisibility(View.GONE);
            binding.passLable.setVisibility(View.GONE);
            binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.grey));
            str = "rejected";

//            Toast.makeText(RegistrationFirstActivity.this, "Default "+count, Toast.LENGTH_SHORT).show();
        } else if (count <= 3 && count > 0) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.week));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.BrickRed));
            str = "rejected";
//            Toast.makeText(RegistrationFirstActivity.this, "Weak "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 4) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
            binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.so_so));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.yellow));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "So-So "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 5 || count == 6) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.good));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.light_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 7) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFourth.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.verygood));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.light_green1));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Very Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 8) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFifth.setBackgroundColor(getColor(R.color.dark_green));
            binding.passLable.setText(getResources().getString(R.string.great));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.dark_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Great "+count, Toast.LENGTH_SHORT).show();
        }

    }

    private void validation(String txt) {
        int count = 0;
        if (txt.length() >= 12) {
            count = count + 2;
        }

        if ((txt.length() >= 6) && (txt.length() < 12)) {
            count = count + 1;
        }

        if (txt.matches(lower)) {
            count = count + 1;
        }

        if (txt.matches(upper)) {
            count = count + 1;
        }

        if (txt.matches(spacialChar)) {
            count = count + 2;
        }

        if (txt.matches(number)) {
            count = count + 2;
        }

        if (count == 0) {
            binding.viewDashLayout.setVisibility(View.GONE);
            binding.passLable.setVisibility(View.GONE);
            binding.viewFirst.setBackgroundColor(getColor(R.color.grey));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.grey));
            str = "rejected";

//            Toast.makeText(RegistrationFirstActivity.this, "Default "+count, Toast.LENGTH_SHORT).show();
        } else if (count <= 3 && count > 0) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.BrickRed));
            binding.viewSec.setBackgroundColor(getColor(R.color.grey));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.week));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.BrickRed));
            str = "rejected";
//            Toast.makeText(RegistrationFirstActivity.this, "Weak "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 4) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.yellow));
            binding.viewSec.setBackgroundColor(getColor(R.color.yellow));
            binding.viewThrd.setBackgroundColor(getColor(R.color.grey));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.so_so));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.yellow));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "So-So "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 5 || count == 6) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.grey));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.good));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.light_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 7) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewSec.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewThrd.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFourth.setBackgroundColor(getColor(R.color.light_green1));
            binding.viewFifth.setBackgroundColor(getColor(R.color.grey));
            binding.passLable.setText(getResources().getString(R.string.verygood));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.light_green1));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Very Good "+count, Toast.LENGTH_SHORT).show();
        } else if (count == 8) {
            binding.viewFirst.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewSec.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewThrd.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFourth.setBackgroundColor(getColor(R.color.dark_green));
            binding.viewFifth.setBackgroundColor(getColor(R.color.dark_green));
            binding.passLable.setText(getResources().getString(R.string.great));
            binding.passLable.setTextColor(ContextCompat.getColor(AccountSettingActivity.this, R.color.dark_green));
            str = "accept";
//            Toast.makeText(RegistrationFirstActivity.this, "Great "+count, Toast.LENGTH_SHORT).show();
        }
    }

    private void openPingenerateState() {

        if (userAccountDetailsModel.getRegisteredPIN1().equals(binding.txtPinEntry.getText().toString())) {
            AppUtility.closeKeyboard(this);
            binding.layoutForView.setVisibility(View.VISIBLE);
            binding.pinViewLayout.setVisibility(View.GONE);
            binding.txtPinEntry.setText("");
            counterForBio = 0;
            createCounterTimer();

        } else {
            binding.txtPinEntry.setText("");

            showErrorMsg(getResources().getString(R.string.pin_error_not_match2), "");
            binding.loginBtn.setVisibility(View.VISIBLE);

        }
    }

    private void userchangepass() {
        AppUtility.updateScreeState("SettingsActivity", userAccountDetailsModel);
        Intent intent = new Intent(AccountSettingActivity.this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
//        if (CHECKSTATE) {
//            if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                    userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                    && MyDigitalClockForScreenLock.getInstance().getMyTimerTime() >= 60) {
//                MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
//                MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//                if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))
//                    startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));          this.finish();
//                else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
//                    binding.txtPinEntry.requestFocus();
//                    binding.pinViewLayout.setVisibility(View.VISIBLE);
//                    binding.layoutForView.setVisibility(View.GONE);
//                }
//                //   startActivity(new Intent(this, OpenPinActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        }
        super.onResume();

    }

    @Override
    protected void onPause() {
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
//            MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//            MyDigitalClockForScreenLock.getInstance().startTimerCounting();
//        }
//        CHECKSTATE = true;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onDestroy();
//        if (userAccountDetailsModel.getIdMethod() != null && (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)
//                || userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)))
//            MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            Intent intent = new Intent(AccountSettingActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(intent);
            finish();
        }
    }

    public void onClickAc(View view) {
        switch (view.getId()) {
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.back_button1:
                Intent intent = new Intent(AccountSettingActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
                break;
            case R.id.btn_ac:
                usernewaccount();
                break;
        }
    }

    private void usernewaccount() {
        if (ValidationCtrlr.getValidatInstance().emailValidator(binding.editTextEmail.getText().toString().trim())) {
         /*   if (!str.isEmpty() && !str.equalsIgnoreCase("rejected")
                    && userAccountDetailsModel.getPassword() != null && !userAccountDetailsModel.getPassword().isEmpty()) {
                AccountModel accountModel = new AccountModel(
                        userAccountDetailsModel.getUuId(),
                        binding.etOldPass.getText().toString().trim(),
                        binding.etNewPass.getText().toString().trim());
                accountViewModel.userchangepass(accountModel);


            } else {
                showErrorMsg(this.getResources().getString(R.string.in_case_you_do_not_remember_the_password_you_can_ask_for_a_new_one));

            }*/
            if (!binding.etOldPass.getText().toString().trim().isEmpty() && !str.isEmpty() && !str.equalsIgnoreCase("rejected")
                    && userAccountDetailsModel.getPassword() != null && !userAccountDetailsModel.getPassword().isEmpty()
                    && userAccountDetailsModel.getPassword().equals(binding.etOldPass.getText().toString().trim())) {
//                if (ValidationCtrlr.getValidatInstance().passawardaValid(binding.etNewPass.getText().toString().trim()))
//                if (ValidationCtrlr.getValidatInstance().passawardaValid(binding.etNewPass.getText().toString().trim())) {

                if (binding.etConfirmPass.getText().toString().equals(binding.etNewPass.getText().toString().trim())) {
                    AccountModel accountModel = new AccountModel(
                            userAccountDetailsModel.getUuId(),
                            binding.etOldPass.getText().toString().trim(),
                            binding.etNewPass.getText().toString().trim());
                    if (AppUtility.isNetworkConnected()) {
                        AppUtility.showProgressBaForRelLay(binding.layoutAc, this);
                        accountViewModel.userchangepass(accountModel);
                    } else {
                        showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));

                    }
//                        else {showErrorMsg
//                            showErrorMsg(this.getResources().getString(R.string.in_case_you_do_not_remember_the_password_you_can_ask_for_a_new_one));
//                        }
                } else {
                    showErrorMsg(AppConstant.changepass, "");
                }

            } else {
                showErrorMsg(this.getResources().getString(R.string.in_case_you_do_not_remember_the_password_you_can_ask_for_a_new_one), "");
            }
        } else {
//                showErrorMsg(AppConstant.newpass);
            showErrorMsg(this.getResources().getString(R.string.wrong_email_address), "");

//                showErrorMsg(AppConstant.changepass);
        }

    }
    /* else {
//            showErrorMsg(AppConstant.changepass);

//                showErrorMsg(this.getResources().getString(R.string.in_case_you_do_not_remember_the_password_you_can_ask_for_a_new_one));

        }

    */


    private void showErrorMsg(String msg, String titleHead) {
//        AppUtility.isDisplaySnacker(binding.layoutAc, this,
//                msg);
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

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
                        binding.pinViewLayout.setVisibility(View.GONE);
                        binding.layoutForView.setVisibility(View.VISIBLE);
                        binding.bioLayout.setVisibility(View.GONE);
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

    private void onTouchIdClick() {
        Log.e("MyTest", "onTouchIdClick");
        if (counterForBio <= 2 && isBiometricCompatibleDevice())  {
            displayBiometricButton();
        } else {
            pinViewStates();

        }

    }

    private void pinViewStates() {
        binding.txtPinEntry.requestFocus();
        binding.pinViewLayout.setVisibility(View.VISIBLE);
        ScreenDetailsModel screenDetailsModel = AppUtility.setOpenAppPin(this);
        if (screenDetailsModel != null) {
            if (screenDetailsModel.getTitle() != null && !screenDetailsModel.getTitle().isEmpty()) {
                binding.txtHeading.setText(screenDetailsModel.getTitle());
            } else {
                binding.txtHeading.setText(getResources().getString(R.string.open_pin_txt));
            }
            if (screenDetailsModel.getDiscription() != null && !screenDetailsModel.getDiscription().isEmpty()) {
                binding.pinTxt.setText(screenDetailsModel.getDiscription());
            } else {
                binding.pinTxt.setText(getResources().getString(R.string.open_pin_purpose));
            }
        }
        binding.layoutForView.setVisibility(View.GONE);
        binding.bioLayout.setVisibility(View.GONE);
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
                binding.pinViewLayout.setVisibility(View.GONE);
                binding.layoutForView.setVisibility(View.GONE);
                binding.bioLayout.setVisibility(View.VISIBLE);
                onTouchIdClick();
            } else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
                pinViewStates();
            }
        }
    }

}