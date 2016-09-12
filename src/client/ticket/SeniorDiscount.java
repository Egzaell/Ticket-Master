package client.ticket;

import java.math.BigDecimal;

import client.ticketNumber.TicketNumber;

public class SeniorDiscount extends TicketDecorator {

	Ticket ticket;
	
	public SeniorDiscount(Ticket ticket) {
		this.ticket = ticket;
		this.route = ticket.getRoute();
		this.price = calculatePrice(route.getDistanceInKm());
		this.type = "Seniorski";
		this.ticketNumber = ticket.getTicketNumber();
		this.client = ticket.getClient();
	}
	
	@Override
	public BigDecimal calculatePrice(double distanceInKm) {
		BigDecimal price = ticket.calculatePrice(distanceInKm);
		BigDecimal discount = new BigDecimal("0.37");
		price = price.multiply(discount);
		return price;
	}

	public TicketNumber getTicketNumber() {
		return ticket.getTicketNumber();
	}
}
