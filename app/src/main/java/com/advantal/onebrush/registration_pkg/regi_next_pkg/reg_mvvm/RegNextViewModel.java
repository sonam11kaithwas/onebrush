package com.advantal.onebrush.registration_pkg.regi_next_pkg.reg_mvvm;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;

import java.util.concurrent.Callable;

/**
 * Created by Sonam on 28-02-2022 14:32.
 */


public class RegNextViewModel extends ViewModel {
    public RegNextModel regModel;
    Context context;
    RelativeLayout relativeLayout;


    public RegNextViewModel(Context context, RegNextModel regModel) {
        this.regModel = regModel;
        this.context = context;
    }


    private void showErrorMsg(String msg,String titleHead) {
        //    text.postValue("Error");
        OneBrushApp.getInstance().showToastmsg(msg);

        AppUtility.alertDialog(context, msg,titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }
}
