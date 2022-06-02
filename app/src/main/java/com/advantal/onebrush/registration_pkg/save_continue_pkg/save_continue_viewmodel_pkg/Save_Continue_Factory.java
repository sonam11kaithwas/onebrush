package com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.registration_pkg.save_continue_pkg.save_continue_model_pkg.Save_Continue_Model;

/**
 * Created by Surbhi joshi on 24-02-2022 18:49.
 */
public class Save_Continue_Factory extends ViewModelProvider.NewInstanceFactory {
    private final Save_Continue_Model save_continue_model;
    private final Context context;

    public Save_Continue_Factory(Context context, Save_Continue_Model save_continue_model) {
        this.save_continue_model = save_continue_model;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new Save_Continue_View_Model(context, save_continue_model);
    }
}
