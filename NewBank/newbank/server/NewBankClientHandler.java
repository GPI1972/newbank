package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {
			// ask for user name
			out.println("Enter Username");
			String userName = in.readLine();
			// ask for password
			out.println("Enter Password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them 
			if(customer != null) {
				out.println(printMenu(customer));
				while(true) {
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
					String responce = bank.processRequest(customer, request);
					out.println(responce);
				}
			}
			else {
				out.println("Log In Failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public String printMenu(CustomerID customer) {
		String menu = " " + "_".repeat(83);
		menu +=   "\n|                                                                                   |"
				+ "\n| Welcome to NewBank, " + customer.getKey() + "!" + " ".repeat(85 - 24 - customer.getKey().length()) + "|";
		menu +=   "\n|                                                                                   |"
				+ "\n| What would you like to do?                                                        |"
				+ "\n| Please insert the commands as instructed below:                                   |"
				+ "\n|                                                                                   |"
				+ "\n| SHOWMYACCOUNTS                                                                    |"
				+ "\n| NEWACCOUNT <Name>                                                                 |"
				+ "\n| NEWPASSWORD <Name>                                                                |"
				+ "\n| MOVE <From> <To> <Amount>                                                         |"
				+ "\n| PAY <Person/Company> <Ammount> FROM <Account>                                     |"
				+ "\n| DEPOSIT <Account> <Amount>                                                        |"
				+ "\n| WITHRDRAW <Account> <Amount>                                                      |"
				+ "\n| CLOSEACCOUNT <Name>                                                               |"
				+ "\n| DIRECTDEBIT <Account> <Person/Company> <MonthlyDebitAmount> <AmountOfMonths>      |"
				+ "\n|                                                                                   |"
				+ "\n| If you need more information on how to use the commands, please type INFO         |"
				+ "\n|" + "_".repeat(83) + "|\n";
		
    	return menu;
	}
	

}
