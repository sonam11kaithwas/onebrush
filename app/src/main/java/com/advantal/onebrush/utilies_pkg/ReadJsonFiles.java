package com.advantal.onebrush.utilies_pkg;

import android.util.Log;

import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.MultiControlQueModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.WelcomeCarouselModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam on 25-02-2022 18:44.
 */
public class ReadJsonFiles {
    static public List<WelcomeCarouselModel> getWelCaroselDataFromLocal() {
        ArrayList<WelcomeCarouselModel> currenciesFormats = new ArrayList<>();
        try {
            InputStream is = OneBrushApp.getInstance().getResources().getAssets().open(AppConstant.WELCAROUSEE);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject countries = new JSONObject(json);
            JSONArray cntry = countries.getJSONArray("data");
            if (cntry != null) {
                for (int i = 0; i < cntry.length(); i++) {
                    currenciesFormats.add(new WelcomeCarouselModel(cntry.getJSONObject(i).getString("imgUrl"),
                            cntry.getJSONObject(i).getString("title"),
                            cntry.getJSONObject(i).getString("descriptions")
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        return currenciesFormats;
    }

    static private List<QuestionModel> getQuestDataFromLocal() {

        ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();
        try {
            InputStream is = OneBrushApp.getInstance().getResources().getAssets().open("ques.json");
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject quesJson = new JSONObject(json);
            JSONArray jsonArray = quesJson.getJSONArray("data");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    //   int  userPhotoId = imageListItem.getInt("user_photo_id")
                    questionModelArrayList.add(new QuestionModel(jsonArray.getJSONObject(i).getInt("ID"),
                            jsonArray.getJSONObject(i).getInt("Position"), jsonArray.getJSONObject(i).getString("Question"),
                            jsonArray.getJSONObject(i).getString("Function"), jsonArray.getJSONObject(i).getString("AnswerData"),
                            jsonArray.getJSONObject(i).getInt("NextPosition"),
                            jsonArray.getJSONObject(i).getInt("AnswerFlag"),
                            jsonArray.getJSONObject(i).getString("RecordCreation"),
                            "", "",
                            "", ""
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).questionDao().insertAllQuestions(questionModelArrayList);
//        getMultiQusDataFromLocal();

        return questionModelArrayList;
    }

    static private List<MultiControlQueModel> getMultiQusDataFromLocal() {
        ArrayList<MultiControlQueModel> questionModelArrayList = new ArrayList<>();
        try {
            InputStream is = OneBrushApp.getInstance().getResources().getAssets().open("multi_ques.json");
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data);

            JSONObject quesJson = new JSONObject(json);
            JSONArray jsonArray = quesJson.getJSONArray("data");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    questionModelArrayList.add(new MultiControlQueModel(jsonArray.getJSONObject(i).getInt("ID"),
                            jsonArray.getJSONObject(i).getInt("MCkey"), jsonArray.getJSONObject(i).getString("ResultingCode"),
                            jsonArray.getJSONObject(i).getInt("NextPosition"),
                            jsonArray.getJSONObject(i).getString("RecordCreation"), jsonArray.getJSONObject(i).getString("RecordCreationFunction"),
                            jsonArray.getJSONObject(i).getString("RecordCreationProfile"),
                            jsonArray.getJSONObject(i).getString("RecordUpdate"),
                            jsonArray.getJSONObject(i).getString("RecordUpdateFunction"),
                            jsonArray.getJSONObject(i).getString("RecordUpdateProfile")
//                            ,jsonArray.getJSONObject(i).getInt("MCkey")

                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            Log.e("Error", je.getMessage());
            je.printStackTrace();
        }
        AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).multiCntrlQuesDao().insertAllQuestions(questionModelArrayList);
        return questionModelArrayList;
    }


}
