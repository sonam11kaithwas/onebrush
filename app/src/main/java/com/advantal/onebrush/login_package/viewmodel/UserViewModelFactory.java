package com.advantal.onebrush.login_package.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.login_package.model_pkg.LoginModelClass;
import com.advantal.onebrush.login_package.viewmodel.LoginViewModel;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final LoginModelClass user;
    private final Context context;

    public UserViewModelFactory(LoginActivity loginActivity, LoginModelClass user) {
        this.context = loginActivity;
        this.user = user;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new LoginViewModel(context, user);
    }


}
