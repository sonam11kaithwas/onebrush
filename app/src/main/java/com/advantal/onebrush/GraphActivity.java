package com.advantal.onebrush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.advantal.onebrush.R;

public class GraphActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private ImageView iv_light1, iv_light2, iv_light3, iv_light4, iv_light5, iv_light6, iv_light7, iv_light8, iv_light9;
    private ImageView iv_dark1, iv_dark2, iv_dark3, iv_dark4, iv_dark5, iv_dark6, iv_dark7, iv_dark8, iv_dark9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        seekBar = findViewById(R.id.seekbar);
        iv_light1 = findViewById(R.id.iv_light_blue);
        iv_light2 = findViewById(R.id.iv_light_purple);
        iv_light3 = findViewById(R.id.iv_light_sky);
        iv_light4 = findViewById(R.id.iv_sky);
        iv_light5 = findViewById(R.id.iv_pista);
        iv_light6 = findViewById(R.id.iv_light_grey);
        iv_light7 = findViewById(R.id.light_pink);
        iv_light8 = findViewById(R.id.light_red);
        iv_light9 = findViewById(R.id.iv_red);

        iv_dark1 = findViewById(R.id.iv_dark_blue);
        iv_dark2 = findViewById(R.id.iv_dark_purple);
        iv_dark3 = findViewById(R.id.iv_dark_sky);
        iv_dark4 = findViewById(R.id.iv_sky1);
        iv_dark5 = findViewById(R.id.iv_dark_pista);
        iv_dark6 = findViewById(R.id.iv_dark_grey);
        iv_dark7 = findViewById(R.id.dark_pink);
        iv_dark8 = findViewById(R.id.dark_red);
        iv_dark9 = findViewById(R.id.iv_red1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e("value", "" + i + "");
                switch (i) {
                    case 1:
                        iv_light1.setVisibility(View.INVISIBLE);
                        iv_dark1.setVisibility(View.VISIBLE);
                        iv_light2.setVisibility(View.VISIBLE);
                        iv_dark2.setVisibility(View.INVISIBLE);


                        break;
                    case 2:
                        iv_light2.setVisibility(View.INVISIBLE);
                        iv_dark2.setVisibility(View.VISIBLE);
                        iv_light1.setVisibility(View.VISIBLE);
                        iv_dark1.setVisibility(View.INVISIBLE);
                        iv_light3.setVisibility(View.VISIBLE);
                        iv_dark3.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        iv_light3.setVisibility(View.INVISIBLE);
                        iv_dark3.setVisibility(View.VISIBLE);
                        iv_light2.setVisibility(View.VISIBLE);
                        iv_dark2.setVisibility(View.INVISIBLE);
                        iv_light4.setVisibility(View.VISIBLE);
                        iv_dark4.setVisibility(View.INVISIBLE);
                        break;
                    case 4:

                        iv_light4.setVisibility(View.INVISIBLE);
                        iv_dark4.setVisibility(View.VISIBLE);
                        iv_light3.setVisibility(View.VISIBLE);
                        iv_dark3.setVisibility(View.INVISIBLE);
                        iv_light5.setVisibility(View.VISIBLE);
                        iv_dark5.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        iv_light5.setVisibility(View.INVISIBLE);
                        iv_dark5.setVisibility(View.VISIBLE);
                        iv_light4.setVisibility(View.VISIBLE);
                        iv_dark4.setVisibility(View.INVISIBLE);
                        iv_light6.setVisibility(View.VISIBLE);
                        iv_dark6.setVisibility(View.INVISIBLE);
                        break;
                    case 6:
                        iv_light6.setVisibility(View.INVISIBLE);
                        iv_dark6.setVisibility(View.VISIBLE);
                        iv_light5.setVisibility(View.VISIBLE);
                        iv_dark5.setVisibility(View.INVISIBLE);
                        iv_light7.setVisibility(View.VISIBLE);
                        iv_dark7.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        iv_light7.setVisibility(View.INVISIBLE);
                        iv_dark7.setVisibility(View.VISIBLE);
                        iv_light6.setVisibility(View.VISIBLE);
                        iv_dark6.setVisibility(View.INVISIBLE);
                        iv_light8.setVisibility(View.VISIBLE);
                        iv_dark8.setVisibility(View.INVISIBLE);
                        break;
                    case 8:
                        iv_light8.setVisibility(View.INVISIBLE);
                        iv_dark8.setVisibility(View.VISIBLE);
                        iv_light7.setVisibility(View.VISIBLE);
                        iv_dark7.setVisibility(View.INVISIBLE);
                        iv_light9.setVisibility(View.VISIBLE);
                        iv_dark9.setVisibility(View.INVISIBLE);
                        break;
                    case 9:
                        iv_light9.setVisibility(View.INVISIBLE);
                        iv_dark9.setVisibility(View.VISIBLE);
                        iv_light8.setVisibility(View.VISIBLE);
                        iv_dark8.setVisibility(View.INVISIBLE);
                        break;


                }




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("1", "");
                Log.e("1", "");


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("2", "");


            }
        });

    }
}