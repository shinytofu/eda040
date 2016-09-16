package todo;
import done.*;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	
	private ClockState state;
	private TimeThread timeThread;
	private InputThread inputThread;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		
		state = new ClockState(output);
		timeThread = new TimeThread(state);
		// Input thread could be in run() below as well. But w/e.
		inputThread = new InputThread(input, state);
		
		timeThread.start();
		inputThread.start();
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
		while (true) {
			output.showTime(state.getTime());		
		}
	}
}
