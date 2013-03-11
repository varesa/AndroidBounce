package fi.dy.esav.GrafiikkaTest;

import android.view.View;

public class RenderThread implements Runnable {

	private BallView v;
	private boolean running = false;
	private boolean paused  = false;
	
	private RenderThread() {
		
	}
	
	public RenderThread(BallView v) {
		this.v = v;
		running = true;
	}
	
	@Override
	public void run() {
		while(running) {
			while(paused) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			v.draw();
		}
	}

}
