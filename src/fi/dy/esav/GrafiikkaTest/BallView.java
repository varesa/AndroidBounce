package fi.dy.esav.GrafiikkaTest;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class BallView extends SurfaceView {

    BallTouchListener touchListener;

	Drawable ball;
	Bitmap ball_bitmap;
	RenderThread renderer;
	Thread		 renderThread;

    final int BALLSIZE = 64;
    final int TOUCHTRESHOLD = 128;

    final float TIMEFACTOR = 0.0000001f;
    final float speedIncrement = 5;
    final float ROTATEFACTOR = 0.1f;

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

        ballx = 200;
        bally = 200;
        ballr = 0;

        ballvx = 10;
        ballvy = 30;
        touchListener = new BallTouchListener(this);
        super.setOnTouchListener(touchListener);
	}

    public Point getBallCenter() {
        return new Point((int) (ballx + 0.5 * BALLSIZE), (int) (bally + 0.5 * BALLSIZE));
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
        } else if(ballx + BALLSIZE > screenX) {
            ballx = screenX - BALLSIZE;
            ballvx = -ballvx;
        }

        if(bally < 0) {
            bally = 0;
            ballvy = -ballvy;
        } else if(bally + BALLSIZE > screenY) {
            bally = screenY - BALLSIZE;
            ballvy = -ballvy;
        }

    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(ballvx,2) + Math.pow(ballvy,2));
    }

    public void addSpeed() {
        float temp;
        temp = ballvx;
        ballvx = ballvy;
        ballvy = temp;
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
        ballx += ballvx*delta;
        bally += ballvy*delta;

        ballr += getSpeed() * delta * ROTATEFACTOR;
        Log.e("fi.dy.esav.GrafiikkaTest", "Ball rotation speed: " + ballr);

        if(ballr > 360) {
            ballr -= 360;
        }

        cbuffer.drawARGB(255,255,255,255);
        cbuffer.rotate(ballr, (float) (ballx + 0.5*BALLSIZE), (float)(bally + 0.5*BALLSIZE));
		cbuffer.drawBitmap(ball_bitmap, ballx, bally, simplePaint);
        cbuffer.rotate(-ballr, (float) (ballx + 0.5*BALLSIZE), (float)(bally + 0.5*BALLSIZE));

        rcanvas.drawBitmap(buffer, 0, 0, simplePaint);
		this.getHolder().unlockCanvasAndPost(rcanvas);
	}
}
