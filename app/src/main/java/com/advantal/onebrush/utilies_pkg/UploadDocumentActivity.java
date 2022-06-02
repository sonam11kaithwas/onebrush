package com.advantal.onebrush.utilies_pkg;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UploadDocumentActivity extends AppCompatActivity {
    private String captureImagePath;
    ActivityResultLauncher<Intent> someActivityResultLauncherForCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            File file = AppUtility.scaleToActualAspectRatio(captureImagePath, 1024f, 1024f);
                            if (file != null) {
                                Uri uri = Uri.fromFile(new File(captureImagePath));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        return;
                    }
                }
            });
    private ArrayList<String> uriArrayList = new ArrayList<>();
    ActivityResultLauncher<Intent> someActivityResultLauncherForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent.getClipData() != null) {
                            int x = intent.getClipData().getItemCount();
                            for (int i = 0; i < x; i++) {
                                Uri selectedImageURI = result.getData().getClipData().getItemAt(i).getUri();
//                                String realPaths = getRealPathFromURI(selectedImageURI);

                                String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, selectedImageURI);

//                                File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
//                                String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null

//                ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//                byte [] b=baos.toByteArray();
//                String temp=Base64.encodeToString(b, Base64.DEFAULT);
                                if (!uriArrayList.contains(realPaths)) {
                                    uriArrayList.add(realPaths);
                                }

                            }
                            onDocumentSelected(uriArrayList);


                        }

                    } else if (result.getData() != null) {

                        if (result.getData().getClipData() != null) {

                            int x = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < x; i++) {
                                Uri selectedImageURI = result.getData().getClipData().getItemAt(i).getUri();

                                String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, selectedImageURI);

//                                File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
//                                String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null


                                if (!uriArrayList.contains(realPaths)) {
                                    uriArrayList.add(realPaths);
                                }
                            }
                        }

                        onDocumentSelected(uriArrayList);
                    }


                }
            });

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public ArrayList<String> getUriArrayList() {
        return uriArrayList;
    }

    public void setUriArrayList(ArrayList<String> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    public void removeFromList(String url) {
        if (uriArrayList != null && uriArrayList.size() != 0) {
            uriArrayList.remove(url);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

    }

    public void getImageFromGallery() {
        if (!AppUtility.askGalaryTakeImagePermiSsion(UploadDocumentActivity.this))
            return;

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        /*this for vedio loading*/
//        galleryIntent.setType("image/* video/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        someActivityResultLauncherForGallery.launch(galleryIntent);
    }

    public void takeImageFromCamera() {
        if (AppUtility.askCameraTakePicture(UploadDocumentActivity.this)) {
            getPictureFromCamera();
        }
    }

    public void getPictureFromCamera() {
        if (!AppUtility.askCameraTakePicture(UploadDocumentActivity.this)) {
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ONEBRUSH Directory");

        if (!path.exists()) {
            path.mkdir();
        }

        File imageFile = null;
        try {
            imageFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", imageFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;


            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

//        startActivityForResult(takePictureIntent, CAMERA_CODE); // IMAGE_CAPTURE = 0
        someActivityResultLauncherForCamera.launch(takePictureIntent);

    }

    private File createImageFile() throws IOException {
        Calendar calendar = Calendar.getInstance();
        long imageFileName = calendar.getTime().getTime();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ONEBRUSH Directory");

        File directoryPath = new File(storageDir.getPath());
        File image = File.createTempFile(
                String.valueOf(imageFileName),  /* prefix */
                ".jpg",         /* suffix */
                directoryPath   /* directory */
        );
        captureImagePath = image.getAbsolutePath();
        return new File(image.getPath());
    }


    public void onDocumentSelected(ArrayList<String> uriArrayList) {
    }
}


//package com.advantal.onebrush.utilies_pkg;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.util.Base64;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.ByteArrayOutputStream;
//import java.util.ArrayList;
//
//public class UploadDocumentActivity extends AppCompatActivity {
//    private static final int PHOTO_EDIT_CODE = 147;
//    private final static int CAPTURE_IMAGE_GALLARY = 222;
//    private final int CAMERA_CODE = 100;
//    private final int ATTACHFILE_CODE = 102;
//    private final String path = "";
//    private String captureImagePath;
//    private ArrayList<String> uriArrayList = new ArrayList<>();
//    private final ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
//
//    public void selectFile() {
//
//        if (AppUtility.askGalaryTakeImagePermiSsion(UploadDocumentActivity.this)) {
//            getImageFromGallery();
//        }
//
//    }
//
//
//    public void getImageFromGallery() {
//        if (!AppUtility.askGalaryTakeImagePermiSsion(UploadDocumentActivity.this))
//            return;
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, CAPTURE_IMAGE_GALLARY);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        switch (requestCode) {
//            case CAPTURE_IMAGE_GALLARY:
//                if (resultCode == RESULT_OK) {
//                    Uri galreyImguriUri = data.getData();
//                    // String gallery_image_Path = PathUtils.getPath(UploadDocumentActivity.this, galreyImguriUri);
//                    String gallery_image_Path = PathUtils.getRealPath(UploadDocumentActivity.this, galreyImguriUri);
//                    String img_extension = gallery_image_Path.substring(gallery_image_Path.lastIndexOf("."));
//                    /******('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions*/
//                    if (img_extension.equals(".jpg") || img_extension.equals(".png") || img_extension.equals(".jpeg")) {
//                        imageEditing(data.getData());
//                    }
//
//                } else {
//                    return;
//                }
//                break;
//
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    /*****upload image edit highlighting feature for image********/
//    private void imageEditing(Uri uri) {
//        new CompressImageInBack(UploadDocumentActivity.this, new CompressImageInBack.OnImageCompressed() {
//            @Override
//            public void onImageCompressed(Bitmap bitmap, Uri uriAsyn) {
//                ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//                byte [] b=baos.toByteArray();
//                String temp=Base64.encodeToString(b, Base64.DEFAULT);
//                uriArrayList.add(temp);
//                bitmapArrayList.add(bitmap);
//
//                onDocumentSelected(uriArrayList, bitmapArrayList);
//            }
//        }, uri).compressImageInBckg();
//    }
//
//    public ArrayList<String> getUriArrayList() {
//        return uriArrayList;
//    }
//
//    public void setUriArrayList(ArrayList<String> uriArrayList) {
//        this.uriArrayList = uriArrayList;
//    }
//
//    public void removeFromList(String url) {
//        if (uriArrayList != null && uriArrayList.size() != 0) {
//            uriArrayList.remove(url);
//        }
//
//    }
//
//    public void onDocumentSelected(ArrayList<String> uriArrayList, ArrayList<Bitmap> bitmapArrayList) {
//    }
//}
//
//
////package com.advantal.onebrush.utilies_pkg;
////
////import android.app.Activity;
////import android.content.ClipData;
////import android.content.Intent;
////import android.content.pm.PackageManager;
////import android.content.pm.ResolveInfo;
////import android.graphics.Bitmap;
////import android.net.Uri;
////import android.os.Bundle;
////import android.os.Environment;
////import android.provider.MediaStore;
////import android.util.Log;
////import android.view.View;
////import android.widget.Toast;
////
////import androidx.activity.result.ActivityResult;
////import androidx.activity.result.ActivityResultCallback;
////import androidx.activity.result.ActivityResultLauncher;
////import androidx.activity.result.contract.ActivityResultContracts;
////import androidx.annotation.Nullable;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.content.FileProvider;
////
////import java.io.File;
////import java.io.IOException;
////import java.util.ArrayList;
////import java.util.Calendar;
////import java.util.List;
////
////public class UploadDocumentActivity extends AppCompatActivity {
////    private static final int PHOTO_EDIT_CODE = 147;
////    private final static int CAPTURE_IMAGE_GALLARY = 222;
////    private final int CAMERA_CODE = 100;
////    private final int ATTACHFILE_CODE = 102;
////    private final String path = "";
////    int PICK_IMAGE_MULTIPLE = 1;
////
////
////    private String captureImagePath;
////    ActivityResultLauncher<Intent> someActivityResultLauncherForCamera = registerForActivityResult(
////            new ActivityResultContracts.StartActivityForResult(),
////            new ActivityResultCallback<ActivityResult>() {
////                @Override
////                public void onActivityResult(ActivityResult result) {
////
////                    if (result.getResultCode() == Activity.RESULT_OK) {
////                        try {
////                            File file = AppUtility.scaleToActualAspectRatio(captureImagePath, 1024f, 1024f);
////                            if (file != null) {
////                                Uri uri = Uri.fromFile(new File(captureImagePath));
////                            }
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    } else {
////                        return;
////                    }
////                }
////            });
////    private ArrayList<String> uriArrayList = new ArrayList<>();
////    ActivityResultLauncher<Intent> someActivityResultLauncherForGallery = registerForActivityResult(
////            new ActivityResultContracts.StartActivityForResult(),
////            new ActivityResultCallback<ActivityResult>() {
////                @Override
////                public void onActivityResult(ActivityResult result) {
////                    if (result.getResultCode() == Activity.RESULT_OK) {
////                        Intent intent = result.getData();
////                        if (intent.getClipData() != null) {
////                            int x = intent.getClipData().getItemCount();
////                            for (int i = 0; i < x; i++) {
////                                Uri selectedImageURI = result.getData().getClipData().getItemAt(i).getUri();
////
////
////                                new CompressImageInBack(UploadDocumentActivity.this, new CompressImageInBack.OnImageCompressed() {
////                                    @Override
////                                    public void onImageCompressed(Bitmap bitmap, Uri uriAsyn) {
////                                        String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, uriAsyn);
////                                        File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
////                                        String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null
////
////
////                                        if (!uriArrayList.contains(realPath)) {
////                                            uriArrayList.add(realPath);
////                                        }
////
////                                    }
////                                }, selectedImageURI
////                                ).compressImageInBckg();
////
////
//////                                String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, selectedImageURI);
////
////                                /*File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
////                                String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null
////
////
////                                if (!uriArrayList.contains(realPath)) {
////                                    uriArrayList.add(realPath);
////                                }
////*/
////                            }
////                            onDocumentSelected(uriArrayList);
////
////
////                        }
////
////                    } else if (result.getData() != null) {
////
////                        if (result.getData().getClipData() != null) {
////
////                            int x = result.getData().getClipData().getItemCount();
////                            for (int i = 0; i < x; i++) {
////                                Uri selectedImageURI = result.getData().getClipData().getItemAt(i).getUri();
////
////                                new CompressImageInBack(UploadDocumentActivity.this, new CompressImageInBack.OnImageCompressed() {
////                                    @Override
////                                    public void onImageCompressed(Bitmap bitmap, Uri uriAsyn) {
////                                        String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, uriAsyn);
////                                        File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
////                                        String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null
////
////
////                                        if (!uriArrayList.contains(realPath)) {
////                                            uriArrayList.add(realPath);
////                                        }
////
////                                    }
////                                }, selectedImageURI
////                                ).compressImageInBckg();
////
////
////                              /*  String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, selectedImageURI);
////
////                                File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
////                                String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null
////
////
////                                if (!uriArrayList.contains(realPath)) {
////                                    uriArrayList.add(realPath);
////                                }*/
////                            }
////                        }
////
////                        onDocumentSelected(uriArrayList);
////                    } else {
////                        onDocumentSelected(uriArrayList);
////
////                    }
////
////
////                }
////            });
////
////
////    public ArrayList<String> getUriArrayList() {
////        return uriArrayList;
////    }
////
////    public void setUriArrayList(ArrayList<String> uriArrayList) {
////        this.uriArrayList = uriArrayList;
////    }
////
////    public void removeFromList(String url) {
////        if (uriArrayList != null && uriArrayList.size() != 0) {
////            uriArrayList.remove(url);
////        }
////
////    }
////
////    @Override
////    protected void onPause() {
////        super.onPause();
////    }
////
////    @Override
////    public void onBackPressed() {
////        super.onBackPressed();
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////    }
////
////    @Override
////    protected void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
////
////    }
////
////    public void getImageFromGallery() {
////        if (!AppUtility.askGalaryTakeImagePermiSsion(UploadDocumentActivity.this))
////            return;
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
////
////        /*if (!AppUtility.askGalaryTakeImagePermiSsion(UploadDocumentActivity.this))
////            return;
////
////        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//////        Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////
//////        galleryIntent.setType("image/* video/*");
////        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////        someActivityResultLauncherForGallery.launch(galleryIntent);*/
////    }
////
////    public void takeImageFromCamera() {
////        if (AppUtility.askCameraTakePicture(UploadDocumentActivity.this)) {
////            getPictureFromCamera();
////        }
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        switch (requestCode) {
////
////            case 1:
////            /*    if (resultCode == RESULT_OK) {
////                    Uri galreyImguriUri = data.getData();
////                    String gallery_image_Path = PathUtils.getRealPath(UploadDocumentActivity.this, galreyImguriUri);
////                    String img_extension = gallery_image_Path.substring(gallery_image_Path.lastIndexOf("."));
////                    *//******('jpg','png','jpeg','pdf','doc','docx','xlsx','csv','xls'); supporting extensions*//*
////                    if (img_extension.equals(".jpg") || img_extension.equals(".png") || img_extension.equals(".jpeg")) {
////                        imageEditing(data.getData());
////                    }
////                } else {
////                    return;
////                }*/
////
////                try {
////                    // When an Image is picked
////                    if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
////                            && null != data) {
////                        // Get the Image from data
////
////                        if (data.getData() != null) {
////
////                            Uri mImageUri = data.getData();
//////                            String gallery_image_Path = PathUtils.getRealPath(UploadDocumentActivity.this, mImageUri);
////
////
////                            imageEditing(mImageUri);
//////                            if (!uriArrayList.contains(gallery_image_Path)) {
//////                                uriArrayList.add(gallery_image_Path);
//////                            }
//////
////                            onDocumentSelected(uriArrayList);
////
////
////                        } else {
////                            if (data.getClipData() != null) {
////                                ClipData mClipData = data.getClipData();
////                                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
////                                for (int i = 0; i < mClipData.getItemCount(); i++) {
////
////                                    ClipData.Item item = mClipData.getItemAt(i);
////                                    Uri uri = item.getUri();
////                                    imageEditing(uri);
////
////                                  /*  mArrayUri.add(uri);
////                                    String gallery_image_Path = PathUtils.getRealPath(UploadDocumentActivity.this, uri);
////
////                                    if (!uriArrayList.contains(gallery_image_Path)) {
////                                        uriArrayList.add(gallery_image_Path);
////                                    }*/
////
////                                }
////
////                                onDocumentSelected(uriArrayList);
////
////                                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
////                            }
////                        }
////                    } else {
////                        Toast.makeText(this, "You haven't picked Image",
////                                Toast.LENGTH_LONG).show();
////                    }
////                } catch (Exception e) {
////                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
////                            .show();
////                }
////                break;
////
////
////        }
////        super.onActivityResult(requestCode, resultCode, data);
////    }
////
////    private void imageEditing(Uri data) {
////        try {
////
////            new CompressImageInBack(UploadDocumentActivity.this, new CompressImageInBack.OnImageCompressed() {
////                @Override
////                public void onImageCompressed(Bitmap bitmap, Uri uriAsyn) {
////                    String realPaths = PathUtils.getRealPath(UploadDocumentActivity.this, uriAsyn);
////                    File file = AppUtility.scaleToActualAspectRatio(realPaths, 1024f, 1024f);
////                    String realPath = file.getAbsolutePath(); // Only return path if physical file exist else return null
////
////
////                    if (!uriArrayList.contains(realPath)) {
////                        uriArrayList.add(realPath);
////                    }
////
////                    onDocumentSelected(uriArrayList);
////
////
////                }
////            }, data
////            ).compressImageInBckg();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    public void getPictureFromCamera() {
////        if (!AppUtility.askCameraTakePicture(UploadDocumentActivity.this)) {
////            return;
////        }
////
////        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////
////        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ONEBRUSH Directory");
////
////        if (!path.exists()) {
////            path.mkdir();
////        }
////
////        File imageFile = null;
////        try {
////            imageFile = createImageFile();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////
////        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", imageFile);
////
////        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
////        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////
////        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
////        for (ResolveInfo resolveInfo : resInfoList) {
////            String packageName = resolveInfo.activityInfo.packageName;
////
////
////            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////        }
////
//////        startActivityForResult(takePictureIntent, CAMERA_CODE); // IMAGE_CAPTURE = 0
////        someActivityResultLauncherForCamera.launch(takePictureIntent);
////
////    }
////
////    private File createImageFile() throws IOException {
////        Calendar calendar = Calendar.getInstance();
////        long imageFileName = calendar.getTime().getTime();
////        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ONEBRUSH Directory");
////
////        File directoryPath = new File(storageDir.getPath());
////        File image = File.createTempFile(
////                String.valueOf(imageFileName),  /* prefix */
////                ".jpg",         /* suffix */
////                directoryPath   /* directory */
////        );
////        captureImagePath = image.getAbsolutePath();
////        return new File(image.getPath());
////    }
////
////
////    public void onDocumentSelected(ArrayList<String> uriArrayList) {
////    }
////}
