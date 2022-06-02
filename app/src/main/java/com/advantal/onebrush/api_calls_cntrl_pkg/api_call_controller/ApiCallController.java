package com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller;

import android.content.Context;
import android.util.Log;

import com.advantal.onebrush.R;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.RetrofitApiClient;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ApiCallController {
    private final int requestCode;
    private final ApiCallCallBack apiCallCallBack;
    private Context context;

    public ApiCallController(ApiCallCallBack apiCalServerReqRes, int requestCode) {
        this.apiCallCallBack = apiCalServerReqRes;
        this.requestCode = requestCode;
    }



    /********Crausal screen data featch from server***********/
    synchronized public void getLoadDataFromSerVerWithGet(String ApiName) {
        if (AppUtility.isNetworkConnected()) {

            RetrofitApiClient.getservices().getWlcCarousle(ApiName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Test---", "Test---" + requestCode);
                            AppConstant.ERRORAPPREARMSG = "";

                            try {
                                if (jsonObject.has("responseCode") && jsonObject.get("responseCode").getAsString().equals("200")) {
                                    if (jsonObject.has("data") && jsonObject.getAsJsonArray("data").size() > 0) {
                                        Log.e("Test---", "Test---" + (new Gson().toJson(jsonObject.getAsJsonArray("data"))));
                                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                                , ApiServices.get_welcome_carousel, "SplashScreenActivity",
                                                (new Gson().toJson(jsonObject.getAsJsonArray("data"))));
                                        OneBrushAppPrefrence.getSharedprefInstance().setWelcomeCarousels(new Gson().toJson(jsonObject.getAsJsonArray("data")));
                                    } else if (jsonObject.has("message")) {
                                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                                , ApiServices.get_welcome_carousel, "SplashScreenActivity",
                                                jsonObject.get("message").getAsString());
                                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                                    }
                                } else if (jsonObject.has("message")) {
                                    OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                            , ApiServices.get_welcome_carousel, "SplashScreenActivity",
                                            jsonObject.get("message").getAsString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                apiCallCallBack.getApiErrorResponce(e.getMessage(), "");

                            }
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApionComplete();
                        }
                    });
        } else {
            networkerror();
        }
    }


    public void getLoadDataFromSerVer(JsonObject reqData, String ApiName) {
        if (AppUtility.isNetworkConnected()) {

            RetrofitApiClient.getservices().apiCall(ApiName, reqData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppConstant.ERRORAPPREARMSG = "";

                            apiCallCallBack.getSuccessResponce(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                apiCallCallBack.getApiErrorResponce(e.getMessage(), "");

                            }
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApionComplete();
                        }
                    });
        } else {
            networkerror();
        }
    }

    public void updateUserData(JsonObject reqData, String ApiName) {
        if (AppUtility.isNetworkConnected()) {

            RetrofitApiClient.getservices().updateUser(ApiName, reqData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppConstant.ERRORAPPREARMSG = "";

                            apiCallCallBack.getSuccessResponce(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                apiCallCallBack.getApiErrorResponce(e.getMessage(), "");

                            }
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApionComplete();
                        }
                    });
        } else {
            networkerror();
        }
    }

    public void getDataFromServerWithParameters(String ApiName) {
        if (AppUtility.isNetworkConnected()) {

            RetrofitApiClient.getservices().getDataFromServerWithParameters(ApiName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppConstant.ERRORAPPREARMSG = "";

                            apiCallCallBack.getSuccessResponce(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                apiCallCallBack.getApiErrorResponce(e.getMessage(), "");

                            }
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApionComplete();
                        }
                    });
        } else {
            networkerror();
        }
    }

    public void getCaseDataList(String uuId) {
//    public void getDataFromSerVer(String id, String apiName) {
        if (AppUtility.isNetworkConnected()) {

            RetrofitApiClient.getservices().

                    getCaseDataList(uuId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppConstant.ERRORAPPREARMSG = "";

                            apiCallCallBack.getSuccessResponce(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                apiCallCallBack.getApiErrorResponce(e.getMessage(), "");
                            }
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApionComplete();
                        }
                    });
        } else {
            networkerror();
        }
    }


    public void getDataFromSerVer(String apiName) {
        if (AppUtility.isNetworkConnected()) {

            RetrofitApiClient.getservices().
                    getPatientList(apiName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppConstant.ERRORAPPREARMSG = "";

                            apiCallCallBack.getSuccessResponce(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            if (e.getMessage().trim().equalsIgnoreCase("Handshake failed")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.ssl_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.ssl_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getCause() != null && e.getCause().getMessage().trim().equalsIgnoreCase("android_getaddrinfo failed: EAI_NODATA (No address associated with hostname)")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.denti_ser_error), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));
                            } else if (e.getMessage().trim().equalsIgnoreCase("HTTP 401")) {
                                AppConstant.ERRORAPPREARMSG = OneBrushApp.getInstance().getResources().getString(R.string.web_adrs);
                                apiCallCallBack.getApiErrorResponce(OneBrushApp.getInstance().getResources().getString(R.string.web_adrs), OneBrushApp.getInstance().getResources().getString(R.string.noInternet));

                            } else {
                                apiCallCallBack.getApiErrorResponce(e.getMessage(), "");
                            }
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApionComplete();
                        }
                    });
        } else {
            networkerror();
        }
    }


    private void networkerror() {

    }
}
