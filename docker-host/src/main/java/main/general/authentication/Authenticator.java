package main.general.authentication;

import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import main.general.authentication.models.User;
import main.general.authentication.models.UserCache;
import main.general.database.mysql.MysqlConnector;

/**
 * this class placed into general package because it will use in both controller
 * and filter sections<br>
 * this class provide the login and register methods and their submethods (like
 * validating password) as well<br>
 * <b>the login logic</b>
 * <ol>
 * <li>validate the password and telephone (if they are invalid it will
 * broke)</li>
 * <li>check on cache memory (implements more performance)</li>
 * <li>check on database</li>
 * </ol>
 * <b>the register logic</b>
 * <ol>
 * <li>validate the password and telephone (if they are invalid it will broke)
 * </li>
 * <li>check on cache memory (implements more performance) (to haven't any
 * match)</li>
 * <li>check on database (to haven't any match then add to both cache and
 * database)</li>
 * </ol>
 * 
 * @author abolfazlsadeqi2001
 * @see {@link main.controllers.authentication.AuthenticationController}
 */
@Component
public class Authenticator {
	// age section
	static final int MINIMUM_AGE = 7;
	static final int MAXIMUM_AGE = 71;
	// cache section
	static Set<UserCache> users = new HashSet<UserCache>();
	static final int MINUTES_TO_REMOVE_FROM_CACHE = 10;
	static final int SECONDS_TO_REMOVE_FROM_CACHE = 0;
	static final int MAXIMUM_VALIDATE_MINUTES = 59;
	static final int MAXIMUM_VALIDATE_HOUR = 23;
	// validating password section
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int PASSWORD_MAX_LENGTH = 12;

	/**
	 * this method validate the password as out criterias like length and has
	 * alphabetical and numberid<br>
	 * <b>If you want to change error message go here they aren't class
	 * variables</b>
	 * 
	 * @param password our password to validate
	 * @return return the validated password (it will trim)
	 * @throws Exception if our password is invalid it will throw an exception which
	 *                   include problem message
	 */
	static String validatePassword(String password) throws Exception {
		String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
		password = password.trim();

		if (password.length() < PASSWORD_MIN_LENGTH) {
			throw new Exception("password length must be logner");
		}

		if (password.length() > PASSWORD_MAX_LENGTH) {
			throw new Exception("password length must shorter");
		}

		if (!Pattern.matches(pattern, password)) {
			throw new Exception("invalid password");
		}

		return password;
	}

	/**
	 * validate the telephone number by our criteria (pattern) if it invalid throw
	 * an exception
	 * 
	 * @param telephone telephone to validate
	 * @return return validated telephone
	 * @throws Exception exception when connect and execute the query
	 */
	static String validateTelephone(String telephone) throws Exception {
		telephone = telephone.trim();
		String pattern = "^09[0-9]{9}$";

		if (!Pattern.matches(pattern, telephone)) {
			throw new Exception("your telephone number doesn't meet the standars");
		}

		return telephone;
	}

	/**
	 * just check the age number out for be between on min and max number
	 * @param age int
	 * @return return the name if meet the standards
	 * @throws Exception
	 */
	static int validateAge(int age) throws Exception {
		if(age < MINIMUM_AGE)
			throw new Exception("you haven't enough age");
		if(age > MAXIMUM_AGE)
			throw new Exception("you have a lot of age");
		return age;
	}
	
	/**
	 * validate the name if it meet the standards return validated name(trimed)
	 * @param name String
	 * @return
	 * @throws Exception
	 */
	static String validateName(String name) throws Exception {
		String pattern = "^[a-zA-Z0-9]{3,8}$";
		name = name.trim();
		if(!Pattern.matches(pattern, name)) {
			throw new Exception("invalid name");
		}
		return name;
	}
	
	/**
	 * validate the family if it meet the standards return validated name(trimed)
	 * @param family String
	 * @return
	 * @throws Exception
	 */
	static String validateFamily(String family) throws Exception {
		String pattern = "^[a-zA-Z0-9]{3,8}$";
		family = family.trim();
		if(!Pattern.matches(pattern, family)) {
			throw new Exception("invalid family");
		}
		return family;
	}
	
