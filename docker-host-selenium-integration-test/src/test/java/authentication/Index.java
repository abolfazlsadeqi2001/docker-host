package authentication;

import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import authentication.models.Password;
import authentication.models.Phone;
import general.MysqlConnector;
import general.SeleniumTestParent;

/**
 * @author abolfazlsadeqi2001
 * @version 1.2.1
 * @see {@link general.SeleniumTestParent}
 */
public class Index extends SeleniumTestParent {
	private static final String PATH_TO_HTML = "/authentication";
	// cookie section
	private final String PASSWORD_COOKIE_NAME = "password";
	private final String TELEPHONE_COOKIE_NAME = "telephone";
	// phone section
	Phone[] invalidPhones = new Phone[] {
			new Phone("01397534791", "your pattern match a telephone nubmer that start by 01"),
			new Phone("19397534791", "your pattern does allow a nubmer which start by 19"),
			new Phone("0939753479", "your pattern does allow a nubmer which hasn't enogh characters"),
			new Phone("093975347911", "your pattern allow a number which has a greator than length"),
			new Phone("mamadzoro12", "your pattern allowed the chars for telephone") };
	Phone[] validPhones = new Phone[] { new Phone("09397534791", "correct") };
	// password section
	Password[] invalidPasswords = new Password[] { new Password("12341234", "pattern allowed only numberic password"),
			new Password("mamad1", "pattern allowed small length"),
			new Password("mamadmamad", "pattern allowed only chars password"),
			new Password("adadbxorodsfsf12121", "pattern allowed long length password") };
	Password[] validPasswords = new Password[] { new Password("adadbxoro12", "correct") };

	// setup the address to test
	@BeforeClass
	public static void beforeAll() {
		prepration(PATH_TO_HTML);
	}
	// remove all cookies and database caches
	@Before
	public void beforeEach() throws Exception {
		// clear cookies
		driver.manage().deleteAllCookies();
		// remove all phones from database
		String deleteQueryTemplate = "DELETE FROM users WHERE telephone='%s'";
		for (Phone phone : invalidPhones) {
			MysqlConnector.set(String.format(deleteQueryTemplate, phone.getNumber()));
		}
		for (Phone phone : validPhones) {
			MysqlConnector.set(String.format(deleteQueryTemplate, phone.getNumber()));
		}
	}
	
	// do everything in beforeEach method
	@After
	public void afterEach() throws Exception {
		beforeEach();
	}
	// login
	@Test
	public void testLoginForm() {
		driver.findElement(
				By.cssSelector("form[name='login-form'][method='POST'][autocomplete='off'][action='/login']"));
	}

	@Test
	public void testTelephoneLoginInput() {
		driver.findElement(By.cssSelector(
				"form[name='login-form'] input[type='text'][name='telephone'][required='required'][pattern][title]:not([form])"));
	}

	@Test
	public void testPasswordLoginInput() {
		driver.findElement(By.cssSelector(
				"form[name='login-form'] input[type='password'][name='password'][required='required'][pattern][title]:not([form])"));
	}

	@Test
	public void testSubmitLoginInput() {
		driver.findElement(
				By.cssSelector("form[name='login-form'] input[type='submit'][value='login']:not([formaction])"));
	}

