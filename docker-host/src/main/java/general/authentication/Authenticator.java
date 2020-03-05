package general.authentication;

import java.util.regex.Pattern;

/**
 * this class placed into general package because it will use in both controller and filter sections<br>
 * this class provide the login and register methods and their submethods (like validating password) as well<br>
 * <b>the login logic</b>
 * <ol>
 * <li> validate the password and telephone (if they are invalid it will broke)</li>
 * <li> check on cache memory (implements more performance)</li>
 * <li> check on database</li>
 * </ol>
 * <b>the register logic</b>
 * <ol>
 * <li> validate the password and telephone (if they are invalid it will broke) </li>
 * <li> check on cache memory (implements more performance) (to haven't any match) </li>
 * <li> check on database (to haven't any match then add to both cache and database) </li>
 * </ol>
 * @author abolfazlsadeqi2001
 * TODO add filter also to see
 * @see
 * {@link main.controllers.authentication.AuthenticationController}
 */
public class Authenticator {
	// validating password section
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int PASSWORD_MAX_LENGTH = 12;
	
	/**
	 * this method validate the password as out criterias like length and has alphabetical and numberid<br>
	 * <b>If you want to change error message go here they aren't class variables</b>
	 * @param password our password to validate
	 * @return return the validated password (it will trim)
	 * @throws Exception if our password is invalid it will throw an exception which include problem message
	 */
	public static String validatePassword(String password) throws Exception {
		String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
		password = password.trim();
		
		if(password.length() < PASSWORD_MIN_LENGTH) {
			throw new Exception("password length must be logner");
		}
		
		if(password.length() > PASSWORD_MAX_LENGTH) {
			throw new Exception("password length must shorter");
		}
		
		if(!Pattern.matches(pattern, password)) {
			throw new Exception("invalid password");
		}
		
		return password;
	}
	
	/**
	 * validate the telephone number by our criteria (pattern)
	 * if it invalid throw an exception
	 * @param telephone
	 * @return
	 * @throws Exception
	 */
	public static String validateTelephone(String telephone) throws Exception {
		telephone = telephone.trim();
		String pattern = "^09[0-9]{9}$";
		
		if(!Pattern.matches(pattern, telephone)) {
			throw new Exception();
		}
		
		return telephone;
	}
}
