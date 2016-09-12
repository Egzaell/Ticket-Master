package client.dataContainer;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import client.route.Route;
import client.station.Station;

public class DataContainerTest {

	Station targetStation;
	Station startStation;
	Station someStation, someStation2, someStation3;
	Route route1;
	Route route2, route3, route4, route5, route6;
	DataContainer dataContainer;

	@Before
	public void setUp() {
		targetStation = new Station("Kielce");
		startStation = new Station("Warszawa");
		someStation = new Station("Radom");
		someStation2 = new Station("Lodz");
		someStation3 = new Station("Szczecin");
		route1 = new Route(startStation, someStation, 127.78, "01:00", "02:57");
		route2 = new Route(someStation, targetStation, 76.12, "03:15", "04:20");
		route3 = new Route(startStation, someStation2, 250.0, "07:24", "10:15");
		route4 = new Route(someStation2, someStation, 150.0, "06:34", "12:13");
		route5 = new Route(someStation3, someStation2, 382.12, "08:12", "13:15");
		route6 = new Route(someStation2, startStation, 250.0, "14:16", "15:23");
		Route route7 = new Route(someStation, startStation, 127.78, "12:46", "16:50");
		dataContainer = new DataContainer();
		dataContainer.registerRoute(route1);
		dataContainer.registerRoute(route2);
		dataContainer.registerRoute(route3);
		dataContainer.registerRoute(route4);
		dataContainer.registerRoute(route5);
		dataContainer.registerRoute(route6);
		dataContainer.registerRoute(route7);
	}

	@Test
	public void existingRouteTest() {
		ArrayList<Route> routeList = dataContainer.generateRoute(someStation3, targetStation);
		System.out.println(routeList);
	}

}
