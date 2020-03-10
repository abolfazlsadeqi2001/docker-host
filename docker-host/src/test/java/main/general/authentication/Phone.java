package main.general.authentication;

public class Phone {
	private String number;
	private String message;
	
	public Phone(String phone, String message) {
		super();
		this.number = phone;
		this.message = message;
	}

	public String getNumber() {
		return number;
	}

	public String getMessage() {
		return message;
	}
}
