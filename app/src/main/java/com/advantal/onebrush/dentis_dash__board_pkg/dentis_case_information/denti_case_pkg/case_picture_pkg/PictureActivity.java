package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.case_picture_pkg;

import static com.advantal.onebrush.utilies_pkg.AppConstant.FORWARD_SLASH;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityPictureBinding;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.CaseInformationActivity;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.bumptech.glide.Glide;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by Sonam on 12-05-2022 14:13.
 */
public class PictureActivity extends AppCompatActivity {
    LinearLayout dot_layout;
    List<XRayImageListModl> galleryImageList = new ArrayList<>();
    TextView tv_name, tv_date, tv_status;
    ImageView expandedImageView, chat_im_zoom;
    LinearLayout expanded_image_bg;
//    FrameLayout container;
    RelativeLayout container;
    private UserAccountDetailsModel userAccountDetailsModel;
    //    int[] images = {R.drawable.gallery, R.drawable.gallery, R.drawable.gallery, R.drawable.gallery};
    private ViewPager view_pager_for_attchment;
    private IndicatorAdapter indicatorAdapter;
    private ImageView[] dots;
    //    private int selectedPosition = 1;
    private Dialog enterFieldDialog;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private PictureAdapter pictureAdapter;
    private List<AnswerLogDataModel> allQuesAnswerList = new ArrayList<>();
    private AnswerLogDataModel answerLogDataModel;
    private ActivityPictureBinding binding;
    private MyCountDownTimer countDownTimer;
    private int counterForBio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picture);

        CasePictureViewModel caseViewModel = ViewModelProviders.of(this)
                .get(CasePictureViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture);
        binding.setLifecycleOwner(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        createCounterTimer();


//        galleryImageList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).xRayImageDao().getAllXRaysListByCaseId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());
        galleryImageList.add(0, new XRayImageListModl("", 0, OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId()));
        galleryImageList.add(1, new XRayImageListModl("", 0, OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId()));


        AppUtility.updateScreeState("PictureActivity", userAccountDetailsModel);
        dot_layout = findViewById(R.id.dot_layout);
        container = findViewById(R.id.layout);
        view_pager_for_attchment = findViewById(R.id.view_pager_for_attchment);
        tv_name = findViewById(R.id.tv_patient_name);
        tv_date = findViewById(R.id.tv_patient_dentist);
        tv_status = findViewById(R.id.tv_patient_status);
        //        img_profilePic = findViewById(R.id.img_profilePic);
        //        img_profile = findViewById(R.id.img_profile_Pic);
        //        img_profilePic_delete = findViewById(R.id.img_profilePic_delete);


        allQuesAnswerList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().
                getAllQuesAnswerListByCaseId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());


        if (allQuesAnswerList != null && allQuesAnswerList.size() > 1) {
            answerLogDataModel = allQuesAnswerList.get(allQuesAnswerList.size() - 1);
            binding.tvPatientName.setText(answerLogDataModel.getPatientNm() != null &&
                    !answerLogDataModel.getPatientNm().isEmpty() ? answerLogDataModel.getPatientNm() : userAccountDetailsModel.getName() + " " + userAccountDetailsModel.getSurname());
            binding.tvPatientRegis.setText(answerLogDataModel.getCreatedDate());
            if (answerLogDataModel.getCaseId() == 1) {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " st request");

            } else if (answerLogDataModel.getCaseId() == 2) {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " nd request");
            } else if (answerLogDataModel.getCaseId() == 3) {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " rd request");
            } else {
                binding.tvPatientReq.setText("Your " + answerLogDataModel.getCaseId() + " th request");
            }
            binding.tvPatientStatus.setText(answerLogDataModel.getStatus());
            binding.tvPatientStatus.setTextColor(Color.parseColor("#ffa600"));

            binding.tvPatientDentist.setText(answerLogDataModel.getDentist() != null && !answerLogDataModel.getDentist().isEmpty() ? answerLogDataModel.getDentist() : "Dr. Dentina");

        }


