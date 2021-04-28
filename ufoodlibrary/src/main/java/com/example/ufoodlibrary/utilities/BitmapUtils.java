package com.example.ufoodlibrary.utilities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Alfonso on 02-Apr-16.
 */
public  class BitmapUtils {

    private static final float BYTES_PER_PX = 4.0f;


    public static void loadImage(int id,ImageView image) {
        if(readBitmapInfo(id) > MemUtils.megabytesFree()) {
            subSampleImage(image,id,32);
        } else {
            image.setImageResource(id);
        }
    }

    private static float readBitmapInfo(int id) {
        final Resources res = G3Application.getAppContext().getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        final float imageHeight = options.outHeight;
        final float imageWidth = options.outWidth;
        final String imageMimeType = options.outMimeType;

        Log.d("ScaleBeforeLoad", "w, h, type: " + imageWidth + ", " + imageHeight + ", " + imageMimeType);
        Log.d("ScaleBeforeLoad", "estimated memory required in MB: " + imageWidth * imageHeight * BYTES_PER_PX / MemUtils.BYTES_IN_MB);

        return imageWidth * imageHeight * BYTES_PER_PX / MemUtils.BYTES_IN_MB;
    }

    private static void subSampleImage(ImageView image,int id, int powerOf2) {
        if(powerOf2 < 1 || powerOf2 > 32) {
            Log.e("ScaleBeforeLoad", "trying to appply upscale or excessive downscale: " + powerOf2);
            return;
        }

        final Resources res = G3Application.getAppContext().getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = powerOf2;
        final Bitmap bmp = BitmapFactory.decodeResource(res, id, options);

        image.setImageBitmap(bmp);
    }
}
