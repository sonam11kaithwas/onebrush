package com.advantal.onebrush.denti_qus_pkg.denti_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.denti_qus_pkg.DentiQusModel;

/**
 * Created by Surbhi joshi on 01-03-2022 12:01.
 */
public class DentiQusFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context context;
    private final DentiQusModel dentiQusModel;

    public DentiQusFactory(Context context, DentiQusModel dentiQusModel) {
        this.context = context;
        this.dentiQusModel = dentiQusModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DentiQusViewModel(context, dentiQusModel);
    }
}
