package com.simulationFrameworkDT.simulation.state;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Clock implements Serializable {

	private static final long serialVersionUID = 1L;

	// Attributes
	private volatile int animationSpeed;
	private volatile int readSpeed;
	private Date dataDateTime;
	private String clockMode;

	// ClockMode
	public final static String DISCRET = "discret";
	public final static String CONTINUOUS = "continuous";
	
	// Animation Speed
	public final static int FAST   = 10;
	public final static int NORMAL = 100;
	public final static int SLOW   = 800;
	
	// Read Speed
	public final static int ONE_TO_ONE  = 1000;
	public final static int ONE_TO_FIVE = 5000;
	public final static int ONE_TO_TEN  = 10000;
	public final static int ONE_TO_THIRTY = 30000;
	public final static int ONE_TO_SIXTY  = 60000;

	public Clock() {
		clockMode = DISCRET;
		readSpeed = ONE_TO_FIVE;
		animationSpeed = FAST;
	}
	
	public void getNextTick(Date date) {
		if (clockMode == DISCRET && date !=null) {
			dataDateTime = date;
		}
	}

	//================================================================================
    // Animation Speed
    //================================================================================

	public void setFastSpeed() {
		this.animationSpeed = FAST;
		System.out.println("=======> set Fast Speed");
	}

	public void setNormalSpeed() {
		this.animationSpeed = NORMAL;
		System.out.println("=======> set Normal Speed");
	}

	public void setSlowSpeed() {
		this.animationSpeed = SLOW;
		System.out.println("=======> set Slow Speed");
	}

	//================================================================================
    // Read Speed
    //================================================================================
	
	public void setOneToOneSpeed() {
		this.readSpeed = ONE_TO_ONE;
		System.out.println("=======> set One To One Speed");
	}

	public void setOneToFiveSpeed() {
		this.readSpeed = ONE_TO_FIVE;
		System.out.println("=======> set One To Five Speed");
	}

	public void setOneToTenSpeed() {
		this.readSpeed = ONE_TO_TEN;
		System.out.println("=======> set One To Ten Speed");
	}

	public void setOneToThirtySpeed() {
		this.readSpeed = ONE_TO_THIRTY;
		System.out.println("=======> set One To Thirty Speed");
	}

	public void setOneToSixtySpeed() {
		this.readSpeed = ONE_TO_SIXTY;
		System.out.println("=======> set One To Sixty Speed");
	}
}
