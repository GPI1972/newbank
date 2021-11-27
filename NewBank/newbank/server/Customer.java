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
	
	// function to deposit money in a customer's account
	public String depositMoney(String account, String amount){
		double deposit = Double.parseDouble(amount);
		String infoMessage = "";
        
        // iterate through all the customer's accounts
        // if account found then deposit amount
        for(Account a : accounts) {
            if (account.equalsIgnoreCase(a.getCustomer())){
            	a.addMoney(deposit);
            	infoMessage = "Succesfully deposited " + deposit + " to account " + a.getCustomer();
            	return infoMessage;
            }
        }
        return "Account not found.";
	}

	
	// function to move money between a customer's accounts
	public String moveMoney(String from, String to, String amountstr) {
		double amount = Double.parseDouble(amountstr);
        String infoMessage = "";
		for(Account a : accounts) {
			for (Account b: accounts) {
				if (from.equalsIgnoreCase(a.getCustomer())){
	            	if (to.equalsIgnoreCase(b.getCustomer())){
	            		if(a.removeMoney(amount)){
	            			b.addMoney(amount);
	            			infoMessage = "Successfully moved " + amount + " from account " + a.getCustomer() + " to account " + b.getCustomer();
	            			return infoMessage;
	            		}
	            		else {
	            			return "Insufficient funds. Please check account balance.";
	            		}
	            	}
	            }
			}
		}
		return "Account not found.";
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
                // if account found AND funds available then withdraw amount
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
        
}
