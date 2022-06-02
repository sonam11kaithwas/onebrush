package com.advantal.onebrush.utilies_pkg;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * Created by Sonam on 25-05-2022 19:20.
 */
public class MyTransformation extends BitmapTransformation {
    private int mOrientation = 0;


    public MyTransformation(Context context, int orientation) {
//        super(context);
        mOrientation = orientation;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int exifOrientationDegrees = getExifOrientationDegrees(mOrientation);
//        return TransformationUtils.rotateImageExif(toTransform, pool, exifOrientationDegrees);
        return TransformationUtils.rotateImageExif(pool, toTransform, exifOrientationDegrees);
    }

    private int getExifOrientationDegrees(int orientation) {
        int exifInt;
        switch (orientation) {
            case 90:
                exifInt = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            // other cases
            default:
                exifInt = ExifInterface.ORIENTATION_NORMAL;
                break;
        }
        return exifInt;
    }
//https://stackoverflow.com/questions/34811415/how-to-rotate-image-using-glide-library-like-in-picasso
    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}

