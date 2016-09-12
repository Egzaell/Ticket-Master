package client.ticket;

import java.math.BigDecimal;

import client.ticketNumber.TicketNumber;

public class HandicapDiscount extends TicketDecorator {

	Ticket ticket;
	
	public HandicapDiscount(Ticket ticket) {
		this.ticket = ticket;
		this.route = ticket.getRoute();
		this.price = calculatePrice(route.getDistanceInKm());
		this.type = "Dla osob niepelnosprawnych";
		this.ticketNumber = ticket.getTicketNumber();
		this.client = ticket.getClient();
	}
	
	@Override
	public BigDecimal calculatePrice(double distanceInKm) {
		BigDecimal price = ticket.calculatePrice(distanceInKm);
		BigDecimal discount = new BigDecimal("0.78");
		price = price.multiply(discount);
		return price;
	}

	public TicketNumber getTicketNumber() {
		return ticket.getTicketNumber();
	}
}
