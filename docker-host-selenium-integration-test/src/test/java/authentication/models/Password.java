package authentication.models;

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
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
