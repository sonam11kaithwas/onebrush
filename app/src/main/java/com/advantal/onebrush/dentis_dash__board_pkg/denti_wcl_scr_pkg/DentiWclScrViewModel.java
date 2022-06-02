package com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.advantal.onebrush.api_calls_cntrl_pkg.ApiServices;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallController;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.MultiControlQueModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.ReadJsonFiles;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.advantal.onebrush.utilies_pkg.syncronize_pkg.SyncronizeModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam on 22-02-2022 14:34.
 */
public class DentiWclScrViewModel extends AndroidViewModel implements ApiCallCallBack {
    private final Context context;
    private MutableLiveData<List<WelcomeCarouselModel>> welCarousllist = new MutableLiveData<>();

    public DentiWclScrViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void getallScreenDetail() {
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetSCreenDetails);
        requestor.getDataFromSerVer(ApiServices.get_all_screen_Detail);

    }

    public void getCrausalDataFromServer() {
        List<WelcomeCarouselModel> list = OneBrushAppPrefrence.getSharedprefInstance().getWelcomeCarousels();
        if (list == null || list.size() == 0) {
            list = ReadJsonFiles.getWelCaroselDataFromLocal();
        }
        welCarousllist.setValue(list);
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetWLCrosl);
        requestor.getLoadDataFromSerVerWithGet(ApiServices.get_welcome_carousel);

    }

    private void getQuestionsDataFromSerever() {
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetAllQuestions);
        requestor.getDataFromServerWithParameters(ApiServices.getAllQuestions);
    }

    private void getMultiQuestionsDataFromSerever() {
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetMultiQuestions);
        requestor.getDataFromSerVer(ApiServices.get_all_multicontrol);
    }

    public MutableLiveData<List<WelcomeCarouselModel>> getWelCarousllist() {
        return welCarousllist;
    }

    public void setWelCarousllist(MutableLiveData<List<WelcomeCarouselModel>> welCarousllist) {
        this.welCarousllist = welCarousllist;
    }

    public void getDentiSyncroNization() {
        ApiCallController requestor = new ApiCallController(this, ApiCallCallBack.GetSyncronization);
        requestor.getDataFromSerVer(ApiServices.get_all_syncronization);
    }


    @Override
    public void getApionComplete() {
    }


    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {
        switch (requestCode) {
            case GetSCreenDetails:
                try {
                    if (jsonObject.has("responseCode") && jsonObject.get("responseCode").getAsString().equals("200")) {
                        if (jsonObject.has("data") && jsonObject.getAsJsonArray("data").size() > 0)
                            OneBrushAppPrefrence.getSharedprefInstance().setScreenDetailsdData(new Gson().toJson(jsonObject.getAsJsonArray("data")));
                        else if (jsonObject.has("message")) {
                            OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                        }
                    } else if (jsonObject.has("message")) {
                        OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ApiCallCallBack.GetMultiQuestions:
                if (jsonObject.has("data") && jsonObject.getAsJsonArray("data") != null) {
                    String questDataStr = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                    Type queDataListType = new TypeToken<List<MultiControlQueModel>>() {
                    }.getType();
                    List<MultiControlQueModel> questionModelList = new Gson().fromJson(questDataStr, queDataListType);

                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().deletMultiQuesData();
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().insertAllQuestions(questionModelList);
                } else {
//                    ReadJsonFiles.getMultiQusDataFromLocal();
                }


                break;
            case ApiCallCallBack.GetAllQuestions:
                if (jsonObject.has("data") && jsonObject.getAsJsonArray("data") != null) {
                    String questDataStr = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                    Type queDataListType = new TypeToken<List<QuestionModel>>() {
                    }.getType();
                    List<QuestionModel> questionModelList = new Gson().fromJson(questDataStr, queDataListType);

                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().deleteQuestionData();
                    AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().insertAllQuestions(questionModelList);
                } else {
//                    ReadJsonFiles.getQuestDataFromLocal();
                }
                break;
            case ApiCallCallBack.GetSyncronization:
                if (jsonObject.get("responseCode").getAsString().equals("200")) {
                    String syncDataStr = new Gson().toJson(jsonObject.get("data"));
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
//                    ReadJsonFiles.getQuestDataFromLocal();
//                    ReadJsonFiles.getMultiQusDataFromLocal();
                }
                break;
            case ApiCallCallBack.GetWLCrosl:
                String str = new Gson().toJson(jsonObject.getAsJsonArray("data"));
                Type listType = new TypeToken<List<WelcomeCarouselModel>>() {
                }.getType();
                List<WelcomeCarouselModel> welcomeCarouselModelList = new Gson().fromJson(str, listType);
                welCarousllist.setValue(welcomeCarouselModelList);
                break;

        }
    }

    @Override
    public void getApiErrorResponce(String error, String titleHeader) {
        OneBrushApp.getInstance().showToastmsg(error);
    }
}
