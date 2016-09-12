package server.dataBaseConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import client.client.Client;
import client.dataContainer.DataContainer;
import client.route.Route;
import client.station.Station;
import client.ticket.NormalTicket;
import client.ticket.Ticket;
import client.ticketNumber.TicketNumber;

public class DataBaseConnector {

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:Data_Base.db";
	private Connection connection;
	private Statement statement;
	private DataContainer dataContainer;
	private List<Client> clientsList = new ArrayList<>();
	private List<Station> stationsList = new ArrayList<>();
	private List<Route> routesList = new ArrayList<>();
	private List<Ticket> ticketsList = new ArrayList<>();

	public DataBaseConnector(DataContainer dataContainer) {
		getJDBCDriverClass();
		connection = connectToDataBase();
		statement = createStatement();
		this.dataContainer = dataContainer;
		createTables();
		selectDataFromDataBase();
	}

	public void saveDataToDataBase() {
		dataContainer.lock();
		updateLists();
		dropTables();
		createTables();
		System.out.println("Rozpoczynam zapis danych do bazy:");
		System.out.println("Zapisuje klientow:");
		for (Client client : clientsList) {
			insertClient(client);
			System.out.print("#");
		}
		System.out.println("");
		System.out.println("Zapisuje stacje:");
		for (Station station : stationsList) {
			insertStation(station);
			System.out.print("#");
		}
		System.out.println("");
		System.out.println("Zapisuje trasy:");
		for (Route route : routesList) {
			insertRoute(route);
			System.out.print("#");
		}
		System.out.println("");
		System.out.println("Zapisuje bilety:");
		for (Ticket ticket : ticketsList) {
			insertTicket(ticket);
			System.out.print("#");
		}
		System.out.println("");
		System.out.println("Zapis zakonczony powodzeniem!");
		dataContainer.unlock();
	}

	private void selectDataFromDataBase() {
		dataContainer.lock();
		selectStations();
		selectClients();
		selectRoutes();
		selectTickets();
		dataContainer.unlock();
	}

	private void updateLists() {
		clientsList.clear();
		stationsList.clear();
		routesList.clear();
		ticketsList.clear();
		Client[] clientsArray = dataContainer.getClientsArray();
		Station[] stationsArray = dataContainer.getStationsArray();
		Route[] routesArray = dataContainer.getRoutesArray();
		Ticket[] ticketsArray = dataContainer.getTicketsArray();
		for (Client client : clientsArray) {
			clientsList.add(client);
		}
		for (Station station : stationsArray) {
			stationsList.add(station);
		}
		for (Route route : routesArray) {
			routesList.add(route);
		}
		for (Ticket ticket : ticketsArray) {
			ticketsList.add(ticket);
			Route route = ticket.getRoute();
			if (!routesList.contains(route)) {
				routesList.add(route);
			}
		}
	}

	private void createTables() {
		String createCients = "CREATE TABLE IF NOT EXISTS CLIENTS (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName VARCHAR(255), lastName VARCHAR(255), login VARCHAR(255), password VARVHAR(255))";
		String createStations = "CREATE TABLE IF NOT EXISTS STATIONS (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255))";
		String createRoutes = "CREATE TABLE IF NOT EXISTS ROUTES(id INTEGER PRIMARY KEY AUTOINCREMENT, startingStation_id INTEGER REFERENCES STATIONS(id), targetStation_id INTEGER REFERENCES STATIONS(id), distanceInKm DECIMAL(8,2), startingHour VARCHAR(20), endingHour VARCHAR(20), midStationsIds VARCHAR(255))";
		String createTickets = "CREATE TABLE IF NOT EXISTS TICKETS(id INTEGER PRIMARY KEY AUTOINCREMENT, price DECIMAL(8,2), route_id INTEGER REFERENCES ROUTES(id), client_id INTEGER REFERENCES CLIENTS(id), ticketNumber VARCHAR(255), type VARCHAR(255))";
		try {
			statement.execute(createCients);
			statement.execute(createStations);
			statement.execute(createRoutes);
			statement.execute(createTickets);
		} catch (SQLException e) {
			System.err.println("blad przy tworzeniu tabel");
			e.printStackTrace();
		}
	}

	private void dropTables() {
		String dropCients = "DROP TABLE CLIENTS";
		String dropStations = "DROP TABLE STATIONS";
		String dropRoutes = "DROP TABLE ROUTES";
		String dropTickets = "DROP TABLE TICKETS";
		try {
			statement.execute(dropCients);
			statement.execute(dropStations);
			statement.execute(dropRoutes);
			statement.execute(dropTickets);
		} catch (SQLException e) {
			System.err.println("blad przy usowaniu tabel");
			e.printStackTrace();
		}
	}

