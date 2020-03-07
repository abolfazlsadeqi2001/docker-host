package main.general.authentication;

import org.junit.jupiter.api.Test;

public class UserTest {
	/**
	 * check both equals and equals by telephone and password methods
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEquals() throws Exception {
		User user1 = new User();
		user1.setTelephone("09397534791");
		user1.setPassword("aabbc1212");
		user1.setId(1);

		User user2 = new User();
		user2.setTelephone("09397534791");
		user2.setPassword("aabbc1212");
		user2.setId(2);

		if (!user1.equalsByTelephoneAndPassword(user2))
			throw new Exception("same users pass and tel don't equal!");

		if (user1.equals(user2))
			throw new Exception("different ids equal!");

		user1.setId(2);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}

	@Test
	public void testUserFields() throws Exception {
		User user = new User();
		
		if(user.getTelephone() == null) {
			throw new Exception("telephone is null");
		}
		
		if(user.getPassword() == null) {
			throw new Exception("password is null");
		}
		
		if(user.getExceptionMessage() == null) {
			throw new Exception("exception is null");
		}
		
	}
}
