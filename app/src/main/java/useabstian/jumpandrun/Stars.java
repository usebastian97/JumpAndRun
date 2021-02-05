package useabstian.jumpandrun;

import android.content.Context;

import jumpandrun.usebastian.R;

public class Stars extends InfiniteScrollingBackground {


    Stars(Context context, int gameWidth, int gameHeight) {
        super(context, gameWidth, gameHeight);

        this.VELOCITY = 0.02;
        this.drawableResource = R.drawable.stars;
        this.bitmapVerticalAlignment = BitmapVerticalAlignment.TOP;
        this.relativeHeight = 0.7;
        this.relativeVerticalOffset = 0;

        init();
    }
}
