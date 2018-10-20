package web.schach.gruppe6.util;

public class Task extends Awaitable implements Runnable {
	
	private final Runnable run;
	
	public Task(Runnable run) {
		this.run = run;
	}
	
	@Override
	public void run() {
		run.run();
		trigger();
	}
}
