package web.schach.gruppe6.util;

public class Awaitable {
	
	private boolean isTriggered = false;
	
	public synchronized void trigger() {
		isTriggered = true;
		notifyAll();
	}
	
	public synchronized void await() throws InterruptedException {
		while (!isTriggered)
			this.wait();
	}
}
