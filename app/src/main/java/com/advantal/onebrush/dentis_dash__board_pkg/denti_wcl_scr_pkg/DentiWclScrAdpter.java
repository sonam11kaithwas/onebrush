package com.advantal.onebrush.dentis_dash__board_pkg.denti_wcl_scr_pkg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.advantal.onebrush.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam on 10-02-2022 12:04.
 */
public class DentiWclScrAdpter extends PagerAdapter {
    private final Context context;
    private View view;
    private TextView txt1, txt2;
    private ImageView img;
    private LayoutInflater layoutInflater;
    private List<WelcomeCarouselModel> welcomeCarouselModelList = new ArrayList<>();

    public DentiWclScrAdpter(Context context, List<WelcomeCarouselModel> welcomeCarouselModelList) {
        this.context = context;
        this.welcomeCarouselModelList = welcomeCarouselModelList;
    }

    @Override
    public int getCount() {
        return welcomeCarouselModelList.size();  //no of pages
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.bio_metr_adpter_layout, container, false);

        txt1 = view.findViewById(R.id.txt_heading);
        txt2 = view.findViewById(R.id.txt_details);
        img = view.findViewById(R.id.img);

        if (welcomeCarouselModelList.size() > 0) {
            WelcomeCarouselModel model = welcomeCarouselModelList.get(position);
            if (model != null) {
                if (model.getTitle_font_color() != null && !model.getTitle_font_color().isEmpty()) {
                    try {
                        String white = model.getTitle_font_color();
                        int whiteInt = Color.parseColor(white);
                        Log.e("", "");
                        txt1.setTextColor(whiteInt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (model.getTitle() != null && !model.getDescriptions().equals("")) {
                    if (model.getTitle_font_size() != null && !model.getTitle_font_size().isEmpty())
                        txt1.setTextSize(Float.parseFloat(model.getTitle_font_size()));
//                    if (model.getTitle_font_color()!=null&&!model.getTitle_font_color().isEmpty())
//                        txt1.setTextColor(Integer.parseInt(model.getTitle_font_color()));

                    if (model.getTitle_font_style() != null && !model.getTitle_font_style().isEmpty()) {
                        if (model.getTitle_font_style().equalsIgnoreCase("italic")) {
                            txt1.setTypeface(txt1.getTypeface(), Typeface.ITALIC);

                        } else if (model.getTitle_font_style().equalsIgnoreCase("bold")) {
                            txt1.setTypeface(txt1.getTypeface(), Typeface.BOLD);

                        } else if (model.getTitle_font_style().equalsIgnoreCase("normal")) {
                            txt1.setTypeface(txt1.getTypeface(), Typeface.NORMAL);

                        }
                    }
                    txt1.setText(model.getTitle());
                } else {
                    txt2.setText("Not title found");
                }
                if (model.getDescriptions() != null && !model.getDescriptions().equals("")) {
                    if (model.getDescriptions_font_size() != null && !model.getDescriptions_font_size().isEmpty())
                        txt2.setTextSize(Float.parseFloat(model.getDescriptions_font_size()));
//                    if (model.getDescriptions_font_color()!=null&&!model.getDescriptions_font_color().isEmpty())
//                        txt2.setTextColor(Integer.parseInt(model.getDescriptions_font_color()));

                    if (model.getDescriptions_font_style() != null && !model.getDescriptions_font_style().isEmpty()) {
                        if (model.getDescriptions_font_style().equalsIgnoreCase("italic")) {
                            txt2.setTypeface(txt2.getTypeface(), Typeface.ITALIC);

                        } else if (model.getDescriptions_font_style().equalsIgnoreCase("bold")) {
                            txt2.setTypeface(txt2.getTypeface(), Typeface.BOLD);

                        } else if (model.getDescriptions_font_style().equalsIgnoreCase("normal")) {
                            txt2.setTypeface(txt2.getTypeface(), Typeface.NORMAL);

                        }
                    }

                    if (model.getDescriptions_font_color() != null && !model.getDescriptions_font_color().isEmpty()) {
                        try {
                            String white = model.getDescriptions_font_color();
                            int whiteInt = Color.parseColor(white);
                            Log.e("", "");
                            txt2.setTextColor(whiteInt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    txt2.setText(model.getDescriptions());
                } else {
                    txt2.setText("Not descripption found");

                }

                if (model.getImgUrl() != null && !model.getImgUrl().isEmpty())
                    Glide.with(context).load(model.getImgUrl())
                            .placeholder(R.drawable.wcl_img).into(img);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            container.addView(view);
        }
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);  //remove page
    }

    public void updateData(List<WelcomeCarouselModel> welcomeCarouselModels) {
        this.welcomeCarouselModelList = welcomeCarouselModels;
        this.notifyDataSetChanged();
    }
}


