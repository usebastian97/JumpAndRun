package useabstian.jumpandrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class InfiniteScrollingBackground extends GameObject {

    double VELOCITY;
    int drawableResource;
    double relativeHeight;
    double relativeVerticalOffset;
    BitmapVerticalAlignment bitmapVerticalAlignment;

    public enum BitmapVerticalAlignment {
        TOP, BOTTOM
    }

    private Bitmap bitmaps[];
    //Rect variable for keeping info of ground position
    private Rect bitmapPositions[];

    private double offsetInternal = 0;

    InfiniteScrollingBackground(Context context, int gameWidth, int gameHeight) {
        super(context, gameHeight, gameWidth);
    }

    void init(){
        bitmaps = new Bitmap[2];
        bitmapPositions = new Rect[2];

        for (int i = 0; i < bitmaps.length; i++) {

            Bitmap bitmap = decodeSampledBitmapFromResource(context.getResources(), drawableResource, -1, (int)(gameHeight*relativeHeight));

            bitmaps[i] = bitmap;

            Rect bitmapPosition = new Rect();

            if(bitmapVerticalAlignment == BitmapVerticalAlignment.BOTTOM){
                bitmapPosition.bottom = (int)(gameHeight - gameHeight*relativeVerticalOffset);
                bitmapPosition.top = bitmapPosition.bottom - bitmaps[i].getHeight();
            } else if(bitmapVerticalAlignment == BitmapVerticalAlignment.TOP){
                bitmapPosition.top = (int)(relativeVerticalOffset*gameHeight);
                bitmapPosition.bottom = bitmapPosition.top + bitmaps[i].getHeight();
            }

            bitmapPosition.left = (i) * bitmaps[i].getWidth();
            bitmapPosition.right = bitmapPosition.left + bitmaps[i].getWidth();

            bitmapPositions[i] = bitmapPosition;

        }
    }

    //method for updating the ground position
    public void update(long delta, float gameSpeed) {

        offsetInternal = offsetInternal - (VELOCITY * gameSpeed * delta);

        int offset = (int) (offsetInternal);

        if (Math.abs(offset) > (bitmaps[0].getWidth())) {

            offset = ((bitmaps[0].getWidth()) - Math.abs(offset));
            offsetInternal = offset;

            List<Rect> list = Arrays.asList(bitmapPositions);
            Collections.reverse(list);
            bitmapPositions = (Rect[]) list.toArray();

        }

        for (int i = 0; i < bitmaps.length; i++) {
            bitmapPositions[i].left = offset + ((i) * (bitmaps[i].getWidth()));
            bitmapPositions[i].right = bitmapPositions[i].left + bitmaps[i].getWidth();
        }

    }


    public void draw(Canvas canvas){

        for (int i = 0; i < bitmaps.length; i++) {
            canvas.drawBitmap(bitmaps[i], null, bitmapPositions[i], null);
        }

    }


}
