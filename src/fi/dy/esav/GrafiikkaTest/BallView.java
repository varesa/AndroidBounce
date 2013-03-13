package fi.dy.esav.GrafiikkaTest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class BallView extends SurfaceView {

	Drawable ball;
	Bitmap ball_bitmap;
	RenderThread renderer;
	Thread		 renderThread;

    static final int BALLHEIGHT = 64;
    static final int BALLWIDTH = 64;

    static final float TIMEFACTOR = 0.0000001f;
    static final float speedIncrement = 5;

    float ballx;
    float bally;
    float ballr;

    float ballvx;
    float ballvy;

    private Bitmap buffer;
    private Canvas cbuffer;

    private Paint simplePaint;

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

        ballx = 0;
        bally = 0;
        ballr = 0;

        ballvx = 10;
        ballvy = 30;
	}
	
	public void startDraw() {
		if(renderer == null) {
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
        if(oldTime == 0) {
            oldTime = System.currentTimeMillis();
        }
        long deltaTime = System.currentTimeMillis() - oldTime;
        oldTime = System.currentTimeMillis();
        return deltaTime;
    }

    private void checkCollisions() {
        if(ballx < 0) {
            ballx = 0;
            ballvx = -ballvx;
        } else if(ballx + BALLWIDTH > screenX) {
            ballx = screenX - BALLWIDTH;
            ballvx = -ballvx;
        }

        if(bally < 0) {
            bally = 0;
            ballvy = -ballvy;
        } else if(bally + BALLHEIGHT > screenY) {
            bally = screenY - BALLHEIGHT;
            ballvy = -ballvy;
        }

    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(ballvx,2) + Math.pow(ballvy,2));
    }

	public void draw() {
		while(!this.getHolder().getSurface().isValid()) continue;

        Canvas rcanvas = this.getHolder().lockCanvas();

        screenX = rcanvas.getWidth();
        screenY = rcanvas.getHeight();

        checkCollisions();

        if(rcanvas == null) return;

        if(ball_bitmap == null) {
			ball_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
		}

        if(buffer == null) {
            buffer = Bitmap.createBitmap(rcanvas.getWidth(), rcanvas.getHeight(), Bitmap.Config.ARGB_4444);
        }

        if(cbuffer == null) {
            cbuffer = new Canvas(buffer);
        }

        if(simplePaint == null) {
            simplePaint = new Paint();
        }

        float delta = getDrawingTime()*(float)TIMEFACTOR;
        Log.e("fi.dy.esav.GrafiikkaTest", "XVel: " + ballvx);
        ballx += ballvx*delta;
        bally += ballvy*delta;
        Log.e("fi.dy.esav.GrafiikkaTest", "Time passed: " + delta + ", new x: " + ballx + ", new y: " + bally);

        cbuffer.drawARGB(255,255,255,255);
		cbuffer.drawBitmap(ball_bitmap, ballx, bally, simplePaint);

        rcanvas.drawBitmap(buffer, 0, 0, simplePaint);
		this.getHolder().unlockCanvasAndPost(rcanvas);
	}
}
