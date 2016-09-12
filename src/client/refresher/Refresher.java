package client.refresher;

import client.dataContainer.DataContainer;
import client.gui.GUI;
import client.station.Station;
import client.ticket.Ticket;

public class Refresher implements Runnable {

	private DataContainer dataContainer;
	private GUI gui;
	private int stationQuantity;
	private int ticketsQuantity;

	public Refresher(DataContainer dataContainer, GUI gui) {
		this.dataContainer = dataContainer;
		this.gui = gui;
	}

	@Override
	public void run() {
		while (dataContainer.isProgramWorking) {
			dataContainer.lock();
			refresh();
			dataContainer.unlock();
		}
	}

	private void refresh() {
		refreshStations();
		refreshTickets();
	}

	private void refreshStations() {
		Station[] stationsArray = dataContainer.getStationsArray();
		if (stationsArray.length != stationQuantity) {
			gui.startingStationModel.clear();
			gui.targetStationModel.clear();
			for (Station station : stationsArray) {
				gui.startingStationModel.addElement(station);
				gui.targetStationModel.addElement(station);
			}
			stationQuantity = stationsArray.length;
		}
	}

	private void refreshTickets() {
		Ticket[] ticketsArray = dataContainer.getTicketsArray();
		if (ticketsArray.length != ticketsQuantity) {
			gui.myTicketsModel.clear();
			for (Ticket ticket : ticketsArray) {
				gui.myTicketsModel.addElement(ticket);
			}
			ticketsQuantity = ticketsArray.length;
		}
	}
}
