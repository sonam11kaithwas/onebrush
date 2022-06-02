package com.advantal.onebrush.splash_scr_pkg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;
import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.MultiControlQueModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.DentiWclScrActivity;
import com.advantal.onebrush.setting_pkg.log_file_pkg.LogFileController;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.syncronize_pkg.SyncronizeModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SplashScreenActivity extends AppCompatActivity {

    Observer observer = new Observer() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Object o) {
            Intent intent = new Intent(SplashScreenActivity.this, DentiWclScrActivity.class);
            startActivity(intent);
            finish();
        }


        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    private void fetchDataFromServer() {


        LogFileController.getLogFileInstance().addDataInStringBuilder("Request", ApiServices.get_welcome_carousel, "SplashScreenActivity", ApiServices.get_welcome_carousel);

        ApiCallController requestor = new ApiCallController(new ApiCallCallBack() {
            @Override
            public void getSuccessResponce(JsonObject jsonObject, int requestCod) {
                Log.e("Test---", "Test---" + requestCod);

            }

            @Override
            public void getApiErrorResponce(String error, String titleHeader) {
                LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                        , ApiServices.get_welcome_carousel, "SplashScreenActivity", error);
                showErrorMsg(error, titleHeader);

            }

            @Override
            public void getApionComplete() {

                LogFileController.getLogFileInstance().addDataInStringBuilder("Request", ApiServices.get_all_syncronization,
                        "SplashScreenActivity", ApiServices.get_all_syncronization);
                ApiCallController requestor = new ApiCallController(new ApiCallCallBack() {
                    @Override
                    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
                        Log.e("Test---", "Test---" + requestCode);

                        if (jsonObject.get("responseCode").getAsString().equals("200")) {
                            String syncDataStr = new Gson().toJson(jsonObject.get("data"));
                            Log.e("DataForDenti", syncDataStr);
                            LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                    , ApiServices.get_all_syncronization, "SplashScreenActivity", syncDataStr);

                            SyncronizeModel syncronizeModel = new Gson().fromJson(syncDataStr, SyncronizeModel.class);

                            SyncronizeModel appPrefrenceSyncronizeModel = OneBrushAppPrefrence.getSharedprefInstance().getSyncronizedData();

                            if (appPrefrenceSyncronizeModel == null) {
                                OneBrushAppPrefrence.getSharedprefInstance().setSyncronizedData(syncDataStr);
                                getMultiQuestionsDataFromSerever();
                                getQuestionsDataFromSerever();
                            } else if (syncronizeModel != null && appPrefrenceSyncronizeModel != null) {
                                if (syncronizeModel.getMasterFlag() == appPrefrenceSyncronizeModel.getMasterFlag()) {
                                    if (syncronizeModel.getRecordsInDialogueControl() != appPrefrenceSyncronizeModel.getRecordsInDialogueControl()) {
                                        OneBrushAppPrefrence.getSharedprefInstance().setSyncronizedData(syncDataStr);
                                        getQuestionsDataFromSerever();
                                    }
                                    if (syncronizeModel.getRecordsInMultiControl() != appPrefrenceSyncronizeModel.getRecordsInMultiControl()) {
                                        OneBrushAppPrefrence.getSharedprefInstance().setSyncronizedData(syncDataStr);
                                        getMultiQuestionsDataFromSerever();
                                    }
                                    if (syncronizeModel.getRecordsInMultiControl() != appPrefrenceSyncronizeModel.getRecordsInMultiControl()) {
                                        OneBrushAppPrefrence.getSharedprefInstance().setSyncronizedData(syncDataStr);
                                    }
                                } else {
                                    OneBrushAppPrefrence.getSharedprefInstance().setSyncronizedData(syncDataStr);
                                    getQuestionsDataFromSerever();
                                    getMultiQuestionsDataFromSerever();
                                }
                            }
                        } else {
//                            ReadJsonFiles.getQuestDataFromLocal();
//                            ReadJsonFiles.getMultiQusDataFromLocal();

                        }
                    }

                    @Override
                    public void getApiErrorResponce(String error, String titleHeader) {
                        LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                                , ApiServices.get_all_syncronization, "SplashScreenActivity", error);
                        showErrorMsg(error, titleHeader);

                    }

                    @Override
                    public void getApionComplete() {

                    }
                }, ApiCallCallBack.GetSyncronization);
                requestor.getDataFromSerVer(ApiServices.get_all_syncronization);
            }
        }, ApiCallCallBack.GetWLCrosl);
        requestor.getLoadDataFromSerVerWithGet(ApiServices.get_welcome_carousel);
    }


    private void showErrorMsg(String msg, String titleHead) {
        AppUtility.alertDialog(this, msg, titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (AppUtility.isNetworkConnected())
            fetchDataFromServer();
    }

    @Override
    protected void onResume() {
        Observable.timer(5, TimeUnit.SECONDS).subscribe(observer);
        super.onResume();
    }


    private void getQuestionsDataFromSerever() {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.getAllQuestions, "SplashScreenActivity", ApiServices.getAllQuestions);

        ApiCallController requestor = new ApiCallController(new ApiCallCallBack() {
            @Override
            public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
                Log.e("Test---", "Test---" + requestCode);

                if (jsonObject.has("data") && jsonObject.getAsJsonArray("data") != null) {
                    String questDataStr = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                    Log.e("DataForDenti", questDataStr);
                    Type queDataListType = new TypeToken<List<QuestionModel>>() {
                    }.getType();
                    List<QuestionModel> questionModelList = new Gson().fromJson(questDataStr, queDataListType);

                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().deleteQuestionData();
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().insertAllQuestions(questionModelList);

                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , ApiServices.getAllQuestions, "SplashScreenActivity", questDataStr);
                } else {
//                    ReadJsonFiles.getQuestDataFromLocal();
                }
            }

            @Override
            public void getApiErrorResponce(String error, String titleHeader) {
                LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                        , ApiServices.getAllQuestions, "SplashScreenActivity", error);
                showErrorMsg(error, titleHeader);

            }

            @Override
            public void getApionComplete() {

            }
        }, ApiCallCallBack.GetAllQuestions);
        requestor.getDataFromServerWithParameters(ApiServices.getAllQuestions);
    }

    private void getMultiQuestionsDataFromSerever() {
        LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                , ApiServices.get_all_multicontrol, "SplashScreenActivity", ApiServices.get_all_multicontrol);
        ApiCallController requestor = new ApiCallController(new ApiCallCallBack() {
            @Override
            public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
                Log.e("Test---", "Test---" + requestCode);

                if (jsonObject.has("data") && jsonObject.getAsJsonArray("data") != null) {
                    String questDataStr = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                    Log.e("DataForDenti", questDataStr);

                    Type queDataListType = new TypeToken<List<MultiControlQueModel>>() {
                    }.getType();
                    List<MultiControlQueModel> questionModelList = new Gson().fromJson(questDataStr, queDataListType);

                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().deletMultiQuesData();
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().insertAllQuestions(questionModelList);

                    LogFileController.getLogFileInstance().addDataInStringBuilder("Responce"
                            , ApiServices.get_all_multicontrol, "SplashScreenActivity", questDataStr);
                } else {
//                    ReadJsonFiles.getMultiQusDataFromLocal();
                }

            }

            @Override
            public void getApiErrorResponce(String error, String titleHeader) {
                showErrorMsg(error, titleHeader);

            }

            @Override
            public void getApionComplete() {

            }
        }, ApiCallCallBack.GetMultiQuestions);
        requestor.getDataFromSerVer(ApiServices.get_all_multicontrol);
    }


}