package main.general.authentication.models;

import main.general.authentication.Authenticator;

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
	
	// TODO write test that the cache memory and database has a correct value
	// placed it into Authenticator
	public void addMoney(int cost) throws Exception {
		money += cost;
		try {
			// set money on cache memory
			User user = this;
			Authenticator.getUserByTelephoneAndPasswordFromCache(user).setMoney(money);
			// set money on database
			Authenticator.updateMoney(user);
		} catch (Exception e) {
			throw new Exception("unknown user!I can't add money because of "+e.getMessage());
		}
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

	public int getUserId() {
		return user_id;
	}

	public void setUserId(int id) {
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
		
		if(user.getUserId() != getUserId())
			return false;
		if(user.getAge() != getAge())
			return false;
		if(!user.getFamily().equals(getFamily()))
			return false;
		if(!user.getName().equals(getName()))
			return false;

		return equalsByTelephoneAndPassword(user);
	}
	
	@Override
	public String toString() {
		String body = "";
		
		body += "exception:"+getExceptionMessage()+"|";
		body += "id:"+getUserId()+"|";
		body += "telephone:"+getTelephone()+"|";
		body += "password:"+getPassword()+"|";
		body += "name:"+getName()+"|";
		body += "family:"+getFamily()+"|";
		body += "age:"+getAge();
		
		return body;
	}
}
