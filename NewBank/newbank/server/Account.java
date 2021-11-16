package newbank.server;

public class Account {
	
	private String accountName;
	private double openingBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	// getter function to get balance--maybe we should rename openingBalance to balance?
	public double getBalance(){
		return openingBalance;
	}
	
	// getter function to get account name
	public String getCustomer(){
		return accountName;
	}
	
	// function to add money to an account
	public void addMoney(double amount){
		openingBalance += amount;
	}
	
	//function to remove money from an account
	public void removeMoney(double amount){
		if(openingBalance >= amount) {
			openingBalance -= amount;
		}
	} 
}
