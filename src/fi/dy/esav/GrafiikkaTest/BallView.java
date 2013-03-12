package fi.dy.esav.GrafiikkaTest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

public class BallView extends SurfaceView {

	Drawable ball;
	Bitmap ball_bitmap;
	RenderThread renderer;
	Thread		 renderThread;

    static final float TIMEFACTOR = 0.000000001f;
    static final float speedIncrement = 5;

    float ballx;
    float bally;
    float ballr;

    float ballvx;
    float ballvy;
	
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

	public void draw() {
		while(!this.getHolder().getSurface().isValid()) continue;
		Canvas canvas = this.getHolder().lockCanvas();
		if(canvas == null) return;
		if(ball_bitmap == null) {
			ball_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
		}

        float delta = getDrawingTime()*(float)TIMEFACTOR;
        ballx += ballvx*delta;
        bally += ballvy*delta;
        Log.e("fi.dy.esav.GrafiikkaTest", "Time passed: " + delta + ", new x: " + ballx + ", new y: " + bally);

		Paint paint = new Paint();

		canvas.drawBitmap(ball_bitmap, ballx, bally, paint);
		
		this.getHolder().unlockCanvasAndPost(canvas);
	}
}
