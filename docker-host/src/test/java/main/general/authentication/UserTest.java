package main.general.authentication;

import org.junit.jupiter.api.Test;

import main.general.authentication.models.User;

public class UserTest {
	int[] ids= new int[] {
		1,
		2,
	};
	
	String[] phones = new String[] {
		"1111",
		"2222"
	};
	String[] passwords = new String[] {
		"ab12",
		"ab11"
	};
	String[] names = new String[] {
		"ab",
		"12"
	};
	String[] families = new String[] {
		"adf",
		"qers"
	};
	int[] ages = new int[] {
		12,
		48
	};
	
	int[] moneies = new int[] {
			4000,
			2000
	};
	/**
	 * check both equals and equals by telephone and password methods
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
			throw new Exception("different ids equal!");

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
			throw new Exception("different ids equal!");

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
			throw new Exception("different ids equal!");

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
			throw new Exception("different ids equal!");

		user1.setMoney(moneies[1]);
		if (!user1.equals(user2))
			throw new Exception("same users don't equal");
	}
	
	// TODO change different ids message
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
	 * @throws Exception
	 */
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
		
		if(user.getName() == null) {
			throw new Exception("exception is null");
		}
		
		if(user.getFamily() == null) {
			throw new Exception("exception is null");
		}
		
	}
}
