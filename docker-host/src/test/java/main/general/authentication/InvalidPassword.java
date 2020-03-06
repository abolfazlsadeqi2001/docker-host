package main.general.authentication;

public class InvalidPassword {
	private String password;
	private String message;
	
	public InvalidPassword(String password, String message) {
		super();
		this.password = password;
		this.message = message;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getMessage() {
		return message;
	}
}
