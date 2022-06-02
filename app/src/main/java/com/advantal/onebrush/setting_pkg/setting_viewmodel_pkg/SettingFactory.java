package com.advantal.onebrush.setting_pkg.setting_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.setting_pkg.SettingModel;

/**
 * Created by Surbhi joshi on 01-03-2022 18:44.
 */
public class SettingFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context context;
    private final SettingModel settingModel;

    public SettingFactory(Context context, SettingModel settingModel) {
        this.context = context;
        this.settingModel = settingModel;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SettingViewModel(context, settingModel);
    }
}
