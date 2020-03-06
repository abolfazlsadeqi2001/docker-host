package main.general.authentication;

import java.sql.ResultSet;

public class AuthenticatorFront {
	public static User login(String telephone, String password) throws Exception {
		User user = new User();
		// set user fields to validate from cache or database
		user.setPassword(Authenticator.validatePassword(password));
		user.setTelephone(Authenticator.validateTelephone(telephone));
		// get whole user datas using result set from database
		ResultSet set = Authenticator.getByTelephoneAndPassword(user);
		
		user.setId(set.getInt("id"));

		set.close();
		return user;
	}
	
	/**
	 * to register a user using these fiels
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
		ResultSet set = Authenticator.getByTelephoneAndPassword(user);
		user.setId(set.getInt("id"));
		// return user
		set.close();
		return user;
	}
}
