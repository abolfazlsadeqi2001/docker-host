package authentication.models;

public class Family {
	private String family;
	private String message;
	
	public Family(String family, String message) {
		super();
		this.family = family;
		this.message = message;
	}
	
	public Family(String family) {
		super();
		this.family = family;
		this.message = family + " is a correct value";
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
