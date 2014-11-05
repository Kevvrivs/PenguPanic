package model;

import java.util.ArrayList;

public class Group {
	private int groupID;
	private ArrayList<Account> accounts;
	

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(Account a){
		accounts.add(a);
	}
	
	public void removeAccount(Account a){
		accounts.remove(a);
	}

}
