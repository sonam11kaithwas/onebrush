package com.advantal.onebrush.denti_qus_pkg.add_photo_pkg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivityMultiplePhotoBinding;
import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.gallery_view_model_pkg.GalleryFactory;
import com.advantal.onebrush.denti_qus_pkg.add_photo_pkg.gallery_view_model_pkg.GalleryViewModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

import java.util.ArrayList;

public class MultiplePhotoActivity extends AppCompatActivity {

    private static final int Read_Permission = 101;
    ArrayList<Uri> uri = new ArrayList<>();
    ImageAdapter adapter;
    LinearLayoutManager layoutManager;
    private GalleryViewModel galleryViewModel;
    private ActivityMultiplePhotoBinding binding;
    private ImageModel imageModel;
    private UserAccountDetailsModel userAccountDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_multiple_photo);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_multiple_photo);
        galleryViewModel = ViewModelProviders.of(this, new GalleryFactory(this
                , new ImageModel(R.drawable.ic_img))).get(GalleryViewModel.class);
        binding.setGalleryModel(galleryViewModel);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());

        AppUtility.updateScreeState("MultiplePhotoActivity", userAccountDetailsModel);
        binding.recyclerview.findViewById(R.id.recyclerview);
        binding.ibUpload.findViewById(R.id.ib_upload);

//        adapter = new ImageAdapter(uri, new ImageAdapter.RemoveDataForGallery() {
//            @Override
//            public void setGalaryRmvPos(int pos) {
//
//            }
//        });
        binding.recyclerview.setLayoutManager(new GridLayoutManager(MultiplePhotoActivity.this, 4));
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public void onClickImages(View view) {
        switch (view.getId()) {
            case R.id.ib_upload:
//                getImageFromGallery();

                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
        }
    }

    private void getImageFromGallery() {
        if (!AppUtility.askGalaryTakeImagePermiSsion(MultiplePhotoActivity.this))
            return;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

//        someActivityResultLauncher.launch(galleryIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }


                adapter.notifyDataSetChanged();
            }

        } else if (data.getData() != null) {
            String imageURL = data.getData().getPath();
            uri.add(Uri.parse(imageURL));
        }


    }
}