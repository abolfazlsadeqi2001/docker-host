package filters.authentication;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import authentication.models.Password;
import authentication.models.Phone;
import general.MysqlConnector;
import general.SeleniumTestParent;

public class AuthenticationFilter {
	// global configuration
	private static WebDriver driver;
	private static String URI_PATH = "/panel";
	private final String PASSWORD_COOKIE_NAME = "password";
	private final String TELEPHONE_COOKIE_NAME = "telephone";
	// phone section
	private Phone[] validPhones = new Phone[] { new Phone("09397534791", "correct"),
			new Phone("09397534790", "correct") };
	private Phone[] invalidPhones = new Phone[] {
			new Phone("01397534791", "your pattern match a telephone nubmer that start by 01"),
			new Phone("19397534791", "your pattern does allow a nubmer which start by 19"),
			new Phone("0939753479", "your pattern does allow a nubmer which hasn't enogh characters"),
			new Phone("093975347911", "your pattern allow a number which has a greator than length"),
			new Phone("mamadzoro12", "your pattern allowed the chars for telephone") };
	// password section
	private Password[] validPasswords = new Password[] { new Password("adadbxoro12", "correct") };
	private Password[] invalidPasswords = new Password[] {
			new Password("12341234", "pattern allowed only numberic password"),
			new Password("mamad1", "pattern allowed small length"),
			new Password("mamadmamad", "pattern allowed only chars password"),
			new Password("adadbxorodsfsf12121", "pattern allowed long length password") };
	/**
	 * before everything open firefox then maximize it
	 */
	@BeforeClass
	public static void setup() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
	}

	/**
	 * this method will be runned before each test case method which will change the
	 * firefox url to destination test url (sometimes the url changed like click on
	 * a link or submit a form)
	 */
	@Before
	public void beforeNewTest() {
		driver.get(SeleniumTestParent.PAGE_URL + URI_PATH);
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

	/**
	 * this method will be runned when the test finished to close the browser
	 */
	@AfterClass
	public static void exitation() {
		driver.quit();
	}

	// =====================>start testing
	@Test
	public void testByInvalidTelephone() {
		for (Phone phone : invalidPhones) {
			Cookie telephoneCookie = new Cookie(TELEPHONE_COOKIE_NAME, phone.getNumber());
			driver.manage().addCookie(telephoneCookie);
			
			Cookie passwordCookie = new Cookie(PASSWORD_COOKIE_NAME, validPasswords[0].getPassword());
			driver.manage().addCookie(passwordCookie);
			
			driver.get(SeleniumTestParent.PAGE_URL+URI_PATH);
			
			if (!driver.getTitle().equals("authentication")) {
				fail("with a wrong telephone cookie doesn't go to authentication");
			}
		}
	}
	
	@Test
	public void testByInvalidPassword() {
		for (Password password : invalidPasswords) {
			Cookie telephoneCookie = new Cookie(TELEPHONE_COOKIE_NAME, validPhones[0].getNumber());
			driver.manage().addCookie(telephoneCookie);
			
			Cookie passwordCookie = new Cookie(PASSWORD_COOKIE_NAME, password.getPassword());
			driver.manage().addCookie(passwordCookie);
			
			driver.get(SeleniumTestParent.PAGE_URL+URI_PATH);
			
			if (!driver.getTitle().equals("authentication")) {
				fail("with a wrong password cookie doesn't go to authentication");
			}
		}
	}
	
	@Test
	public void testByAllRightCookies() {
		Cookie telephoneCookie = new Cookie(TELEPHONE_COOKIE_NAME, validPhones[0].getNumber());
		driver.manage().addCookie(telephoneCookie);
		
		Cookie passwordCookie = new Cookie(PASSWORD_COOKIE_NAME, validPasswords[0].getPassword());
		driver.manage().addCookie(passwordCookie);
		
		driver.get(SeleniumTestParent.PAGE_URL+URI_PATH);
		
		if (!driver.getTitle().equals("user panel")) {
			fail("with all right cookies doesn't go to panel");
		}
	}
	
	@Test
	public void testByAllWrongCookies() {
		Cookie telephoneCookie = new Cookie(TELEPHONE_COOKIE_NAME, invalidPhones[0].getNumber());
		driver.manage().addCookie(telephoneCookie);
		
		Cookie passwordCookie = new Cookie(PASSWORD_COOKIE_NAME, invalidPasswords[0].getPassword());
		driver.manage().addCookie(passwordCookie);
		
		driver.get(SeleniumTestParent.PAGE_URL+URI_PATH);
		
		if (!driver.getTitle().equals("authentication")) {
			fail("with all wrong cookies doesn't go to authentication");
		}
	}
}
