package client.ticket;

import java.math.BigDecimal;

import client.ticketNumber.TicketNumber;

public class ChildToFourYearsDiscount extends TicketDecorator {

	Ticket ticket;

	public ChildToFourYearsDiscount(Ticket ticket) {
		this.ticket = ticket;
		this.route = ticket.getRoute();
		this.price = calculatePrice(route.getDistanceInKm());
		this.type = "Dla dzieci do lat 4";
		this.ticketNumber = ticket.getTicketNumber();
		this.client = ticket.getClient();
	}

	@Override
	public BigDecimal calculatePrice(double distanceInKm) {
		BigDecimal price = ticket.calculatePrice(distanceInKm);
		BigDecimal discount = new BigDecimal("0");
		price = price.multiply(discount);
		return price;
	}
	
	public TicketNumber getTicketNumber() {
		return ticket.getTicketNumber();
	}

}
