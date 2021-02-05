package useabstian.jumpandrun;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import jumpandrun.usebastian.R;

public class MainActivity extends AppCompatActivity {

    FrameLayout mainContainer;
    GameView gameView;

    private static final float GAME_WIDTH = 800;
    private float scaleFactor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        double screenRatio = (size.x * 1.0 ) / (size.y * 1.0);

        scaleFactor = GAME_WIDTH / size.x;

        gameView = new GameView(this, (int)GAME_WIDTH, (int)(GAME_WIDTH/screenRatio), scaleFactor);

        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gameView.setLayoutParams(params);

        mainContainer = findViewById(R.id.main_container);
        mainContainer.addView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
