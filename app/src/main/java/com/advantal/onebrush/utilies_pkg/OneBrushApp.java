package com.advantal.onebrush.utilies_pkg;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

/**
 * Created by Sonam on 10-02-2022 12:39.
 */
public class OneBrushApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static OneBrushApp oneBrush;

    public static synchronized OneBrushApp getInstance() {
        return oneBrush;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        oneBrush = this;

    }

//    public void


    public void showToastmsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.e("onActivityStopped","");
        OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }




    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        OneBrushAppPrefrence.getSharedprefInstance().setLastLaunchTime(AppUtility.getCurrentDateTime(AppConstant.DATE_TIME_Frmt));
        Log.e("Case----", "Case----");
    }
}
