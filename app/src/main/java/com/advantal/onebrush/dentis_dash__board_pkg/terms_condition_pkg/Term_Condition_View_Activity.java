package com.advantal.onebrush.dentis_dash__board_pkg.terms_condition_pkg;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.DentiWclScrActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg.PinGenerateActivity;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.util.concurrent.Callable;

public class Term_Condition_View_Activity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    PDFView pdfView, pdfView1;
    WebView webView1, webView2;
    private AppCompatButton term_nxt_btn;
    private CheckBox checkbox1, checkbox2, checkbox3;
    private UserAccountDetailsModel userAccountDetailsModel;
    private TextView tv_checkbox_text1, tv_checkbox_text2, tv_checkbox_text3;
    private RelativeLayout parent_lay;
    private WebView web;
    private String agreedToTermsAndCond, agreedToPersonalData, agreedToInformation = "0000-00-00 00:00:00";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition_view);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("Term_Condition_View_Activity", userAccountDetailsModel);

        inilizeViews();

    }

    private void inilizeViews() {
        term_nxt_btn = findViewById(R.id.term_nxt_btn);
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox3 = findViewById(R.id.checkbox3);
        tv_checkbox_text1 = findViewById(R.id.tv_checkbox_text1);
        tv_checkbox_text2 = findViewById(R.id.tv_checkbox_text2);
        tv_checkbox_text3 = findViewById(R.id.tv_checkbox_text3);
        checkbox1.setOnCheckedChangeListener(this);
        checkbox2.setOnCheckedChangeListener(this);
        checkbox3.setOnCheckedChangeListener(this);
        parent_lay = findViewById(R.id.parent_lay);
        pdfView = findViewById(R.id.pdfView);
//        webView1 = findViewById(R.id.webView_tc);

     /*   WebView webView = new WebView( Term_Condition_View_Activity.this );
//        webView.getSettings().setAppCacheMaxSize( 10 * 1024 * 1024 );
        webView.getSettings().setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
        webView.getSettings().setAllowFileAccess( true );
        webView.getSettings().setAppCacheEnabled( true );
        webView.getSettings().setJavaScriptEnabled( true );
        webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );

        if ( !isNetworkAvailable() ) {
            webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }

        webView.loadUrl( "your_url" );*/

        pdfView = findViewById(R.id.pdfView);

        pdfView.fromAsset("onebrush_tnc.pdf")
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10).onError(new OnErrorListener() {
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        })
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();


        tv_checkbox_text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Term_Condition_View_Activity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_terms_con_layout, viewGroup, false);
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.getWindow().setLayout(600, 400); //Controlling width and height.

                dialogView.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                pdfView1 = dialogView.findViewById(R.id.pdfView1);
                pdfView1.fromAsset("onebrush_tnc.pdf")
                        .defaultPage(0)
                        .enableAnnotationRendering(true)
                        .scrollHandle(new DefaultScrollHandle(dialogView.getContext()))
                        .spacing(10).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                })
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .load();
                Window window = alertDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog.show();

            }
        });

    }

   /* private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //    AppUtility.updateScreeState("Term_Condition_View_Activity", userAccountDetailsModel);
    }

    @Override
    public void onBackPressed() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
        else {
            AppUtility.updateScreeState("DentiWclScrActivity", userAccountDetailsModel);
            Intent intent = new Intent(Term_Condition_View_Activity.this, DentiWclScrActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            this.finish();
        }

    }

    public void termsClickEvents(View view) {
        switch (view.getId()) {
            case R.id.term_nxt_btn:
                term_nxt_btn.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        term_nxt_btn.setEnabled(true);
                    }
                }, 300);
                openPinScreen();
                break;
            case R.id.cross_img:
                onBackPressed();
                break;
        }
    }

    private void openPinScreen() {
        userAccountDetailsModel.setAgreedToTermsAndCond(agreedToTermsAndCond);
        userAccountDetailsModel.setAgreedToPersonalData(agreedToPersonalData);
        userAccountDetailsModel.setAgreedToInformation(agreedToInformation);
        AppUtility.updateScreeState("Term_Condition_View_Activity", userAccountDetailsModel);
        Intent intent = new Intent(this, PinGenerateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        this.finish();
    }

    private void viewsVisibility() {
        if (checkbox1.isChecked() && checkbox2.isChecked()) {
            term_nxt_btn.setVisibility(View.VISIBLE);
        } else {
            term_nxt_btn.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.checkbox1:
                if (checkbox1.isChecked()) {
                    agreedToTermsAndCond = AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss");
                } else {
                    agreedToTermsAndCond = "";
                }
                viewsVisibility();
                break;
            case R.id.checkbox2:
                if (checkbox2.isChecked()) {
                    agreedToPersonalData = AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss");
                } else {
                    agreedToPersonalData = "";
                }
                viewsVisibility();
                break;
            case R.id.checkbox3:
                if (checkbox3.isChecked()) {
                    agreedToInformation = AppUtility.getCurrentDateTime("yyyy-MM-dd kk:mm:ss");
                } else {
                    agreedToInformation = "0000-00-00 00:00:00";
                }

                break;
        }

    }

    private void loadTermsCond() {
        if (AppUtility.isNetworkConnected()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(AppConstant.termsconditionURL));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(browserIntent);
        } else {
            showErrorMsg(getResources().getString(R.string.bad_internet_connection), getResources().getString(R.string.noInternet));
        }
    }


    private void showErrorMsg(String msg,String titleHead) {
        AppUtility.alertDialog(this, msg,titleHead
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });

    }

    @Override
    public void onClick(View view) {

    }
}