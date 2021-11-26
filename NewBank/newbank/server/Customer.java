package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString() + '\n';
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
	public String makePayment(String account, String name, String amount) {
		double payment = Double.parseDouble(amount);
		String message = "";
		for(Account a : accounts) {
            if (account.equalsIgnoreCase(a.getCustomer())){
              if(a.removeMoney(payment)){
            	  message = amount + " paid to " + name;
              }
              else {
            	  message = "Insufficient funds";
              }
            }
            else {
            	message = "Account not found";
            }          	  
        }
		 /*if name of payment receiver is the same as existing bank account name,
		  *  the customer receives the payment (i.e. Pay Main John 100 - John gets 100)
		  */
		for(Account a : accounts) {
			if (name == a.getAccountName()) {
               	a.addMoney(payment);
         }
		}
		return message;     	
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
        
}
