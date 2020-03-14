package authentication.models;

public class Phone {
	private String number;
	private String message;
	
	public Phone(String number, String message) {
		super();
		this.number = number;
		this.message = message;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
