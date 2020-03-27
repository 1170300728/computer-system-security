package bank.util;

public class Todo {
	private long id;
	private long User_id;
	private String name;
	private int money;
	private int statue;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser_id() {
		return User_id;
	}
	public void setUser_id(long user_id) {
		this.User_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getStatue() {
	  return this.statue;
	}
	public void setStatue(int statue) {
	  this.statue = statue;
	}
	
}
