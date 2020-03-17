package main.general.authentication.models;

import main.general.authentication.AuthenticatorFront;

/**
 * each changes in this class must applied into
 * {@link main.general.authentication.AuthenticatorFront#register(String, String)} and
 * {@link main.general.authentication.AuthenticatorFront#login(String, String)} and test case in
 * {@link main.general.authentication.UserTest} also in
 * {@link main.general.authentication.models.User#toString()} and their tests
 * @author abolfazlsadeqi2001
 *
 */
public class User {
	private String telephone;
	private String password;
	private String exceptionMessage;
	private String name;
	private String family;
	private int user_id;
	private int age;
	private int money;
	
	public User() {
		telephone = "";
		password = "";
		exceptionMessage = "";
		family = "";
		name = "";
	}
	
	/**
	 * @param cost to add some money (don't sensitive on negative or positive)
	 * @throws Exception
	 */
	public void addMoney(int cost) throws Exception {
		if(cost >= 0)
			AuthenticatorFront.changeMoney(this, cost);
		else 
			AuthenticatorFront.changeMoney(this, -cost);
	}
	
	/**
	 * @param cost to minus some money (don't sensitive on negative or positive)
	 * @throws Exception
	 */
	public void minusMoney(int cost) throws Exception {
		if(cost < 0)
			AuthenticatorFront.changeMoney(this, cost);
		else 
			AuthenticatorFront.changeMoney(this, -cost);
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public int getId() {
		return user_id;
	}

	public void setId(int id) {
		this.user_id = id;
	}

	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean equalsByTelephoneAndPassword(User user) {
		if(!user.getPassword().equals(getPassword()))
			return false;
		if(!user.getTelephone().equals(getTelephone()))
			return false;
		
		return true;
	}
	
	/**
	 * any field is added must be added here
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof User))
			return false;
		User user = (User) obj;
		
		if(user.getId() != getId())
			return false;
		if(!user.getName().equals(getName()))
			return false;
		if(!user.getFamily().equals(getFamily()))
			return false;
		if(user.getAge() != getAge())
			return false;
		if(user.getMoney() != getMoney())
			return false;
		if(!user.getExceptionMessage().equals(getExceptionMessage()))
			return false;

		return equalsByTelephoneAndPassword(user);
	}
	
	@Override
	public String toString() {
		String body = "";
		
		body += "exception:"+getExceptionMessage()+"|";
		body += "id:"+getId()+"|";
		body += "telephone:"+getTelephone()+"|";
		body += "password:"+getPassword()+"|";
		body += "name:"+getName()+"|";
		body += "family:"+getFamily()+"|";
		body += "age:"+getAge();
		
		return body;
	}
}
