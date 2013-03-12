package fi.dy.esav.GrafiikkaTest;

import android.util.Log;
import android.view.View;

public class RenderThread implements Runnable {

    private static boolean MAX_FPS = false;

	private BallView v;
	boolean running = true;
	boolean paused = false;

	private RenderThread() {

	}

	public RenderThread(BallView v) {
		this.v = v;
		running = true;
	}

	@Override
	public void run() {
		while (running) {
			v.draw();
            if(!MAX_FPS) {
			    try {
				    Thread.sleep(5);
			    } catch (InterruptedException e) {
				    e.printStackTrace();
			    }
            }
			while (paused && running) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
