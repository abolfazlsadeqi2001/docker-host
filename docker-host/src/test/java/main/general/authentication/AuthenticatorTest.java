package main.general.authentication;

import static org.assertj.core.api.Assertions.fail;

import java.sql.ResultSet;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.general.authentication.Authenticator;
import main.general.authentication.models.Family;
import main.general.authentication.models.Name;
import main.general.authentication.models.Password;
import main.general.authentication.models.Phone;
import main.general.authentication.models.User;
import main.general.authentication.models.UserCache;
import main.general.database.mysql.MysqlConnector;

public class AuthenticatorTest {
	// name section
	Name[] validatedNames = new Name[] { new Name("abolfazl", "correct") };
	Name[] invalidatedNames = new Name[] { new Name("ab", "too short"), new Name("abolfazlsadeqi", "too long") };
	// name section
	Family[] validatedFamilies = new Family[] { new Family("abolfazl", "correct") };
	Family[] invalidatedFamilies = new Family[] { new Family("ab", "too short"), new Family("abolfazlsadeqi", "too long") };
	// age section
	private final int MINIMUM_REQUIRED_AGE = 7;
	private final int MAXIMUM_REQUIRED_AGE = 71;
	// expire time section
	private final int MINIMUM_MINUTES_FOR_EXPIRE_DATE = 5;
	private final int MAXIMUM_VALIDATE_MINUTES = 59;
	private final int MAXIMUM_VALIDATE_HOUR = 23;
	// phone section
	private final static String[] TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES = new String[] { "1111", "2222" };
	private final Phone[] invalidPhones = new Phone[] { new Phone("051-57388377", "allowed a home nubmer with -"),
			new Phone("02152782121", "validated a home nubmer without -"),
			new Phone("0930039295", "validated a short phone number"),
			new Phone("093003925687", "validated a long phone number"),
			new Phone("19397536145", "validated a phone number start with 1"),
			new Phone("02397543487", "validated a phone nubmer start wit 02"),
			new Phone("nothing", "allowed the default value of controller") };
	private final String validatedPhone = "09397531212";
	// password section
	private final Password[] invalidPasswords = new Password[] { new Password("1434123412", "allowed only numberic"),
			new Password("ababdxogdas", "allowed only ahphabetical"),
			new Password("ababd12", "allowed short length password"),
			new Password("abaabd1234456", "allowed large length password"),
			new Password("nothing", "allowed the default value of controller") };
	private final String validatedPassword = "ababdxoro13";
	private final String validatedPassword1 = "ababdxoro1";

