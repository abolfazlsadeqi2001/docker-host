package main.general.authentication;

import static org.assertj.core.api.Assertions.fail;

import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.general.authentication.models.User;
import main.general.database.mysql.MysqlConnector;

public class UserTest {
	int[] ids = new int[] { 1, 2, };

	String[] phones = new String[] { "1111", "2222" };
	String[] passwords = new String[] { "ab12", "ab11" };
	String[] names = new String[] { "ab", "12" };
	String[] families = new String[] { "adf", "qers" };
	int[] ages = new int[] { 12, 48 };

	int[] moneies = new int[] { 4000, 2000 };

	@BeforeEach
	public void beforeEach() throws Exception {
		Authenticator.users.clear();
		String deleteQueryTemplete = "DELETE FROM users WHERE telephone='%s'";
		for (String phone : phones) {
			MysqlConnector.set(String.format(deleteQueryTemplete, phone));
		}
	}
	
	@AfterEach
	public void afterEach() throws Exception {
		beforeEach();
	}
	
	/**
	 * check both equals and equals by telephone and password methods
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEqualsForId() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[0]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[0]);
		user2.setPassword(passwords[0]);
		user2.setName(names[0]);
		user2.setFamily(families[0]);
		user2.setAge(ages[0]);
		user2.setMoney(moneies[0]);
		user2.setId(2);

		if (!user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("same users passwrod and telephone don't equal!");

		if (user1.equals(user2))
			throw new Exception("different ids equal!");

		user1.setId(2);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}

	@Test
	public void testEqualsForName() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[1]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[0]);
		user2.setPassword(passwords[0]);
		user2.setName(names[0]);
		user2.setFamily(families[0]);
		user2.setAge(ages[0]);
		user2.setMoney(moneies[0]);
		user2.setId(1);

		if (!user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("same users passwrod and telephone don't equal!");

		if (user1.equals(user2))
			throw new Exception("different names equal!");

		user1.setName(names[0]);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}

	@Test
	public void testEqualsForFamily() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[0]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[0]);
		user2.setPassword(passwords[0]);
		user2.setName(names[0]);
		user2.setFamily(families[1]);
		user2.setAge(ages[0]);
		user2.setMoney(moneies[0]);
		user2.setId(1);

		if (!user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("same users passwrod and telephone don't equal!");

		if (user1.equals(user2))
			throw new Exception("different family equal!");

		user1.setFamily(families[1]);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}

	@Test
	public void testEqualsForAge() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[0]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[0]);
		user2.setPassword(passwords[0]);
		user2.setName(names[0]);
		user2.setFamily(families[0]);
		user2.setAge(ages[1]);
		user2.setMoney(moneies[0]);
		user2.setId(1);

		if (!user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("same users passwrod and telephone don't equal!");

		if (user1.equals(user2))
			throw new Exception("different ages equal!");

		user1.setAge(ages[1]);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}

	@Test
	public void testEqualsForMoney() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[0]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[0]);
		user2.setPassword(passwords[0]);
		user2.setName(names[0]);
		user2.setFamily(families[0]);
		user2.setAge(ages[0]);
		user2.setMoney(moneies[1]);
		user2.setId(1);

		if (!user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("same users passwrod and telephone don't equal!");

		if (user1.equals(user2))
			throw new Exception("different moneies equal!");

		user1.setMoney(moneies[1]);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}

	@Test
	public void testEqualsForPassword() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[0]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[0]);
		user2.setPassword(passwords[1]);
		user2.setName(names[0]);
		user2.setFamily(families[0]);
		user2.setAge(ages[0]);
		user2.setMoney(moneies[0]);
		user2.setId(1);

		if (user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("difference users passwrod equal!");

		if (user1.equals(user2))
			throw new Exception("different passwords equal!");

		user1.setPassword(passwords[1]);
		if (!user1.equals(user2))
			throw new Exception("same users properties don't equal");
	}

	@Test
	public void testEqualsForTelephone() throws Exception {
		User user1 = new User();
		user1.setTelephone(phones[0]);
		user1.setPassword(passwords[0]);
		user1.setName(names[0]);
		user1.setFamily(families[0]);
		user1.setAge(ages[0]);
		user1.setMoney(moneies[0]);
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone(phones[1]);
		user2.setPassword(passwords[0]);
		user2.setName(names[0]);
		user2.setFamily(families[0]);
		user2.setAge(ages[0]);
		user2.setMoney(moneies[0]);
		user2.setId(1);

		if (user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("difference users telephones equal!");

		if (user1.equals(user2))
			throw new Exception("different telephones equal!");

		user1.setTelephone(phones[1]);
		if (!user1.equals(user2))
			throw new Exception("same users properties don't equal");
	}

	/**
	 * some of user fields must not equals to null like strings
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUserFields() throws Exception {
		User user = new User();

		if (user.getTelephone() == null) {
			throw new Exception("telephone is null");
		}

		if (user.getPassword() == null) {
			throw new Exception("password is null");
		}

		if (user.getExceptionMessage() == null) {
			throw new Exception("exception is null");
		}

		if (user.getName() == null) {
			throw new Exception("exception is null");
		}

		if (user.getFamily() == null) {
			throw new Exception("exception is null");
		}

	}

	// TODO implements for positive values
	// TODO implements for minus
	/**
	 * test add money for negative value<br>
	 * also test to be same in database and cache
	 */
	@Test
	public void testAddMoneyForNegativeValue() throws Exception {
		// define user section
		User user = new User();
		user.setTelephone(phones[0]);
		user.setPassword(passwords[0]);
		user.setName(names[0]);
		user.setFamily(families[0]);
		user.setAge(ages[0]);
		user.setMoney(moneies[0]);
		// insert to database section
		String insertQueryTemplete = "INSERT INTO users(telephone,password,name,family,age,money) VALUES('%s','%s','%s','%s','%d','%d')";
		String insertQuery = String.format(insertQueryTemplete, user.getTelephone(),user.getPassword(),user.getName(),user.getFamily(),user.getAge(),user.getMoney());
		MysqlConnector.set(insertQuery);
		// attach to cache memory
		Authenticator.attachToCache(user);
		// add a negative value test section
		int moneyToAdd = 4000;
		int finalResultOfMoney = user.getMoney() + moneyToAdd;
		user.addMoney(-moneyToAdd);
		// check into cache memory
		boolean cacheHasASameValueWithFinalResult = Authenticator.getUserByTelephoneAndPasswordFromCache(user).getMoney() == finalResultOfMoney;
		if(!cacheHasASameValueWithFinalResult)
			fail("doesn't have a same value for final result and cache value");
		// check into database
		String selectQueryTemplete = "SELECT money FROM users WHERE telephone='%s'";
		String selectQuery = String.format(selectQueryTemplete, phones[0]);
		ResultSet set = MysqlConnector.get(selectQuery);
		set.next();
		boolean DataBaseHasASameValueWithFinalResult = set.getInt("money") == finalResultOfMoney;
		if(!DataBaseHasASameValueWithFinalResult)
			throw new Exception("doesn't have a same value by database and final result");
	}
}
