package com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.gallery_view_model_pkg;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.ImageModel;

public class GalleryViewModel extends ViewModel {
    private final Context context;
    private final ImageModel imageModel;

    public GalleryViewModel(Context context, ImageModel imageModel) {
        this.context = context;
        this.imageModel = imageModel;
    }
}
