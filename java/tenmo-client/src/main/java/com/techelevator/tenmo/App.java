package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private RestTemplate restTemplate;
    private AccountService accountService;
    private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new AccountService(API_BASE_URL), new TransferService(API_BASE_URL), new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(AccountService accountService, TransferService transferService, ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
		this.restTemplate = new RestTemplate();
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		BigDecimal currentBalance = accountService.getBalance(currentUser.getToken());
		System.out.println("Your current balance is: $" + currentBalance);

	}


	private void viewTransferHistory() {
		// TODO Auto-generated method stub

		//PROMPT USER TO VIEW ALL HISTORY OR DETAILED TRANSFER
		Integer userSelection = console.getUserInputInteger("SELECT 1 to see all transfer history or SELECT 2 " +
				"to view specific transfer details");
		if(userSelection == 1){
			//ALL HISTORY
			System.out.println("\n-------------------------------------------\n" +
								"Transfer \n" +
								"ID          From        To         Amount\n" +
								"-------------------------------------------");
			Transfer[] transferHistoryList = transferService.transferHistory(currentUser.getToken());
			for (Transfer transfers : transferHistoryList){
				System.out.println(transfers.getTransferId() + "        " + transfers.getAccountFrom() +"        "+
						transfers.getAccountTo() + "       $" + transfers.getTransferAmount());
			}
		} else {
			//DETAILED TRANSFER
			Integer userInput = console.getUserInputInteger("Please enter the Transfer Id for the transfer details" +
					" you would like to view");
			Transfer transfer = new Transfer();
			transfer = transferService.getDetailsByTransferId(currentUser.getToken(), userInput.longValue());
			System.out.println("\n--------------------------------------------\n" +
								"Transfer Details\n" +
								"--------------------------------------------");
			System.out.println(transfer.toString());
		}
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		BigDecimal currentUserAccount = accountService.getBalance(currentUser.getToken());
		//LIST OF USERS
		System.out.println("\n-------------------------------------------\n" +
				"User ID          Name\n" +
				"-------------------------------------------");
		User[] listOfUsers = transferService.listOfUsers(currentUser.getToken());
		for (User user : listOfUsers){
			System.out.println(user.getId()+"             " + user.getUsername());
		}
		System.out.println("-------------------------------------------\n");

		//PROMPT FOR USER ID
			Integer userId = console.getUserInputInteger("Enter ID of user you are sending money to");
		//PROMPT FOR AMOUNT TO TRANSFER
				Integer transferAmount = console.getUserInputInteger("Enter amount to transfer");
		//CHECK FOR SUFFICIENT FUNDS
				if(currentUserAccount.compareTo(BigDecimal.valueOf(transferAmount)) >= 0){
					Transfer transfer = new Transfer(userId, currentUser.getUser().getId(), BigDecimal.valueOf(transferAmount));
					transferService.createTransfer(transfer, currentUser.getToken());
					System.out.println("\n\n**** Transfer Successful ****");
				} else {
					System.out.println("\n\n**** Insufficient funds. Transfer cannot be completed ****");
				}
			}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