	@Ignore
	@Test
	public void testTelephoneLoginInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit']"));
		// incorrect phones
		for (Phone phone : invalidPhones) {
			fillInput(telephone, phone.getNumber(), "you have to see a telephone error", phone.getMessage(),
					submitElement);
		}
		// correct phone
		fillInput(telephone, validPhones[0].getNumber(), "you have not to see any telephone error",
				"your pattern doesn't allow a validate number", submitElement);
	}

	@Ignore
	@Test
	public void testPasswordLoginInputPattern() throws Exception {
		WebElement password = driver.findElement(By.cssSelector("form[name='login-form'] [name='password']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit']"));
		// incorrect password
		for (Password password2 : invalidPasswords) {
			fillInput(password, password2.getPassword(), "you have to see a password error", password2.getMessage(),
					submit);
		}
		// correct password
		fillInput(password, validPasswords[0].getPassword(), "you have not to see a password error",
				validPasswords[0].getMessage(), submit);
	}

	// TODO insert user to have a correct validation
	@Ignore
	@Test
	public void testAddCookieOnCorrectLogin() throws Exception {
		driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']")).sendKeys(validPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='login-form'] [name='password']")).sendKeys(validPasswords[0].getPassword());
		driver.findElement(By.cssSelector("form[name='login-form'] [type='submit']")).click();

		Thread.sleep(1000);

		boolean isHasCorrectValueForTelephoneCookie = false;
		boolean isHasCorrectValueForPasswordCookie = false;

		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TELEPHONE_COOKIE_NAME)) {
				if (cookie.getValue().equals(validPhones[0].getNumber())) {
					isHasCorrectValueForTelephoneCookie = true;
				}
			}

			if (cookie.getName().equals(PASSWORD_COOKIE_NAME)) {
				if (cookie.getValue().equals(validPasswords[0].getPassword())) {
					isHasCorrectValueForPasswordCookie = true;
				}
			}
		}

		if (!isHasCorrectValueForTelephoneCookie || !isHasCorrectValueForPasswordCookie)
			fail("doesn't have correct value for telephone or password cookie");
	}

	@Ignore
	@Test
	public void testAddCookieOnIncorrectLogin() {
		driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']")).sendKeys(invalidPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='login-form'] [name='password']")).sendKeys(invalidPasswords[0].getPassword());
		driver.findElement(By.cssSelector("form[name='login-form'] [type='submit']")).click();

		boolean isHasTelephoneCookie = false;
		boolean isHasPasswordCookie = false;

		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TELEPHONE_COOKIE_NAME)) {
				isHasTelephoneCookie = true;
			}

			if (cookie.getName().equals(PASSWORD_COOKIE_NAME)) {
				isHasPasswordCookie = true;
			}
		}

		if (isHasTelephoneCookie || isHasPasswordCookie)
			fail("have telephone or password cookie");
	}
	// register
	@Test
	public void testRegisterForm() {
		driver.findElement(
				By.cssSelector("form[name='register-form'][method='POST'][autocomplete='off'][action='/register']"));
	}

	@Test
	public void testTelephoneRegisterInput() {
		driver.findElement(By.cssSelector(
				"form[name='register-form'] input[type='text'][name='telephone'][required='required'][pattern][title]:not([form])"));
	}

	@Test
	public void testPasswordRegisterInput() {
		driver.findElement(By.cssSelector(
				"form[name='register-form'] input[type='password'][name='password'][required='required'][pattern][title]:not([form])"));
	}

	@Test
	public void testNameRegisterInput() {
		driver.findElement(By.cssSelector(
				"form[name='register-form'] input[name='name'][type='text'][title][pattern][required='required']:not([form])"));
	}

	@Test
	public void testSubmitRegisterInput() {
		driver.findElement(
				By.cssSelector("form[name='register-form'] input[type='submit'][value='register']:not([formaction])"));
	}

	@Ignore
	@Test
	public void testNameRegisterInputPattern() throws Exception {
		WebElement name = driver.findElement(By.cssSelector("form[name='register-form'] [name='name']"));
		WebElement submitElement = driver
				.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformationMessage = "you have to see a name error";

		fillInput(name, "ab", errorInformationMessage, "less than 3 chars is accepted", submitElement);
		fillInput(name, "abolfl123", errorInformationMessage, "more than 8 chars is accepted", submitElement);
		fillInput(name, "abolfazl", "you have to see nothing about name", "correct value doesn't allowed",
				submitElement);
	}

	@Ignore
	@Test
	public void testFamilyRegisterInputPattern() throws Exception {
		WebElement family = driver.findElement(By.cssSelector("form[name='register-form'] [name='family']"));
		WebElement submitElement = driver
				.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformationMessage = "you have to see a family error";

		fillInput(family, "ab", errorInformationMessage, "less than 3 chars is accepted", submitElement);
		fillInput(family, "abolfl123", errorInformationMessage, "more than 8 chars is accepted", submitElement);
		fillInput(family, "abolfazl", "you have to see nothing about family", "correct value doesn't allowed",
				submitElement);
	}

	@Ignore
	@Test
	public void testAgeRegisterInputPattern() throws Exception {
		WebElement age = driver.findElement(By.cssSelector("form[name='register-form'] [name='age']"));
		WebElement submitElement = driver
				.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformationMessage = "you have to see a name error";

		fillInput(age, "72", errorInformationMessage, "less than 71 is accepted", submitElement);
		fillInput(age, "6", errorInformationMessage, "less than 7 is accepted", submitElement);
		fillInput(age, "18", "you have not to see anything error about age", "correct value doesn't allowed",
				submitElement);
	}

	@Ignore
	@Test
	public void testTelephoneRegisterInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']"));
		WebElement submitElement = driver
				.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));

		// incorrect phones
		for (Phone phone : invalidPhones) {
			fillInput(telephone, phone.getNumber(), "you have to see a telephone error", phone.getMessage(),
					submitElement);
		}
		// correct phone
		fillInput(telephone, validPhones[0].getNumber(), "you have not to see any telephone error",
				"your pattern doesn't allow a validate number", submitElement);
	}
	
	@Ignore
	@Test
	public void testPasswordRegisterPattern() throws Exception {
		WebElement password = driver.findElement(By.cssSelector("form[name='register-form'] [name='password']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		// incorrect password
		for (Password password2 : invalidPasswords) {
			fillInput(password, password2.getPassword(), "you have to see a password error", password2.getMessage(),
					submit);
		}
		// correct password
		fillInput(password, validPasswords[0].getPassword(), "you have not to see a password error",
				validPasswords[0].getMessage(), submit);
	}

	@Ignore
	@Test
	public void testAddCookieOnCorrectRegistration() throws Exception {
		driver.findElement(By.cssSelector("form[name='register-form'] [name='name']")).sendKeys("abolfazl");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='family']")).sendKeys("sadeqi");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='age']")).sendKeys("18");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']")).sendKeys("09397534791");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='password']")).sendKeys("mamadzoro12");
		driver.findElement(By.cssSelector("form[name='register-form'] [type='submit']")).click();

		Thread.sleep(1000);

		boolean isHasCorrectValueForTelephoneCookie = false;
		boolean isHasCorrectValueForPasswordCookie = false;

		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("telephone")) {
				if (cookie.getValue().equals("09397534791")) {
					isHasCorrectValueForTelephoneCookie = true;
				}
			}

			if (cookie.getName().equals("password")) {
				if (cookie.getValue().equals("mamadzoro12")) {
					isHasCorrectValueForPasswordCookie = true;
				}
			}
		}

		if (!isHasCorrectValueForTelephoneCookie || !isHasCorrectValueForPasswordCookie)
			throw new Exception("doesn't have correct value for telephone or password cookie");
	}

	// make two section one for invalid fields and invalid for such a phone number
	@Ignore
	@Test
	public void testAddCookieOnIncorrectRegistration() throws Exception {
		driver.findElement(By.cssSelector("form[name='register-form'] [name='name']")).sendKeys("abolfazl");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='family']")).sendKeys("sadeqi");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='age']")).sendKeys("18");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']")).sendKeys("0939753479");
		driver.findElement(By.cssSelector("form[name='register-form'] [name='password']")).sendKeys("mamadzoro12");
		driver.findElement(By.cssSelector("form[name='register-form'] [type='submit']")).click();

		boolean isHasTelephoneCookie = false;
		boolean isHasPasswordCookie = false;

		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("telephone")) {
				isHasTelephoneCookie = true;
			}

			if (cookie.getName().equals("password")) {
				isHasPasswordCookie = true;
			}
		}

		if (isHasTelephoneCookie || isHasPasswordCookie)
			throw new Exception("have telephone or password cookie");
	}
}
