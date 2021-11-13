package newbank.server;

import java.util.ArrayList;
import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);
		
		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		customers.put("Christina", christina);
		
		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			case "NEWACCOUNT Main" : return newAccountMain(customer);
			case "NEWACCOUNT Savings" : return newAccountSavings(customer);
			case "MOVE Main to Savings" : return moveInstructions();
			case "MOVE Savings to Main" : return moveInstructions();
			case "DEPOSIT" : return depositInstructions();
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}
	
	private String newAccountMain(CustomerID customer) {
		(customers.get(customer.getKey())).addAccount(new Account("\n"+"Main", 0.0));
		return "SUCCESS";
	}
	
	private String newAccountSavings(CustomerID customer) {
		(customers.get(customer.getKey())).addAccount(new Account("\n"+"Savings", 0.0));
		return "SUCCESS";
	}
	
	private String moveInstructions() {
		return "Enter the amount you wish to move:";
	}
	
	private String depositInstructions() {
		return "Enter the amount you wish to deposit:";
	}
	
}
