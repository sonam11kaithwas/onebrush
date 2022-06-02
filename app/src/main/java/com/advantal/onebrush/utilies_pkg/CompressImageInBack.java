package com.advantal.onebrush.utilies_pkg;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CompressImageInBack {
    private final Context context;
    //    private final Uri uriAsyn;
    OnImageCompressed onImageCompressed;
    int count = 0;
    ArrayList<Uri> uriArrayList = new ArrayList<>();
    ArrayList<String> uriStringList = new ArrayList<>();
    private String savedImagePath;
private RelativeLayout layout;
    public CompressImageInBack(Context context, OnImageCompressed onImageCompressed, ArrayList<Uri> uriArrayList, RelativeLayout layout) {
        this.context = context;
        this.onImageCompressed = onImageCompressed;
        this.uriArrayList = uriArrayList;
        this.layout=layout;
        this.count = 0;
//        this.uriAsyn = uriArrayList;
    }


    public void compressImageInBckg() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap imageBitmapFromURI = null;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AppUtility.showProgressBaForRelLay(layout,context);

                    }
                });


                imageBitmapFromURI = getImageBitmapFromURI(uriArrayList.get(count));
                if (imageBitmapFromURI != null) {//saveBitmap &&

                    saveImageToCache(imageBitmapFromURI);
                }


                final Bitmap finalImageBitmapFromURI = imageBitmapFromURI;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (onImageCompressed != null) {
//                            onImageCompressed.onImageCompressed(savedImagePath);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            count++;
                            if (uriArrayList.size() > count) {
                                AppUtility.hideProgressBar(context);

                                compressImageInBckg();
                            } else {
                                AppUtility.hideProgressBar(context);
                                onImageCompressed.onImageCompressed(uriStringList);
                            }
//                                onImageCompressed.onImageCompressed(uriStringList);
                        }

                    }
                });


            }
        });

    }




    synchronized private void saveImageToCache(Bitmap bitmap) {
        String mTimeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String mImageName = "onebrush_" + mTimeStamp + ".jpg";

        ContextWrapper wrapper = new ContextWrapper(context);

        File file = wrapper.getDir("Images", MODE_PRIVATE);

        file = new File(file, mImageName);

        try {

            OutputStream stream = null;

            stream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            stream.flush();

            stream.close();
            Log.e("", "Filed Saved Successfully");

            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri mImageUri = Uri.parse(file.getAbsolutePath());
        savedImagePath = mImageUri.getPath();
        uriStringList.add(savedImagePath);
    }

    synchronized private Bitmap getImageBitmapFromURI(Uri imagePath) {
        ContentResolver resolver = context.getContentResolver();
        InputStream is;
        try {
            is = resolver.openInputStream(imagePath);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "Image not found.", e);
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);

        // scale the image
        float maxSideLength = 1000;
        float scaleFactor = Math.min(maxSideLength / opts.outWidth, maxSideLength / opts.outHeight);
        // do not upscale!
        if (scaleFactor < 1) {
            opts.inDensity = 10000;
            opts.inTargetDensity = (int) ((float) opts.inDensity * scaleFactor);
        }
        opts.inJustDecodeBounds = false;

        try {
            is.close();
        } catch (IOException e) {
            // ignore
            Log.e("TAG", "Image not found.", e);

        }
        try {
            is = resolver.openInputStream(imagePath);
        } catch (FileNotFoundException e) {
            Log.e("TAG", "Image not found.", e);
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
        try {
            is.close();
        } catch (IOException e) {
            // ignore
        }

        return bitmap;

    }

    public interface OnImageCompressed {
                void onImageCompressed(ArrayList<String> uriArrayList);
//        void onImageCompressed(String uriArrayList);
    }

}

