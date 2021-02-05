package useabstian.jumpandrun;

import android.content.Context;
import android.media.SoundPool;

import jumpandrun.usebastian.R;

public class RestartButton extends GameButton {


    public RestartButton(Context context, int gameWidth, int gameHeight, SoundPool soundPool) {
        super(context, gameWidth, gameHeight, soundPool);

        drawableResource = R.drawable.restart_button;

        init();
    }
}
