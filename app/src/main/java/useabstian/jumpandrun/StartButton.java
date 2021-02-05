package useabstian.jumpandrun;

import android.content.Context;
import android.media.SoundPool;
import jumpandrun.usebastian.R;

public class StartButton extends GameButton {


    public StartButton(Context context, int gameWidth, int gameHeight, SoundPool soundPool) {

        super(context, gameWidth, gameHeight, soundPool);
        drawableResource = R.drawable.start_button;

        init();



    }
}
