package general.authentication;

import org.junit.jupiter.api.Test;

public class AuthenticatorTest {

	@Test
	public void testValidatePassword() throws Exception {
		String errorMessage = "";
		// invalid passwords
		InvalidPassword[] invalids = new InvalidPassword[]{
				new InvalidPassword("1434123412", "allowed only numberic"),
				new InvalidPassword("ababdxogdas", "allowed only ahphabetical"),
				new InvalidPassword("ababd12", "allowed short length password"),
				new InvalidPassword("abaabd1234456", "allowed large length password")
		};
		
		for (InvalidPassword invalidPassword : invalids) {
			try {
				Authenticator.validatePassword(invalidPassword.getPassword());
				errorMessage = invalidPassword.getMessage();
				break;
			} catch (Exception e) {
			}
		}
		
		// validated
		{
			try {
				String password = "ababdxoro13";
				Authenticator.validatePassword(password);
			} catch (Exception e) {
				errorMessage = "doesn't allow a valid password!";
			}
		}
		
		// check the boolean value
		if(!errorMessage.equals(""))
			throw new Exception(errorMessage);
	}
	
	@Test
	public void testValidateTelephone() throws Exception {
		String errorMessage = "";
		// invalids
		InvalidPhone[] invalidPhones = new InvalidPhone[] {
			new InvalidPhone("051-57388377", "allowed a home nubmer with -"),
			new InvalidPhone("02152782121", "validated a home nubmer without -"),
			new InvalidPhone("0930039295", "validated a short phone number"),
			new InvalidPhone("093003925687", "validated a long phone number"),
			new InvalidPhone("19397536145", "validated a phone number start with 1"),
			new InvalidPhone("02397543487","validated a phone nubmer start wit 02")
		};
		
		for (InvalidPhone invalidPhone : invalidPhones) {
			try {
				Authenticator.validateTelephone(invalidPhone.getPhone());
				errorMessage = invalidPhone.getMessage();
				break;
			} catch (Exception e) {
			}
		}
		
		// valid
		try {
			String phone = "09397531212";
			Authenticator.validateTelephone(phone);
		} catch (Exception e) {
			errorMessage = "doesn't allow a validated phone number";
		}
		
		// check the boolean value
		if(!errorMessage.equals("")) {
			throw new Exception(errorMessage);
		}
	}

}
