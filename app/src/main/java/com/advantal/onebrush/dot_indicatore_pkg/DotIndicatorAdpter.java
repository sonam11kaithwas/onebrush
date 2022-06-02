package com.advantal.onebrush.dot_indicatore_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.advantal.onebrush.R;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.MyCallBacks;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;


public class DotIndicatorAdpter extends PagerAdapter implements View.OnClickListener {

    private final Context context;
    public String[] headings;
    View view;
    int[] layout_images = {R.drawable.illustration,
            R.drawable.iphone_12_pro_mockup_label
            , R.drawable.mockup
            , R.drawable.mockup
    };
    String[] txt_contains;
    private TextView txt1, txt2;
    private ImageView imageView;
    private LayoutInflater layoutInflater;
    private CardView card_view, card_view2, card_view_click;
    private final MyCallBacks myCallBacks;

    public DotIndicatorAdpter(Context context, MyCallBacks myCallBacks) {
        this.context = context;
        this.txt_contains = AppConstant.temptxt_contains;
        this.headings = AppConstant.tempHead_contains;
        this.myCallBacks = myCallBacks;
    }

    @Override
    public int getCount() {
        return layout_images.length;  //no of pages
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);   //comapre instaces
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (position == 3) {
            view = layoutInflater.inflate(R.layout.cardview_layout, container, false);

            card_view = view.findViewById(R.id.card_view);
            card_view2 = view.findViewById(R.id.card_view2);
            card_view_click = view.findViewById(R.id.card_view_click);
            card_view.setOnClickListener(this);
            card_view_click.setOnClickListener(this);
            card_view2.setOnClickListener(this);


        } else {
            view = layoutInflater.inflate(R.layout.customlayout, container, false);
            imageView = view.findViewById(R.id.images);
            txt1 = view.findViewById(R.id.txt_heading);
            txt2 = view.findViewById(R.id.txt_details);
            imageView.setImageResource(layout_images[position]);
            txt1.setText(headings[position]);
            txt2.setText(txt_contains[position]);
        }
        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);  //remove page

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view:
                if (myCallBacks != null) {
                    card_view_click.setVisibility(View.VISIBLE);
                    card_view.setVisibility(View.INVISIBLE);
                    myCallBacks.setCallBack("SHOW");
                }
                break;
            case R.id.card_view_click:
                if (myCallBacks != null) {
                    card_view_click.setVisibility(View.INVISIBLE);
                    card_view.setVisibility(View.VISIBLE);
                    myCallBacks.setCallBack("");
                }
                break;
            case R.id.card_view2:
                OneBrushApp.getInstance().showToastmsg("Coming soon this feature");
                break;
        }
    }


}


