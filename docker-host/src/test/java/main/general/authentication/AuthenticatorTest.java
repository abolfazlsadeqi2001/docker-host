package main.general.authentication;

import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.general.authentication.Authenticator;
import main.general.authentication.User;
import main.general.database.mysql.MysqlConnector;

public class AuthenticatorTest {
	// phone section
	private final static String[] TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES = new String[] { "1111", "2222" };
	private final InvalidPhone[] invalidPhones = new InvalidPhone[] {
			new InvalidPhone("051-57388377", "allowed a home nubmer with -"),
			new InvalidPhone("02152782121", "validated a home nubmer without -"),
			new InvalidPhone("0930039295", "validated a short phone number"),
			new InvalidPhone("093003925687", "validated a long phone number"),
			new InvalidPhone("19397536145", "validated a phone number start with 1"),
			new InvalidPhone("02397543487", "validated a phone nubmer start wit 02"),
			new InvalidPhone("nothing", "allowed the default value of controller") };
	private final String validatedPhone = "09397531212";
	// password section
	private final InvalidPassword[] invalidPasswords = new InvalidPassword[] {
			new InvalidPassword("1434123412", "allowed only numberic"),
			new InvalidPassword("ababdxogdas", "allowed only ahphabetical"),
			new InvalidPassword("ababd12", "allowed short length password"),
			new InvalidPassword("abaabd1234456", "allowed large length password"),
			new InvalidPassword("nothing", "allowed the default value of controller") };
	private final String validatedPassword = "ababdxoro13";
	private final String validatedPassword1 = "ababdxoro1";

	/**
	 * each telephone number saved in database as test must removed before
	 */
	@BeforeEach
	public void before() {
		try {
			for (String currentPhone : TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES) {
				String deleteQueryTemplate = "DELETE FROM users WHERE tel like %s";
				String deleteQuery = String.format(deleteQueryTemplate, currentPhone);
				MysqlConnector.set(deleteQuery);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * delete all recorded datas from database
	 */
	@AfterEach
	public void after() {
		try {
			for (String currentPhone : TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES) {
				String deleteQueryTemplate = "DELETE FROM users WHERE tel like %s";
				String deleteQuery = String.format(deleteQueryTemplate, currentPhone);
				MysqlConnector.set(deleteQuery);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * this mehtod just used some validate and invalidate passwords to checkup in
	 * validatePassword method
	 * 
	 * @throws Exception
	 */
	@Test
	public void testValidatePassword() throws Exception {
		String errorMessage = "";
		// invalid passwords test
		for (InvalidPassword invalidPassword : invalidPasswords) {
			try {
				Authenticator.validatePassword(invalidPassword.getPassword());
				errorMessage = invalidPassword.getMessage();
				break;
			} catch (Exception e) {
			}
		}

		// validated password test
		try {
			Authenticator.validatePassword(validatedPassword);
		} catch (Exception e) {
			errorMessage = "doesn't allow a valid password!";
		}

		// check the boolean value
		if (!errorMessage.equals(""))
			throw new Exception(errorMessage);
	}

	/**
	 * this method just used some validate and invalidate phone numbers to check up in
	 * validateTelephone method
	 * 
	 * @throws Exception
	 */
	@Test
	public void testValidateTelephone() throws Exception {
		String errorMessage = "";
		// invalids
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
			Authenticator.validateTelephone(validatedPhone);
		} catch (Exception e) {
			errorMessage = "doesn't allow a validated phone number";
		}

		// check the boolean value
		if (!errorMessage.equals("")) {
			throw new Exception(errorMessage);
		}
	}

	@Test
	public void testIsExistsTelephoneNumber() throws Exception {
		// create a new entry
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0]+"','"+validatedPassword+"')";
		MysqlConnector.set(insertQuery);

		User user = new User();
		user.setPassword(validatedPassword);
		user.setTelephone(TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0]);

		boolean isExistsTel1 = Authenticator.isExistsTelephoneNumber(user);

		if (!isExistsTel1) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}
		// delte new instance
		String deleteQuery = "DELETE FROM users WHERE tel like '"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0]+"' and pass like '"+validatedPassword+"'";
		MysqlConnector.set(deleteQuery);

		boolean isExistsTel2 = Authenticator.isExistsTelephoneNumber(user);

		if (isExistsTel2) {
			throw new Exception("deleted phone nubmer exists!");
		}
	}

	/**
	 * test it
	 * @throws Exception
	 */
	@Test
	public void testGetTelephoneNumberAndPassword() throws Exception {
		// insert new entry
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]+"','"+validatedPassword+"')";
		MysqlConnector.set(insertQuery);

		User user = new User();
		user.setPassword(validatedPassword);
		user.setTelephone(TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]);

		try {
			Authenticator.getByTelephoneAndPassword(user);
		} catch (Exception e) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}

		// update new entry (pass)
		String updateQuery1 = "UPDATE users SET pass='"+validatedPassword1+"' WHERE tel like '"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]+"'and pass like '"+validatedPassword+"'";
		MysqlConnector.set(updateQuery1);

		// don't update password from user we want use our previous datas
		boolean isFail = false;
		try {
			Authenticator.getByTelephoneAndPassword(user);
		} catch (Exception e) {
			isFail = true;
		}

		if (!isFail) {
			throw new Exception("updated password exists!");
		}
		// update new entry (nubmer)
		String updateQuery2 = "UPDATE users SET tel='"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0]+"' WHERE tel like '"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]+"'and pass like '"+validatedPassword1+"'";
		MysqlConnector.set(updateQuery2);

		// don't update tel from user we want use our previous datas but set passwrod to
		// ensure
		user.setPassword(validatedPassword1);
		isFail = false;
		try {
			Authenticator.getByTelephoneAndPassword(user);
		} catch (Exception e) {
			isFail = true;
		}

		if (!isFail) {
			throw new Exception("updated number exists!");
		}
	}

	/**
	 * test the insert method just by add an user by that the pull it from database
	 * @throws Exception
	 */
	@Test
	public void testInsertNewUser() throws Exception {
		// make an user then insert it to database
		User user = new User();
		user.setTelephone(TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]);
		user.setPassword(validatedPassword);

		Authenticator.insertNewUser(user);
		// get added user from database
		String query = "SELECT * FROM users WHERE tel like '"+TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]+"' and pass like '"+validatedPassword+"'";
		ResultSet set = MysqlConnector.get(query);
		if (!set.next()) {
			throw new Exception("the user wasn't added");
		}
	}

}
