package client.ticket;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import client.client.Client;
import client.dataContainer.DataContainer;
import client.route.Route;
import client.ticketNumber.TicketNumber;

public abstract class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;
	protected BigDecimal price;
	protected Route route;
	protected Client client;
	protected TicketNumber ticketNumber;
	protected String type;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Route getRoute() {
		return route;
	}

	public Client getClient() {
		return client;
	}

	public TicketNumber getTicketNumber() {
		return ticketNumber;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object aTicket) {
		Ticket ticket = (Ticket) aTicket;
		TicketNumber ticketNumber = ticket.getTicketNumber();
		return this.ticketNumber.equals(ticketNumber);
	}
	
	@Override
	public String toString() {
		return route +  " | cena: " + price;
	}
	
	protected BigDecimal calculatePrice(double distanceInKm) {
		MathContext mathContext = new MathContext(2, RoundingMode.CEILING);
		double price = distanceInKm * 0.09;
		this.price = new BigDecimal(price);
		this.price = this.price.round(mathContext);
		return this.price;
	}
}
