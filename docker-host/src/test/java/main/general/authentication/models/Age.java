package main.general.authentication.models;

public class Age {
	private int age;
	private String message;
	
	public Age(int age, String message) {
		super();
		this.age = age;
		this.message = message;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