//        pictureAdapter = new PictureAdapter(this, galleryImageList,
//                new PictureAdapter.openGalleryImage() {
//                    @Override
//                    public void setGalaryPicImg(View thumbView, Drawable bmp, String img_url) {
//                        getChatImageZoomDialog(thumbView, bmp, img_url);
//                    }
//                });


        indicatorAdapter = new IndicatorAdapter(this, galleryImageList, new IndicatorAdapter.openGalleryImage2() {
            @Override
            public void setGalaryPicImg(View thumbView, Drawable bmp, String img_url) {
                getChatImageZoomDialog(thumbView, bmp, img_url);
            }
        });
        view_pager_for_attchment.setAdapter(indicatorAdapter);
//        dots = new ImageView[indicatorAdapter.getCount()];
        dots = new ImageView[2];
        dots();
        view_pager_for_attchment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedViewSet(position);
            }

            @Override
            public void onPageSelected(int position) {
              /*  selectedPosition = position + 1;
                Log.e("pos", "" + selectedPosition + "");
*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("state", "" + state + "");
                //                selectedPosition=state;
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

    private void showErrorMsg(String msg, String titleHead) {
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });

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

    @Override
    protected void onStop() {
        Log.e("Last Date", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        if (countDownTimer != null)
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        super.onStop();
    }

    private void getChatImageZoomDialog(View thumbView, Drawable bmp, String img_url) {

        enterFieldDialog = new Dialog(PictureActivity.this);
        enterFieldDialog.setCancelable(false);
        enterFieldDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        enterFieldDialog.setContentView(R.layout.gallery_full_screen);

        Window window = enterFieldDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);

        expanded_image_bg = enterFieldDialog.findViewById(R.id.expanded_image_bg);
        expandedImageView = enterFieldDialog.findViewById(R.id.expanded_image);
        chat_im_zoom = enterFieldDialog.findViewById(R.id.chat_im_zoom);
        enterFieldDialog.show();
        zoomImageFromThumb(thumbView, bmp, img_url);

    }

    private void zoomImageFromThumb(View thumbView, Drawable bmp, String img_url) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        Glide.with(this).load(img_url)
                .placeholder(bmp)
                .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//220
                .into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        container
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expanded_image_bg.setVisibility(View.VISIBLE);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                    //enterFieldDialog.dismiss();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expanded_image_bg.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        enterFieldDialog.dismiss();//dialog close
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expanded_image_bg.setVisibility(View.GONE);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });


        chat_im_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbView.setAlpha(1f);
                expanded_image_bg.setVisibility(View.GONE);
                expandedImageView.setVisibility(View.GONE);
                mCurrentAnimator = null;
                enterFieldDialog.dismiss();
            }
        });
    }

    public void dots() {
//        for (int i = 0; i < indicatorAdapter.getCount(); i++) {
        for (int i = 0; i < 2; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.tab_deselector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);

            dot_layout.addView(dots[i], params);

        }
        //        if (dots!=null&&dots.length>0)
        dots[0].setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.selected_dot));
    }


    private void selectedViewSet(int position) {
        for (int i = 0; i < 2; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.tab_deselector));
        }
        try {
            dots[position].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.selected_dot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoginForUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    public void onClickHandler(View view) {
        switch (view.getId()) {
            case R.id.cross_img_bio:
                Log.e("", "");
                this.finish();
                break;
            case R.id.cross_img_for_pin:
                this.finish();
                break;
            case R.id.login_btn:OneBrushAppPrefrence.getSharedprefInstance().setsignIn(false);
                startLoginForUser();
                break;
            case R.id.back_btn_pic:
                PictureActivity.this.onBackPressed();
            case R.id.iv_cross:
                this.finish();

        }
    }

    public void onBackPressed() {
        if (binding.layoutForView.getVisibility() == View.GONE)
            this.finish();
        else {
            Intent i = new Intent(PictureActivity.this, CaseInformationActivity.class);
            startActivity(i);
            this.finish();

        }
        //Do whatever custom stuff you wish here
    }
}
