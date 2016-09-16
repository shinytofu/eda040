package todo;

public class TimeThread extends Thread {
	private ClockState state;
	private long sytemTime;
	public TimeThread(ClockState state) {
		this.state = state;
		this.sytemTime = System.currentTimeMillis();
	}
	public void run() {
		while (true) {
			sytemTime += 1000;
			long delta = sytemTime - System.currentTimeMillis();
			if (delta > 0) {
				try {
					sleep(delta);
				} catch (InterruptedException e) {
					// RIP
				}
			}
			state.tick();
		}
	}
}
