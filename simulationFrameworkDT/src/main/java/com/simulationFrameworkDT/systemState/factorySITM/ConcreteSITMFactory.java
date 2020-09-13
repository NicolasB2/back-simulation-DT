package com.simulationFrameworkDT.systemState.factorySITM;

import java.util.ArrayList;

import com.simulationFrameworkDT.systemState.factoryInterfaces.AbstractModelFactory;
import com.simulationFrameworkDT.systemState.factoryInterfaces.IArc;
import com.simulationFrameworkDT.systemState.factoryInterfaces.IBus;
import com.simulationFrameworkDT.systemState.factoryInterfaces.ICalendar;
import com.simulationFrameworkDT.systemState.factoryInterfaces.ILine;
import com.simulationFrameworkDT.systemState.factoryInterfaces.ILineStop;
import com.simulationFrameworkDT.systemState.factoryInterfaces.IPlanVersion;
import com.simulationFrameworkDT.systemState.factoryInterfaces.IScheduleTypes;
import com.simulationFrameworkDT.systemState.factoryInterfaces.IStop;
import com.simulationFrameworkDT.systemState.factoryInterfaces.ITask;
import com.simulationFrameworkDT.systemState.factoryInterfaces.ITrip;

public class ConcreteSITMFactory implements AbstractModelFactory {

	public static final int planVersionID = 180;

	@Override
	public IPlanVersion createPlanVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IStop> createStops() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ILine> createLines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IBus> createBuses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IArc> createArcs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ITask> createTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ITrip> createTrips() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ILineStop> createLineStops() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IScheduleTypes> createScheduleTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ICalendar> createCalendars() {
		// TODO Auto-generated method stub
		return null;
	}
	
}