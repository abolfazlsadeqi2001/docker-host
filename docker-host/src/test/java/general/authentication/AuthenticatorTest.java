package general.authentication;

import java.sql.ResultSet;

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
				new InvalidPassword("abaabd1234456", "allowed large length password"),
				new InvalidPassword("nothing", "allowed the default value of controller")
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
			new InvalidPhone("02397543487","validated a phone nubmer start wit 02"),
			new InvalidPhone("nothing", "allowed the default value of controller")
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
		// create a new entry
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('09397534791','adadbxoro12')";
		MysqlConnector.set(insertQuery);
		
		User user = new User();
		user.setPassword("adadbxoro12");
		user.setTelephone("09397534791");
		
		boolean isExistsTel1 = Authenticator.isExistsTelephoneNumber(user);
		
		if(!isExistsTel1) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}
		// delte new instance
		String deleteQuery = "DELETE FROM users WHERE tel like '09397534791'and pass like 'adadbxoro12'";
		MysqlConnector.set(deleteQuery);
		
		boolean isExistsTel2 = Authenticator.isExistsTelephoneNumber(user);
		
		if(isExistsTel2) {
			throw new Exception("deleted phone nubmer exists!");
		}
	}

	@Test
	public void testGetTelephoneNumberAndPassword() throws Exception {
		// insert new entry
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('09397534791','adadbxoro12')";
		MysqlConnector.set(insertQuery);
		
		User user = new User();
		user.setPassword("adadbxoro12");
		user.setTelephone("09397534791");
		
		try {
			Authenticator.getByTelephoneAndPassword(user);
		}catch(Exception e) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}
		
		// update new entry (pass)
		String updateQuery1 = "UPDATE users SET pass='adadbxoro1' WHERE tel like '09397534791'and pass like 'adadbxoro12'";
		MysqlConnector.set(updateQuery1);
		
		// don't update password from user we want use our previous datas
		boolean isFail = false;
		try {
			Authenticator.getByTelephoneAndPassword(user);
		}catch (Exception e) {
			isFail = true;
		}
		
		if(!isFail) {
			throw new Exception("updated password exists!");
		}
		// update new entry (nubmer)
		String updateQuery2 = "UPDATE users SET tel='1111' WHERE tel like '09397534791'and pass like 'adadbxoro1'";
		MysqlConnector.set(updateQuery2);
		
		// don't update tel from user we want use our previous datas but set passwrod to ensure
		user.setPassword("adadbxoro1");
		isFail = false;
		try {
			Authenticator.getByTelephoneAndPassword(user);
		} catch(Exception e) {
			isFail = true;
		}
		
		if(!isFail) {
			throw new Exception("updated number exists!");
		}
		
		// clean inserted datas
		String deleteQuery = "DELETE FROM users WHERE tel like '1111' and pass like 'adadbxoro1'";
		MysqlConnector.set(deleteQuery);
	}
	
	@Test
	public void testInsertNewUser() throws Exception {
		// make an user then insert it to database
		User user = new User();
		user.setTelephone("09397534791");
		user.setPassword("ababdxoro12");
		
		Authenticator.insertNewUser(user);
		// get added user from database
		String query = "SELECT * FROM users WHERE tel like '09397534791' and pass like 'ababdxoro12'";
		ResultSet set = MysqlConnector.get(query);
		if(!set.next()) {
			throw new Exception("the user wasn't added");
		}
		// remove added user
		String deleteQuery = "DELETE FROM users WHERE tel like '09397534791' and pass like 'ababdxoro12'";
		MysqlConnector.set(deleteQuery);
	}
	
}
