package fi.dy.esav.GrafiikkaTest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

public class BallView extends SurfaceView {

	Drawable ball;
	Bitmap ball_bitmap;
	
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
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		
	}
	
	public void draw() {
		while(!this.getHolder().getSurface().isValid()) continue;
		Canvas canvas = this.getHolder().lockCanvas();
		
		if(ball_bitmap == null) {
			ball_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
		}
		
		Paint paint = new Paint();
		canvas.drawBitmap(ball_bitmap,0, 0, paint);
		
		this.getHolder().unlockCanvasAndPost(canvas);
	}
}
