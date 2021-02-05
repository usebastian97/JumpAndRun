package useabstian.jumpandrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemies {

    public static class Enemy extends GameObject {

        private static final double VELOCITY = 0.6;

        private Bitmap bitmap;
        private Rect bitmapPosition;

        public Rect getBitmapPosition() {
            return bitmapPosition;
        }

        private Enemy(Context context, int gameWidth, int gameHeight, int bitmapResourceId, int desiredHeight) {

            super(context, gameHeight, gameWidth);

            this.bitmap = decodeSampledBitmapFromResource(context.getResources(), bitmapResourceId, -1, desiredHeight);

            //sets initial obstacle position
            bitmapPosition = new Rect(gameWidth, (int) (gameHeight * 0.85) - bitmap.getHeight(), gameWidth + bitmap.getWidth(), (int) (gameHeight * 0.85));
        }

        private int offset = 0;
        private double offsetInternal = 0;
        boolean outOfBounds = false;
        boolean active = false;

        void update(double delta, float gameSpeed) {

            if (Math.abs(offset) <= gameWidth + bitmap.getWidth()) {

                offsetInternal = offsetInternal - (VELOCITY * gameSpeed) * delta;
                offset = (int) (offsetInternal);

                bitmapPosition.left = gameWidth + offset;
                bitmapPosition.right = bitmap.getWidth() + (gameWidth + offset);

            } else {
                reset();
            }
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(bitmap, null, bitmapPosition, null);
        }


        void reset() {

            offset = 0;
            offsetInternal = 0;
            outOfBounds = false;
            active = false;

            bitmapPosition.left = gameWidth;
            bitmapPosition.right = gameWidth + bitmap.getWidth();

        }

    }


    //inner static factory class, for creating the Enemy objects
    public static class Factory {

        Context context;
        int gameWidth;
        int gameHeight;

        private static final int TOTAL_ENEMIES = 5;

        //constants for controlling the enemies size
        private static final double[] ENEMIES_RELATIVE_HEIGHTS = new double[]{0.12, 0.12, 0.11, 0.14, 0.16};

        private int ENEMY_INDEX;

        private List<Enemy> enemyPool;

        public List<Enemy> getEnemyPool() {
            return enemyPool;
        }

        //Factory class constructor
        public Factory(Context context, int gameWidth, int gameHeight) {

            this.context = context;
            this.gameHeight = gameHeight;
            this.gameWidth = gameWidth;


            enemyPool = new ArrayList<>();

            //loop for creating Bitmap objects from drawable resources (png images)
            for (int i = 0; i < TOTAL_ENEMIES; i++) {
                //get resource id using resource string name
                int bitmapResourceId = context.getResources().getIdentifier("obstacle" + (i + 1), "drawable", context.getPackageName());

                enemyPool.add(new Enemy(context, gameWidth, gameHeight, bitmapResourceId, (int)(gameHeight * ENEMIES_RELATIVE_HEIGHTS[i])));

            }

        }

        //factory method for getting the random Enemy
        public Enemy getRandomObstacle() {
            Random rnd = new Random();
            ENEMY_INDEX = rnd.nextInt(enemyPool.size());

            return enemyPool.get(ENEMY_INDEX);
        }

    }


    /********************ENEMIES MAIN CLASS LOGIC***************************/

    //enemies class global variables
    private Context context;
    private int gameHeight;
    private int gameWidth;
    private Enemies.Factory enemiesFactory;

    private List<Enemy> enemies;

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void resetAllEnemies() {
        for (Enemy obstacle : enemies) {
            obstacle.reset();
        }
    }

    //enemies class constructor
    public Enemies(Context context, int gameWidth, int gameHeight) {

        this.context = context;
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

        enemiesFactory = new Factory(context, gameWidth, gameHeight);
        enemies = enemiesFactory.getEnemyPool();

    }


    //control variable for controlling the obstacle creation timing
    private int control = 0;
    private long deltaCounter = 0;
    private Random random = new Random();

    public void update(long delta, float gameSpeed) {


        deltaCounter += delta;

        //logic for updating the enemies
        //calculate the control variable random value
        //control variable determines when new obstacle is going to be created
        if (control == 0) {
            control = random.nextInt((int) (1000 / gameSpeed)) + (int) (800 / gameSpeed);
        }

        //when is time to generate new obstacle
        if (deltaCounter >= control) {

            //create new obstacle and add it to obstacle list
            Enemy enemy = enemiesFactory.getRandomObstacle();
            enemy.active = true;

            //reset control variable and frame counter for next obstacle creation
            control = 0;
            deltaCounter = 0;
        }

        //update every obstacle on screen
        //loop trough obstacle list and update
        for (Enemy enemy : enemies) {
            //if obstacle is visible call update method
            if (enemy.active) {
                enemy.update(delta, gameSpeed);
            }
        }

    }


    public void draw(Canvas canvas) {

        //draw every obstacle
        for (Enemy enemy : enemies) {
            if (enemy.active) {
                enemy.draw(canvas);
            }
        }

    }



}
