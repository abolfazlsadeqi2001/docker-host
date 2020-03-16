package main.general.authentication;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.general.authentication.models.Age;
import main.general.authentication.models.Family;
import main.general.authentication.models.Name;
import main.general.authentication.models.Password;
import main.general.authentication.models.Phone;
import main.general.authentication.models.User;
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
	// name valid and invalid
	Name[] invalidNames = new Name[] {
			new Name("ab","so short")
	};
	Name[] validNames = new Name[] {
		new Name("abolfazl","correct")	
	};
	// family valid and invalid
	Family[] invalidFamilies = new Family[] {
		new Family("ab","short family")
	};
	Family[] validFamilies = new Family[] {
		new Family("sadeqi","correct")	
	};
	// age valid and invalid
	Age[] invalidAges = new Age[] {
		new Age(6,"too young")
	};
	Age[] validAges = new Age[] {
			new Age(18,"correct")
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
		user.setUserId(1);
		user.setTelephone(validPhones[0].getNumber());
		user.setPassword(validPasswords[0].getPassword());
		user.setName(validNames[0].getName());
		user.setFamily(validFamilies[0].getFamily());
		user.setAge(validAges[0].getAge());
		Authenticator.attachToCache(user);
		
		User gottenUser = AuthenticatorFront.login(user.getTelephone(), user.getPassword());
		if(!user.equals(gottenUser)) {
			throw new Exception("doesn't catch the user correctly");
		}
	}
	
	@Test
	public void testLoginToGetAnUserWithoutAnyCache() throws Exception {
		String insertTemplete = "INSERT INTO users(telephone,password,name,family,age) values('%s','%s','%s','%s','%d')";
		String query = String.format(insertTemplete, validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		MysqlConnector.set(query);
		
		User user = AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		if(user == null) {
			throw new Exception("a null reference thrown");
		}
		
		if(!user.getFamily().equals(validFamilies[0].getFamily())) {
			throw new Exception("family must not be empty");
		}
		
		if(!user.getName().equals(validNames[0].getName())) {
			throw new Exception("name must not be empty");
		}
		
		if(user.getAge() != validAges[0].getAge()) {
			throw new Exception("age must not be zero");
		}
		
		if(!user.getTelephone().equals( validPhones[0].getNumber())) {
			throw new Exception("telephone must not be empty");
		}
		
		if(!user.getPassword().equals(validPasswords[0].getPassword())) {
			throw new Exception("password must not be empty");
		}
	}
	
	@Test
	public void testLoginToAttachNewUserToCache() throws Exception {
		User user = new User();
		user.setAge(validAges[0].getAge());
		user.setName(validNames[0].getName());
		user.setFamily(validFamilies[0].getFamily());
		user.setTelephone(validPhones[0].getNumber());
		user.setPassword(validPasswords[0].getPassword());
		
		String insertTemplete = "INSERT INTO users(telephone,password,name,family,age) values('%s','%s','%s','%s','%d')";
		String query = String.format(insertTemplete, user.getTelephone(), user.getPassword(), user.getName(), user.getFamily(), user.getAge());
		MysqlConnector.set(query);
		
		User gottenUser = AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		if(Authenticator.users.size() != 1) {
			fail("users cahce set doesn't equals 1");
		}
		if(!Authenticator.users.iterator().next().getUser().equalsByTelephoneAndPassword(user)) {
			fail("the first cached user element doesn't has match telephone and password");
		}
		
		if(!gottenUser.equalsByTelephoneAndPassword(user)) {
			fail("the gotten doesn't has match telephone and password");
		}
	}
	
	@Test
	public void testDontAddToCahceOnWrongLogin() throws Exception {
		try {
			AuthenticatorFront.login(validPhones[0].getNumber(), validPasswords[0].getPassword());
		} catch (Exception e) {
		} finally {
			if(Authenticator.users.size() != 0)
				throw new Exception("unknow user on database added to cache by login!");
		}
	}
	
	@Test
	public void testDontAddToCahceOnInvalidPropertiesLogin() throws Exception {
		try {
			AuthenticatorFront.login(invalidPhones[0].getNumber(), validPasswords[0].getPassword());
		} catch (Exception e) {
		} finally {
			if(Authenticator.users.size() != 0)
				throw new Exception("unknow user on database added to cache by login!");
		}
	}
	
	@Test
	public void testAddingUserOnCorrectRegister() throws Exception {
		User user = new User();
		user.setTelephone(validPhones[0].getNumber());
		user.setPassword(validPasswords[0].getPassword());
		user.setName(validNames[0].getName());
		user.setFamily(validFamilies[0].getFamily());
		user.setAge(validAges[0].getAge());
		user.setMoney(0);
		
		AuthenticatorFront.register(user.getTelephone(), user.getPassword(),user.getName(),user.getFamily(),user.getAge());
		User gotUser = Authenticator.getUserByTelephoneAndPasswordFromCache(user);
		user.setUserId(gotUser.getUserId());// Id generated into database so it is essential to set user id for equals method
		if(!gotUser.equals(user)) {
			throw new Exception("the gotten user wasn't equals.what the hell was that man");
		}
	}
	
	@Test
	public void testRegisterTelephoneValidation() throws Exception {
		// invalid
		boolean isHasPhoneErrorMessage = false;
		try {
			AuthenticatorFront.register(invalidPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
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
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		} catch(Exception e) {
			fail(e.getMessage());
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
			AuthenticatorFront.register(validPhones[0].getNumber(), invalidPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
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
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		}catch(Exception e) {
			if(e.getMessage().contains("password")) {
				throw new Exception("with valid password show a password error");
			}
		}
	}
	
	@Test
	public void testRegisterNameValidation() throws Exception {
		// invalid
		boolean isHasNameErrorMessage = false;
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),invalidNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		} catch(Exception e) {
			if(e.getMessage().contains("name")) {
				isHasNameErrorMessage = true;
			}
		}
		
		if(!isHasNameErrorMessage ) {
			throw new Exception("doesn't make name error");
		}
		// valid
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		}catch(Exception e) {
			if(e.getMessage().contains("name")) {
				throw new Exception("with valid name show a name error");
			}
		}
	}
	
	@Test
	public void testRegisterFamilyValidation() throws Exception {
		// invalid
		boolean isHasFamilyErrorMessage = false;
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),invalidFamilies[0].getFamily(),validAges[0].getAge());
		} catch(Exception e) {
			if(e.getMessage().contains("family")) {
				isHasFamilyErrorMessage = true;
			}
		}
		
		if(!isHasFamilyErrorMessage ) {
			throw new Exception("doesn't make family error");
		}
		// valid
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		}catch(Exception e) {
			if(e.getMessage().contains("family")) {
				throw new Exception("with valid famlily show a family error");
			}
		}
	}
	
	@Test
	public void testRegisterAgeValidation() throws Exception {
		// invalid
		boolean isHasAgeErrorMessage = false;
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),invalidAges[0].getAge());
		} catch(Exception e) {
			if(e.getMessage().contains("age")) {
				isHasAgeErrorMessage = true;
			}
		}
		
		if(!isHasAgeErrorMessage ) {
			throw new Exception("doesn't make age error");
		}
		// valid
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(), validPasswords[0].getPassword(),validNames[0].getName(),validFamilies[0].getFamily(),validAges[0].getAge());
		}catch(Exception e) {
			if(e.getMessage().contains("age")) {
				throw new Exception("with valid age show a age error");
			}
		}
	}
	
	@Test
	public void testDontAddToCahceOnWrongRegister() throws Exception {
		try {
			AuthenticatorFront.register(validPhones[0].getNumber(),
					validPasswords[0].getPassword(),
					validNames[0].getName(),
					validFamilies[0].getFamily(),
					validAges[0].getAge());
		} catch (Exception e) {
		} finally {
			if(Authenticator.users.size() != 1)
				throw new Exception("unknow user on database doesn't added to cache by login!");
		}
	}
	
	@Test
	public void testDontAddToCahceOnInvalidPropertiesRegister() throws Exception {
		try {
			AuthenticatorFront.register(invalidPhones[0].getNumber(),
					validPasswords[0].getPassword(),
					validNames[0].getName(),
					validFamilies[0].getFamily(),
					validAges[0].getAge());
		} catch (Exception e) {
		} finally {
			if(Authenticator.users.size() != 0)
				throw new Exception("unknow user on database added to cache by login!");
		}
	}
	
}
