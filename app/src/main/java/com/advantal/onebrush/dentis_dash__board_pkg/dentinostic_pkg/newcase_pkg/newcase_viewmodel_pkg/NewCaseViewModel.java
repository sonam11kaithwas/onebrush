package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_viewmodel_pkg;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_model_pkg.NewCaseModel;

public class NewCaseViewModel extends ViewModel {
    private final Context context;
    private final NewCaseModel newCaseModel;
    public MutableLiveData<String> name = new MutableLiveData<>();



    public NewCaseViewModel(Context context, NewCaseModel newCaseModel) {
        this.context = context;
        this.newCaseModel = newCaseModel;
    }
}
