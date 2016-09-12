package client.dataContainer;

import java.util.ArrayList;
import java.util.List;

import client.client.Client;
import client.lock.Lock;
import client.route.Route;
import client.station.Station;
import client.ticket.Ticket;
import client.ticketNumber.TicketNumber;

public class DataContainer {

	public static final String ADMIN_LOGIN = "admin";
	public boolean isProgramWorking = true;
	private List<Ticket> ticketList = new ArrayList<>();
	private List<Route> routesList = new ArrayList<>();
	private List<Client> clientList = new ArrayList<>();
	private List<Station> stationList = new ArrayList<>();
	private List<Route> workRouteList = new ArrayList<>();
	private Lock lock = new Lock();

	public boolean registerRoute(Route route) {
		if (routesList.contains(route)) {
			return false;
		} else {
			routesList.add(route);
			return true;
		}
	}
	
	public boolean registerStation(Station station) {
		if (stationList.contains(station)) {
			return false;
		} else {
			stationList.add(station);
			return true;
		}
	}
	
	public boolean registerClient(Client client) {
		if (clientList.contains(client)) {
			return false;
		} else {
			clientList.add(client);
			return true;
		}
	}
	
	public boolean registerTicket(Ticket ticket) {
		if (ticketList.contains(ticket)) {
			return false;
		} else {
			ticketList.add(ticket);
			return true;
		}
	}
	
	public boolean loginClient(String login, String password) {
		boolean isOperationSuccesfull = false;
		for (Client client : clientList) {
			if (client.getLogin().equals(login) && client.getPassword().equals(password)) {
				isOperationSuccesfull = true;
			}
		}
		return isOperationSuccesfull;
	}
	
	public boolean isSpecificTicketOnList(TicketNumber ticketNumber) {
		boolean isSpecificTicketOnList = false;
		for (Ticket ticket : ticketList) {
			TicketNumber testTicketNumber = ticket.getTicketNumber();
			if (testTicketNumber.equals(ticketNumber)) {
				isSpecificTicketOnList = true;
			}
		}
		return isSpecificTicketOnList;
	}
	
	public Ticket getTicketByTicketNumber(TicketNumber ticketNumber) {
		Ticket ticket = null;
		for (Ticket testTicket : ticketList) {
			TicketNumber testTicketNumber = testTicket.getTicketNumber();
			if (testTicketNumber.equals(ticketNumber)) {
				ticket = testTicket;
			}
		}
		return ticket;
	}
	
	public Client getClientByLogin(String login) {
		Client client = null;
		for (Client testClient : clientList) {
			if (testClient.getLogin().equals(login)) {
				client = testClient; 
			}
		}
		return client;
	}
	
	public Ticket[] getTicketsArray() {
		Object[] objectsArray = ticketList.toArray();
		Ticket[] ticketsArray = new Ticket[objectsArray.length];
		for (int i = 0; i < objectsArray.length; i++) {
			ticketsArray[i] = (Ticket) objectsArray[i];
		}
		return ticketsArray;
	}
	
	public Route[] getRoutesArray() {
		Object[] objectsArray = routesList.toArray();
		Route[] routesArray = new Route[objectsArray.length];
		for (int i = 0; i < objectsArray.length; i++) {
			routesArray[i] = (Route) objectsArray[i];
		}
		return routesArray;
	}
	
	public Client[] getClientsArray() {
		Object[] objectsArray = clientList.toArray();
		Client[] clientsArray = new Client[objectsArray.length];
		for (int i = 0; i < objectsArray.length; i++) {
			clientsArray[i] = (Client) objectsArray[i];
		}
		return clientsArray;
	}
	
	public Station[] getStationsArray() {
		Object[] objectsArray = stationList.toArray();
		Station[] stationsArray = new Station[objectsArray.length];
		for (int i = 0; i < objectsArray.length; i++) {
			stationsArray[i] = (Station) objectsArray[i];
		}
		return stationsArray;
	}
	
	public Station getStationByIndex(int index) {
		Station station = stationList.get(index);
		return station;
	}

	public ArrayList<Route> generateRoute(Station startingStation, Station targetStation) {
		workRouteList.clear();
		ArrayList<Route> possibleRoutesList = searchForPossibleRoutes(startingStation, targetStation);
		return possibleRoutesList;
	}

	public Integer getTicketListSize() {
		return ticketList.size();
	}

	public Route getRouteByStations(Station startingStation, Station targetStation) {
		Route route = new Route();
		Route[] routeArray = getRoutesArray();
		for (Route testRoute : routeArray) {
			Station testStartingStation = testRoute.getStartingStation();
			Station testTargetStation = testRoute.getTargetStation();
			if (testStartingStation.equals(startingStation) && testTargetStation.equals(targetStation)) {
				route = testRoute;
			}
		}
		return route;
	}

	public ArrayList<Route> getRoutesByStartingStation(Station startingStation) {
		ArrayList<Route> routesList = new ArrayList<>();
		Route[] routeArray = getRoutesArray();
		for (Route testRoute : routeArray) {
			if (testRoute.getStartingStation().equals(startingStation)) {
				routesList.add(testRoute);
			}
		}
		return routesList;
	}
	
	public void lock() {
		try {
			lock.lock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unlock() {
		lock.unlock();
	}

	private ArrayList<Route> searchForPossibleRoutes(Station startingStation, Station targetStation) {
		ArrayList<Route> possibleRoutes = new ArrayList<>();
		ArrayList<Route> routesByStartingStation = getRoutesByStartingStation(startingStation);
		for (Route route : routesByStartingStation) {
			Station testTargetStation = route.getTargetStation();
			if (!workRouteList.contains(route)) {
				workRouteList.add(route);
				if (testTargetStation.equals(targetStation)) {
					possibleRoutes.add(route);
				} else {
					ArrayList<Route> anotherRoutes = searchForPossibleRoutes(testTargetStation, targetStation);
					ArrayList<Route> mergedRoutes = mergeRoutes(anotherRoutes, startingStation, route, targetStation);
					possibleRoutes.addAll(mergedRoutes);
				}
			}
		}
		return possibleRoutes;
	}

	private ArrayList<Route> mergeRoutes(ArrayList<Route> routeList, Station startingStation, Route parentRoute,
			Station targetStation) {
		ArrayList<Route> mergedRoutes = new ArrayList<Route>();
		for (Route route : routeList) {
			Route mergedRoute = new Route();
			mergedRoute.setStartingStation(startingStation);
			mergedRoute.setDistanceInKm(parentRoute.getDistanceInKm() + route.getDistanceInKm());
			mergedRoute.setTargetStation(targetStation);
			mergedRoute.getMidStationList().add(route.getStartingStation());
			mergedRoute.setEndingHour(route.getEndingHour());
			mergedRoute.setStartingHour(parentRoute.getStartingHour());
			for (Station station : route.getMidStationList()) {
				mergedRoute.getMidStationList().add(station);
			}
			mergedRoutes.add(mergedRoute);
		}
		return mergedRoutes;
	}

}
