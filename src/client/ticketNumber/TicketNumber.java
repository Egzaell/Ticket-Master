package client.ticketNumber;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import client.dataContainer.DataContainer;

public class TicketNumber implements Serializable {

	private static final long serialVersionUID = 1L;
	private int[] number;
	private Date date;
	private String code;
	private SimpleDateFormat dateFormat;
	private Random generator = new Random();
	transient private DataContainer dataContainer;
	
	public TicketNumber(String code) {
		this.code = code;
	}
	
	public TicketNumber(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
		number = generateNumber();;
		dateFormat = new SimpleDateFormat("MM-yyyy");
		date = new Date();
		code = dateFormat.format(date) + "-" + numberToString();
	}
	
	public String getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
	@Override
	public boolean equals(Object aTicketNumber) {
		TicketNumber ticketNumber = (TicketNumber) aTicketNumber;
		String code = ticketNumber.getCode();
		return this.code.equals(code);
	}
	
	private int[] generateNumber() {
		int[] values = new int[15];
		for(int i = 0; i < 15; i++) {
			values[i] = generator.nextInt(10);
		}
		return values;
	}
	
	private String numberToString() {
		String number = "";
		for(int i : this.number){
			number += i;
		}
		return number + "-" + getTicketQuantity();
	} 
	
	private String getTicketQuantity() {
		Integer quantity = dataContainer.getTicketListSize();
		return quantity.toString();
	}
}
