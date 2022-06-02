package com.advantal.onebrush.denti_qus_pkg.open_camera_pkg.open_camera_view_pkg;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.ImageModel;
import com.advantal.onebrush.denti_qus_pkg.open_camera_pkg.CameraModel;

public class OpenCameraViewModel extends ViewModel {

    private final Context context;
    private final CameraModel cameraModel;

    public OpenCameraViewModel(Context context, CameraModel cameraModel) {
        this.context = context;
        this.cameraModel = cameraModel;
    }
}
