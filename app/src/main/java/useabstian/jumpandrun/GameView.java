package useabstian.jumpandrun;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import jumpandrun.usebastian.R;


public class GameView extends SurfaceView implements Runnable {

    Context context;
    int gameWidth;
    int gameHeight;


    Thread gameThread;
    SurfaceHolder surfaceHolder;

    Canvas canvas;
    Paint paint;

    private boolean running;

    Drawable nightSky;
    Stars stars;
    MountainsHigh mountainsHigh;
    MountainsLow mountainsLow;
    Ground ground;
    Runner runner;
    Enemies enemies;

    StartButton startButton;
    RestartButton restartButton;

    public enum GameState {
        PRE_START, STARTED, GAME_OVER, COMPLETED
    }

    private GameState gameState;

    //initial game speed
    private float gameSpeed = 1.0f;

    private static final double BASE_WIDTH = 800;

    private float scaleFactor;

    Typeface customFont;


    float score = 0;
    int highScore;

    SoundPool soundPool;
    int failSoundId;
    int completedSoundId;

    public GameView(Context context, int gameWidth, int gameHeight, float scaleFactor) {
        super(context);

        this.context = context;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        this.scaleFactor = scaleFactor;

        surfaceHolder = getHolder();
        surfaceHolder.setFixedSize(gameWidth, gameHeight);

        paint = new Paint();

        nightSky = getResources().getDrawable(R.drawable.sky);
        nightSky.setBounds(0, 0, gameWidth, gameHeight);
        nightSky.setDither(true);

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        failSoundId = soundPool.load(context, R.raw.fail, 1);
        completedSoundId = soundPool.load(context, R.raw.win, 1);

        stars = new Stars(context, gameWidth, gameHeight);
        mountainsHigh = new MountainsHigh(context, gameWidth, gameHeight);
        mountainsLow = new MountainsLow(context, gameWidth, gameHeight);
        ground = new Ground(context, gameWidth, gameHeight);
        runner = new Runner(context, gameWidth, gameHeight, soundPool);
        enemies = new Enemies(context, gameWidth, gameHeight);

        startButton = new StartButton(context, gameWidth, gameHeight, soundPool);
        restartButton = new RestartButton(context, gameWidth, gameHeight, soundPool);

        customFont = ResourcesCompat.getFont(context, R.font.luckiest_guy);

        highScore = loadHighScore();

        gameState = GameState.PRE_START;

    }


    long startTime;
    long endTime;
    long delta;
    int fps = 0;



    @Override
    public void run() {

        while(running) {

            startTime = System.currentTimeMillis();


            update(delta);

            draw();

            endTime = System.currentTimeMillis();

            delta = endTime - startTime;

            if(delta != 0){
                fps = (int)(1000 / delta);
            }

        }

    }

    private void update(long delta){

        stars.update(delta, gameSpeed);
        mountainsHigh.update(delta, gameSpeed);
        mountainsLow.update(delta, gameSpeed);
        ground.update(delta, gameSpeed);
        runner.update(delta, gameSpeed);


        switch (gameState){

            case PRE_START:

                gameSpeed = 0;
                startButton.update();

                break;
            case STARTED:


                //with each update the game is faster
                gameSpeed = gameSpeed + (0.0004f * ((float) delta / 16f));

                //game maximal speed
                if (gameSpeed >= 3.0) {
                    gameSpeed = 0;
                    gameState = GameState.COMPLETED;
                    soundPool.play(completedSoundId, 1, 1, 1, 0, 1.0f);
                    runner.stopRunning();

                }


                enemies.update(delta, gameSpeed);

                for (int i = 0; i < enemies.getEnemies().size(); i++) {

                    Enemies.Enemy obstacle = enemies.getEnemies().get(i);

                    if(obstacle.active){

                        if(Collision.detectCollision(runner, obstacle)){
                            runner.stopRunning();
                            gameState = GameState.GAME_OVER;
                            soundPool.play(failSoundId, 1, 1, 1, 0, 1.0f);

                            if (score > highScore) {
                                highScore = (int) score;
                                saveHighScore(highScore);
                            }

                        }

                    }

                }


                break;
            case GAME_OVER:

                restartButton.update();
                gameSpeed = 0.0f;
                break;
            case COMPLETED:
                break;

        }



    }

