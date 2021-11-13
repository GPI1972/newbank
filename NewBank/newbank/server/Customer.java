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
	
}
