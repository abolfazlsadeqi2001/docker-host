package main.general.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.general.database.mysql.MysqlConnector;

/**
 * you have to have the same members for invalid and valid arrays<br>
 * danger this test case might hit your database informations!
 * @author abolfazlsadeqi2001
 */
public class AuthenticatorFrontTest {
	// phones valid and invalid
	Phone[] invalidPhones = new Phone[] {
			new Phone("091","doesn't have enough chars")
	};
	
	Phone[] validPhones = new Phone[] {
			new Phone("09397534790","correct")
	};
	// password valid and invalid
	Password[] invalidPasswords = new Password[] {
		new Password("ababdaqrt","don't have number")
	};
	Password[] validPasswords = new Password[] {
			new Password("ababd1212","correct")
		};
	@BeforeEach
	public void beforeEach() throws Exception {
		// clear cache
		Authenticator.users.clear();
		// clear database
		String deleteTemplate = "DELETE FROM users where telephone='%s'";
		for (Phone phone : invalidPhones) {
			String number = phone.getNumber();
			MysqlConnector.set(String.format(deleteTemplate, number));
		}
		
		for (Phone phone : validPhones) {
			String number = phone.getNumber();
			MysqlConnector.set(String.format(deleteTemplate, number));
		}
	}
	
	@Test
	public void testLoginTelephoneValidation() throws Exception {
		// invalid
		boolean isHasPhoneErrorMessage = false;
		try {
			AuthenticatorFront.login(invalidPhones[0].getNumber(), validPasswords[0].getPassword());
		} catch(Exception e) {
			if(e.getMessage().contains("phone")) {
				isHasPhoneErrorMessage = true;
			}
		}
		
		if(!isHasPhoneErrorMessage) {
			throw new Exception("doesn't make telephone error");
		}
		// valid
		try {
			AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		} catch(Exception e) {
			if(e.getMessage().contains("phone")) {
				throw new Exception("with valid phone show a phone error");
			}
		}
	}
	
	@Test
	public void testLoginPasswordValidation() throws Exception {
		// invalid
		boolean isHasPasswordErrorMessage = false;
		try {
			AuthenticatorFront.login(validPhones[0].getNumber(), invalidPasswords[0].getPassword());
		} catch(Exception e) {
			if(e.getMessage().contains("password")) {
				isHasPasswordErrorMessage = true;
			}
		}
		
		if(!isHasPasswordErrorMessage ) {
			throw new Exception("doesn't make password error");
		}
		// valid
		try {
			AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		}catch(Exception e) {
			if(e.getMessage().contains("password")) {
				throw new Exception("with valid password show a password error");
			}
		}
	}
	
	@Test
	public void testLoginToUseAnExistenceCache() throws Exception {
		User user = new User();
		user.setId(1);
		user.setTelephone(validPhones[0].getNumber());
		user.setPassword(validPasswords[0].getPassword());
		Authenticator.attachToCache(user);
		
		User gottenUser = AuthenticatorFront.login(user.getTelephone(), user.getPassword());
		if(!user.equalsByTelephoneAndPassword(gottenUser)) {
			throw new Exception("doesn't catch the user correctly");
		}
	}
	
	@Test
	public void testLoginToGetAnUserWithoutAnyCache() throws Exception {
		String insertTemplete = "INSERT INTO users(telephone,password) values('%s','%s')";
		String query = String.format(insertTemplete, validPhones[0].getNumber(), validPasswords[0].getPassword());
		MysqlConnector.set(query);
		
		User user = AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		if(user == null) {
			throw new Exception("a null reference thrown");
		}
	}
	
	@Test
	public void testLoginToAttachNewUserToCache() throws Exception {
		String insertTemplete = "INSERT INTO users(telephone,password) values('%s','%s')";
		String query = String.format(insertTemplete, validPhones[0].getNumber(), validPasswords[0].getPassword());
		MysqlConnector.set(query);
		
		AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		boolean result = Authenticator.users.size() != 0;
		if(!result) {
			throw new Exception("size greator than 0");
		}
	}
	
	@Test
	public void testAddingUserOnCorrectRegister() throws Exception {
		User user = new User();
		user.setTelephone(validPhones[0].getNumber());
		user.setPassword(validPasswords[0].getPassword());
		
		AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword());
		User gotUser = Authenticator.getUserByTelephoneAndPasswordFromCache(user);
		if(!gotUser.equalsByTelephoneAndPassword(user)) {
			throw new Exception("the gotten user wasn't equals.what the hell was that man");
		}
	}
	
	@Test
	public void testRegisterTelephoneValidation() throws Exception {
		// invalid
		boolean isHasPhoneErrorMessage = false;
		try {
			AuthenticatorFront.register(invalidPhones[0].getNumber(), validPasswords[0].getPassword());
		} catch(Exception e) {
			if(e.getMessage().contains("phone")) {
				isHasPhoneErrorMessage = true;
			}
		}
		
		if(!isHasPhoneErrorMessage) {
			throw new Exception("doesn't make telephone error");
		}
		// valid
		try { 
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword());
		} catch(Exception e) {
			System.out.println(e.getMessage());
			if(e.getMessage().contains("phone")) {
				throw new Exception("with valid phone show a phone error");
			}
		}
	}
	
	@Test
	public void testRegisterPasswordValidation() throws Exception {
		// invalid
		boolean isHasPasswordErrorMessage = false;
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), invalidPasswords[0].getPassword());
		} catch(Exception e) {
			if(e.getMessage().contains("password")) {
				isHasPasswordErrorMessage = true;
			}
		}
		
		if(!isHasPasswordErrorMessage ) {
			throw new Exception("doesn't make password error");
		}
		// valid
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword());
		}catch(Exception e) {
			if(e.getMessage().contains("password")) {
				throw new Exception("with valid password show a password error");
			}
		}
	}
	
}
