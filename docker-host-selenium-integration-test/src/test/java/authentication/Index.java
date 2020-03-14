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

import authentication.models.Age;
import authentication.models.Family;
import authentication.models.Name;
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
	// name section
	Name[] invalidNames = new Name[] { new Name("ab", "too short"), new Name("a1", "too short"),
			new Name("15", "too short"), new Name("ababababa", "too long"), new Name("abafafab1", "too long"),
			new Name("123456789", "too long") };
	Name[] validNames = new Name[] { new Name("123"), new Name("a1b"), new Name("abs"), new Name("abolfaz1"),
			new Name("12345678"), new Name("abolfazl") };
	// family section
	Family[] invalidFamilies = new Family[] { new Family("ab", "too short"), new Family("a1", "too short"),
			new Family("15", "too short"), new Family("ababababa", "too long"), new Family("abafafab1", "too long"),
			new Family("123456789", "too long") };
	Family[] validFamilies = new Family[] { new Family("123"), new Family("a1b"), new Family("abs"),
			new Family("abolfaz1"), new Family("12345678"), new Family("abolfazl") };
	// age section
	Age[] invalidAges = new Age[] { new Age(6), new Age(72) };
	Age[] validAges = new Age[] { new Age(7), new Age(71), new Age(18), new Age(10), new Age(42) };

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
		for (Phone phone : validPhones) {
			fillInput(telephone, phone.getNumber(), "you have not to see any telephone error",
					"your pattern doesn't allow a validate number", submitElement);
		}
	}

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
		for (Password password2 : validPasswords) {
			fillInput(password, password2.getPassword(), "you have not to see a password error", password2.getMessage(),
					submit);
		}
	}

	@Test
	public void testAddCookieOnCorrectLogin() throws Exception {
		String insertQueryTemplate = "INSERT INTO users(telephone,password,name,family,age) VALUES('%s','%s','%s','%s','%s')";
		String insertQuery = String.format(insertQueryTemplate, validPhones[0].getNumber(),
				validPasswords[0].getPassword(), validNames[0].getName(), validFamilies[0].getFamily(),
				validAges[0].getAge());
		MysqlConnector.set(insertQuery);

		driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']"))
				.sendKeys(validPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='login-form'] [name='password']"))
				.sendKeys(validPasswords[0].getPassword());
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

	@Test
	public void testAddCookieOnIncorrectLogin() {
		driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']"))
				.sendKeys(invalidPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='login-form'] [name='password']"))
				.sendKeys(invalidPasswords[0].getPassword());
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

	@Test
	public void testNameRegisterInputPattern() throws Exception {
		WebElement name = driver.findElement(By.cssSelector("form[name='register-form'] [name='name']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		// invalid names
		for (Name nameObject : invalidNames) {
			fillInput(name, nameObject.getName(), "you have to see a name error", nameObject.getMessage(), submit);
		}
		// valid names
		for (Name nameObject : validNames) {
			fillInput(name, nameObject.getName(), "you have not to see a name error", nameObject.getMessage(), submit);
		}
	}
	
	@Test
	public void testFamilyRegisterInputPattern() throws Exception {
		WebElement family = driver.findElement(By.cssSelector("form[name='register-form'] [name='family']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		// invalid names
		for (Family familyObject : invalidFamilies) {
			fillInput(family, familyObject.getFamily(), "you have to see a family error", familyObject.getMessage(),
					submit);
		}
		// valid names
		for (Family family2 : validFamilies) {
			fillInput(family, family2.getFamily(), "you have not to see a family error", family2.getMessage(), submit);
		}
	}

	@Test
	public void testAgeRegisterInputPattern() throws Exception {
		WebElement age = driver.findElement(By.cssSelector("form[name='register-form'] [name='age']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		// invalid ages
		for (Age age2 : invalidAges) {
			fillInput(age, String.valueOf(age2.getAge()), "you have to see an age error", age2.getMessage(), submit);
		}
		// valid ages
		for (Age age2 : validAges) {
			fillInput(age, String.valueOf(age2.getAge()), "you have not to see an age error", age2.getMessage(), submit);
		}
	}

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
		for (Phone phone : validPhones) {
			fillInput(telephone, phone.getNumber(), "you have not to see any telephone error",
					"your pattern doesn't allow a validate number", submitElement);
		}
	}

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
		for (Password password2 : validPasswords) {
			fillInput(password, password2.getPassword(), "you have not to see a password error", password2.getMessage(),
					submit);
		}
	}

	@Test
	public void testAddCookieOnCorrectRegistration() throws Exception {
		driver.findElement(By.cssSelector("form[name='register-form'] [name='name']"))
				.sendKeys(validNames[0].getName());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='family']"))
				.sendKeys(validFamilies[0].getFamily());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='age']"))
				.sendKeys(String.valueOf(validAges[0].getAge()));
		driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']"))
				.sendKeys(validPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='password']"))
				.sendKeys(validPasswords[0].getPassword());
		driver.findElement(By.cssSelector("form[name='register-form'] [type='submit']")).click();

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
			throw new Exception("doesn't have correct value for telephone or password cookie");
	}

	// make two section one for invalid fields and invalid for such a phone number
	@Test
	public void testAddCookieOnIncorrectRegistrationField() throws Exception {
		driver.findElement(By.cssSelector("form[name='register-form'] [name='name']"))
				.sendKeys(invalidNames[0].getName());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='family']"))
				.sendKeys(validFamilies[0].getFamily());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='age']"))
				.sendKeys(String.valueOf(validAges[0].getAge()));
		driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']"))
				.sendKeys(validPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='password']"))
				.sendKeys(validPasswords[0].getPassword());
		driver.findElement(By.cssSelector("form[name='register-form'] [type='submit']")).click();

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
			throw new Exception("have telephone or password cookie");
	}

	@Test
	public void testAddCookieOnIncorrectRegistrationRepeatedTelephone() throws Exception {
		String insertQueryTemplate = "INSERT INTO users(telephone,password,name,family,age) VALUES('%s','%s','%s','%s','%s')";
		String insertQuery = String.format(insertQueryTemplate, validPhones[0].getNumber(),
				validPasswords[0].getPassword(), validNames[0].getName(), validFamilies[0].getFamily(),
				validAges[0].getAge());
		MysqlConnector.set(insertQuery);

		driver.findElement(By.cssSelector("form[name='register-form'] [name='name']"))
				.sendKeys(validNames[0].getName());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='family']"))
				.sendKeys(validFamilies[0].getFamily());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='age']"))
				.sendKeys(String.valueOf(validAges[0].getAge()));
		driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']"))
				.sendKeys(validPhones[0].getNumber());
		driver.findElement(By.cssSelector("form[name='register-form'] [name='password']"))
				.sendKeys(validPasswords[0].getPassword());
		driver.findElement(By.cssSelector("form[name='register-form'] [type='submit']")).click();

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
			throw new Exception("have telephone or password cookie");
	}
}
