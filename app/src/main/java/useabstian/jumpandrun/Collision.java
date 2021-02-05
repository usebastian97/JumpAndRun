package useabstian.jumpandrun;

import android.graphics.Rect;

public class Collision {

    //method for detecting collision between Runner and enemies
    public static boolean detectCollision(Runner runner, Enemies.Enemy obstacle){

        //get Runner and Enemy bounds
        Rect runnerPosition = runner.getSpritePosition();
        Rect originalObstaclePosition = obstacle.getBitmapPosition();

        //tweak enemy bounds
        Rect obstaclePosition = new Rect();
        obstaclePosition.top = (int)(originalObstaclePosition.top*1.1);
        obstaclePosition.left = (int)(originalObstaclePosition.left*1.25);
        obstaclePosition.right = (int)(originalObstaclePosition.right*0.75);
        obstaclePosition.bottom = (int)(originalObstaclePosition.bottom);

        //detect collision using built-in intersects method of Rect class
        return Rect.intersects(runnerPosition, obstaclePosition);

    }

}
