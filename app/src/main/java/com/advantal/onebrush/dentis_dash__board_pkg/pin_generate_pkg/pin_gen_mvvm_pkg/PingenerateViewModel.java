package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_mvvm_pkg;


import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.pin_gen_model_pkg.PinGenReqModel;

/**
 * Created by Sonam on 11-02-2022 16:37.
 */
public class PingenerateViewModel extends ViewModel {
    private final Context context;
    public MutableLiveData<String> registeredPIN1 = new MutableLiveData<>();
    public MutableLiveData<String> registeredPIN2 = new MutableLiveData<>();
    private PinGenReqModel pinGenReqModel;

    public PingenerateViewModel(Context context, PinGenReqModel pinGenReqModel) {
        this.context = context;
        this.pinGenReqModel = pinGenReqModel;
    }


    public boolean isPinConfirm(String registeredPIN) {
        pinGenReqModel.setRegisteredPIN1(registeredPIN);
        pinGenReqModel.setRegisteredPIN2(registeredPIN2.getValue());
        if (pinGenReqModel!=null&&pinGenReqModel.getRegisteredPIN2()!=null&&registeredPIN.equals(pinGenReqModel.getRegisteredPIN2())) {
            return true;
        }
        return false;
    }
}
