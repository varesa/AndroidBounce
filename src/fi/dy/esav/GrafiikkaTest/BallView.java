package fi.dy.esav.GrafiikkaTest;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class BallView extends SurfaceView {

    BallTouchListener touchListener;

    Drawable ball;
    Bitmap ball_bitmap;
    RenderThread renderer;
    Thread renderThread;

    private Vibrator vibrator;
    private MediaPlayer sndEffect;

    final int BALLSIZE = 64;

    final float TIMEFACTOR = 0.0000001f;

    final float SPEEDINCREMENT = 60f;
    final float FRICTION = 0.10f;


    final float ROTATEFACTOR = 0.35f;

    float ballx;
    float bally;
    float ballr;

    float ballvx;
    float ballvy;

    private Bitmap buffer;
    private Canvas cbuffer;

    private Paint simplePaint;
    private Paint arrowPaint;

    private int screenX;
    private int screenY;

    public BallView(Context context) {
        super(context);
        init();
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        ball = getResources().getDrawable(R.drawable.ball);

        ballx = 200;
        bally = 200;
        ballr = 45;

        ballvx = 5;
        ballvy = 15;
        touchListener = new BallTouchListener(this);
        super.setOnTouchListener(touchListener);
        ((Activity) this.getContext()).setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    public Point getBallCenter() {
        return new Point((int) (ballx + 0.5 * BALLSIZE), (int) (bally + 0.5 * BALLSIZE));
    }

    public void startDraw() {
        if (renderer == null) {
            renderer = new RenderThread(this);
            renderThread = new Thread(renderer);
            renderThread.start();
        }
        renderer.running = true;
        renderer.paused = false;
    }

    public void pauseDraw() {
        renderer.paused = true;
    }

    public void stopDraw() {
        renderer.running = false;
    }

    private long oldTime = 0;

    private long getTimePassed() {
        if (oldTime == 0) {
            oldTime = System.currentTimeMillis();
        }
        long deltaTime = System.currentTimeMillis() - oldTime;
        oldTime = System.currentTimeMillis();
        return deltaTime;
    }

    private void checkCollisions() {
        if (ballx < 0) {
            ballx = 0;
            ballvx = -ballvx;
            collisionEffect();
        } else if (ballx + BALLSIZE > screenX) {
            ballx = screenX - BALLSIZE;
            ballvx = -ballvx;
            collisionEffect();
        }

        if (bally < 0) {
            bally = 0;
            ballvy = -ballvy;
            collisionEffect();
        } else if (bally + BALLSIZE > screenY) {
            bally = screenY - BALLSIZE;
            ballvy = -ballvy;
            collisionEffect();
        }

    }

    private void collisionEffect() {
        if (vibrator == null) {
            vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(60);

        if (sndEffect == null) {
            sndEffect = MediaPlayer.create(getContext(), R.raw.bounce);
        }
        sndEffect.start();
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(ballvx, 2) + Math.pow(ballvy, 2));
    }

    public void addSpeed(float distance) {

        float cur_r = (float) (Math.sqrt(Math.pow(ballvx, 2) + Math.pow(ballvy, 2)));
        float cur_theta = (float) (Math.atan2(ballvy, ballvx));

        //cur_r *= 0.3;

        float cur_vx = (float) (cur_r * Math.cos(cur_theta));
        float cur_vy = (float) (cur_r * Math.sin(cur_theta));

        float new_theta = (float) (Math.toRadians(ballr));
        float new_r = (float) (SPEEDINCREMENT / distance);

        float new_vx = (float) ((cur_r + new_r) * Math.cos(new_theta));
        float new_vy = (float) ((cur_r + new_r) * Math.sin(new_theta));

        /*float r = (float) (Math.sqrt(Math.pow(ballvx, 2) + Math.pow(ballvy, 2)));
        float theta = (float) (Math.atan2(ballvy, ballvx) + 90);

        //theta = (float) (0.30 * theta + 0.7 * Math.toRadians(ballr));
        theta = (float) (Math.toRadians(ballr));
        r += SPEEDINCREMENT;

        ballvx = (float) (r * Math.cos(theta));
        ballvy = (float) (r * Math.sin(theta));
        Log.e("dy.fi.esav.GrafiikkaTest", "Angle in degrees: " + ballr + ", and in radians: " + Math.toRadians(ballr));*/

        ballvx = new_vx;// + new_vx;
        ballvy = new_vy;// + cur_vy;


    }

    public void decreaseSpeed(float delta) {
        float r = (float) (Math.sqrt(Math.pow(ballvx, 2) + Math.pow(ballvy, 2)));
        float theta = (float) (Math.atan2(ballvy, ballvx));

        r -= FRICTION * delta;

        ballvx = (float) (r * Math.cos(theta));
        ballvy = (float) (r * Math.sin(theta));
    }

    public void draw() {
        while (!this.getHolder().getSurface().isValid()) continue;

        Canvas rcanvas = this.getHolder().lockCanvas();

        screenX = rcanvas.getWidth();
        screenY = rcanvas.getHeight();

        checkCollisions();

        if (rcanvas == null) return;

        if (ball_bitmap == null) {
            ball_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        }

        if (buffer == null) {
            buffer = Bitmap.createBitmap(rcanvas.getWidth(), rcanvas.getHeight(), Bitmap.Config.ARGB_4444);
        }

        if (cbuffer == null) {
            cbuffer = new Canvas(buffer);
        }

        if (simplePaint == null) {
            simplePaint = new Paint();
        }

        if (arrowPaint == null) {
            arrowPaint = new Paint();
            arrowPaint.setStrokeWidth(3f);
        }

        float delta = getDrawingTime() * (float) TIMEFACTOR;

        decreaseSpeed(delta);

        ballx += ballvx * delta;
        bally += ballvy * delta;

        ballr += getSpeed() * delta * ROTATEFACTOR;


        if (ballr > 360) {
            ballr -= 360;
        }

        cbuffer.drawARGB(255, 255, 255, 255);
        cbuffer.rotate(ballr, (float) (ballx + 0.5 * BALLSIZE), (float) (bally + 0.5 * BALLSIZE));
        cbuffer.drawBitmap(ball_bitmap, ballx, bally, simplePaint);
        //cbuffer.drawLine((float) (ballx + 0.5 * BALLSIZE), (float) (bally + 0.5 * BALLSIZE + 60), (float) (ballx + 0.5 * BALLSIZE), (float) (bally + 0.5 * BALLSIZE + 60 + 35), arrowPaint);
        cbuffer.drawLine((float) (ballx + 0.5 * BALLSIZE + 60), (float) (bally + 0.5 * BALLSIZE), (float) (ballx + 0.5 * BALLSIZE + 60 + 35), (float) (bally + 0.5 * BALLSIZE), arrowPaint);
        cbuffer.rotate(-ballr, (float) (ballx + 0.5 * BALLSIZE), (float) (bally + 0.5 * BALLSIZE));

        rcanvas.drawBitmap(buffer, 0, 0, simplePaint);
        this.getHolder().unlockCanvasAndPost(rcanvas);
    }
}
