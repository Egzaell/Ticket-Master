package client.mainApp;

import client.dataContainer.DataContainer;
import client.gui.GUI;
import client.networking.Networking;
import client.refresher.Refresher;

public class TicketMaster {

	public static void main(String[] args) {
		DataContainer dataContainer = new DataContainer();
		Networking networking = new Networking(dataContainer);
		Thread networkingThread = new Thread(networking);
		GUI gui = new GUI(dataContainer);
		Refresher refresher = new Refresher(dataContainer, gui);
		Thread refresherThread = new Thread(refresher);
		networkingThread.start();
		refresherThread.start();
	}
}
