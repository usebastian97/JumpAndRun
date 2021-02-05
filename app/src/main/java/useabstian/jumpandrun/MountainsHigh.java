package useabstian.jumpandrun;

import android.content.Context;

import jumpandrun.usebastian.R;


public class MountainsHigh extends InfiniteScrollingBackground {


    MountainsHigh(Context context, int gameWidth, int gameHeight) {
        super(context, gameWidth, gameHeight);

        this.VELOCITY = 0.05;
        this.drawableResource = R.drawable.mountains_high;
        this.bitmapVerticalAlignment = BitmapVerticalAlignment.BOTTOM;
        this.relativeHeight = 0.5;
        this.relativeVerticalOffset = 0.1;

        init();
    }


}
