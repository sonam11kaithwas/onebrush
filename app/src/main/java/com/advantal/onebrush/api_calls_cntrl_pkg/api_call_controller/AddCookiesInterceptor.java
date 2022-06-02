package com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller;

import android.util.Log;

import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aplite_pc302 on 8/8/18.
 */

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) OneBrushAppPrefrence.getSharedprefInstance().getHeadersSet(new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}
