package client.station;

import java.io.Serializable;

public class Station implements Serializable {

	private String name;
	
	public Station() {
		this.name = "";
	}

	public Station(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override 
	public boolean equals(Object aStation) {
		Station station = (Station) aStation;
		String name = station.getName();
		return this.name.equals(name);
	}
}
