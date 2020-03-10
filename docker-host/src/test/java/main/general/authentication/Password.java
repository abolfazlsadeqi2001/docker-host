package main.general.authentication;

public class Password {
	private String password;
	private String message;
	
	public Password(String password, String message) {
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
