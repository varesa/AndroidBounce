package fi.dy.esav.GrafiikkaTest;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

//import android.view.View.O;

/**
 * Created with IntelliJ IDEA.
 * User: esa
 * Date: 3/13/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BallTouchListener implements OnTouchListener {

    BallView ballview;

    private BallTouchListener() {

    }

    public BallTouchListener(BallView ballview) {
        this.ballview = ballview;
    }

    public boolean onTouch(View view, MotionEvent event) {
        Log.e("fi.dy.esav.GrafiikkaTest", "Detected a touch");
        float touchX = event.getX();
        float touchY = event.getY();

        float ballx = ballview.getBallCenter().x;
        float bally = ballview.getBallCenter().y;

        if(Utils.getDistance(touchX, touchY, ballx, bally) < 0.5 * ballview.BALLSIZE) {
            Log.e("fi.dy.esav.GrafiikkaTest", "Hit the ball!!!");
        }

        return false;
    }

}
