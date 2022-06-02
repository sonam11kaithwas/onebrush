package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.case_picture_pkg;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.advantal.onebrush.R;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Surbhi on 2022-03-28 04:46 PM.
 */

public class IndicatorAdapter extends PagerAdapter {
    private final Context context;
    private final LayoutInflater layoutInflater;
    View view;
    ImageView img;
    openGalleryImage2 openGalleryImage;
    List<XRayImageListModl> galleryImageList = new ArrayList<>();
    List<XRayImageListModl> galleryImageListForAttchment = new ArrayList<>();
    private ImageView imageView;
    private PictureAdapter pictureAdapter;

    public IndicatorAdapter(Context context, List<XRayImageListModl> galleryImageList, openGalleryImage2 openGalleryImage) {
        this.context = context;
        this.openGalleryImage = openGalleryImage;
        this.galleryImageList = galleryImageList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return galleryImageList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = null;
        if (position == 0) {
            itemView = layoutInflater.inflate(R.layout.slider_layout, container, false);
            ImageView imageView = itemView.findViewById(R.id.myimage);
            imageView.setImageResource(R.drawable.panlocator);
            Objects.requireNonNull(container).addView(itemView);
        } else {

            itemView = layoutInflater.inflate(R.layout.slider_layout, container, false);
            RecyclerView recyclerView = itemView.findViewById(R.id.recycler);
            recyclerView.setVisibility(View.VISIBLE);
            ImageView imageView = itemView.findViewById(R.id.myimage);
            imageView.setVisibility(View.GONE);

            if (galleryImageList.size() > 0) {
                galleryImageListForAttchment.clear();

                galleryImageListForAttchment.addAll(galleryImageList);

                galleryImageListForAttchment.remove(0);
            }


//            if (pictureAdapter == null) {
                pictureAdapter = new PictureAdapter(context, new PictureAdapter.openGalleryImage() {
                    @Override
                    public void setGalaryPicImg(View thumbView, Drawable bmp, String img_url) {
                        openGalleryImage.setGalaryPicImg(thumbView, bmp, img_url);


                    }
                });
                recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
                recyclerView.setAdapter(pictureAdapter);
                pictureAdapter.notifyDataSetChanged();
//            }
            Objects.requireNonNull(container).addView(itemView);
        }/* else if (position == 2) {
            itemView = layoutInflater.inflate(R.layout.slider_layout, container, false);
            ImageView imageView = itemView.findViewById(R.id.myimage);
            imageView.setImageResource(R.drawable.camera);

            Objects.requireNonNull(container).addView(itemView);
        }*/


        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      /*  View view = (View) object;
        container.removeView(view);*/
        container.removeView((RelativeLayout) object);
//remove page

    }

    public interface openGalleryImage2 {
        void setGalaryPicImg(View thumbView, Drawable bmp, String img_url);
    }


}
