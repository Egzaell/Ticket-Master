package client.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import client.client.Client;
import client.dataContainer.DataContainer;
import client.route.Route;
import client.station.Station;
import client.ticket.ChildToFourYearsDiscount;
import client.ticket.HandicapDiscount;
import client.ticket.NormalTicket;
import client.ticket.SeniorDiscount;
import client.ticket.StudentDiscount;
import client.ticket.Ticket;
import client.ticketNumber.TicketNumber;
import client.ticketPrinter.TicketPrinter;
import client.user.User;

public class GUI {

	public static final String[] DISCOUNT_VALUES = new String[] { "dziecko do lat 4", "osoba niepelnosprawna",
			"uczen/student", "rencista" };
	public static DefaultListModel<String> discountModel = new DefaultListModel<>();
	public DefaultListModel<Station> startingStationModel = new DefaultListModel<>();
	public DefaultListModel<Station> targetStationModel = new DefaultListModel<>();
	public DefaultListModel<Route> possibleRoutesModel = new DefaultListModel<>();
	public DefaultListModel<Ticket> myTicketsModel = new DefaultListModel<>();
	private User user;
	private DataContainer dataContainer;
	private JFrame mainFrame;
	private JPanel loginPanel;
	private JTabbedPane tabbedPane;
	private JLabel loginPanelLoginLabel;
	private JTextField loginPanelLoginTextField;
	private JLabel loginPanelPasswordLabel;
	private JTextField loginPanelPasswordTextField;
	private JButton loginPanelLoginButton;
	private JButton loginPanelExitButton;
	private JPanel registerPanel;
	private JLabel registerPanelFirstNameLabel;
	private JTextField registerPanelFirstNameTextField;
	private JLabel registerPanelLastNameLabel;
	private JTextField registerPanelLastNameTextField;
	private JLabel registerPanelLoginLabel;
	private JTextField registerPanelLoginTextField;
	private JLabel registerPanelPasswordLabel;
	private JTextField registerPanelPasswordTextField;
	private JButton registerPanelRegisterButton;
	private JTabbedPane clientTabbedPane;
	private JPanel ticketReservationPanel;
	private JPanel stationChoicePanel;
	private JPanel startingStationPanel;
	private JLabel startingStationPanelLabel;
	private JList startingStationPanelStationList;
	private JScrollPane startingStationPanelScrollPane;
	private JPanel targetStationPanel;
	private JLabel targetStationPanelLabel;
	private JList targetStationPanelStationList;
	private JScrollPane targetStationPanelSrcollPane;
	private JPanel routeChoicePanel;
	private JLabel routeChoicePanelLabel;
	private JList routeChoicePanelList;
	private JScrollPane routeChoicePanelScrollPane;
	private JButton ticketReservationPanelGenerateTicketButton;
	private JPanel ticketOptionPanel;
	private JLabel ticketOptionPanelLabel;
	private JList ticketOptionPanelList;
	private JScrollPane ticketOptionPanelScrollPane;
	private JButton ticketReservationPanelSearchButton;
	private JPanel myTicketsPanel;
	private JList myTicketsPanelList;
	private JScrollPane myTicketsPanelScrollPane;
	private JButton myTicketsPanelPrintTicketButton;
	private JButton myTicketsPanelExitButton;
	private JLabel ticketReservationPanelTicketLabel;
	private JButton ticketReservationPanelSaveTicketButton;
	private JTabbedPane adminTabbedPane;
	private JPanel addingRoutesPanel;
	private JPanel addingStationPanel;
	private JPanel createStationPanel;
	private JTextField createStationPanelStationTextField;
	private JButton createStationPanelCreateStationButton;
	private JList addingStationPanelStationList;
	private JScrollPane addingStationPanelStationScrollPane;
	private JLabel addingStationPanelLabel;
	private JPanel routeCreationPanel;
	private JPanel routeDetailsPanel;
	private JPanel startingHourPanel;
	private JLabel startingHourLabel;
	private JTextField startingHourTextField;
	private JPanel endingHourPanel;
	private JLabel endingHourPanelLabel;
	private JTextField endingHourPanelTextField;
	private JButton addingRoutePanelCreateRouteButton;
	private JPanel ticketCheckPanel;
	private JLabel ticketCheckPanelLabel;
	private JTextField ticketCheckPanelTextField;
	private JButton ticketCheckPanelCheckButton;
	private JLabel ticketCheckPanelResultLabel;
	private JPanel ticketCheckPanelFunctionPanel;
	private JButton addingRoutesPanelExitButton;
	private ArrayList<Route> possibleRoutesList;
	private Ticket ticket;
	private JLabel distanceLabel;
	private JTextField distanceTextField;

