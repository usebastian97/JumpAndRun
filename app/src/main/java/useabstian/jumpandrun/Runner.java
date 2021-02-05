package useabstian.jumpandrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.SoundPool;

import jumpandrun.usebastian.R;

public class Runner extends GameObject {

    private Bitmap runnerSpritesheet;

    //rects for determining current sprite and its position
    private int currentSpriteIndex;
    private Rect currentSprite;
    private Rect spritePosition;

    public Rect getSpritePosition() {
        return spritePosition;
    }

    public enum RunnerState {
        STANDING, RUNNING, JUMPING
    }

    private RunnerState runnerState;

    private static final int BMP_COLUMNS = 9;

    private int spriteHeight;
    private int spriteWidth;

    private int jumpTopLimit;
    private int initialTop;

    SoundPool soundPool;
    private int jumpSoundId;

    //runner class constructor
    public Runner(Context context, int gameWidth, int gameHeight, SoundPool soundPool) {

        super(context, gameHeight, gameWidth);

        this.soundPool = soundPool;

        //desired Runner relative height
        //spriteHeight = gameHeight / 6;
        spriteWidth = gameHeight / 8;

        //load sprite sheet
        runnerSpritesheet = decodeSampledBitmapFromResource(context.getResources(), R.drawable.runner, (spriteWidth*BMP_COLUMNS), -1);

        //spriteWidth = bitmapWidth / BMP_COLUMNS;
        spriteHeight = bitmapHeight;

        currentSpriteIndex = 0;


        initialTop = gameHeight - spriteHeight - (int) (gameHeight * 0.15);
        jumpTopLimit = (gameHeight / 3);

        //sets initial sprite
        currentSprite = new Rect(0, 0, spriteWidth, spriteHeight);
        spritePosition = new Rect((int) (gameWidth * 0.1), initialTop, spriteWidth + (int) (gameWidth * 0.1), gameHeight - (int) (gameHeight * 0.15));

        runnerState = RunnerState.STANDING;

        jumpSoundId = soundPool.load(context, R.raw.jump3, 1);

    }


    //keep track of total delta
    private float deltaCounter = 0;

    //update runner position
    public void update(long delta, float gameSpeed) {


        deltaCounter += delta;

        float period = ((100) - (10 * gameSpeed));

        switch (runnerState) {

            case STANDING:

                //cycle trough 2 sprites
                if ((deltaCounter / period) > 1) {
                    if (currentSpriteIndex < 1) {
                        currentSpriteIndex++;
                    } else {
                        currentSpriteIndex = 0;
                    }

                    deltaCounter = deltaCounter - period;
                }
                //set running sprite
                currentSprite.left = currentSpriteIndex * spriteWidth;
                currentSprite.right = currentSprite.left + spriteWidth;

                //set sprite position on screen
                spritePosition.top = gameHeight - spriteHeight - (int) (gameHeight * 0.15);
                spritePosition.bottom = gameHeight - (int) (gameHeight * 0.15);

                break;
            case RUNNING:


                //cycle trough 4 sprites
                if ((deltaCounter / period) > 1) {
                    if (currentSpriteIndex < 5) {
                        currentSpriteIndex++;
                    } else {
                        currentSpriteIndex = 2;
                    }

                    deltaCounter = deltaCounter - period;
                }
                //set running sprite
                currentSprite.left = currentSpriteIndex * spriteWidth;
                currentSprite.right = currentSprite.left + spriteWidth;

                //set sprite position on screen
                spritePosition.top = gameHeight - spriteHeight - (int) (gameHeight * 0.15);
                spritePosition.bottom = gameHeight - (int) (gameHeight * 0.15);

                break;
            case JUMPING:

                //raise The Runner
                if ((deltaCounter / period) <= 2) {

                    currentSpriteIndex = 7;


                    currentSprite.left = currentSpriteIndex * spriteWidth;
                    currentSprite.right = currentSprite.left + spriteWidth;


                    float newTop = ((initialTop - jumpTopLimit) * (deltaCounter / (period * 2)));

                    //raise up the Runner
                    spritePosition.left = (int) (gameWidth * 0.1);
                    spritePosition.top = initialTop - (int) newTop;
                    spritePosition.right = spriteWidth + (int) (gameWidth * 0.1);
                    spritePosition.bottom = (spritePosition.top + spriteHeight);


                    //hold
                } else if ((deltaCounter / period) > 2 && (deltaCounter / period) <= 3) {

                    if (spritePosition.top < jumpTopLimit) {
                        float newTop = ((initialTop - jumpTopLimit));
                        spritePosition.top = initialTop - (int) newTop;
                    }

                    //wait for one whole period

                    //low down The Runner
                } else if ((deltaCounter / period) > 3 && (deltaCounter / period) <= 5) {
                    //set second jump frame

                    currentSpriteIndex = 8;

                    currentSprite.left = currentSpriteIndex * spriteWidth;
                    currentSprite.right = currentSprite.left + spriteWidth;

                    float newTop = ((initialTop - jumpTopLimit) * ((deltaCounter - (period * 3)) / (period * 2)));

                    //low down the Runner
                    spritePosition.left = (int) (gameWidth * 0.1);
                    spritePosition.top = jumpTopLimit + (int) newTop;
                    spritePosition.right = spriteWidth + (int) (gameWidth * 0.1);
                    spritePosition.bottom = (spritePosition.top + spriteHeight);

                    //jump is finished, return to running
                } else {
                    runnerState = RunnerState.RUNNING;
                    currentSpriteIndex = 2;
                    deltaCounter = deltaCounter - (period * 5);
                }

                break;

        }

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(runnerSpritesheet, currentSprite, spritePosition, null);
    }


    //public method to perform jump
    public void jump() {

        if (runnerState == RunnerState.RUNNING) {
            deltaCounter = 0;
            runnerState = RunnerState.JUMPING;
            soundPool.play(jumpSoundId, 1, 1, 1, 0, 1.0f);
        }
    }


    public void startRunning() {
        runnerState = RunnerState.RUNNING;
    }

    public void stopRunning() {
        runnerState = RunnerState.STANDING;

    }

}
