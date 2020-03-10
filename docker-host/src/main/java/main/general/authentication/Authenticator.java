package main.general.authentication;

import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
	// cache section
	private static Set<UserCache> users = new HashSet<UserCache>();
	static final int MINUTES_TO_REMOVE_FROM_CACHE = 10;
	static final int SECONDS_TO_REMOVE_FROM_CACHE = 0;
	// validating password section
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int PASSWORD_MAX_LENGTH = 12;
	
	/**
	 * get telephone and password after validating get an user instance which
	 * include all of user properties in database if they aren't exists in database
	 * or they aren't valid return null
	 * 
	 * @param telephone string telephone nubmer
	 * @param password  sring password
	 * @return return an user that has all his properties from cache or database if
	 *         an exception wasn't thrown
	 */

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
	 * TODO make test
	 * get expire time by {@value main.general.authentication.Authenticator#MINUTES_TO_REMOVE_FROM_CACHE} minutes and
	 * {@value main.general.authentication.Authenticator#SECONDS_TO_REMOVE_FROM_CACHE} seconds after current time
	 * @return expireTime
	 */
	static LocalTime getExpireTime() {
		int currentHour = LocalTime.now().getHour();
		int expireMinutes = LocalTime.now().getMinute()+MINUTES_TO_REMOVE_FROM_CACHE;
		int expireSeconds = LocalTime.now().getSecond()+SECONDS_TO_REMOVE_FROM_CACHE;
		LocalTime expireTime = LocalTime.of(currentHour,expireMinutes,expireSeconds);
		
		return expireTime;
	}
	
	/**
	 * TODO make test
	 * this method add the given user to cache memory by a time that equals ten minutes after current time
	 * @param user a user object that you want to add to your cache
	 */
	static void attachToCache(User user) {
		System.out.println("attaching new user");
		UserCache cache = new UserCache();
		cache.setExpireTime(getExpireTime());
		cache.setUser(user);
		
		users.add(cache);
	}
	
	/**
	 * this method find user just by telephone and password (user param only have to password and telephone)
	 * @param user user to find by password and telephone
	 * @return an user object if exists that user by same telephone and password otherwise return null
	 * TODO write test for cache memory
	 */
	static User getUserByTelephoneAndPasswordFromCache(User user) {
		System.out.println("start reading from cahce");
		for (UserCache userCache : users) {
			User currentUser = userCache.getUser();
			if(currentUser.equalsByTelephoneAndPassword(user)) {
				return user;
			}
		}
		System.out.println("user not found");
		return null;
	}
	
	/**
	 * this method check all of the attached CacheUsers up to don't expired its expire time if it does remove it
	 * (it means for a specific time that specified in {@link main.general.authentication.Authenticator#MINUTES_TO_REMOVE_FROM_CACHE}
	 * and {@link main.general.authentication.Authenticator#SECONDS_TO_REMOVE_FROM_CACHE} the user object doesn't used so it must be deleted
	 * to clear the memory space)
	 * TODO add schedule for this
	 * TODO make test
	 */
	@Scheduled(fixedRate = MINUTES_TO_REMOVE_FROM_CACHE * 60 * 1000)
	static void checkExpireCachesTime() {
		System.out.println("check expire time up");
		for (UserCache userCache : users) {
			LocalTime now = LocalTime.now();
			LocalTime expireCacheTime = userCache.getExpireTime();
			
			int compareResult = now.compareTo(expireCacheTime);
			if(compareResult <= 0) {
				users.remove(userCache);
			}
		}
	}
	
	/**
	 * return existance of telephone number in database
	 * 
	 * @param user User object which contain the telephone number
	 * @return return true when in database we have a telephone number like param
	 * @throws Exception exception when connect and execute the query
	 */
	static boolean isExistsTelephoneNumber(User user) throws Exception {
		String queryTemplate = "SELECT * FROM users WHERE telephone='%s'";
		String query = String.format(queryTemplate, user.getTelephone());
		ResultSet set = MysqlConnector.get(query);

		if(set.next()) {
			return true;
		}
		// if in set doesn't any phone match return false
		return false;
	}
	
	/**
	 * return existance of telephone number that have a same password in database
	 * 
	 * @param user User object which contain the telephone number and password
	 * @return if have a row that include the same number and same password return
	 *         that row by resultset otherwise return null
	 * @throws Exception exception when connect and execute the query
	 */ 
	static ResultSet getByTelephoneAndPasswordFromDatabase(User user) throws Exception {
		String queryTemplate = "SELECT * FROM users WHERE telephone='%s' and password='%s'";
		String query = String.format(queryTemplate, user.getTelephone(), user.getPassword());
		ResultSet set = MysqlConnector.get(query);

		if(set.next()) {// if we have at least 1 row
			return set;// return and do not throw exception in following command
		}
		
		throw new Exception("unknown user in database");
	}

	/**
	 * insert an user data into database
	 * @param user include all datas to save into database
	 * @throws Exception exception when connect and execute the query
	 */
	static void insertNewUser(User user) throws Exception {
		String queryTemplate = "INSERT INTO users(telephone,password) VALUES('%s','%s')";
		String query = String.format(queryTemplate, user.getTelephone(), user.getPassword());
		MysqlConnector.set(query);
	}
}
