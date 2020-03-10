package main.general.authentication;

import java.sql.ResultSet;

public class AuthenticatorFront {
	/**
	 * testing the following cases can only by console<br>
	 * <ul>
	 * 	<li>check to add the logged user into cache memory</li>
	 * 	<li>check that the cache memory used when and user exists</li>
	 * 	<li>check to have validate the password and telephone</li>
	 * </ul>
	 * to login a user using these fiedls<br>
	 * any exception mean loin process failed<br>
	 * exception messages usually are written by myself that means they are can be used for give users some information
	 * about why he cannot register or login
	 * TODO write another login without validation the fields
	 * TODO write a test about validating
	 * @param telephone
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static User login(String telephone, String password) throws Exception {
		User user = new User();
		// set user fields to validate from cache or database
		user.setPassword(Authenticator.validatePassword(password));
		user.setTelephone(Authenticator.validateTelephone(telephone));
		/*
		 * get user datas if it is exists in cache so return cachedUser otherwise using database and if it doesn't exists too
		 * throw exception from getByTelephoneAndPasswordFromDatabase
		 */
		User cachedUser = Authenticator.getUserByTelephoneAndPasswordFromCache(user);
		if(cachedUser != null) {
			return cachedUser;
		}else {// get whole user datas using result set from database then close it and add the user to cache memory
			ResultSet set = Authenticator.getByTelephoneAndPasswordFromDatabase(user);
			
			user.setId(set.getInt("id"));
			
			Authenticator.attachToCache(user);
			set.close();
		}

		return user;
	}
	
	/**
	 * testing the following cases can only by console<br>
	 * <ul>
	 * 	<li>add user to cache memory</li>
	 * </ul>
	 * to register a user using these fiels<br>
	 * any exception mean register process faild<br>
	 * exception messages usually are written by myself that means they are can be used for give users some information
	 * about why he cannot register or login
	 * TODO write another login without validation the fields
	 * TODO write a test about validating
	 * @param telephone
	 * @param password
	 * @return return an user if the process of saving doesn't have any exception
	 * @throws Exception exceptions in process of saving to database
	 */
	public static User register(String telephone, String password) throws Exception {
		User user = new User();
		// set user fields to validate from cache or database
		user.setPassword(Authenticator.validatePassword(password));
		user.setTelephone(Authenticator.validateTelephone(telephone));
		// check that the telephone number doesn't exists
		if (Authenticator.isExistsTelephoneNumber(user)) {
			throw new Exception("anoher user use a same phone number");
		}
		// insert to database
		Authenticator.insertNewUser(user);
		// get user datas on database (just id)
		ResultSet set = Authenticator.getByTelephoneAndPasswordFromDatabase(user);
		user.setId(set.getInt("id"));
		// attach new user to cache and return it
		Authenticator.attachToCache(user);
		set.close();
		return user;
	}
}
