package todo;

import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;

public class ClockState {
	private ClockOutput output;
	private MutexSem sem;
	
	private int clockTime;
	private int alarmTime;
	private boolean alarmFlag;
	private boolean alarmState;
	private int alarmRings;
	private int alarmRingsMax;
	
	public ClockState(ClockOutput output) {
		this.sem = new MutexSem();
		this.output = output;
		
		clockTime = 5955;
		alarmTime = 10000;
		alarmState = false;
		alarmRings = 0;
		alarmRingsMax = 5;
	}
	public void setAlarmFlag(boolean flag) {
		alarmFlag = flag;
	}
	public void setAlarmTime(int time) {
		alarmTime = time;
	}
	public boolean shouldAlarmTrigger() {
		return alarmFlag && alarmTime == clockTime;
	}
	public void setAlarmState(boolean isActive) {
		alarmState = isActive;
	}
	public boolean getAlarmState() {
		return alarmState;
	}
	public void tick() {
		sem.take();
		int nextTime = clockTime + 1;
		if (nextTime % 100 > 59) {
			// Remove the seconds and add a minute
			nextTime -= 60;
			nextTime += 100;
		}
		if (nextTime % 10000 > 5959) {
			// Remove minutes and add an hour
			nextTime -= 6000;
			nextTime += 10000;
		}
		if (nextTime > 235959) {
			// New dawn!
			nextTime = 0;
		}
		clockTime = nextTime;
		if (shouldAlarmTrigger()) {
			setAlarmState(true);
		}
		if (getAlarmState() && alarmRings < alarmRingsMax) {
			alarmRings += 1;
			output.doAlarm();
		} else {
			setAlarmState(false);
			alarmRings = 0;
		}
		sem.give();
	}
	public void setTime(int time) {
		sem.take();
		clockTime = time;
		sem.give();
	}
	public int getTime() {
		sem.take();
		sem.give();
		return clockTime;
	}
}
