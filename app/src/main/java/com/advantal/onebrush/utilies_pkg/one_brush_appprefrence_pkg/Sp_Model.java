package com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg;

import com.advantal.onebrush.dentis_dash__board_pkg.ScreenDetailsModel;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.WelcomeCarouselModel;
import com.advantal.onebrush.utilies_pkg.syncronize_pkg.SyncronizeModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sonam on 25-02-2022 15:45.
 */
public interface Sp_Model {
    List<WelcomeCarouselModel> getWelcomeCarousels();

    void setWelcomeCarousels(String Crausal);

    String getPreferredLanguage();

    void setPreferredLanguage(String preferredLanguage);

    SyncronizeModel getSyncronizedData();

    void setSyncronizedData(String syncronizedData);

    ScreenDetailsModel getScreenDetailsdData(String pos);

    void setScreenDetailsdData(String syncronizedData);

    int getCaseId();

    void setCaseId(int caseId);

    void setpatientId(int patientId);

    int getpatient();

    int getSelectedPatientId();

    void setSelectedPatientId(int patientId);

    int getSelectedCaseId();

    void setSelectedCaseId(int caseId);

    String getSelectedUserId();

    void setSelectedUserId(String caseId);

    String getOLDSelectedUserId();

    void setOLDSelectedUserId(String caseId);

    String getLastLaunchTime();

    void setLastLaunchTime(String caseId);

    void clearSharedPreference();

    boolean getFirstRegistrationCompleted();

    void setFirstRegistrationCompleted(boolean enable);

    boolean getsignIn();

    void setsignIn(boolean enable);
    Set<String> getHeadersSet(Set<String> objects);

    void setHeadersSet(HashSet<String> cookies);
}
