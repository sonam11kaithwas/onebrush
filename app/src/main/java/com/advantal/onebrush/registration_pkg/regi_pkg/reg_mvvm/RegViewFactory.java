package com.advantal.onebrush.registration_pkg.regi_pkg.reg_mvvm;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by Sonam on 28-02-2022 14:32.
 */
public class RegViewFactory extends ViewModelProvider.NewInstanceFactory {
    private final RegModel regModel;
    private final Context context;

    public RegViewFactory(Context context, RegModel regModel) {
        this.context = context;
        this.regModel = regModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {


        return (T) new RegViewModel(context, regModel);
    }
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T)new RegNextViewModel(context, user);
}

