package web.schach.gruppe6.util;

public class Task implements Runnable {
	
	private final Runnable run;
	private boolean isTriggered = false;
	
	public Task(Runnable run) {
		this.run = run;
	}
	
	@Override
	public void run() {
		run.run();
		synchronized (this) {
			isTriggered = true;
			notifyAll();
		}
	}
	
	public synchronized void await() throws InterruptedException {
		while (!isTriggered)
			this.wait();
	}
}
