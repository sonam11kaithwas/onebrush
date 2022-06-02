package com.advantal.onebrush.denti_qus_pkg;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;

public class CustomProgressActivity extends AppCompatActivity {
    private final int progr = 0;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress);

        progress_bar = findViewById(R.id.progress_bar);

//        findViewById(R.id.button_incr).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (progr <= 90) {
//                    progr += 10;
//                    updateProgressBar();
//                }else {
//                    progress_bar.setProgressDrawable(ContextCompat.getDrawable(CustomProgressActivity.this,
//                            R.drawable.progress_colo_bg));
//                }
//            }
//        });
//        updateProgressBar();
    }

    private void updateProgressBar() {
        progress_bar.setProgress(progr);
    }
}