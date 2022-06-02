package com.advantal.onebrush.forgot_password_pkg.forgot_viewmodel_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_mvvm_pkg.PingenerateViewModel;
import com.advantal.onebrush.forgot_password_pkg.forgot_model_pkg.ForgotModelClass;

public class ForgotViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ForgotModelClass forgotModelClass;
    private final Context context;

    public ForgotViewModelFactory(Context context,ForgotModelClass forgotModelClass) {
        this.forgotModelClass = forgotModelClass;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ForgotViewModel(context, forgotModelClass);

    }
}
