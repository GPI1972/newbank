package newbank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

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
                    // code based on "Reading Text File into Java HashMap", Dec 2020
                    // Available from: https://www.geeksforgeeks.org/reading-text-file-into-java-hashmap/?ref=rp
                    // Accessed on 04/12/2021
                    try{
                      File passwordFile = new File("NewBank/newbank/server/nbPassword.txt");
                      BufferedReader passwordFileReader = new BufferedReader(new FileReader(passwordFile));
                      String oneLine = null;
                      HashMap<String,String> passwordHash = new HashMap<>();

                      // write username & password to hashmap
                      while ((oneLine = passwordFileReader.readLine()) != null){
                        passwordHash.put(oneLine.split(",")[0], oneLine.split(",")[1]);
                      }
                      passwordFileReader.close();
                      
                      // check if password is correct
                      if(password.equals(passwordHash.get(userName))){
                        return new CustomerID(userName);
                      }
                    }
                    catch(Exception e){
                          e.printStackTrace();
                    }
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
                            case "NEWACCOUNT" : return newAccount(customer, commandLine.get(1));
                            case "PAY" : return payment(customer, commandLine.get(1), commandLine.get(2), commandLine.get(4));
                            case "WITHDRAW" : return withdraw(customer, commandLine.get(1), commandLine.get(2));
                            case "CLOSEACCOUNT" : return closeAccount(customer, commandLine.get(1));
                            case "DEPOSIT" : return deposit(customer, commandLine.get(1), commandLine.get(2));
                            case "MOVE" : return move(customer, commandLine.get(1), commandLine.get(2), commandLine.get(3));
                            case "NEWPASSWORD" : return newPassword(customer, commandLine.get(1));
                            case "INFO": return printMenuInfo();
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
	
	private String newAccount(CustomerID customer, String newAccount) {
		(customers.get(customer.getKey())).addAccount(new Account(newAccount, 0.0));
		return "SUCCESS";
	}
	
	
	// deposit method that deposits money in one account
	private String deposit(CustomerID customer, String account, String amount) {
		return customers.get(customer.getKey()).depositMoney(account, amount);
	}
	
	// move method that moves amount from one account to another
	private String move(CustomerID customer, String from, String to, String amount) {
		return customers.get(customer.getKey()).moveMoney(from, to, amount);
	}
	
	// Method to pay custom person custom amount of money (i.e. "PAY Ruby 100 FROM Main").
	private String payment(CustomerID customer, String name, String amount, String account) {
		double dbAmount = Double.parseDouble(amount);
		String message = "";
		Customer to_customer = customers.get(name);
		if (to_customer == null) {
			return message += "The customer " + name + " could not be found.";
		} else {
			message = (customers.get(customer.getKey())).makePayment(account, dbAmount);
			if (message == "Success.") {
				to_customer.getAccounts().get(0).addMoney(dbAmount);
				return "Succesfully paid " + name + " from " + account;
			} else {
				return message;
				}
		}
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
        
        /*Method to create new password*/
        private String newPassword(CustomerID customer, String password) {
        	return (customers.get(customer.getKey())).passwordCreate(password);        	
        }
        
        
        private String printMenuInfo() {
        	return "\n" + "_".repeat(85) 
    				+ "\n\nSHOWMYACCOUNTS"
    				+ "\nReturns a list of all the customers accounts along with their current balance"
    				+ "\ne.g. Main: 1000.00 "
    				+ "\n\nNEWACCOUNT <Name>"
    				+ "\ne.g. NEWACCOUNT Savings"
    				+ "\n\nNEWPASSWORD <Password>"
    				+ "\nPasswords must contain at least 6 characters, with at least one uppercase, digit and special symbol."
    				+ "\ne.g. NEWPASSWORD P$wrd123"
    				+ "\n\nMOVE <From> <To> <Amount>"
    				+ "\ne.g. MOVE Main Savings 100"
    				+ "\n\nPAY <Person/Company> <Ammount> FROM <Account>"
    				+ "\ne.g. PAY John 100 FROM Main"
    				+ "\n\nDEPOSIT <Account> <Amount>"
    				+ "\ne.g. DEPOSIT Main 100 "
    				+ "\n\nWITHRDRAW <Account> <Amount>"
    				+ "\ne.g. WITHDRAW 100"
    				+ "\n\nCLOSEACCOUNT <Name>"
    				+ "\ne.g CLOSEACCOUNT Savings"
    				+ "\n\nOBS: Use . to separate decimals"
    				+ "\n" + "_".repeat(85) +"\n";
        }
}
