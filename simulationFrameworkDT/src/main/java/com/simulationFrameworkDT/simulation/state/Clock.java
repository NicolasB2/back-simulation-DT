package com.simulationFrameworkDT.simulation.state;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Clock extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int seconds;
	private int minutes;
	private int hours;

	private volatile int clockRate;
	private volatile int clockState;
	private String clockMode;

	// ClockMode
	public final static String CONTINUOUS = "continuous";
	public final static String DISCRET = "discret";

	// ClockState
	public final static int STOPED = 0;
	public final static int RUNNING = 1;
	public final static int PAUSED = 2;
	
	// Read Speed
	public final static int ONE_TO_ONE = 1000;
	public final static int ONE_TO_FIVE = 5000;
	public final static int ONE_TO_TEN = 10000;
	public final static int ONE_TO_THIRTY = 30000;
	public final static int ONE_TO_SIXTY = 60000;
	
	// Animation Speed
	public final static int FAST = 10;
	public final static int NORMAL = 100;
	public final static int SLOW = 1000;
	
	private Date dataDateTime;

	public Clock() {
		seconds = 0;
		minutes = 0;
		hours = 0;
		clockRate = 1;
		clockState = PAUSED;
	}
	
	@Override
	public void run() {
		clockState = RUNNING;
		while (clockState != STOPED) {
			while (clockState == RUNNING) {
				try {
					nextTime();
					Thread.sleep(clockRate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void nextTime() {
		setClockState(RUNNING);

		seconds = (int) (seconds + 1);

		if (seconds >= 60) {
			seconds = 0;
			minutes++;

			if (minutes >= 60) {
				minutes = 0;
				hours++;
			}
		}
	}

	public void getNextTick(Date date) {
		if (clockMode == DISCRET && date !=null) {
			dataDateTime = date;
		}
	}

	public void resumeClock() {
		setClockState(RUNNING);
	}
	
	public void pause() {
		setClockState(PAUSED);
	}

	public void stopClock() {
		setClockState(STOPED);
	}
}
