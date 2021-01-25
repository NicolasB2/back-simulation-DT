package com.simulationFrameworkDT.analytics;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.events.DatagramEvent;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class VisualizationAnalytics {

	@Autowired
	private DataSourceSystem dataSource;

	public HashMap<Long, SITMStop> stops; // HashMap with the stops
	public HashMap<Long, ArrayList<DatagramEvent>> stopsBuses; // HashMap with the array of buses in one stop

	public HashMap<Long, ArrayList<Long[]>> stopsWaitingTimes; // Excess Waiting Time at Bus stop
	public HashMap<Long, ArrayList<Long[]>> busesWaitingTimes; // Bus Stop Time

	/*
	 * This method initialize the hash maps with the necessaries stop ids and arrays
	 */
	public void init(Project project) {

		ArrayList<SITMStop> stopsQuery = dataSource.findAllStopsByLine(project.getFileType(),project.getPlanVersionId(), project.getLineId());
		stops = new HashMap<>();
		stopsBuses = new HashMap<>();

		stopsWaitingTimes = new HashMap<>();
		busesWaitingTimes = new HashMap<>();

		for (int i = 0; i < stopsQuery.size(); i++) {
			if (!stops.containsKey(stopsQuery.get(i).getStopId())) {
				stops.put(stopsQuery.get(i).getStopId(), stopsQuery.get(i));
				stopsBuses.put(stopsQuery.get(i).getStopId(), new ArrayList<DatagramEvent>());

				stopsWaitingTimes.put(stopsQuery.get(i).getStopId(), new ArrayList<Long[]>());
				busesWaitingTimes.put(stopsQuery.get(i).getStopId(), new ArrayList<Long[]>());
			}
		}
	}

	/*
	 * This method evaluates if the bus is inside the area of station
	 */
	private static boolean isInTheStop(DatagramEvent datagram, SITMStop stop) {

		double lat1 = stop.getDecimalLatitude();
		double lng1 = stop.getDecimalLongitude();
		double lat2 = datagram.getLatitude();
		double lng2 = datagram.getLongitude();

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return c <= 0.00003 ? true : false;
	}

	/*
	 * This method turn a hash into a datagram class
	 */
	public void analysisPerBus(Event event) throws ParseException {

		if (event instanceof DatagramEvent) {
			DatagramEvent datagram = (DatagramEvent) event;
			analysisPerBus(datagram);
		}
	}

	/*
	 * This method analyze one datagram
	 */
	private void analysisPerBus(DatagramEvent datagram) throws ParseException {

		long stopId = datagram.getStopId();
		ArrayList<DatagramEvent> buses = stopsBuses.get(stopId);
		SITMStop stop = stops.get(stopId);

		boolean isInStation = false;
		int datagramIndex = -1;

		for (int i = 0; i < buses.size(); i++) {
			if (buses.get(i).getBusId() == datagram.getBusId()) {
				isInStation = true;
				datagramIndex = i;
				i = buses.size();
			}
		}

		boolean x = isInTheStop(datagram, stop);

		if (x) {// the bus is inside the polygon

			if (stopId == 502300)
				System.out.println("inside " + datagram.getBusId() + " " + datagram.getDatagramDate());

			if (!isInStation) { // The bus arrive the stop
				stopsBuses.get(stopId).add(datagram);
				Long[] times = new Long[3];
				times[1] = datagram.getDatagramDateTime();
				stopsWaitingTimes.get(stopId).add(times);
			}

		} else if (!x && isInStation) { // The bus is outside the polygon

			if (!buses.isEmpty()) { // The stop isn't empty

				if (stopId == 502300)
					System.out.println("=====> outside " + datagram.getBusId() + " " + datagram.getDatagramDate());

				Long[] times = new Long[4];
				times[0] = datagram.getBusId();// busId
				times[1] = buses.get(datagramIndex).getDatagramDateTime();// arrivalPolygon
				times[2] = datagram.getDatagramDateTime();// leavePolygon
				times[3] = (times[1] + times[2]) / 2;// Time of arrival, open doors
				busesWaitingTimes.get(stopId).add(times);
			}

			buses.remove(datagramIndex);

			if (buses.isEmpty()) {// The stop is empty

				if (stopId == 502300)
					System.out.println("==============> Empty stop " + datagram.getBusId() + "-" + datagram.getDatagramDate());

				int lastPosition = stopsWaitingTimes.get(stopId).size() - 1;
				Long[] times = stopsWaitingTimes.get(stopId).get(lastPosition);
				times[0] = datagram.getBusId();
				times[2] = datagram.getDatagramDateTime();
			}

		}
	}
}
