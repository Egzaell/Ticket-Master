package client.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import client.client.Client;
import client.dataContainer.DataContainer;
import client.route.Route;
import client.station.Station;
import client.ticket.Ticket;

public class Networking implements Runnable {

	protected final String SENDING_END_FLAG = "END";
	protected final String CLIENT_FLAG = "CLIENT";
	protected final String TICKET_FLAG = "TICKET";
	protected final String ROUTE_FLAG = "ROUTE";
	protected final String STATION_FLAG = "STATION";
	protected final String CONNECTION_END_FLAG = "END_CONNECTION";
	protected Socket socket;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	protected DataContainer dataContainer;
	
	public Networking(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
	}
	
	@Override
	public void run() {
		try {
			initializeConnection();
			synchronize();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Zakonczono polaczenie");
		} finally {
			disconnect();
		}
	}
	
	protected void disconnect() {
		try {
			receive();
			sendConnectionEndFlag();
			sendEndingFlag();
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void initializeConnection() throws UnknownHostException, IOException {
			socket = new Socket("127.0.0.1", 8888);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
	}
	
	protected void synchronize() throws ClassNotFoundException, IOException {
		while (dataContainer.isProgramWorking) {
			dataContainer.lock();
			send();
			receive();
			dataContainer.unlock();
		}
	}
	
	protected void receive() throws ClassNotFoundException, IOException {
		String incomingLine = "";
		while (!incomingLine.equals(SENDING_END_FLAG)){
			incomingLine = (String) in.readObject();
			if (incomingLine.equals(CLIENT_FLAG)){
				receiveClient();
			} else if (incomingLine.equals(TICKET_FLAG)) {
				receiveTicket();
			} else if (incomingLine.equals(ROUTE_FLAG)) {
				receiveRoute();
			} else if (incomingLine.equals(STATION_FLAG)) {
				receiveStation();
			}
		}
	}
	
	protected void send() throws IOException {
		sendClients();
		sendStations();
		sendRoutes();
		sendTickets();
		sendEndingFlag();
		out.flush();
	}
	
	protected void sendClients() throws IOException {
		Client[] clientsArray = dataContainer.getClientsArray();
		for (Client client : clientsArray) {
			out.writeObject(CLIENT_FLAG);
			out.writeObject(client);
		}
	}
	
	protected void sendStations() throws IOException {
		Station[] stationArray = dataContainer.getStationsArray();
		for (Station station : stationArray) {
			out.writeObject(STATION_FLAG);
			out.writeObject(station);
		}
	}
	
	protected void sendRoutes() throws IOException {
		Route[] routesArray = dataContainer.getRoutesArray();
		for (Route route : routesArray) {
			out.writeObject(ROUTE_FLAG);
			out.writeObject(route);
		}
	}
	
	protected void sendTickets() throws IOException {
		Ticket[] ticketsArray = dataContainer.getTicketsArray();
		for (Ticket ticket : ticketsArray) {
			out.writeObject(TICKET_FLAG);
			out.writeObject(ticket);
		}
	}
	
	protected void sendEndingFlag() throws IOException {
		out.writeObject(SENDING_END_FLAG);
	}
	
	protected void sendConnectionEndFlag() throws IOException {
		out.writeObject(CONNECTION_END_FLAG);
	}
	
	protected void receiveClient() throws ClassNotFoundException, IOException {
		Client client = (Client) in.readObject();
		dataContainer.registerClient(client);
	}
	
	protected void receiveTicket() throws ClassNotFoundException, IOException {
		Ticket ticket = (Ticket) in.readObject();
		dataContainer.registerTicket(ticket);
	}
	
	protected void receiveRoute() throws ClassNotFoundException, IOException {
		Route route = (Route) in.readObject();
		dataContainer.registerRoute(route);
	}
	
	protected void receiveStation() throws ClassNotFoundException, IOException {
		Station station = (Station) in.readObject();
		dataContainer.registerStation(station);
	}
}
