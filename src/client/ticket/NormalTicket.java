package client.ticket;

import java.math.BigDecimal;

import client.client.Client;
import client.dataContainer.DataContainer;
import client.route.Route;
import client.ticketNumber.TicketNumber;

public class NormalTicket extends Ticket {
	
	public NormalTicket(Route route, Client client, DataContainer dataContainer) {
		this.route = route;
		this.client = client;
		this.price = calculatePrice(route.getDistanceInKm());
		this.ticketNumber = new TicketNumber(dataContainer);
		this.type = "Normalny";
	}
	
	public NormalTicket(Route route, Client client, BigDecimal price, TicketNumber ticketNumber, String type) {
		this.route = route;
		this.client = client;
		this.price = price;
		this.ticketNumber = ticketNumber;
		this.type = type;
	}
	
}
