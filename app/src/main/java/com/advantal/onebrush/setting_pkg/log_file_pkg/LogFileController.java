package com.advantal.onebrush.setting_pkg.log_file_pkg;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.advantal.onebrush.BuildConfig;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.api_calls_cntrl_pkg.RetrofitApiClient;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.google.gson.JsonObject;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sonam on 17-05-2022 16:08.
 */

public class LogFileController {
    private static LogFileController logFileController;
    private static File filePath = null;
    private final StringBuilder sb1 = new StringBuilder();
    private Context context;

    public static LogFileController getLogFileInstance() {
        if (logFileController == null) {
            logFileController = new LogFileController();
            filePath = new File(OneBrushApp.getInstance().getExternalFilesDir(null), "Test.docx");
            try {
                if (!filePath.exists()) {
                    filePath.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logFileController;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addDataInStringBuilder(String type, String apiNm, String classNm, String description) {
        sb1.append("DateTime:" + AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss"));
        sb1.append(" || ");
        sb1.append("Type:" + type);
        sb1.append(" || ");
        sb1.append("Activity:" + apiNm);
        sb1.append(" || ");
        sb1.append("Classname:" + classNm);
        sb1.append(" || ");
        sb1.append("Description:" + description);
        sb1.append("\n");
    }

    public void createFile(RelativeLayout layout) {
        try {
            XWPFDocument xwpfDocument = new XWPFDocument();
            XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
            XWPFRun xwpfRun = xwpfParagraph.createRun();

            xwpfRun.setText(sb1.toString());
            xwpfRun.setFontSize(22);

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            xwpfDocument.write(fileOutputStream);

            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            xwpfDocument.close();

            sendRequestForLogFile(layout);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendRequestForLogFile(RelativeLayout layout) {

        String mimeType = "";
        File file = filePath;
        if (file != null && file.exists()) {
            mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                return;
            }
            UserAccountDetailsModel userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

            RequestBody surveyBody = RequestBody.create(MediaType.parse(mimeType), file);
            MultipartBody.Part docfile = MultipartBody.Part.createFormData(
                    "docfile", file.getName(),
                    surveyBody);
            RequestBody custId = RequestBody.create(MultipartBody.FORM, userAccountDetailsModel.getCustId());
            RequestBody recordCreationFunction = RequestBody.create(MultipartBody.FORM, "android_v_" + BuildConfig.VERSION_NAME
                    + "_add");
            RequestBody recordCreationProfile = RequestBody.create(MultipartBody.FORM, userAccountDetailsModel.getEmailAddress());
            RequestBody languageCode = RequestBody.create(MultipartBody.FORM, OneBrushAppPrefrence.getSharedprefInstance().getPreferredLanguage());

            try {
                if (context != null && layout != null) {
                    AppUtility.showProgressBaForRelLay(layout, context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            LogFileController.getLogFileInstance().addDataInStringBuilder("Request"
                    , "admin/error_log_file_upload", "SettingsActivity", "admin/error_log_file_upload");

            RetrofitApiClient.getservices().sendLogReport(docfile, custId, recordCreationFunction, recordCreationProfile
                    , languageCode).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull JsonObject jsonObject) {
                            Log.e("", "");

                            try {
                                if (jsonObject.has("responseCode")) {
                                    if (jsonObject.get("responseCode").getAsString().equals("200")) {
                                        if (jsonObject.has("message") && (jsonObject.get("message").getAsString() != null)) {
                                            OneBrushApp.getInstance().showToastmsg(jsonObject.get("message").getAsString());
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e("", "");

                        }

                        @Override
                        public void onComplete() {
                            try {
                                if (context != null)
                                    AppUtility.hideProgressBar(context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Log.e("", "");

        }
    }
}
