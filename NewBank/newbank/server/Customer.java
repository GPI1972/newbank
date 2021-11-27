package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	private double balance;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}
	
	public void addAccount(Account account) {
		accounts.add(account);		
	}
	
	//function to get accounts array
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	// function to move money between a customer's accounts
	public boolean moveMoney(Account from, Account to, double amount) {
		if (from.getBalance() >= amount){
			from.removeMoney(amount);
			to.addMoney(amount);
			return true;
		}
		else{
			return false;
		}
	}
	
	/*function to make payment to some person/company
	 * possibly to add code to pay into specific accounts in the future 
	 */
	public void makePayment(double amount, Account openingBalance) {
		balance = openingBalance.getBalance();
		balance -= amount; 		
	}
        
        /*
          Function to withdraw money from a specific account
        */
        public String withdrawMoney(String account, String amount){
                double withdrawal = Double.parseDouble(amount);
                String infoMessage = "";
                
                // iterate through all the customer's accounts
                // if account found AND funds avaialble then withdraw amount
                for(Account a : accounts) {
                    if (account.equalsIgnoreCase(a.getCustomer())){
                      if(a.removeMoney(withdrawal)){
                        infoMessage = "Successfully withdrawn " + amount + " from account " + a.getCustomer();
                      }
                      else{
                        infoMessage = "Insufficient funds. Please check account balance.";
                      }
                    }
                    else {
                      infoMessage = "Account not found.";
                    }
		}
                
                return infoMessage;
        }
        
        
        public String accountEmpty(String account) {
        	String message = "This account does not exist.";
        		for (Account a : accounts) {
        			if (account.equalsIgnoreCase(a.getAccountName())) {
        				if (a.getBalance() == 0.0) {
        					accounts.remove(a);
        					return "Success. The account \"" + a.getAccountName() + "\" was closed.";
        				} else {
        					return "The account balance should be 0. Please withdraw/transfer from the account any existing credits or deposit/transfer to the account any existing debts.";
        				}
        			}
        			}
        			
        		return message;
        		}
        
        /*new password method*/
        public String passwordCreate(String password) {
        	String message = "";
        	if (passwordCheck(password)) {
        		message = "Password successfully updated.";
        	}
        	else 
        		message = "Password update failed. Please ensure password is of at least 6 characters, with at least one uppercase, digit and special symbol.";
        	
        	return message;
        }
        
        /*method to check if the entered password has all necessary requirements*/ 
        public boolean passwordCheck(String password) {
        	boolean acceptable = false;
        	boolean hasLetter = false;
            boolean hasDigit = false;
            boolean hasSymbol = false;
        	String symbols = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
        	char ch;
        	
        	if (password.length() >= 6) {
        		 for (int i = 0; i < password.length(); i++) {
        			  ch = password.charAt(i);
        			 if (Character.isUpperCase(ch)) {
        				 hasLetter = true;
        			 }	 	
        			 else if (Character.isDigit(ch)) {
        				 hasDigit = true;
        			 }	 	
        			 else if (symbols.contains(String.valueOf(ch))){
        				 hasSymbol = true;
        			 }        			
        		 }
        		 if (hasLetter && hasDigit && hasSymbol) {
        			 acceptable = true;
        		 }
        	}
        	else {
        		acceptable = false;
        	}
        	return acceptable;        	
        }
}
