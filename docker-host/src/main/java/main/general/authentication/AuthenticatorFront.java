package main.general.authentication;

import java.sql.ResultSet;

import main.general.authentication.models.User;

public class AuthenticatorFront {
	/**
	 * to login a user using these fields<br>
	 * any exception mean loin process failed<br>
	 * exception messages usually are written by myself that means they are can be used for give users some information
	 * about why he cannot register or login
	 * @param telephone
	 * @param password
	 * @return
	 * @throws Exception
	 */
	// TODO check to have all fields with correct value in both cache and database
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
			user.setUserId(cachedUser.getUserId());
			user.setName(cachedUser.getName());
			user.setFamily(cachedUser.getFamily());
			user.setAge(cachedUser.getAge());
			user.setMoney(cachedUser.getMoney());
		}else {// get whole user datas using result set from database then close it and add the user to cache memory
			ResultSet set = Authenticator.getByTelephoneAndPasswordFromDatabase(user);
			
			user.setUserId(set.getInt("id"));
			user.setName(set.getString("name"));
			user.setFamily(set.getString("family"));
			user.setAge(set.getInt("age"));
			user.setMoney(set.getInt("money"));
			
			Authenticator.attachToCache(user);
			set.close();
		}

		return user;
	}
	
	/**
	 * to register a user using these fields<br>
	 * any exception mean register process fail<br>
	 * exception messages usually are written by myself that means they are can be used for give users some information
	 * about why he cannot register or login
	 * @param telephone
	 * @param password
	 * @return return an user if the process of saving doesn't have any exception
	 * @throws Exception exceptions in process of saving to database
	 */
	public static User register(String telephone, String password,String name,String family,int age) throws Exception {
		User user = new User();
		// set user fields to validate from cache or database
		user.setPassword(Authenticator.validatePassword(password));
		user.setTelephone(Authenticator.validateTelephone(telephone));
		user.setAge(Authenticator.validateAge(age));
		user.setFamily(Authenticator.validateFamily(family));
		user.setName(Authenticator.validateName(name));
		// check that the telephone number doesn't exists
		if (Authenticator.isExistsTelephoneNumberInDatabase(user)) {
			throw new Exception("anoher user use a same phone number");
		}
		// insert to database
		Authenticator.insertNewUser(user);
		// get user datas on database (just id)
		ResultSet set = Authenticator.getByTelephoneAndPasswordFromDatabase(user);
		user.setUserId(set.getInt("id"));
		// attach new user to cache and return it
		Authenticator.attachToCache(user);
		set.close();
		return user;
	}
}
