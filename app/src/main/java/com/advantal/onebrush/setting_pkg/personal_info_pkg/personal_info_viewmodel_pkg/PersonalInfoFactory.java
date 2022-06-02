package com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.setting_pkg.personal_info_pkg.personal_info_model_pkg.PersonalInfoModel;

/**
 * Created by Surbhi joshi on 03-03-2022 18:02.
 */
public class PersonalInfoFactory extends ViewModelProvider.NewInstanceFactory {
    private final PersonalInfoModel personalInfoModel;
    private final Context context;

    public PersonalInfoFactory( Context context,PersonalInfoModel personalInfoModel) {
        this.personalInfoModel = personalInfoModel;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PersonalInfoViewModel(context, personalInfoModel);

    }
}
