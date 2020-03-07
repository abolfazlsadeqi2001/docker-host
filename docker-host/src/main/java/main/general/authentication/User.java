package main.general.authentication;

public class User {
	private String telephone;
	private String password;
	private int id;
	private String exceptionMessage;

	public User() {
		telephone = "";
		password = "";
		id = 0;
		exceptionMessage = "";
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	 * any fields is added must be added here
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof User))
			return false;
		
		User user = (User) obj;
		if(user.getId() != getId())
			return false;

		return equalsByTelephoneAndPassword(user);
	}
}
