package main.general.authentication;

import java.sql.ResultSet;
import java.util.regex.Pattern;

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
 * @author abolfazlsadeqi2001 TODO add filter also to see
 * @see {@link main.controllers.authentication.AuthenticationController}
 */
public class Authenticator {
	// TODO add cachable memory
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
	 * return existance of telephone nubmer in database
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
	 * return existance of telephone nubmer that have a same password in database
	 * 
	 * @param user User object which contain the telephone number and password
	 * @return if have a row that include the same number and same password return
	 *         that row by resultset otherwise return null
	 * @throws Exception exception when connect and execute the query
	 */
	static ResultSet getByTelephoneAndPassword(User user) throws Exception {
		String queryTemplate = "SELECT * FROM users WHERE telephone='%s' and password='%s'";
		String query = String.format(queryTemplate, user.getTelephone(), user.getPassword());
		ResultSet set = MysqlConnector.get(query);

		if(set.next()) {// if we have at least 1 row
			return set;// return and do not throw exception in following command
		}
		
		throw new Exception("unknown user in database");
	}

	/**
	 * insert a user data into database
	 * 
	 * @param user include all datas to save into database
	 * @throws Exception exception when connect and execute the query
	 */
	static void insertNewUser(User user) throws Exception {
		String queryTemplate = "INSERT INTO users(telephone,password) VALUES('%s','%s')";
		String query = String.format(queryTemplate, user.getTelephone(), user.getPassword());
		MysqlConnector.set(query);
	}
}
