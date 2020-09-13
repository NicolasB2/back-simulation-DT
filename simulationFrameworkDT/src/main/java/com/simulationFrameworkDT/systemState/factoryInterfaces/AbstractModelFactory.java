package com.simulationFrameworkDT.systemState.factoryInterfaces;

import java.util.ArrayList;

public interface AbstractModelFactory {
	
	public IPlanVersion createPlanVersion();
	public ArrayList<IStop> createStops();
	public ArrayList<ILine> createLines();
	public ArrayList<IBus> createBuses();
	public ArrayList<IArc> createArcs();
	public ArrayList<ITask> createTasks();
	public ArrayList<ITrip> createTrips();
	public ArrayList<ILineStop> createLineStops();
	public ArrayList<IScheduleTypes> createScheduleTypes();
	public ArrayList<ICalendar> createCalendars();
}