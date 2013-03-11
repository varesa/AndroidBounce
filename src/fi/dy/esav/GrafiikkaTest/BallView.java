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
import android.view.View;

public class BallView extends SurfaceView {

	Drawable ball;
	Bitmap ball_bitmap;
	RenderThread renderer;
	Thread		 renderThread;

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
		Log.e("fi.dy.esav.GrafiikkaTest", "Initialising BallView");

        ballx = 0;
        bally = 0;
        ballr = 0;

        ballvx = 0;
        ballvy = 0;
	}
	
	public void startDraw() {
		Log.e("fi.dy.esav.GrafiikkaTest", "Starting drawing in the BallView");
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

	public void draw() {
		Log.e("fi.dy.esav.GrafiikkaTest", "Drawing in the BallView");
		while(!this.getHolder().getSurface().isValid()) continue;
		Canvas canvas = this.getHolder().lockCanvas();
		if(canvas == null) return;
		if(ball_bitmap == null) {
			ball_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
		}
		
		Paint paint = new Paint();
		canvas.drawBitmap(ball_bitmap, ballx, bally, paint);
		
		this.getHolder().unlockCanvasAndPost(canvas);
		Log.e("fi.dy.esav.GrafiikkaTest", "Drawn in the BallView");
	}
}
