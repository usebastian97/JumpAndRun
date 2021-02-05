package useabstian.jumpandrun;

import android.content.Context;

import jumpandrun.usebastian.R;

public class Ground extends InfiniteScrollingBackground {



    Ground(Context context, int gameWidth, int gameHeight) {
        super(context, gameWidth, gameHeight);


        this.VELOCITY = 0.6;
        this.drawableResource = R.drawable.ground;
        this.bitmapVerticalAlignment = BitmapVerticalAlignment.BOTTOM;
        this.relativeHeight = 0.3;
        this.relativeVerticalOffset = 0;

        init();


    }
}


