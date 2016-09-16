package todo;
import done.ClockInput;
import se.lth.cs.realtime.semaphore.Semaphore;

public class InputThread extends Thread {
	private Semaphore sem;
	private ClockInput input;
	private ClockState state;
	private int prevChoice;
	
	public InputThread(ClockInput input, ClockState state) {
		this.input = input;
		this.state = state;
		this.sem = input.getSemaphoreInstance();
	}
	public void run() {
		while (true) {
			sem.take();
			state.setAlarmFlag(input.getAlarmFlag());
			if (prevChoice != input.getChoice()) {
				if (state.getAlarmState()) {
					state.setAlarmState(false);
				}
				if (prevChoice == ClockInput.SET_TIME) {
					state.setTime(input.getValue());
				}
				if (prevChoice == ClockInput.SET_ALARM) {
					state.setAlarmTime(input.getValue());
				}
				prevChoice = input.getChoice();
			}
		}
	}
}