	public GUI(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
		init();
	}

	public User getUser() {
		return user;
	}

	private void init() {
		loadDiscountValues();
		mainFrame = new JFrame("TicketMaser");
		mainFrame.setSize(460, 320);
		mainFrame.setLayout(new BorderLayout());
		tabbedPane = createFirstPanel();
		mainFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);
		mainFrame.setVisible(true);
	}

	private void loadDiscountValues() {
		for (String value : DISCOUNT_VALUES) {
			discountModel.addElement(value);
		}
	}

	private JTabbedPane createFirstPanel() {
		tabbedPane = new JTabbedPane();
		loginPanel = createLoginPanel();
		registerPanel = createRegisterPanel();
		tabbedPane.addTab(loginPanel.getName(), loginPanel);
		tabbedPane.addTab(registerPanel.getName(), registerPanel);
		return tabbedPane;
	}

	private JPanel createLoginPanel() {
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setName("Logowanie");
		loginPanelLoginLabel = new JLabel("login:");
		loginPanelLoginTextField = new JTextField("");
		loginPanelPasswordLabel = new JLabel("haslo:");
		loginPanelPasswordTextField = new JTextField("");
		loginPanelLoginButton = new JButton("zaloguj");
		loginPanelLoginButton.addActionListener(new LoginListener());
		loginPanelExitButton = new JButton("wyjscie");
		loginPanelExitButton.addActionListener(new ExitListener());
		loginPanel.add(loginPanelLoginLabel);
		loginPanel.add(loginPanelLoginTextField);
		loginPanel.add(loginPanelPasswordLabel);
		loginPanel.add(loginPanelPasswordTextField);
		loginPanel.add(loginPanelLoginButton);
		loginPanel.add(loginPanelExitButton);
		return loginPanel;
	}

	private JPanel createRegisterPanel() {
		registerPanel = new JPanel();
		registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
		registerPanel.setName("Rejestracja");
		registerPanelFirstNameLabel = new JLabel("imie:");
		registerPanelFirstNameTextField = new JTextField("");
		registerPanelLastNameLabel = new JLabel("nazwisko:");
		registerPanelLastNameTextField = new JTextField("");
		registerPanelLoginLabel = new JLabel("login:");
		registerPanelLoginTextField = new JTextField("");
		registerPanelPasswordLabel = new JLabel("haslo:");
		registerPanelPasswordTextField = new JTextField();
		registerPanelRegisterButton = new JButton("zarejestruj");
		registerPanelRegisterButton.addActionListener(new RegisterListener());
		registerPanel.add(registerPanelFirstNameLabel);
		registerPanel.add(registerPanelFirstNameTextField);
		registerPanel.add(registerPanelLastNameLabel);
		registerPanel.add(registerPanelLastNameTextField);
		registerPanel.add(registerPanelLoginLabel);
		registerPanel.add(registerPanelLoginTextField);
		registerPanel.add(registerPanelPasswordLabel);
		registerPanel.add(registerPanelPasswordTextField);
		registerPanel.add(registerPanelRegisterButton);
		return registerPanel;
	}

	private JTabbedPane createAdminTabbedPane() {
		adminTabbedPane = new JTabbedPane();
		addingRoutesPanel = createAddingRoutesPanel();
		ticketCheckPanel = createTicketCheckPanel();
		adminTabbedPane.addTab(addingRoutesPanel.getName(), addingRoutesPanel);
		adminTabbedPane.addTab(ticketCheckPanel.getName(), ticketCheckPanel);
		return adminTabbedPane;
	}

	private JPanel createTicketCheckPanel() {
		ticketCheckPanel = new JPanel();
		ticketCheckPanel.setLayout(new BorderLayout());
		ticketCheckPanel.setName("Sprawdzanie biletu");
		ticketCheckPanelFunctionPanel = new JPanel();
		ticketCheckPanelFunctionPanel.setLayout(new BoxLayout(ticketCheckPanelFunctionPanel, BoxLayout.X_AXIS));
		ticketCheckPanelLabel = new JLabel("podaj numer biletu: ");
		ticketCheckPanelTextField = new JTextField("");
		ticketCheckPanelCheckButton = new JButton("sprawdz");
		ticketCheckPanelCheckButton.addActionListener(new TicketCheckListener());
		ticketCheckPanelResultLabel = new JLabel("");
		ticketCheckPanelFunctionPanel.add(ticketCheckPanelLabel);
		ticketCheckPanelFunctionPanel.add(ticketCheckPanelTextField);
		ticketCheckPanelFunctionPanel.add(ticketCheckPanelCheckButton);
		ticketCheckPanel.add(BorderLayout.NORTH, ticketCheckPanelFunctionPanel);
		ticketCheckPanel.add(BorderLayout.CENTER, ticketCheckPanelResultLabel);
		return ticketCheckPanel;
	}

	private JPanel createAddingRoutesPanel() {
		addingRoutesPanel = new JPanel();
		addingRoutesPanel.setName("Dodawanie tras");
		addingRoutesPanel.setLayout(new BoxLayout(addingRoutesPanel, BoxLayout.Y_AXIS));
		addingStationPanel = createAddingStationPanel();
		routeCreationPanel = createRouteCreationPanel();
		addingRoutePanelCreateRouteButton = new JButton("stworz trase");
		addingRoutePanelCreateRouteButton.addActionListener(new CreateRouteListener());
		addingRoutesPanelExitButton = new JButton("zakoncz program");
		addingRoutesPanelExitButton.addActionListener(new ExitListener());
		addingRoutesPanel.add(addingStationPanel);
		addingRoutesPanel.add(routeCreationPanel);
		addingRoutesPanel.add(addingRoutePanelCreateRouteButton);
		addingRoutesPanel.add(addingRoutesPanelExitButton);
		return addingRoutesPanel;
	}

	private JPanel createAddingStationPanel() {
		addingStationPanel = new JPanel();
		addingStationPanel.setLayout(new BoxLayout(addingStationPanel, BoxLayout.Y_AXIS));
		addingStationPanelLabel = new JLabel("aby dodac stacje wpisz jej nazwe i kliknij przycisk");
		createStationPanel = new JPanel();
		createStationPanel.setLayout(new BoxLayout(createStationPanel, BoxLayout.X_AXIS));
		createStationPanelStationTextField = new JTextField("");
		createStationPanelCreateStationButton = new JButton("stworz stacje");
		createStationPanelCreateStationButton.addActionListener(new CreateStationListener());
		createStationPanel.add(createStationPanelStationTextField);
		createStationPanel.add(createStationPanelCreateStationButton);
		addingStationPanelStationList = new JList(startingStationModel);
		addingStationPanelStationScrollPane = new JScrollPane(addingStationPanelStationList);
		addingStationPanel.add(createStationPanel);
		addingStationPanel.add(addingStationPanelStationScrollPane);
		return addingStationPanel;
	}

	private JPanel createRouteCreationPanel() {
		routeCreationPanel = new JPanel();
		routeCreationPanel.setLayout(new BoxLayout(routeCreationPanel, BoxLayout.Y_AXIS));
		stationChoicePanel = createStationChoicePanel();
		routeDetailsPanel = createRouteDetailsPanel();
		routeCreationPanel.add(stationChoicePanel);
		routeCreationPanel.add(routeDetailsPanel);
		return routeCreationPanel;
	}

	private JPanel createRouteDetailsPanel() {
		routeDetailsPanel = new JPanel();
		routeDetailsPanel.setLayout(new BoxLayout(routeDetailsPanel, BoxLayout.Y_AXIS));
		startingHourPanel = new JPanel();
		startingHourPanel.setLayout(new BoxLayout(startingHourPanel, BoxLayout.X_AXIS));
		startingHourLabel = new JLabel("godzina wyjazdu: (hh:mm)");
		startingHourTextField = new JTextField("");
		startingHourPanel.add(startingHourLabel);
		startingHourPanel.add(startingHourTextField);
		endingHourPanel = new JPanel();
		endingHourPanel.setLayout(new BoxLayout(endingHourPanel, BoxLayout.X_AXIS));
		endingHourPanelLabel = new JLabel("godzina przyjazdu: (hh:mm)");
		endingHourPanelTextField = new JTextField("");
		distanceLabel = new JLabel("dystans w kilometrach (00.00)");
		distanceTextField = new JTextField("");
		endingHourPanel.add(endingHourPanelLabel);
		endingHourPanel.add(endingHourPanelTextField);
		routeDetailsPanel.add(startingHourPanel);
		routeDetailsPanel.add(endingHourPanel);
		routeDetailsPanel.add(distanceLabel);
		routeDetailsPanel.add(distanceTextField);
		return routeDetailsPanel;
	}

	private JTabbedPane createClientTabbedPane() {
		clientTabbedPane = new JTabbedPane();
		ticketReservationPanel = createTicketReservationPanel();
		myTicketsPanel = createMyTicketsPanel();
		clientTabbedPane.addTab(ticketReservationPanel.getName(), ticketReservationPanel);
		clientTabbedPane.addTab(myTicketsPanel.getName(), myTicketsPanel);
		return clientTabbedPane;
	}

	private JPanel createTicketReservationPanel() {
		ticketReservationPanel = new JPanel();
		ticketReservationPanel.setName("Rezerwacja biletow");
		ticketReservationPanel.setLayout(new BoxLayout(ticketReservationPanel, BoxLayout.Y_AXIS));
		stationChoicePanel = createStationChoicePanel();
		ticketReservationPanelSearchButton = new JButton("wyszukaj dostepne trasy");
		ticketReservationPanelSearchButton.addActionListener(new SearchRouteListener());
		routeChoicePanel = createRouteChoicePanel();
		ticketReservationPanelGenerateTicketButton = new JButton("wygeneruj bilet");
		ticketReservationPanelGenerateTicketButton.addActionListener(new GenerateTicketListener());
		ticketOptionPanel = createTicketOptionPanel();
		ticketReservationPanelTicketLabel = new JLabel("");
		ticketReservationPanelSaveTicketButton = new JButton("zatwierdz bilet");
		ticketReservationPanelSaveTicketButton.addActionListener(new SaveTicketListener());
		ticketReservationPanel.add(stationChoicePanel);
		ticketReservationPanel.add(ticketReservationPanelSearchButton);
		ticketReservationPanel.add(routeChoicePanel);
		ticketReservationPanel.add(ticketOptionPanel);
		ticketReservationPanel.add(ticketReservationPanelGenerateTicketButton);
		ticketReservationPanel.add(ticketReservationPanelTicketLabel);
		ticketReservationPanel.add(ticketReservationPanelSaveTicketButton);
		return ticketReservationPanel;
	}

	private JPanel createMyTicketsPanel() {
		myTicketsPanel = new JPanel();
		myTicketsPanel.setName("Moje bilety");
		myTicketsPanel.setLayout(new BorderLayout());
		myTicketsPanelList = new JList(myTicketsModel);
		myTicketsPanelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myTicketsPanelScrollPane = new JScrollPane(myTicketsPanelList);
		myTicketsPanelPrintTicketButton = new JButton("drukuj bilet");
		myTicketsPanelPrintTicketButton.addActionListener(new PrintTicketListener());
		myTicketsPanelExitButton = new JButton("zakoncz program");
		myTicketsPanelExitButton.addActionListener(new ExitListener());
		myTicketsPanel.add(BorderLayout.CENTER, myTicketsPanelScrollPane);
		myTicketsPanel.add(BorderLayout.EAST, myTicketsPanelPrintTicketButton);
		myTicketsPanel.add(BorderLayout.SOUTH, myTicketsPanelExitButton);
		return myTicketsPanel;
	}

	private JPanel createStationChoicePanel() {
		stationChoicePanel = new JPanel();
		stationChoicePanel.setLayout(new GridLayout(1, 2));
		startingStationPanel = new JPanel();
		startingStationPanel.setLayout(new BoxLayout(startingStationPanel, BoxLayout.Y_AXIS));
		startingStationPanelLabel = new JLabel("wybierz stacje poczatkowa");
		startingStationPanelStationList = new JList(startingStationModel);
		startingStationPanelStationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		startingStationPanelScrollPane = new JScrollPane(startingStationPanelStationList);
		startingStationPanel.add(startingStationPanelLabel);
		startingStationPanel.add(startingStationPanelScrollPane);
		targetStationPanel = new JPanel();
		targetStationPanel.setLayout(new BoxLayout(targetStationPanel, BoxLayout.Y_AXIS));
		targetStationPanelLabel = new JLabel("wybierz stacje docelowa");
		targetStationPanelStationList = new JList(targetStationModel);
		targetStationPanelStationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		targetStationPanelSrcollPane = new JScrollPane(targetStationPanelStationList);
		targetStationPanel.add(targetStationPanelLabel);
		targetStationPanel.add(targetStationPanelSrcollPane);
		stationChoicePanel.add(startingStationPanel);
		stationChoicePanel.add(targetStationPanel);
		return stationChoicePanel;
	}

	private JPanel createRouteChoicePanel() {
		routeChoicePanel = new JPanel();
		routeChoicePanel.setLayout(new BoxLayout(routeChoicePanel, BoxLayout.Y_AXIS));
		routeChoicePanelLabel = new JLabel("dostepne trasy:");
		routeChoicePanelList = new JList(possibleRoutesModel);
		routeChoicePanelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		routeChoicePanelScrollPane = new JScrollPane(routeChoicePanelList);
		routeChoicePanel.add(routeChoicePanelLabel);
		routeChoicePanel.add(routeChoicePanelScrollPane);
		return routeChoicePanel;
	}

	private JPanel createTicketOptionPanel() {
		ticketOptionPanel = new JPanel();
		ticketOptionPanel.setLayout(new BoxLayout(ticketOptionPanel, BoxLayout.Y_AXIS));
		ticketOptionPanelLabel = new JLabel(
				"wybierz przyslugujaca znizke (w razie braku znizki zostawic niezaznaczone)");
		ticketOptionPanelList = new JList(discountModel);
		ticketOptionPanelScrollPane = new JScrollPane(ticketOptionPanelList);
		ticketOptionPanel.add(ticketOptionPanelLabel);
		ticketOptionPanel.add(ticketOptionPanelScrollPane);
		return ticketOptionPanel;
	}

	private void changeView(JTabbedPane tabbedPane) {
		mainFrame.remove(this.tabbedPane);
		mainFrame.setSize(1048, 640);
		mainFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);
		mainFrame.setVisible(true);
	}

	private class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			String login = loginPanelLoginTextField.getText();
			String password = loginPanelPasswordTextField.getText();
			boolean isLoginComplete = dataContainer.loginClient(login, password);
			if (isLoginComplete) {
				user = User.getInstance(login);
				if (login.equals(dataContainer.ADMIN_LOGIN)) {
					changeView(createAdminTabbedPane());
				} else {
					changeView(createClientTabbedPane());
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "Zly login lub haslo.", "BLAD",
						JOptionPane.WARNING_MESSAGE);
			}
			dataContainer.unlock();
		}
	}

	private class RegisterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String firstName = registerPanelFirstNameTextField.getText();
			String lastName = registerPanelLastNameTextField.getText();
			String login = registerPanelLoginTextField.getText();
			String password = registerPanelPasswordTextField.getText();
			Client client = new Client.Builder().firstName(firstName).lastName(lastName).login(login).password(password)
					.build();
			dataContainer.lock();
			boolean isRegistrationSuccesfull = dataContainer.registerClient(client);
			dataContainer.unlock();
			if (isRegistrationSuccesfull) {
				JOptionPane.showMessageDialog(new JFrame(),
					    "Zarejestrowano nowego uzytkownika",
					    "SUCCES!",
					    JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
					    "Uzytkownik o podanym loginie juz istnieje.",
					    "BLAD",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private class SearchRouteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			possibleRoutesModel.clear();
			dataContainer.lock();
			int startingStationIndex = startingStationPanelStationList.getSelectedIndex();
			int targetStationIndex = targetStationPanelStationList.getSelectedIndex();
			Station startingStation = dataContainer.getStationByIndex(startingStationIndex);
			Station targetStation = dataContainer.getStationByIndex(targetStationIndex);
			possibleRoutesList = dataContainer.generateRoute(startingStation, targetStation);
			for (Route route: possibleRoutesList) {
				int index = possibleRoutesList.indexOf(route);
				possibleRoutesModel.add(index, route);
			}
			dataContainer.unlock();
		}

	}

	private class GenerateTicketListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			int index = 0; 
			index = routeChoicePanelList.getSelectedIndex();
			Route route = possibleRoutesList.get(index);
			Client client = dataContainer.getClientByLogin(user.getLogin());
			ticket = new NormalTicket(route, client, dataContainer);
			if (!ticketOptionPanelList.isSelectionEmpty()){
				int discountIndex = ticketOptionPanelList.getSelectedIndex();
				String discountType = DISCOUNT_VALUES[discountIndex];
				ticket = makeDiscount(ticket, discountType);
			}
			ticketReservationPanelTicketLabel.setText(ticket.toString());
			dataContainer.unlock();
		}
		
		private Ticket makeDiscount(Ticket ticket, String discountType) {
			Ticket ticketWithDiscount = null;
			if (discountType.equals(DISCOUNT_VALUES[0])){
				ticketWithDiscount = new ChildToFourYearsDiscount(ticket);
			} else if (discountType.equals(DISCOUNT_VALUES[1])) {
				ticketWithDiscount = new HandicapDiscount(ticket);
			} else if (discountType.equals(DISCOUNT_VALUES[2])) {
				ticketWithDiscount = new StudentDiscount(ticket);
			} else if (discountType.equals(DISCOUNT_VALUES[3])) {
				ticketWithDiscount = new SeniorDiscount(ticket);
			}
			return ticketWithDiscount;
		}
	}

	private class SaveTicketListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			dataContainer.registerTicket(ticket);
			dataContainer.unlock();
		}

	}

	private class PrintTicketListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = myTicketsPanelList.getSelectedIndex();
			Ticket ticket = myTicketsModel.getElementAt(index);
			TicketPrinter ticketPrinter = new TicketPrinter(ticket);
			ticketPrinter.printTicket();
		}

	}

	private class CreateStationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();;
			String name = createStationPanelStationTextField.getText();
			Station station = new Station(name);
			boolean isOperationSuccessfull = dataContainer.registerStation(station);
			if (!isOperationSuccessfull) {
				JOptionPane.showMessageDialog(new JFrame(),
					    "Stacja o podanej nazwie juz istnieje.",
					    "BLAD",
					    JOptionPane.WARNING_MESSAGE);
			}
			dataContainer.unlock();
		}

	}

	private class CreateRouteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			int startingStationIndex = startingStationPanelStationList.getSelectedIndex();
			int targetStationIndex = targetStationPanelStationList.getSelectedIndex();
			Station startingStation = dataContainer.getStationByIndex(startingStationIndex);
			Station targetStation = dataContainer.getStationByIndex(targetStationIndex);
			double distanceInKm = Double.parseDouble(distanceTextField.getText());
			String startingHour = startingHourTextField.getText();
			String endingHour = endingHourPanelTextField.getText();
			Route route = new Route(startingStation, targetStation, distanceInKm, startingHour, endingHour);
			boolean isOperationSuccesfull = dataContainer.registerRoute(route);
			dataContainer.unlock();
			if (!isOperationSuccesfull) {
				JOptionPane.showMessageDialog(new JFrame(),
					    "Taka trasa juz istnieje.",
					    "BLAD",
					    JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	private class TicketCheckListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			String testNumber = ticketCheckPanelTextField.getText();
			TicketNumber ticketNumber = new TicketNumber(testNumber);
			boolean isTicketOnList = dataContainer.isSpecificTicketOnList(ticketNumber);
			if (isTicketOnList) {
				Ticket ticket = dataContainer.getTicketByTicketNumber(ticketNumber);
				ticketCheckPanelResultLabel.setText(ticket.toString());
			} else {
				ticketCheckPanelResultLabel.setText("Bilet o podanym numerze nie istnieje");
			}
			dataContainer.unlock();
		}
	}

	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lock();
			dataContainer.isProgramWorking = false;
			dataContainer.unlock();
			System.exit(1);
		}
	}
}