    private void draw(){

        if(surfaceHolder.getSurface().isValid()){

            canvas = surfaceHolder.lockCanvas();

            //draw night sky
            nightSky.draw(canvas);
            stars.draw(canvas);
            mountainsHigh.draw(canvas);
            mountainsLow.draw(canvas);
            ground.draw(canvas);



            drawHighScore(highScore);

            switch (gameState){
                case PRE_START:


                    canvas.drawColor(Color.argb(130, 16, 16, 16));
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(80);
                    drawVerticallyCenteredText(canvas, paint, "JUMMPY & RUN", 0);
                    startButton.draw(canvas);
                    break;


                case STARTED:

                    enemies.draw(canvas);

                    //draw the current score on the screen
                    score = ((gameSpeed - 1) * 5000) - 1;
                    drawScore((int) score);

                    break;
                case GAME_OVER:

                    enemies.draw(canvas);

                    canvas.drawColor(Color.argb(130, 16, 16, 16));
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(50);
                    drawVerticallyCenteredText(canvas, paint, "GAME OVER", 0);
                    restartButton.draw(canvas);

                    break;
                case COMPLETED:

                    canvas.drawColor(Color.argb(130, 16, 16, 16));
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(50);
                    drawVerticallyCenteredText(canvas, paint, "GAME COMPLETED", 0);
                    drawVerticallyCenteredText(canvas, paint, "CONGRATULATION!!!", 50);


                    break;

            }

            runner.draw(canvas);



            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void drawFps(){
        paint.setTextSize((int)(gameWidth*0.025));
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawText("FPS:" + fps, (int)(gameWidth*0.025), (int)(gameWidth*0.05), paint);
    }


    public void pause(){
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException exc){
            Log.e("game loop error", exc.getMessage());
        }
    }

    public void resume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        int action = motionEvent.getAction();


        switch (action) {
            case MotionEvent.ACTION_UP:

                if (gameState == GameState.PRE_START) {

                    boolean pressed = startButton.delegateTouch(motionEvent.getX() * scaleFactor, motionEvent.getY() * scaleFactor, action);

                    if (pressed) {
                        gameState = GameState.STARTED;
                        gameSpeed = 1;
                        runner.startRunning();
                    }

                } else if (gameState == GameState.GAME_OVER) {
                    boolean pressed = restartButton.delegateTouch(motionEvent.getX() * scaleFactor, motionEvent.getY() * scaleFactor, action);

                    if (pressed) {
                        //if game is over and the user taps on the RESTART button, restart the game
                        restartGame();
                    }
                }

                break;

            case MotionEvent.ACTION_DOWN:

                switch (gameState) {
                    case PRE_START:
                        startButton.delegateTouch(motionEvent.getX() * scaleFactor, motionEvent.getY() * scaleFactor, action);
                        break;
                    case STARTED:
                        runner.jump();
                        break;
                    case GAME_OVER:
                        restartButton.delegateTouch(motionEvent.getX() * scaleFactor, motionEvent.getY() * scaleFactor, action);
                        break;
                }


        }
        return true;
    }

    //method for restarting the game
    private void restartGame() {

        //erase all enemies
        enemies.resetAllEnemies();
        gameSpeed = 1f;
        gameState = GameState.STARTED;
        runner.startRunning();

    }




    //helper method for drawing vertically centered text on the screen
    public void drawVerticallyCenteredText(Canvas canvas, Paint paint, String text, int verticalOffset) {

        paint.setTypeface(customFont);

        //to be able to center the text, get his bounds in one rectangle
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        //calculates x and y coordinates of text to be drawn
        int x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        int y = (canvas.getHeight() / 2) - (bounds.height() / 2 - verticalOffset);

        //set the antiAlias and draw the text
        paint.setAntiAlias(true);
        canvas.drawText(text, x, y, paint);
    }


    /******************************SCORE AND HIGH SCORE HANDLING*********************************************/


    private void saveHighScore(int highScore) {
        SharedPreferences settings = context.getSharedPreferences("SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("high_score", highScore);

        editor.apply();

    }


    private int loadHighScore() {

        SharedPreferences settings = context.getSharedPreferences("SETTINGS", 0);
        return settings.getInt("high_score", 0);

    }


    private void drawHighScore(int highScore) {

        String highScoreText = "High Score: " + highScore;

        paint.setTextSize(20);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        Rect bounds = new Rect();
        paint.getTextBounds(highScoreText, 0, highScoreText.length(), bounds);

        canvas.drawText(highScoreText, gameWidth - bounds.width() - (int) (gameWidth * 0.02), 40, paint);

    }

    private void drawScore(int score) {
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score + "/15.000", 20, 40, paint);
    }




}
