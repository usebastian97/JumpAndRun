package useabstian.jumpandrun;

import android.content.Context;

import jumpandrun.usebastian.R;

public class MountainsLow extends InfiniteScrollingBackground {


    MountainsLow(Context context, int gameWidth, int gameHeight) {
        super(context, gameWidth, gameHeight);

        this.VELOCITY = 0.1;
        this.drawableResource = R.drawable.mountains_low;
        this.bitmapVerticalAlignment = BitmapVerticalAlignment.BOTTOM;
        this.relativeHeight = 0.3;
        this.relativeVerticalOffset = 0.1;

        init();
    }


}
