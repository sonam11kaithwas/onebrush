package com.advantal.onebrush.utilies_pkg;

import com.advantal.onebrush.R;

/**
 * Created by Sonam on 10-02-2022 12:37.
 */
public class AppConstant {
    public static final String termsconditionURL = "http://36.255.3.15/Onebrush/onebrush_tnc.pdf";
    public static final String privacypolicyURL = "http://36.255.3.15/Onebrush/onebrush_privacypolicy.pdf";
    public static final String WELCAROUSEE = "mycrasual.json";
    public static final String KEY_NAME = "KeyName";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String FORWARD_SLASH = "/";
    public static long interval = 1 * 1000;
    public static long startTime = 1 * 60 * 1000; // 1 MINS IDLE TIME
    public static String street = "Please Enter street";
    public static String zipCode = "Please Enter Zipcode";
    public static String city = "Please Enter City";
    public static String country = "Please Enter Country";
    public static String name = "Please Enter Name";
    public static String serName = "Please Enter Surname";
    public static String firstName = "Please Enter FirstName";
    public static String feedback = "Please Enter FeedBack";
    public static String termscondition = "Please allow terms and conditions";
    public static String gendercondition = "Please select gender";
    public static String langcondition = "Please select language";
    public static String changepass = "Your old password is not matched! please check.";
    public static String newpass = "Please Enter Valid Password";
    //    public static String ;
    public static String deviceId = "";
    public static int PHONE_NUM_MAX_LIMIT = 12;
    public static int PHONE_NUM_MIN_LIMIT = 4;
    public static String[] cameraPermissions = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"//"android.permission.CAMERA",
    };
    public static String[] galleryPermissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    public static String ID_MENTHOD_PIN = "PIN";
    public static String ID_MENTHOD_Bio = "Biometric";
    public static String DATE_TIME_Frmt = "dd.MM.yyyy kk:mm:ss";
    public static String DATE_Frmt = "yyyy-MM-dd";//"yyyy-mm-dd",
    public static String[] temptxt_contains = {
            OneBrushApp.getInstance().getResources().getString(R.string.onebrush_is_your_companion_for_better_oral_health),
            OneBrushApp.getInstance().getResources().getString(R.string.you_can_use_this_app_to_receive_a_remote_diagnosis_from_our_dentist),
            OneBrushApp.getInstance().getResources().getString(R.string.purchase),
            OneBrushApp.getInstance().getResources().getString(R.string.how_to_use)
    };
    public static String[] tempHead_contains = {
            OneBrushApp.getInstance().getResources().getString(R.string.dentinostic_by_onebrush),
            "", "", ""
    };


    public static String ERRORAPPREARMSG = "";
}
