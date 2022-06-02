package com.advantal.onebrush.registration_pkg.regi_next_pkg.reg_mvvm;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by Sonam on 28-02-2022 14:32.
 */
public class RegNextViewFactory extends ViewModelProvider.NewInstanceFactory {
    private final RegNextModel regModel;
    private final Context context;

    public RegNextViewFactory(Context context, RegNextModel regModel) {
        this.regModel = regModel;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RegNextViewModel(context, regModel);
    }


}

