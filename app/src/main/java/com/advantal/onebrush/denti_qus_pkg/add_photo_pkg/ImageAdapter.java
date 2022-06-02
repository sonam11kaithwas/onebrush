package com.advantal.onebrush.denti_qus_pkg.add_photo_pkg;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyHolder> {

    private final RemoveDataForGallery removeDataForGallery;
    private ArrayList<String> galleryImageList;

    public ImageAdapter(ArrayList<String> galleryImageList, RemoveDataForGallery removeDataForGallery) {
        this.galleryImageList = galleryImageList;
        this.removeDataForGallery = removeDataForGallery;
    }

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.select_photo_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        Bitmap bitmap = (convertInBitMap(galleryImageList.get(position)));
        if (bitmap != null) {
            holder.imageView_select.setImageBitmap(bitmap);
//            holder.imageView_select.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        holder.imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeDataForGallery != null) {
                    removeDataForGallery.setGalaryRmvPos(position, galleryImageList.get(position));
                }
            }
        });
    }

    public void updateGalleryDataList(ArrayList<String> galleryImageList) {
        this.galleryImageList = galleryImageList;
        notifyDataSetChanged();
    }

//    public void updateGalleryDataListForAnswer(ArrayList<String> galleryImageList) {
//        this.galleryImageList=(galleryImageList);
//        notifyDataSetChanged();
//    }

    private Bitmap convertInBitMap(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return galleryImageList.size();
    }


    public interface RemoveDataForGallery {
        void setGalaryRmvPos(int pos, String url);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView_select;
        ImageView imageView_delete;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView_select = itemView.findViewById(R.id.img_profilePic);
            imageView_delete = itemView.findViewById(R.id.img_profilePic_delete);
        }
    }
}
