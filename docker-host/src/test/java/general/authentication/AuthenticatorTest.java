package general.authentication;

import org.junit.jupiter.api.Test;

import general.database.mysql.MysqlConnector;

public class AuthenticatorTest {

	@Test
	public void testValidatePassword() throws Exception {
		String errorMessage = "";
		// invalid passwords
		InvalidPassword[] invalids = new InvalidPassword[]{
				new InvalidPassword("1434123412", "allowed only numberic"),
				new InvalidPassword("ababdxogdas", "allowed only ahphabetical"),
				new InvalidPassword("ababd12", "allowed short length password"),
				new InvalidPassword("abaabd1234456", "allowed large length password")
		};
		
		for (InvalidPassword invalidPassword : invalids) {
			try {
				Authenticator.validatePassword(invalidPassword.getPassword());
				errorMessage = invalidPassword.getMessage();
				break;
			} catch (Exception e) {
			}
		}
		
		// validated
		{
			try {
				String password = "ababdxoro13";
				Authenticator.validatePassword(password);
			} catch (Exception e) {
				errorMessage = "doesn't allow a valid password!";
			}
		}
		
		// check the boolean value
		if(!errorMessage.equals(""))
			throw new Exception(errorMessage);
	}
	
	@Test
	public void testValidateTelephone() throws Exception {
		String errorMessage = "";
		// invalids
		InvalidPhone[] invalidPhones = new InvalidPhone[] {
			new InvalidPhone("051-57388377", "allowed a home nubmer with -"),
			new InvalidPhone("02152782121", "validated a home nubmer without -"),
			new InvalidPhone("0930039295", "validated a short phone number"),
			new InvalidPhone("093003925687", "validated a long phone number"),
			new InvalidPhone("19397536145", "validated a phone number start with 1"),
			new InvalidPhone("02397543487","validated a phone nubmer start wit 02")
		};
		
		for (InvalidPhone invalidPhone : invalidPhones) {
			try {
				Authenticator.validateTelephone(invalidPhone.getPhone());
				errorMessage = invalidPhone.getMessage();
				break;
			} catch (Exception e) {
			}
		}
		
		// valid
		try {
			String phone = "09397531212";
			Authenticator.validateTelephone(phone);
		} catch (Exception e) {
			errorMessage = "doesn't allow a validated phone number";
		}
		
		// check the boolean value
		if(!errorMessage.equals("")) {
			throw new Exception(errorMessage);
		}
	}
	
	@Test
	public void testIsExistsTelephoneNumber() throws Exception {
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('09397534791','adadbxoro12')";
		MysqlConnector.set(insertQuery);
		
		User user = new User();
		user.setPassword("adadbxoro12");
		user.setTelephone("09397534791");
		
		boolean isExistsTel1 = Authenticator.isExistsTelephoneNumber(user);
		
		if(!isExistsTel1) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}
		
		String deleteQuery = "DELETE FROM users WHERE tel like '09397534791'and pass like 'adadbxoro12'";
		MysqlConnector.set(deleteQuery);
		
		boolean isExistsTel2 = Authenticator.isExistsTelephoneNumber(user);
		
		if(isExistsTel2) {
			throw new Exception("deleted phone nubmer exists!");
		}
	}

	@Test
	public void testIsExistsTelephoneNumberAndPassword() throws Exception {
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('09397534791','adadbxoro12')";
		MysqlConnector.set(insertQuery);
		
		User user = new User();
		user.setPassword("adadbxoro12");
		user.setTelephone("09397534791");
		
		Object result1 = Authenticator.isExistsTelephoneNumerAndPassword(user);
		
		if(result1 == null) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}
		
		String updateQuery = "UPDATE users SET pass='adadbxoro1' WHERE tel like '09397534791'and pass like 'adadbxoro12'";
		MysqlConnector.set(updateQuery);
		
		Object result2 = Authenticator.isExistsTelephoneNumerAndPassword(user);
		
		if(result2 != null) {
			throw new Exception("deleted phone nubmer exists!");
		}
		
		// clean inserted datas
		String deleteQuery = "DELETE FROM users WHERE tel like '09397534791'and pass like 'adadbxoro12'";
		MysqlConnector.set(deleteQuery);
	}
	
}