	/**
	 * each telephone number saved in database as test must removed before<br>
	 * users set in Authenticator must clear
	 */
	@BeforeEach
	public void before() {
		try {
			// clear cache memory
			Authenticator.users.clear();
			// clear database
			for (String currentPhone : TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES) {
				String deleteQueryTemplate = "DELETE FROM users WHERE telephone like %s";
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
				String deleteQueryTemplate = "DELETE FROM users WHERE telephone like %s";
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
		for (Password invalidPassword : invalidPasswords) {
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
			if(e.getMessage().contains("password"))
				errorMessage = "doesn't allow a valid password!";
			else
				fail("the failure cause message doesn't include password which means that the user will confuse where is the problem");
		}

		// check the boolean value
		if (!errorMessage.equals(""))
			throw new Exception(errorMessage);
	}

	/**
	 * this method just used some validate and invalidate phone numbers to check up
	 * in validateTelephone method
	 * 
	 * @throws Exception
	 */
	@Test
	public void testValidateTelephone() throws Exception {
		String errorMessage = "";
		// invalids
		for (Phone invalidPhone : invalidPhones) {
			try {
				Authenticator.validateTelephone(invalidPhone.getNumber());
				errorMessage = invalidPhone.getMessage();
				break;
			} catch (Exception e) {
			}
		}

		// valid
		try {
			Authenticator.validateTelephone(validatedPhone);
		} catch (Exception e) {
			if (e.getMessage().contains("telephone"))
				errorMessage = "doesn't allow a validated phone number";
			else
				fail("the failure cause message doesn't include telephone which meands that the user will confuse where is the problem");
		}

		// check the boolean value
		if (!errorMessage.equals("")) {
			throw new Exception(errorMessage);
		}
	}

	@Test
	public void testAgeRanges() throws Exception {
		boolean result1 = Authenticator.MAXIMUM_AGE > MAXIMUM_REQUIRED_AGE;
		boolean result2 = Authenticator.MINIMUM_AGE < MINIMUM_REQUIRED_AGE;
		if (result1 || result2)
			throw new Exception("range doesn't meet the standards");
	}

	@Test
	public void testValidateAgeUsing() throws Exception {
		boolean example1DoCatch = false;
		try {
			Authenticator.validateAge(MINIMUM_REQUIRED_AGE - 1);
		} catch (Exception e) {
			if(e.getMessage().contains("age"))
				example1DoCatch = true;
			else
				fail("the failure cause message doesn't include the age which will may confuse the users about what is the problem");
		}

		boolean example2DoCatch = false;
		try {
			Authenticator.validateAge(MINIMUM_REQUIRED_AGE - 1);
		} catch (Exception e) {
			if(e.getMessage().contains("age"))
				example2DoCatch = true;
			else
				fail("the failure message doesn't have age which may confuse the user");
		}

		if (!example1DoCatch || !example2DoCatch) {
			throw new Exception("don't catch a wrong range");
		}
	}

	@Test
	public void testValidateName() throws Exception {
		for (Name nameObj : validatedNames) {
			Authenticator.validateName(nameObj.getName());
		}

		boolean isntThrown = false;
		for (Name nameObj : invalidatedNames) {
			try {
				Authenticator.validateName(nameObj.getName());
				isntThrown = true;
			} catch (Exception e) {
				if(!e.getMessage().contains("name")) {
					fail("thrown message doesn't contain name which may confuse the user when read the message");
				}
			}
		}
		if (isntThrown) {
			throw new Exception("isn't thrown any exception for invalid name");
		}
	}

	@Test
	public void testValidateFamily() throws Exception {
		for (Family familyObj : validatedFamilies) {
			Authenticator.validateName(familyObj.getFamily()+"    ");// test for trim must removed the spaces
		}

		boolean isntThrown = false;
		for (Family nameObj : invalidatedFamilies) {
			try {
				Authenticator.validateFamily(nameObj.getFamily());
				isntThrown = true;
			} catch (Exception e) {
				if(e.getMessage().contains("family"))
					fail("the exception message doesn't contain family it will so confusing for users");
			}
		}
		if (isntThrown) {
			throw new Exception("isn't thrown any exception for invalid family");
		}
	}

	@Test
	public void testIsExistsTelephoneNumber() throws Exception {
		// create a new entry
		String insertQuery = "INSERT INTO users(telephone,password,name,family,age) VALUES('"
				+ TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0] +
				"','" + validatedPassword + "','"+
				validatedNames[0].getName()+ "','" +
				validatedFamilies[0].getFamily()+ "','" +
				MINIMUM_REQUIRED_AGE+"')";
		MysqlConnector.set(insertQuery);

		User user = new User();
		user.setPassword(validatedPassword);
		user.setTelephone(TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0]);
		user.setName(validatedNames[0].getName());
		user.setFamily(validatedFamilies[0].getFamily());
		user.setAge(MAXIMUM_REQUIRED_AGE);

		boolean isExistsTel1 = Authenticator.isExistsTelephoneNumberInDatabase(user);

		if (!isExistsTel1) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}
		// delte new instance
		String deleteQuery = "DELETE FROM users WHERE telephone like '"
				+ TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0] + "' and password like '" + validatedPassword + "'";
		MysqlConnector.set(deleteQuery);

		boolean isExistsTel2 = Authenticator.isExistsTelephoneNumberInDatabase(user);

		if (isExistsTel2) {
			throw new Exception("deleted phone nubmer exists!");
		}
	}

	/**
	 * test it
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTelephoneNumberAndPassword() throws Exception {
		// insert new entry
		String insertQuery = "INSERT INTO users(telephone,password,name,family,age) VALUES('" +
				TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1] + "','" +
				validatedPassword + "','" +
				validatedNames[0].getName() + "','" +
				validatedFamilies[0].getFamily() + "','" +
				MINIMUM_REQUIRED_AGE
				+"')";
		MysqlConnector.set(insertQuery);

		User user = new User();
		user.setPassword(validatedPassword);
		user.setTelephone(TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]);

		try {
			Authenticator.getByTelephoneAndPasswordFromDatabase(user);
		} catch (Exception e) {
			throw new Exception("inserted phone nubmer doesn't exists!");
		}

		// update new entry (pass)
		String updateQuery1 = "UPDATE users SET password='" + validatedPassword1 + "' WHERE telephone like '"
				+ TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1] + "'and password like '" + validatedPassword + "'";
		MysqlConnector.set(updateQuery1);

		// don't update password from user we want use our previous datas
		boolean isFail = false;
		try {
			Authenticator.getByTelephoneAndPasswordFromDatabase(user);
		} catch (Exception e) {
			isFail = true;
		}

		if (!isFail) {
			throw new Exception("updated password exists!");
		}
		// update new entry (nubmer)
		String updateQuery2 = "UPDATE users SET telephone='" + TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[0]
				+ "' WHERE telephone like '" + TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]
				+ "'and password like '" + validatedPassword1 + "'";
		MysqlConnector.set(updateQuery2);

		// don't update telephone from user we want use our previous datas but set
		// passwrod to
		// ensure
		user.setPassword(validatedPassword1);
		isFail = false;
		try {
			Authenticator.getByTelephoneAndPasswordFromDatabase(user);
		} catch (Exception e) {
			isFail = true;
		}

		if (!isFail) {
			throw new Exception("updated number exists!");
		}
	}

	/**
	 * test the insert method just by add an user by that the pull it from database
	 * 
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
		String query = "SELECT * FROM users WHERE telephone like '" + TEST_PHONE_NUBMERS_USED_IN_DATABASE_TEST_CASES[1]
				+ "' and password like '" + validatedPassword + "'";
		ResultSet set = MysqlConnector.get(query);
		if (!set.next()) {
			throw new Exception("the user wasn't added");
		}
	}

	// this method test that the getExpireTime get a time more than expected
	// minimumExpiretime
	@Test
	public void testGetExpireTime() throws Exception {
		// test that the expire time must bigger than expected minimum
		int currentHour = LocalTime.now().getHour();
		int expireMinutes = LocalTime.now().getMinute() + MINIMUM_MINUTES_FOR_EXPIRE_DATE;
		if (expireMinutes > MAXIMUM_VALIDATE_MINUTES) {
			expireMinutes = 0;
			currentHour += 1;
			if (currentHour > MAXIMUM_VALIDATE_HOUR) {
				currentHour = 0;
			}
		}
		LocalTime minimumExpireTime = LocalTime.of(currentHour, expireMinutes);

		LocalTime expireTime = Authenticator.getExpireTime();

		int comparationResult = expireTime.compareTo(minimumExpireTime);

		if (comparationResult < 0) {
			throw new Exception("the expire time is less than " + minimumExpireTime);
		}
		// check up the maximum validate hour and minutes
		if (Authenticator.MAXIMUM_VALIDATE_HOUR != MAXIMUM_VALIDATE_HOUR) {
			throw new Exception("problem with Maximum validate hour");
		}

		if (Authenticator.MAXIMUM_VALIDATE_MINUTES != MAXIMUM_VALIDATE_MINUTES) {
			throw new Exception("problem with Maximum validate minutes");
		}
		// check for make an expire time in 23:59 (it mustn't throw an exception)
		JOptionPane.showMessageDialog(null, "make your system time to 23:59 \n"
				+ "as you must know that the localtime throw an exception when make an hour more than 23\n"
				+ "when our expire minutes more than 59 it must equals the minutes to 0 and ++ the hour\n"
				+ "otherwise it throw an exception and if the time is 23+1 it is more than 23 so throw exception\n"
				+ "after do that click on ok to test");
		Authenticator.getExpireTime();
	}

	@Test
	public void testAttachToCache() throws Exception {
		User user = new User();
		// check the users set size (must be one)
		Authenticator.attachToCache(user);
		int size = Authenticator.users.size();
		if (size != 1) {
			throw new Exception("user doesn't add");
		}
		// check to have a same reference by our user
		User gotUser = Authenticator.getUserByTelephoneAndPasswordFromCache(user);
		if (gotUser != user) {
			throw new Exception("the users reference don't match");
		}
		// check to have a expire time
		int currentHour = LocalTime.now().getHour();
		int expireMinutes = LocalTime.now().getMinute() + MINIMUM_MINUTES_FOR_EXPIRE_DATE;
		if (expireMinutes > MAXIMUM_VALIDATE_MINUTES) {
			expireMinutes = 0;
			currentHour += 1;
			if (currentHour > MAXIMUM_VALIDATE_HOUR) {
				currentHour = 0;
			}
		}
		LocalTime minimumExpireTime = LocalTime.of(currentHour, expireMinutes);

		UserCache cache = Authenticator.users.iterator().next();
		int result = cache.getExpireTime().compareTo(minimumExpireTime);
		if (result < 0) {
			throw new Exception("cache expire time less than minimum expire time");
		}
	}

	@Test
	public void testGetUserByTelephoneAndPasswordFromCache() throws Exception {
		User user1 = new User();
		user1.setUserId(1);
		user1.setPassword("mamad");
		user1.setTelephone("091");
		Authenticator.attachToCache(user1);
		user1.setPassword("alil");

		User user2 = new User();
		user2.setUserId(2);
		user2.setPassword("ali");
		user2.setTelephone("092");
		Authenticator.attachToCache(user2);

		User gotUser1 = Authenticator.getUserByTelephoneAndPasswordFromCache(user1);
		if (!gotUser1.equalsByTelephoneAndPassword(user1)) {
			throw new Exception("user isn't same");
		}

		User gotUser2 = Authenticator.getUserByTelephoneAndPasswordFromCache(user1);
		if (gotUser2.equalsByTelephoneAndPassword(user2)) {
			throw new Exception("different users are same");
		}

		User gotUser3 = Authenticator.getUserByTelephoneAndPasswordFromCache(user2);
		if (!gotUser3.equalsByTelephoneAndPassword(user2)) {
			throw new Exception("user isn't same");
		}
	}

	@Test
	public void testCheckExpireCachesTime() throws Exception {
		User user = new User();

		LocalTime expiredTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute() - 1);

		UserCache cache = new UserCache();
		cache.setUser(user);
		cache.setExpireTime(expiredTime);

		Authenticator.checkExpireCachesTime();

		int size = Authenticator.users.size();
		if (size != 0) {
			throw new Exception("size must be 0");
		}
	}
}
