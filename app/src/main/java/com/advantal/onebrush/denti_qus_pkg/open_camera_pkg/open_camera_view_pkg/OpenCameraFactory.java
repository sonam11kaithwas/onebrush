package com.advantal.onebrush.denti_qus_pkg.open_camera_pkg.open_camera_view_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.ImageModel;
import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.gallery_view_model_pkg.GalleryViewModel;
import com.advantal.onebrush.denti_qus_pkg.open_camera_pkg.CameraModel;

public class OpenCameraFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final CameraModel cameraModel;

    public OpenCameraFactory(Context context, CameraModel cameraModel) {
        this.context = context;
        this.cameraModel = cameraModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OpenCameraViewModel(context, cameraModel);

    }
}
