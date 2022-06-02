package com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_view_model;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_model.PatientModel;
import com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_model_pkg.PersonalInfoModel;

public class PatientViewModel extends ViewModel {
    private final Context context;
    private final PatientModel patientModel;

    public PatientViewModel(Context context, PatientModel patientModel) {
        this.context = context;
        this.patientModel = patientModel;
    }
}
