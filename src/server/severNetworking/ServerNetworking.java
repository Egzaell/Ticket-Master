package server.severNetworking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.dataContainer.DataContainer;
import client.networking.Networking;
import client.ticket.*;

public class ServerNetworking extends Networking {
	
	private boolean isConnectionActive;

	public ServerNetworking(DataContainer dataContainer) {
		super(dataContainer);
		isConnectionActive = true;
	}
	
	public ServerNetworking(DataContainer dataContainer, Socket socket) {
		this(dataContainer);
		this.socket = socket;
	}
	
	protected void initializeConnection() throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}
	
	protected void synchronize() throws IOException, ClassNotFoundException {
		while (isConnectionActive) {
			dataContainer.lock();
			send();
			receive();
			dataContainer.unlock();
		}
	}
	
	protected void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			} else if (incomingLine.equals(CONNECTION_END_FLAG)) {
				isConnectionActive = false;
			}
		}
	}

}
