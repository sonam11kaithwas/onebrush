package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.newcase_pkg.newcase_model_pkg.NewCaseModel;

public class NewCaseModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final NewCaseModel newCaseModel;
    private final Context context;


    public NewCaseModelFactory(Context context, NewCaseModel newCaseModel) {
        this.newCaseModel = newCaseModel;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewCaseViewModel(context, newCaseModel);

    }
}
