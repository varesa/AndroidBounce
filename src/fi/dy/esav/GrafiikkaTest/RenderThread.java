package fi.dy.esav.GrafiikkaTest;

import android.util.Log;
import android.view.View;

public class RenderThread implements Runnable {

	private BallView v;
	boolean running = true;
	boolean paused = false;

	private RenderThread() {

	}

	public RenderThread(BallView v) {
		Log.e("fi.dy.esav.GrafiikkaTest", "Initialising RenderThread");
		this.v = v;
		running = true;
	}

	@Override
	public void run() {
		Log.e("fi.dy.esav.GrafiikkaTest", "Running RenderThread");
		while (running) {
			Log.e("fi.dy.esav.GrafiikkaTest", "Rendering");
			v.draw();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (paused && running) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
