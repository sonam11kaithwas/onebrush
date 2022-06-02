package com.advantal.onebrush.utilies_pkg;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.fingerprint.FingerprintManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.denti_qus_pkg.Dentinostic_Qus_Ans_Activity;
import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.MultiplePhotoActivity;
import com.advantal.onebrush.denti_qus_pkg.open_camera_pkg.Denti_Camera_Activity;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintCnfrmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.DentiWclScrActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticDashboardActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.NewCaseActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.PatientListActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.CaseInformationActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.case_picture_pkg.PictureActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.QuestionsActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.diagnostic_pkg.DiagnosisActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.invoice_pkg.InvoiceActivity;
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
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class AppUtility {
    private static final int UPLOAD_FILE = 1;
    private static Snackbar snackbar;
    private static TextView textView;
    private static ProgressBar progressBar;

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    /*resize image*/
    public static File scaleToActualAspectRatio(String captureImagePath, Float maxHeight, Float maxWidth) {
        try {

            Bitmap bitmap = BitmapFactory.decodeFile(captureImagePath);


            if (bitmap != null) {
                try {
                    float actualHeight = bitmap.getHeight();
                    float actualWidth = bitmap.getWidth();
// float maxHeight = 1024.0f;
// float maxWidth = 1024.0f;
                    float imgRatio = actualWidth / actualHeight;
                    float maxRatio = maxWidth / maxHeight;
                    if (actualHeight > maxHeight || actualWidth > maxWidth) {
                        if (imgRatio < maxRatio) {
//adjust width according to maxHeight
                            imgRatio = maxHeight / actualHeight;
                            actualWidth = imgRatio * actualWidth;
                            actualHeight = maxHeight;
                        } else if (imgRatio > maxRatio) {
//adjust height according to maxWidth
                            imgRatio = maxWidth / actualWidth;
                            actualHeight = imgRatio * actualHeight;
                            actualWidth = maxWidth;
                        } else {
                            actualHeight = maxHeight;
                            actualWidth = maxWidth;
                        }
                    }


                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) actualWidth, (int) actualHeight, true);
                    if (!captureImagePath.equals("")) {
                        ExifInterface exif = new ExifInterface(captureImagePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();

                        switch (orientation) {
                            case 6:
                                matrix.postRotate(90f);
                                break;

                            case 3:
                                matrix.postRotate(180f);
                                break;
                            case 8:
                                matrix.postRotate(270f);
                                break;
                        }

                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) actualWidth, (int) actualHeight, matrix, true);

                        try {
                            FileOutputStream out = new FileOutputStream(captureImagePath);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(captureImagePath);
    }


    public static String getDateByFormat(String format) { //get  date time
        SimpleDateFormat gettingfmt = new SimpleDateFormat(format, Locale.getDefault());
        String formated = gettingfmt.format(Calendar.getInstance().getTime());
        return formated;
    }

    public static void alertDialog(Context context, String message, String dai_title_txt,
                                   final Callable<Boolean> function) {
        try {
            TextView dialog_msg, dai_title;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
            alertDialog.setView(customLayout);
            alertDialog.setCancelable(false);

            dialog_msg = customLayout.findViewById(R.id.dia_msg);
            dai_title = customLayout.findViewById(R.id.dai_title);
            if (!dai_title_txt.isEmpty()) {
                dai_title.setText(dai_title_txt);
            }
            dialog_msg.setText(message);

            alertDialog.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        function.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
//            OneBrushApp.getInstance().showToastmsg("Something went wrong here.");
        }
    }


    public static void alertDialogForTwoEvent(Context context, String message,//String posTxt,String negTxt
                                              final Callable<Boolean> function, Callable<Boolean> forNegativeEvent) {
        try {

            TextView dialog_msg;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();// (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customLayout = inflater.inflate(R.layout.dialog_layout, null);
            alertDialog.setView(customLayout);
            alertDialog.setCancelable(false);

            dialog_msg = customLayout.findViewById(R.id.dia_msg);

            dialog_msg.setText(message);

            alertDialog.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        function.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            alertDialog.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            OneBrushApp.getInstance().showToastmsg("Something went wrong here.");
        }
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) OneBrushApp.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static Boolean fingerPrintAvableOrNot(Context context) {
        boolean FINGREALLOWORNOT = false;

        try {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                FINGREALLOWORNOT = false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                FINGREALLOWORNOT = false;
            } else {
                Log.e("", "");
                FINGREALLOWORNOT = true;
                // Everything is ready for fingerprint authentication
            }
            return FINGREALLOWORNOT;
        } catch (Exception e) {
            e.printStackTrace();
            FINGREALLOWORNOT = false;

        }
        return FINGREALLOWORNOT;

    }


    public static boolean askGalaryTakeImagePermiSsion(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (verifyPermissions(AppConstant.galleryPermissions)) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context, AppConstant.galleryPermissions, 1);
            }

        } else {
            return true;
        }
        return false;
    }

    public static boolean verifyPermissions(String[] grantResults) {
        for (String result : grantResults) {
            if (ActivityCompat.checkSelfPermission(OneBrushApp.getInstance(), result) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean askStoragePerMission(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, UPLOAD_FILE);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return true;

            }
        } else {
            return true;
        }
        return false;

    }

    /*show snackbar*/
    public static void isDisplaySnacker(View view, Context context, String string) {
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View snackView = inflater.inflate(R.layout.snackbar_layout, null);
        textView = snackView.findViewById(R.id.textView1);
        textView.setText(string);
        snackbar.getView().setBackground(ContextCompat.getDrawable(OneBrushApp.getInstance(), R.drawable.contianer_snackbar));
        snackbar.getView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
        parentParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        snackBarView.setLayoutParams(parentParams);
        parentParams.gravity = Gravity.TOP;
        parentParams.setMargins(0, 0, 0, 0);
        snackBarView.addView(snackView, 0);
        snackbar.show();
    }

    public static JsonObject getJsonObject(String params) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(params).getAsJsonObject();
        return obj;
    }


    public static String getCurrentDateTime(String formate) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(formate, Locale.getDefault());
        String formated = gettingfmt.format(Calendar.getInstance().getTime());
        return formated;
    }


    public static String getTImeFormate(String formate) {
        SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format1.parse(formate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(format2.format(date));
        return format2.format(date);
    }

    public static void closeKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void showProgressBaForRelLay(RelativeLayout layout, Context context) {
        try {

            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            progressBar.setClickable(false);

            layout.addView(progressBar, params);
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean askCameraTakePicture(Context context) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            if (verifyPermissions(AppConstant.cameraPermissions)) {

                return true;
            } else {

                ActivityCompat.requestPermissions((Activity) context, AppConstant.cameraPermissions, 1);
            }
        } else {

            return true;
        }


        return false;
    }

    public static void hideProgressBar(Context context) {
        try {
            if (progressBar != null) {

                /*******************Enable toucha on input field*****************/
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            /*******************Enable toucha on input field*****************/
            ((Activity) context).
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            e.printStackTrace();
        }
    }

    public static ScreenDetailsModel setOpenAppPin(Context context) {
        ScreenDetailsModel screenDetailsModel = new ScreenDetailsModel();
        screenDetailsModel = OneBrushAppPrefrence.getSharedprefInstance().getScreenDetailsdData("OPEN_PIN");
        return screenDetailsModel;
    }

    public static Class getLastStateForLaunch(UserAccountDetailsModel userAccountDetailsModel) {
        String com_approval = userAccountDetailsModel.getLastScreen();
        Log.e("sona", userAccountDetailsModel.getLastScreen());

        switch (com_approval) {
            case "SettingsActivity":
                return SettingsActivity.class;
            case "TermsConditionActivity":
                return TermsConditionActivity.class;
            case "PrivacyPolicyActivity":
                return PrivacyPolicyActivity.class;

            case "CaseInformationActivity":
                return CaseInformationActivity.class;
            case "ContinueWithPaymentActivity":
                return ContinueWithPaymentActivity.class;
            case "PaymentAcceptActivity":
                return PaymentAcceptActivity.class;
            case "DonePaymentActivity":
                return DonePaymentActivity.class;
            case "Term_Condition_View_Activity":
                return Term_Condition_View_Activity.class;
            case "PinGenerateActivity":
                return PinGenerateActivity.class;
            case "PinConfirmActivity":
                return PinConfirmActivity.class;
            case "OpenPinActivity":
                return OpenPinActivity.class;
            case "FingerPrintActivity":
                return FingerPrintActivity.class;
            case "FingerPrintCnfrmActivity":
                return FingerPrintCnfrmActivity.class;
            case "DentinosticDashboardActivity":
                return DentinosticDashboardActivity.class;
            case "NewCaseActivity":
                return NewCaseActivity.class;

            case "RegistrationFirstActivity":
                return RegistrationFirstActivity.class;
            case "Registr_NextActivity":
                return Registr_NextActivity.class;
            case "SaveContinueActivity":
                return SaveContinueActivity.class;
            case "LoginActivity":
                return LoginActivity.class;
            case "Personal_InfoActivity":
                return Personal_InfoActivity.class;
            case "Dentinostic_Qus_Ans_Activity":
                return Dentinostic_Qus_Ans_Activity.class;
            case "PatientListActivity":
                return PatientListActivity.class;
            case "AddPatientActivity":
                return AddPatientActivity.class;
            case "Denti_Camera_Activity":
                return Denti_Camera_Activity.class;
            case "MultiplePhotoActivity":
                return MultiplePhotoActivity.class;
            case "DiagnosisActivity":
                return DiagnosisActivity.class;
            case "InvoiceActivity":
                return InvoiceActivity.class;
            case "QuestionsActivity":
                return QuestionsActivity.class;
            case "PictureActivity":
                return PictureActivity.class;
            case "AccountSettingActivity":
                return AccountSettingActivity.class;
            case "FeedBackActivity":
                return FeedBackActivity.class;
            case "Forgot_Password_Activity":
                return Forgot_Password_Activity.class;


            default:
                return DentiWclScrActivity.class;
        }
    }

    public static boolean getCompareTwoTime(String lastSyncDateTimeeeee) {
        String lastSyncDateTimes = OneBrushAppPrefrence.getSharedprefInstance().getLastLaunchTime();//12.05.2022 15:42:55
//        lastSyncDateTime=lastSyncDateTimenotuse;
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstant.DATE_TIME_Frmt);

        Date d = null;
        try {
            d = sdf.parse(lastSyncDateTimes);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        String currentDateTime = sdf.format(cal.getTime());
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 1);
        String newLastSyncDateTime = sdf.format(cal.getTime());

        try {
            if (sdf.parse(newLastSyncDateTime).before(sdf.parse(currentDateTime))) {
                return true;
            }
            System.out.println("DATE______-------------" + sdf.parse(newLastSyncDateTime).before(sdf.parse(currentDateTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateScreeState(String state, UserAccountDetailsModel userAccountDetailsModel) {
        userAccountDetailsModel.setLastScreen(state);
        userAccountDetailsModel.setLastAction(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(userAccountDetailsModel);
    }

    public static void updateScreeStateTime(UserAccountDetailsModel userAccountDetailsModel) {
        userAccountDetailsModel.setLastAction(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().updateAccountDetails(userAccountDetailsModel);
    }

    public static void clearAllDataBase() {
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().delete();
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().delete();
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().delete();
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).answerLogDataDao().delete();
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).patientDao().delete();
    }

    public static boolean showSnackbar() {
        if (snackbar != null) {
            return !snackbar.isShown();
        }
        return false;

    }

    public static void setupUI(View view, final Context context) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard((Activity) context);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, context);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("TAG :", e.getMessage());
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static void timeCounterReset(MyCountDownTimer countDownTimer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    public static void timeCounterStop(MyCountDownTimer countDownTimer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
