package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.dentinostic_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.DentinosticModel;

public class DentinosticViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final DentinosticModel dentinosticModel;
    private final Context context;


    public DentinosticViewModelFactory( Context context,DentinosticModel dentinosticModel) {
        this.dentinosticModel = dentinosticModel;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DentinosticViewModel(context, dentinosticModel);

    }
}
