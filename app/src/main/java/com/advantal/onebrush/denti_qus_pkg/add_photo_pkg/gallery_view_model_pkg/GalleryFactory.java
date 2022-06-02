package com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.gallery_view_model_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.ImageModel;

public class GalleryFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final ImageModel imageModel;

    public GalleryFactory(Context context, ImageModel imageModel) {
        this.context = context;
        this.imageModel = imageModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GalleryViewModel(context, imageModel);
    }
}
