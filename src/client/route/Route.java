package client.route;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.station.Station;

public class Route implements Serializable {

	private static final String NEW_LINE = System.getProperty("line.separator");
	private Station startingStation;
	private Station targetStation;
	private Double distanceInKm;
	private Date startingHour;
	private Date endingHour;
	private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm");
	private List<Station> midStationList = new ArrayList<>();

	public Route() {
		this.distanceInKm = 0.0;
		this.startingStation = new Station();
		this.targetStation = new Station();
	}

	public Route(Station startingStation, Station targetStation, double distanceInKm, String startingHour,
			String endingHour) {
		this.startingStation = startingStation;
		this.targetStation = targetStation;
		this.distanceInKm = distanceInKm;
		parseStringsToDates(startingHour, endingHour);
	}

	public void setStartingStation(Station startingStation) {
		this.startingStation = startingStation;
	}

	public void setTargetStation(Station targetStation) {
		this.targetStation = targetStation;
	}

	public void setDistanceInKm(Double distanceInKm) {
		this.distanceInKm = distanceInKm;
	}

	public Station getStartingStation() {
		return startingStation;
	}

	public Station getTargetStation() {
		return targetStation;
	}

	public double getDistanceInKm() {
		return distanceInKm;
	}

	public Date getStartingHour() {
		return startingHour;
	}

	public Date getEndingHour() {
		return endingHour;
	}
	
	public String getStartingHourString() {
		return hourFormat.format(startingHour);
	}

	public String getEndingHourString() {
		return hourFormat.format(endingHour);
	}

	public void setStartingHour(Date startingHour) {
		this.startingHour = startingHour;
	}

	public void setEndingHour(Date endingHour) {
		this.endingHour = endingHour;
	}

	public ArrayList<Station> getMidStationList() {
		return (ArrayList<Station>) midStationList;
	}

	@Override
	public boolean equals(Object aRoute) {
		Route route = (Route) aRoute;
		Station startingStation = route.getStartingStation();
		Station targetStation = route.getTargetStation();
		Double distanceInKm = route.getDistanceInKm();
		Date startingHour = route.getStartingHour();
		Date endingHour = route.getEndingHour();
		return this.startingStation.equals(startingStation) && this.targetStation.equals(targetStation)
				&& this.distanceInKm.equals(distanceInKm) && this.startingHour.equals(startingHour)
				&& this.endingHour.equals(endingHour);
	}

	@Override
	public String toString() {
		if (midStationList.isEmpty()) {
			return "Trasa: " + startingStation + " - " + targetStation + ": odleglosc - " + distanceInKm + " km"
					+ NEW_LINE + " godzina wyjazdu: " + hourFormat.format(startingHour) + ", godzina pzyjazdu: "
					+ hourFormat.format(endingHour) + NEW_LINE + " czas podrozy: "
					+ getTravelTime(startingHour, endingHour) + "h";
		} else {
			return "Trasa: " + startingStation + " - " + targetStation + ": odleglosc - " + distanceInKm + " km"
					+ NEW_LINE + " przez: " + buildMidStationString() + NEW_LINE + " godzina wyjazdu: "
					+ hourFormat.format(startingHour) + ", godzina pzyjazdu: " + hourFormat.format(endingHour)
					+ NEW_LINE + " czas podrozy: " + getTravelTime(startingHour, endingHour) + "h";
		}
	}

	private void parseStringsToDates(String startingHour, String endingHour) {
		try {
			this.startingHour = hourFormat.parse(startingHour);
			this.endingHour = hourFormat.parse(endingHour);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String buildMidStationString() {
		String midStations = "";
		for (Station station : midStationList) {
			midStations += station.getName() + " ";
		}
		return midStations;
	}

	private int getTravelTime(Date date1, Date date2) {
		int timeInHours = 0;
		Double time = (date2.getTime() - date1.getTime()) / 60000.0;
		if (time <= 0.0) {
			time += 720.0;
		}
		time = time / 60.0;
		timeInHours = (int) Math.round(time);
		return timeInHours;
	}

}
