package general.authentication;

public class InvalidPhone {
	private String phone;
	private String message;
	
	public InvalidPhone(String phone, String message) {
		super();
		this.phone = phone;
		this.message = message;
	}

	public String getPhone() {
		return phone;
	}

	public String getMessage() {
		return message;
	}
}
