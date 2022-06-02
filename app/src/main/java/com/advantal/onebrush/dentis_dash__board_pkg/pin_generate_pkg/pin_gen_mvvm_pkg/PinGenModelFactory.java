package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_mvvm_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinGenerateActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_model_pkg.PinGenReqModel;
import com.advantal.onebrush.login_package.LoginActivity;
import com.advantal.onebrush.login_package.model_pkg.LoginModelClass;
import com.advantal.onebrush.login_package.viewmodel.LoginViewModel;

/**
 * Created by Sonam on 17-02-2022 18:20.
 */
public class PinGenModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final PinGenReqModel pinGenReqModel;
    private final Context context;

    public PinGenModelFactory(Context context, PinGenReqModel pinGenReqModel) {
        this.context = context;
        this.pinGenReqModel = pinGenReqModel;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new PingenerateViewModel(context, pinGenReqModel);
    }


}