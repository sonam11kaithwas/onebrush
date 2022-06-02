package com.advantal.onebrush.utilies_pkg;

import android.os.CountDownTimer;
import android.util.Log;

import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

/**
 * Created by Sonam on 29-04-2022 14:08.
 */
public class MyCountDownTimer extends CountDownTimer {
    private final UserIntractionDisable userIntractionDisable;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, UserIntractionDisable userIntractionDisable) {
        super(millisInFuture, countDownInterval);
        this.userIntractionDisable = userIntractionDisable;
//        OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(MyDateUtils.getCurrentDateTimePlusOne(AppConstant.DATE_TIME_Frmt));
    }


    @Override
    public void onFinish() {
        Log.e("Test", " ---onFinish");
        if (userIntractionDisable != null) {
            OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(MyDateUtils.getCurrentDateTimeMinusOne(AppConstant.DATE_TIME_Frmt));

            userIntractionDisable.getUserPinWindow();
        }
    }


    @Override
    public void onTick(long millisUntilFinished) {
        /*******No any ACTION On This Call Back******/
        Log.e("Test", " ---onTick");

    }

    public interface UserIntractionDisable {
        void getUserPinWindow();
    }
}

