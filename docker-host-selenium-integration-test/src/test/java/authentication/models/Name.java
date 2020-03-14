package authentication.models;

public class Name {
	private String name;
	private String message;
	
	public Name(String name, String message) {
		super();
		this.name = name;
		this.message = message;
	}

	public Name(String name) {
		super();
		this.name = name;
		this.message = name + " is a correct value";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