	/**
	 * get expire time by
	 * {@value main.general.authentication.Authenticator#MINUTES_TO_REMOVE_FROM_CACHE}
	 * minutes after current time
	 * 
	 * @return expireTime
	 */
	static LocalTime getExpireTime() {
		int currentHour = LocalTime.now().getHour();
		int expireMinutes = LocalTime.now().getMinute() + MINUTES_TO_REMOVE_FROM_CACHE;

		if (expireMinutes > MAXIMUM_VALIDATE_MINUTES) {
			expireMinutes = 0;
			currentHour += 1;
			if (currentHour > MAXIMUM_VALIDATE_HOUR) {
				currentHour = 0;
			}
		}

		LocalTime expireTime = LocalTime.of(currentHour, expireMinutes);

		return expireTime;
	}

	/**
	 * this method add the given user to cache memory by a time that equals ten
	 * minutes after current time
	 * 
	 * @param user a user object that you want to add to your cache
	 */
	static void attachToCache(User user) {
		UserCache cache = new UserCache();
		cache.setExpireTime(getExpireTime());
		cache.setUser(user);

		users.add(cache);
	}

	/**
	 * this method find user just by telephone and password (user param only have to
	 * password and telephone) from cache
	 * 
	 * @param user user to find by password and telephone
	 * @return an user object if exists that user by same telephone and password
	 *         otherwise return null
	 */
	public static User getUserByTelephoneAndPasswordFromCache(User user) {
		for (UserCache userCache : users) {
			User currentUser = userCache.getUser();
			if (currentUser.equalsByTelephoneAndPassword(user)) {
				return currentUser;
			}
		}
		return null;
	}

	/**
	 * this method check all of the attached CacheUsers up to don't expired its
	 * expire time if it does remove it (it means for a specific time that specified
	 * in
	 * {@link main.general.authentication.Authenticator#MINUTES_TO_REMOVE_FROM_CACHE}
	 * and
	 * {@link main.general.authentication.Authenticator#SECONDS_TO_REMOVE_FROM_CACHE}
	 * the user object doesn't used so it must be deleted to clear the memory space)
	 */
	@Scheduled(fixedRate = MINUTES_TO_REMOVE_FROM_CACHE * 60 * 1000)
	static void checkExpireCachesTime() {
		for (UserCache userCache : users) {
			LocalTime now = LocalTime.now();
			LocalTime expireCacheTime = userCache.getExpireTime();

			int compareResult = now.compareTo(expireCacheTime);
			if (compareResult <= 0) {
				users.remove(userCache);
			}
		}
	}

	/**
	 * return existence of telephone number in database
	 * 
	 * @param user User object which contain the telephone number
	 * @return return true when in database we have a telephone number like param
	 * @throws Exception exception when connect and execute the query
	 */
	static boolean isExistsTelephoneNumberInDatabase(User user) throws Exception {
		String queryTemplate = "SELECT * FROM users WHERE telephone='%s'";
		String query = String.format(queryTemplate, user.getTelephone());
		ResultSet set = MysqlConnector.get(query);

		if (set.next()) {
			return true;
		}
		// if in set doesn't any phone match return false
		return false;
	}

	/**
	 * return existence of telephone number that have a same password in database
	 * 
	 * @param user User object which contain the telephone number and password
	 * @return if have a row that include the same number and same password return
	 *         that row by result set otherwise return null
	 * @throws Exception exception when connect and execute the query
	 */
	static ResultSet getByTelephoneAndPasswordFromDatabase(User user) throws Exception {
		String queryTemplate = "SELECT * FROM users WHERE telephone='%s' and password='%s'";
		String query = String.format(queryTemplate, user.getTelephone(), user.getPassword());
		ResultSet set = MysqlConnector.get(query);

		if (set.next()) {// if we have at least 1 row
			return set;// return and do not throw exception in following command
		}

		throw new Exception("unknown user in database");
	}

	/**
	 * insert an user data into database
	 * 
	 * @param user include all datas to save into database
	 * @throws Exception exception when connect and execute the query
	 */
	static void insertNewUser(User user) throws Exception {
		String queryTemplate = "INSERT INTO users(telephone,password,name,family,age) VALUES('%s','%s','%s','%s','%s')";
		String query = String.format(queryTemplate, user.getTelephone(), user.getPassword(),user.getName(),user.getFamily(),user.getAge());
		MysqlConnector.set(query);
	}
	
	// TODO write test
	/**
	 * get an user object use it phone to update the money by its money
	 * @param user
	 * @throws Exception
	 */
	public static void updateMoney(User user) throws Exception {
		String queryTemplate = "UPDATE users SET money='%s' WHERE telephone='%s'";
		String query = String.format(queryTemplate, user.getMoney(), user.getTelephone());
		MysqlConnector.set(query);
	}
}
