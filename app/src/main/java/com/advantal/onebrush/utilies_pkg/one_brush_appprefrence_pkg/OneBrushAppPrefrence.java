package com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.WelcomeCarouselModel;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.syncronize_pkg.SyncronizeModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sonam on 25-02-2022 15:44.
 */

public class OneBrushAppPrefrence implements Sp_Model {
    private static final Sp_Model INSTANCE = new OneBrushAppPrefrence();
    //   *********** setup preferences ********
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;
    private final String PREF_NAME = "onebrush_pref";
    private final String FIRSTREGSELECTED = "first_reg_selected";
    private final String WELCOME_CAROUSELS = "welcome_carousels";
    private final String SCREEN_DETAILS_DATA = "screen_details_data";
    private final String PREFEREDLANG = "preferedlang";
    private final String CASEID = "caseid";
    private final String USERID = "userid";
    private final String OLDUSERID = "olduserid";
    private final String SELECTEDCASEID = "selectedcaseid";
    private final String LASTLAUNCHTIME = "lastlaunchtime";
    private final String PATIENTID = "patientid";
    private final String SELECTED_PATIENT_ID = "seleceted_patient_id";
    private final String SYNCRONIEDDATA = "syncronized_data";
    private final String FORSIGNUP = "forsignup";
    private final String PREF_COOKIES = "pref_cookies";

    public OneBrushAppPrefrence() {
        sp = OneBrushApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static Sp_Model getSharedprefInstance() {
        return INSTANCE;
    }

    @Override
    public boolean getsignIn() {
        return sp.getBoolean(FORSIGNUP, false);
    }

    @Override
    public void setsignIn(boolean enable) {
        editor.putBoolean(FORSIGNUP, enable);
        editor.commit();

    }

    @Override
    public Set<String> getHeadersSet(Set<String> objects) {
        return sp.getStringSet(PREF_COOKIES, objects);
    }

    @Override
    public void setHeadersSet(HashSet<String> cookies) {
        editor.putStringSet(PREF_COOKIES, cookies);
        editor.commit();
    }
    @Override
    public boolean getFirstRegistrationCompleted() {
        return sp.getBoolean(FIRSTREGSELECTED, false);
    }


    @Override
    public void setFirstRegistrationCompleted(boolean enable) {
        editor.putBoolean(FIRSTREGSELECTED, enable);
        editor.commit();
    }

    @Override
    public String getLastLaunchTime() {
        Log.e("MyDate-----", AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        return sp.getString(LASTLAUNCHTIME, AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
    }

    @Override
    public void setLastLaunchTime(String launchTime) {
        editor.putString(LASTLAUNCHTIME, launchTime);
        editor.commit();
    }

    @Override
    public void clearSharedPreference() {
        editor.clear();
        editor.commit();
    }

    @Override
    public ScreenDetailsModel getScreenDetailsdData(String screenContent) {
        try {
            String str = sp.getString(SCREEN_DETAILS_DATA, "");
            Type queDataListType = new TypeToken<List<ScreenDetailsModel>>() {
            }.getType();
            List<ScreenDetailsModel> questionModelList = new Gson().fromJson(str, queDataListType);
            if (questionModelList != null) {
                for (ScreenDetailsModel model : questionModelList) {
                    if (model.getScreenName() != null && model.getScreenName().equalsIgnoreCase(screenContent)) {
                        return model;
                    }
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new ScreenDetailsModel();
    }

    @Override
    public void setScreenDetailsdData(String syncronizedData) {
        editor.putString(SCREEN_DETAILS_DATA, syncronizedData);
        editor.commit();
    }

    @Override
    public String getSelectedUserId() {
        return sp.getString(USERID, "1");
    }

    @Override
    public void setSelectedUserId(String userId) {
        editor.putString(USERID, userId);
        editor.commit();
    }

    @Override
    public String getOLDSelectedUserId() {
        return sp.getString(OLDUSERID, "1");
    }

    @Override
    public void setOLDSelectedUserId(String oldUserId) {
        editor.putString(OLDUSERID, oldUserId);
        editor.commit();
    }


    @Override
    public int getSelectedPatientId() {
        return sp.getInt(SELECTED_PATIENT_ID, 0);
    }

    @Override
    public void setSelectedPatientId(int patientId) {
        editor.putInt(SELECTED_PATIENT_ID, patientId);
        editor.commit();
    }

    @Override
    public int getCaseId() {
        return sp.getInt(CASEID, 0);
    }

    @Override
    public void setCaseId(int caseId) {
        editor.putInt(CASEID, caseId);
        editor.commit();
    }

    @Override
    public int getSelectedCaseId() {
        return sp.getInt(SELECTEDCASEID, 1);
    }

    @Override
    public void setSelectedCaseId(int caseId) {
        editor.putInt(SELECTEDCASEID, caseId);
        editor.commit();
    }

    @Override
    public void setpatientId(int patientId) {
        editor.putInt(PATIENTID, patientId);
        editor.commit();
    }

    @Override
    public int getpatient() {
        return sp.getInt(PATIENTID, 1);

    }

    @Override
    public String getPreferredLanguage() {
        return sp.getString(PREFEREDLANG, "en");
    }

    @Override
    public void setPreferredLanguage(String preferredLanguage) {
        editor.putString(PREFEREDLANG, preferredLanguage);
        editor.commit();
    }

    @Override
    public SyncronizeModel getSyncronizedData() {
        SyncronizeModel responce = new Gson().fromJson(sp.getString(SYNCRONIEDDATA, null), SyncronizeModel.class);
        return responce;
    }

    @Override
    public void setSyncronizedData(String syncronizedData) {
        editor.putString(SYNCRONIEDDATA, syncronizedData);
        editor.commit();
    }

    @Override
    public List<WelcomeCarouselModel> getWelcomeCarousels() {
        String convert = sp.getString(WELCOME_CAROUSELS, "");
        Type listType = new TypeToken<List<WelcomeCarouselModel>>() {
        }.getType();
        ArrayList<WelcomeCarouselModel> data = new Gson().fromJson(convert, listType);
        return data;
    }

    @Override
    public void setWelcomeCarousels(String crausal) {
        editor.putString(WELCOME_CAROUSELS, crausal);
        editor.commit();
    }


}
