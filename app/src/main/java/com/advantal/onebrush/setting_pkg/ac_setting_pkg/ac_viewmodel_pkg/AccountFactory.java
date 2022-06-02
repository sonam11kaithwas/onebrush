package com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.setting_pkg.ac_setting_pkg.ac_model_pkg.AccountModel;

/**
 * Created by Surbhi joshi on 09-03-2022 18:49.
 */
public class AccountFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context context;
    private final AccountModel accountModel;

    public AccountFactory(Context context, AccountModel accountModel) {
        this.context = context;
        this.accountModel = accountModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AccountViewModel(context, accountModel);

    }
}
