package com.simulationFrameworkDT.model.factorySITM;

import java.util.ArrayList;

import com.simulationFrameworkDT.model.factoryInterfaces.AbstractModelFactory;
import com.simulationFrameworkDT.model.factoryInterfaces.IArc;
import com.simulationFrameworkDT.model.factoryInterfaces.IBus;
import com.simulationFrameworkDT.model.factoryInterfaces.ICalendar;
import com.simulationFrameworkDT.model.factoryInterfaces.ILine;
import com.simulationFrameworkDT.model.factoryInterfaces.ILineStop;
import com.simulationFrameworkDT.model.factoryInterfaces.IPlanVersion;
import com.simulationFrameworkDT.model.factoryInterfaces.IScheduleTypes;
import com.simulationFrameworkDT.model.factoryInterfaces.IStop;
import com.simulationFrameworkDT.model.factoryInterfaces.ITask;
import com.simulationFrameworkDT.model.factoryInterfaces.ITrip;

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