	private void insertClient(Client client) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO CLIENTS VALUES(NULL, ?, ?, ?, ?);");
			preparedStatement.setString(1, client.getFirstName());
			preparedStatement.setString(2, client.getLastName());
			preparedStatement.setString(3, client.getLogin());
			preparedStatement.setString(4, client.getPassword());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("blad przy wstawianiu klienta");
			e.printStackTrace();
		}
	}

	private void insertStation(Station station) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO STATIONS VALUES(NULL, ?);");
			preparedStatement.setString(1, station.getName());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("blad przy wstawianiu stacji");
			e.printStackTrace();
		}
	}

	private void insertRoute(Route route) {
		int startingStationId = stationsList.indexOf(route.getStartingStation()) + 1;
		int targetStationId = stationsList.indexOf(route.getTargetStation()) + 1;
		double distanceInKm = route.getDistanceInKm();
		String startingHour = route.getStartingHourString();
		String endingHour = route.getEndingHourString();
		String midStationsIds = generateMidStationsIds(route);
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO ROUTES VALUES(NULL, ?, ?, ?, ?, ?, ?);");
			preparedStatement.setInt(1, startingStationId);
			preparedStatement.setInt(2, targetStationId);
			preparedStatement.setDouble(3, distanceInKm);
			preparedStatement.setString(4, startingHour);
			preparedStatement.setString(5, endingHour);
			preparedStatement.setString(6, midStationsIds);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("blad przy wstawianiu trasy");
			e.printStackTrace();
		}
	}

	private void insertTicket(Ticket ticket) {
		double price = ticket.getPrice().doubleValue();
		int routeId = routesList.indexOf(ticket.getRoute()) + 1;
		int clientId = clientsList.indexOf(ticket.getClient()) + 1;
		String ticketNumber = ticket.getTicketNumber().toString();
		String type = ticket.getType();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO TICKETS VALUES(NULL, ?, ?, ?, ?, ?);");
			preparedStatement.setDouble(1, price);
			preparedStatement.setInt(2, routeId);
			preparedStatement.setInt(3, clientId);
			preparedStatement.setString(4, ticketNumber);
			preparedStatement.setString(5, type);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("blad przy wstawianiu biletu");
			e.printStackTrace();
		}

	}

	private void selectClients() {
		String firstName;
		String lastName;
		String login;
		String password;
		Client client;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM CLIENTS");
			while (result.next()) {
				firstName = result.getString("firstName");
				lastName = result.getString("lastName");
				login = result.getString("login");
				password = result.getString("password");
				client = new Client.Builder().firstName(firstName).lastName(lastName).login(login).password(password)
						.build();
				clientsList.add(client);
				dataContainer.registerClient(client);
			}
		} catch (SQLException e) {
			System.err.println("blad przy pobieraniu kilenta");
			e.printStackTrace();
		}
	}

	private void selectStations() {
		String name;
		Station station;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM STATIONS");
			while (result.next()) {
				name = result.getString("name");
				station = new Station(name);
				stationsList.add(station);
				dataContainer.registerStation(station);
			}
		} catch (SQLException e) {
			System.err.println("blad przy pobieraniu stacji");
			e.printStackTrace();
		}
	}

	private void selectRoutes() {
		Route route;
		int startingStationId;
		int targetStationId;
		Station startingStation;
		Station targetStation;
		double distanceInKm;
		String startingHour;
		String endingHour;
		ArrayList<Station> midStationList;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM ROUTES");
			while (result.next()) {
				startingStationId = result.getInt("startingStation_id") - 1;
				targetStationId = result.getInt("targetStation_id") - 1;
				distanceInKm = result.getDouble("distanceInKm");
				startingHour = result.getString("startingHour");
				endingHour = result.getString("endingHour");
				midStationList = decodeMidStationIds(result.getString("midStationsIds"));
				startingStation = stationsList.get(startingStationId);
				targetStation = stationsList.get(targetStationId);
				route = new Route(startingStation, targetStation, distanceInKm, startingHour, endingHour);
				route.getMidStationList().addAll(midStationList);
				routesList.add(route);
				dataContainer.registerRoute(route);
			}
		} catch (SQLException e) {
			System.err.println("blad przy pobieraniu trasy");
			e.printStackTrace();
		}
	}

	private void selectTickets() {
		Ticket ticket;
		int routeId;
		int clientId;
		Route route;
		Client client;
		BigDecimal price;
		TicketNumber ticketNumber;
		String type;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM TICKETS");
			while (result.next()) {
				routeId = result.getInt("route_id") - 1;
				clientId = result.getInt("client_id") - 1;
				price = result.getBigDecimal("price");
				ticketNumber = new TicketNumber(result.getString("ticketNumber"));
				type = result.getString("type");
				route = routesList.get(routeId);
				client = clientsList.get(clientId);
				ticket = new NormalTicket(route, client, price, ticketNumber, type);
				dataContainer.registerTicket(ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String generateMidStationsIds(Route route) {
		String midStationsIds = "";
		ArrayList<Station> midStationsList = route.getMidStationList();
		for (Station station : midStationsList) {
			int index = stationsList.indexOf(station) + 1;
			midStationsIds += index + ",";
		}
		return midStationsIds;
	}

	private ArrayList<Station> decodeMidStationIds(String midStationIds) {
		ArrayList<Station> midStations = new ArrayList<>();
		if (!midStationIds.equals("")) {
			String[] ids = midStationIds.split(",");
			for (String id : ids) {
				int index = Integer.parseInt(id) - 1;
				Station station = stationsList.get(index);
				midStations.add(station);
			}
		}
		return midStations;
	}

	private Class getJDBCDriverClass() {
		Class jdbcClass = null;
		try {
			jdbcClass = Class.forName(DataBaseConnector.DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Brak sterownika JDBC!");
			e.printStackTrace();
		}
		return jdbcClass;
	}

	private Connection connectToDataBase() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL);
		} catch (SQLException e) {
			System.err.println("Problem z otwarciem polaczenia");
			e.printStackTrace();
		}
		return connection;
	}

	private Statement createStatement() {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.err.println("Problem ze stworzeniem statement");
			e.printStackTrace();
		}
		return statement;
	}

}
