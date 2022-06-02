package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.case_picture_pkg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surbhi on 2022-03-29 05:36 PM.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyView> {
    private final Context mContext;
    private final openGalleryImage openGalleryImage;
    List<XRayImageListModl> galleryImageList = new ArrayList<>();


    public PictureAdapter(Context mContext,
                          openGalleryImage openGalleryImage) {
//    public PictureAdapter(Context mContext, List<XRayImageListModl> galleryImageList, openGalleryImage openGalleryImage) {
        this.mContext = mContext;
        this.galleryImageList = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).xRayImageDao().getAllXRaysListByCaseId(OneBrushAppPrefrence.getSharedprefInstance().getSelectedCaseId());;
//        if (galleryImageList.size()>0){
//            galleryImageList.remove(0);
//        }
        this.openGalleryImage = openGalleryImage;

    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.show_picture, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(mContext).load(galleryImageList.get(position).getImgUrl())
                    .placeholder(R.drawable.welcome).into(holder.imageView);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openGalleryImage.setGalaryPicImg(view, ((ImageView) view).getDrawable(), galleryImageList.get(position).getImgUrl());
                }
            });

//        }
    }

    private Bitmap convertInBitMap(Uri image) {
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = OneBrushApp.getInstance().getContentResolver().query(image, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
            Log.e("", "");
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public int getItemCount() {
        return galleryImageList.size();
    }

    public interface openGalleryImage {
        void setGalaryPicImg(View thumbView, Drawable bmp, String img_url);
    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView imageView, imageView_pic;
        LinearLayout multipleImages;

        public MyView(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_profilePic);
            multipleImages = itemView.findViewById(R.id.multipleImages);
        }
    }
}
