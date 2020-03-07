package authentication;

public class ClientInfo {
	private String telephone;
	private String password;
	
	public ClientInfo(String telephone,String password) {
		setTelephone(telephone);
		setPassword(password);
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
}
