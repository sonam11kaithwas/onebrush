package com.advantal.onebrush;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg.DentiWclScrActivity;
import com.advantal.onebrush.dot_indicatore_pkg.DotIndicatorAdpter;
import com.advantal.onebrush.utilies_pkg.MyCallBacks;

public class MainActivity extends AppCompatActivity {
    LinearLayout dot_layout;
    private ViewPager view_pager;
    private DotIndicatorAdpter dotIndicatorAdpter;
    private ImageView[] dots;
    private int selectedPosition = 1;
    private Button wlcm_nxt_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        double i = Math.random();
        Log.e("", "");


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


//        Locale locale = new Locale("de");
//        Configuration config = getBaseContext().getResources().getConfiguration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//

        finfViews();
    }


    private void finfViews() {
        view_pager = findViewById(R.id.view_pager);
        dot_layout = findViewById(R.id.dot_layout);
        wlcm_nxt_btn = findViewById(R.id.wlcm_nxt_btn);

        dotIndicatorAdpter = new DotIndicatorAdpter(MainActivity.this, new MyCallBacks() {
            @Override
            public void setCallBack(String str) {
                if (!str.equals(""))
                    wlcm_nxt_btn.setVisibility(View.VISIBLE);
                else wlcm_nxt_btn.setVisibility(View.INVISIBLE);
            }
        });
        view_pager.setAdapter(dotIndicatorAdpter);
        dots = new ImageView[dotIndicatorAdpter.getCount()];
        dots();
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedViewSet(position);
            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position + 1;
                Log.e("pos", "" + selectedPosition + "");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("state", "" + state + "");
//                selectedPosition=state;
            }
        });
    }


    public void dots() {
        for (int i = 0; i < dotIndicatorAdpter.getCount(); i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                    R.drawable.tab_deselector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);

            dot_layout.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(MainActivity
                        .this,
                R.drawable.selected_dot));
    }

    private void selectedViewSet(int pos) {
        for (int i = 0; i < dotIndicatorAdpter.getCount(); i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                    R.drawable.tab_deselector));
        }
        dots[pos].setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                R.drawable.selected_dot));
        if (pos == 3) {
            wlcm_nxt_btn.setText(getResources().getString(R.string.start_dental));
            wlcm_nxt_btn.setVisibility(View.INVISIBLE);
        } else {
            wlcm_nxt_btn.setText(getResources().getString(R.string.next));
            wlcm_nxt_btn.setVisibility(View.VISIBLE);

        }
    }

    public void viewClickEvent(View view) {
        switch (view.getId()) {
            case R.id.wlcm_nxt_btn:
                Log.e("", "");
                if (selectedPosition == 4) {
                    Intent intent = new Intent(this, DentiWclScrActivity.class);
                    // Intent intent = new Intent(this, SignIn_RegistrationActivity.class);
                    startActivity(intent);
                    this.finish();

                } else {
                    selectedViewSet(selectedPosition);
                    view_pager.setCurrentItem(selectedPosition);
                }

                break;
        }


    }

}