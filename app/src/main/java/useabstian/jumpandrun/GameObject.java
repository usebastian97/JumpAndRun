package useabstian.jumpandrun;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class GameObject {

    Context context;
    int gameWidth;
    int gameHeight;

    int bitmapWidth;
    int bitmapHeight;


    GameObject(Context context, int gameHeight, int gameWidth){

        this.context = context;
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

    }


    Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                           int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        float ratio = (float) imageWidth / (float) imageHeight;

        if(reqHeight == -1){
            reqHeight = (int)(reqWidth / ratio);
        } else if (reqWidth == -1){
            reqWidth = (int)(reqHeight * ratio);
        }

        bitmapWidth = reqWidth;
        bitmapHeight = reqHeight;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);

        return Bitmap.createScaledBitmap(
                bitmap,
                reqWidth,
                reqHeight,
                false);

    }


}
