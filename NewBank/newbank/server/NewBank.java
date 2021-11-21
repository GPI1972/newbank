package newbank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private String amountToPay;
	private double amount;
	private Account openingBalance;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		bhagy.addAccount(new Account("Savings", 200.0));
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
		// split input into individual strings
                Scanner scanner = new Scanner(request);
                scanner.useDelimiter(" ");
                ArrayList<String> commandLine = new ArrayList<>();
                while (scanner.hasNext()){
                    commandLine.add(scanner.next());
                }
                try{
                    if(customers.containsKey(customer.getKey())) {
                            switch(commandLine.get(0)) {
                            case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
                            case "NEWACCOUNT Main" : return newAccountMain(customer);
                            case "NEWACCOUNT Savings" : return newAccountSavings(customer);
                            case "PAY" : return payment(customer, openingBalance, commandLine.get(1), commandLine.get(2));
                            case "WITHDRAW" : return withdraw(customer, commandLine.get(1), commandLine.get(2));
                            case "CLOSEACCOUNT" : return closeAccount(customer, commandLine.get(1));
                            // case "MOVE" : return move(customer, commandLine.get(1));
                            // case "DEPOSIT" : return deposit(customer, commandLine.get(1), commandLine.get(2));
                            }
                    }
                }
                catch(Exception e){
                    return "Incorrect command or missing parameters. Please try again.";
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
	
	/*
	 * 
	// move method that moves amount from one account to another
	// *need to specify which account to move money from/to
	// *need to work out how to access one particular account of a customer
	private String move(CustomerID customer, String amountstr) {
		double amount = Double.parseDouble(amountstr);
		ArrayList<Account> accountsList = new ArrayList<>();
		accountsList = (customers.get(customer.getKey())).getAccounts();
		(customers.get(customer.getKey())).moveMoney(accountsList[0], accountsList[1], amount);
		return "SUCCESS";
	}
	
	// deposit method that deposits money in one account
	// *need to specify which account to deposit money to
	// *need to work out how to access one particular account of a customer
	private String deposit(CustomerID customer, String accountstr, String amountstr) {
		double amount = Double.parseDouble(amountstr);
		ArrayList<Account> accountsList = new ArrayList<>();
		accountsList = (customers.get(customer.getKey())).getAccounts();
		accountList[0].addMoney(amount);
		return "SUCCESS";	}
	*
	*/
	
	/* Method to pay custom person custom amount of money (i.e. "PAY Ruby 100").
	 * Currently goes into infinite loop, more troubleshooting required to make it work. 
	 */
	private String payment(CustomerID customer, Account openingBalance, String name, String amountToPay) {
		amount = Double.parseDouble(amountToPay);
		(customers.get(customer.getKey())).makePayment(amount,openingBalance);
		return "SUCCESS";	
	}
        
        /*
          Method to withdraw an amount of money from the customer's account
        */
        private String withdraw(CustomerID customer, String account, String amount) {
                return customers.get(customer.getKey()).withdrawMoney(account, amount);
	}
        
        /* Method to close an account */
        private String closeAccount(CustomerID customer, String account) {
        	return customers.get(customer.getKey()).accountEmpty(account);
      
        }
}
