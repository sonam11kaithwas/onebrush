package com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_view_model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_model.PatientModel;

public class PatientFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context context;
    private final PatientModel patientModel;

    public PatientFactory(Context context, PatientModel patientModel) {
        this.context = context;
        this.patientModel = patientModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PatientViewModel(context, patientModel);

    }
}
