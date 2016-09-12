package client.ticket;

import java.math.BigDecimal;

public abstract class TicketDecorator extends Ticket {

	public abstract BigDecimal calculatePrice(double distanceInKm);
}
