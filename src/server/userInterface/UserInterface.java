package server.userInterface;

import java.util.Scanner;

import client.dataContainer.DataContainer;
import server.dataBaseConnector.DataBaseConnector;

public class UserInterface implements Runnable{
	
	private static final String WELCOME_MESSAGE = ".:Program TicketMasterServer:.";
	private static final String MENU = "1. Zapisz dane do bazy, 2. Zakoncz";
	private static final String ERROR_MESSAGE = "!!!BLAD!!!";
	private DataBaseConnector dataBaseConnector;
	private DataContainer dataContainer;
	private Scanner scanner;
	
	public UserInterface(DataContainer dataContainer, DataBaseConnector dataBaseConnector) {
		this.dataContainer = dataContainer;
		this.dataBaseConnector = dataBaseConnector;
		scanner = new Scanner(System.in);
		init();
	}
	
	@Override
	public void run() {
		while (dataContainer.isProgramWorking) {
			printMenu();
			int userChoice = getUserChoice();
			reactToUserChoice(userChoice);
		}
	}
	
	private void init() {
		System.out.println(WELCOME_MESSAGE);
	}
	
	private void printMenu() {
		System.out.println(MENU);
	}
	
	private int getUserChoice() {
		int userChoice = scanner.nextInt();
		return userChoice;
	}
	
	private void reactToUserChoice(int userChoice) {
		if (userChoice == 1) {
			dataBaseConnector.saveDataToDataBase();
		} else if (userChoice == 2) {
			dataContainer.lock();
			dataContainer.isProgramWorking = false;
			dataContainer.unlock();
			System.exit(1);
		} else {
			System.out.println(ERROR_MESSAGE);
		}
	}
}
