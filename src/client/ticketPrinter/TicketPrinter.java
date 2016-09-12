package client.ticketPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import client.ticket.Ticket;

public class TicketPrinter {

	private final String TICKET_HEADER = "Bilet kolejowy numer: ";
	private final String NEW_LINE = System.getProperty("line.separator");
	private final String SEPARATOR = "##################################";
	private final String OWNER = "wystawiony na : ";
	private final String TYPE = "typ biletu: ";
	private final String PRICE = "cena: ";
	private Ticket ticket;
	private File file;
	private FileWriter fileWriter;

	public TicketPrinter(Ticket ticket) {
		this.ticket = ticket;
	}
	
	public void printTicket() {
		file = new File(generateFileName());
		try {
			fileWriter = new FileWriter(file);
			generateTicketValues();
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String generateFileName() {
		String fileName = "Bilet " + ticket.getTicketNumber().toString() +".txt";
		return fileName;
	}
	
	private void generateTicketValues() throws IOException {
		fileWriter.append(TICKET_HEADER);
		fileWriter.append(ticket.getTicketNumber().toString());
		fileWriter.append(NEW_LINE);
		fileWriter.append(TYPE);
		fileWriter.append(ticket.getType());
		fileWriter.append(NEW_LINE);
		fileWriter.append(SEPARATOR);
		fileWriter.append(NEW_LINE);
		fileWriter.append(ticket.getRoute().toString());
		fileWriter.append(NEW_LINE);
		fileWriter.append(PRICE + ticket.getPrice().toString());
		fileWriter.append(NEW_LINE);
		fileWriter.append(SEPARATOR);
		fileWriter.append(NEW_LINE);
		fileWriter.append(OWNER);
		fileWriter.append(ticket.getClient().toString());
	}
}
