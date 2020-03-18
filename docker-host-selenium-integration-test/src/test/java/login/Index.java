package login;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import authentication.models.Age;
import authentication.models.Family;
import authentication.models.Name;
import authentication.models.Password;
import authentication.models.Phone;
import general.MysqlConnector;
import general.SeleniumTestParent;

public class Index {
	// global configuration
	private static WebDriver driver;
	private static String URI_PATH = "/login";
	private final String COMPLETE_URL_TEMPLATE = SeleniumTestParent.PAGE_URL + URI_PATH + "?telephone=%s&password=%s";
	// phone section
	private Phone[] invalidPhones = new Phone[] {
			new Phone("01397534791", "your pattern match a telephone nubmer that start by 01"),
			new Phone("19397534791", "your pattern does allow a nubmer which start by 19"),
			new Phone("0939753479", "your pattern does allow a nubmer which hasn't enogh characters"),
			new Phone("093975347911", "your pattern allow a number which has a greator than length"),
			new Phone("mamadzoro12", "your pattern allowed the chars for telephone") };
	private Phone[] validPhones = new Phone[] { new Phone("09397534791", "correct"),
			new Phone("09397534790", "correct") };
	// password section
	private Password[] invalidPasswords = new Password[] {
			new Password("12341234", "pattern allowed only numberic password"),
			new Password("mamad1", "pattern allowed small length"),
			new Password("mamadmamad", "pattern allowed only chars password"),
			new Password("adadbxorodsfsf12121", "pattern allowed long length password") };
	private Password[] validPasswords = new Password[] { new Password("adadbxoro12", "correct") };
	// name section
	private Name[] validNames = new Name[] { new Name("123"), new Name("a1b"), new Name("abs"), new Name("abolfaz1"),
			new Name("12345678"), new Name("abolfazl") };
	// family section
	private Family[] validFamilies = new Family[] { new Family("123"), new Family("a1b"), new Family("abs"),
			new Family("abolfaz1"), new Family("12345678"), new Family("abolfazl") };
	// age section
	private Age[] validAges = new Age[] { new Age(7), new Age(71), new Age(18), new Age(10), new Age(42) };

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
	// TODO check contains methods up
	// TODO remove confirmations message into authentication
	// TODO move all valid and invalid phones and passwords into a general container
	// TODO refactor all of the tests variables
	@Test
	public void testWithIncorrectTelephoneFields() {
		for (Phone phone : invalidPhones) {
			driver.get(String.format(COMPLETE_URL_TEMPLATE, phone.getNumber(), validPasswords[0].getPassword()));
			WebElement errorContainer = driver.findElement(By.cssSelector(".error-container"));
			if (!errorContainer.getText().contains("phone")) {
				fail("error message doesn't contain phone error");
			}
		}
	}

	@Test
	public void testWithIncorrectPasswordFields() {
		for (Password password : invalidPasswords) {
			driver.get(String.format(COMPLETE_URL_TEMPLATE, validPhones[0].getNumber(), password.getPassword()));
			WebElement errorContainer = driver.findElement(By.cssSelector(".error-container"));
			if (!errorContainer.getText().contains("password")) {
				fail("error message doesn't contain password error");
			}
		}
	}

	@Test
	public void testWithCorrectFieldsButUnknownUser() {
		driver.get(String.format(COMPLETE_URL_TEMPLATE, validPhones[0].getNumber(), validPasswords[0].getPassword()));
		WebElement errorContainer = driver.findElement(By.cssSelector(".error-container"));
		if (!errorContainer.getText().contains("unknown user"))
			fail("'unknown user doesn't found into error message");
	}
	
	@Test
	public void testWithCorrectLoginAndKnownUsers() throws Exception {
		// insert into database
		String insertTemplate = "INSERT INTO users(telephone,password,name,family,age) VALUES('%s','%s','%s','%s','%s')";
		String insertQuery = String.format(insertTemplate, validPhones[0].getNumber(), validPasswords[0].getPassword(),
				validNames[0].getName(), validFamilies[0].getFamily(), validAges[0].getAge());
		MysqlConnector.set(insertQuery);
		// test section
		driver.get(String.format(COMPLETE_URL_TEMPLATE, validPhones[0].getNumber(),validPasswords[0].getPassword()));
		if(!driver.getTitle().equals("user panel"))
			fail(null);
	}
}
