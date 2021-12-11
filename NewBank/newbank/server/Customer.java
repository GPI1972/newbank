package newbank.server;

import java.util.ArrayList;
import java.util.Calendar;

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
	public String makePayment(String account, double amount) {
		String message = "Account not found";
		for(Account a : accounts) {
            if (account.equalsIgnoreCase(a.getCustomer())){
              if(a.removeMoney(amount)){
            	  message = "Success.";
              }
              else {
            	  message = "Insufficient funds";
              }
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
                // if account found AND funds available then withdraw amount
                for(Account a : accounts) {
                    if (account.equalsIgnoreCase(a.getCustomer())){
                      if(a.removeMoney(withdrawal)){
                        infoMessage = "Successfully withdrawn " + amount + " from account " + a.getCustomer();
                        return infoMessage;
                      }
                      else{
                        infoMessage = "Insufficient funds. Please check account balance.";
                        return infoMessage;
                      }
                    }
		}
                infoMessage = "Account not found.";
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
        
        /*Method to set up direct debit, it checks the account chosen and removes ('reserves') necessary amount 
         * to be paid all together for the number of months selected, i.e 5 moneys over 10 months - 50 moneys minus in the account.
         * Method start direct debit count from next month, i.e. set up in December, starts from January*/
        public String debitSetUp(String account, String name, String amount, String months) {
        	double debitAmount = Double.parseDouble(amount);
        	int monthAmount = Integer.parseInt(months);
    		String message = "";
    		
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.MONTH, monthAmount);
    		int deadline = calendar.get(Calendar.MONTH);
    		String[] month = new String[] {"January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December" };
    	    
    	    String deadlineInText = month[calendar.get(Calendar.MONTH)];
    	    String year = Integer.toString(calendar.get(Calendar.YEAR));
    		
    		for(Account a : accounts) {
                if (account.equalsIgnoreCase(a.getCustomer())){
                  if(a.getBalance()>= debitAmount){
                	  for (int i=0; i<=deadline; i++){
                		  a.removeMoney(debitAmount); 
                	      }
                	  message = "Direct debit set up for " + name + " until " + deadlineInText + " " + year + ".";
                  }
                  else {
                	  message = "Insufficient funds.";
                  }
                }
    		}
        	return message;
        	
        }
}
