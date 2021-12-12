package newbank.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Customer {
	
	private ArrayList<Account> accounts;
	private String password;
	
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
	
	//function to get Customer's password
	public String getPassword() {
		return password;
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
        public String passwordCreate(String newpassword) {
        	String message = "";
        	if (passwordCheck(newpassword)) {
        		message = "Password successfully updated.";
        	}
        	else 
        		message = "Password update failed. Please ensure password is of at least 6 characters, with at least one uppercase, digit and special symbol.";
        	
        	return message;
        }
        
        /* change password method
         * currently does not alter txt file with updated password--needs debugging 
         * this method would also change any other customer's password if they had the same old password, so it is not secure */
        public String passwordChange(String oldpassword, String newpassword) {
        	String message = "";
        	if (passwordCheck(newpassword)) {
        		// create new file type, using path to password text file
        		File passwordFile = new File("NewBank/newbank/server/nbPassword.txt");
                String oldContent = "";
                BufferedReader reader = null;
                FileWriter writer = null;
                try
                {
                    // create new reader to read current lines from file
                	reader = new BufferedReader(new FileReader(passwordFile));
                    //Reading all the lines of input text file into the oldContent string
                    String line = reader.readLine();
                    while (line != null) 
                    {
                        // append each line from file to oldContent string
                    	oldContent = oldContent + line + System.lineSeparator();
                        line = reader.readLine();
                    }
                    // use replaceAll() method to replace occurrences of the old password with the new password, save in new string
                    String newContent = oldContent.replaceAll(oldpassword, newpassword);
                    //Rewrite the input text file with this new string
                    writer = new FileWriter(passwordFile);
                    writer.write(newContent);
                    message = "Password successfully updated.";
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        reader.close();
                        writer.close();
                    } 
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                }
        	}
        	else {
        		message = "Password change failed. Please ensure new password is of at least 6 characters, with at least one uppercase, digit and special symbol.";
        	}
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
