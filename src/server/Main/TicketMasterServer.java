package server.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.dataContainer.DataContainer;
import server.dataBaseConnector.DataBaseConnector;
import server.severNetworking.ServerNetworking;
import server.userInterface.UserInterface;

public class TicketMasterServer {

	ServerSocket serverSocket;
	DataContainer dataContainer;
	
	public TicketMasterServer(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
		openServerSocket();
	}
	
	private ServerSocket openServerSocket() {
		try {
			serverSocket = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serverSocket;
	}
	
	private void acceptConnection(Socket socket) {
			ServerNetworking serverNetworking = new ServerNetworking(dataContainer, socket);
			Thread clientHandler = new Thread(serverNetworking);
			clientHandler.start();
	}
	
	public static void main(String[] args) throws IOException {
		DataContainer dataContainer = new DataContainer();
		DataBaseConnector dataBaseConnector = new DataBaseConnector(dataContainer);
		UserInterface userInterface = new UserInterface(dataContainer, dataBaseConnector);
		Thread userInterfaceThread = new Thread(userInterface);
		userInterfaceThread.start();
		TicketMasterServer server = new TicketMasterServer(dataContainer);
		while (dataContainer.isProgramWorking) {
			server.acceptConnection(server.serverSocket.accept());
		}
	}

}
