package authentication;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import general.SeleniumTestParent;

/**
 * @author abolfazlsadeqi2001
 * @version 1.2.0
 * @see
 * {@link general.SeleniumTestParent}
 */
public class Index extends SeleniumTestParent {
	private static final String PATH_TO_HTML = "/authentication";
	
	// setup the address to test
	@BeforeClass
	public static void beforeAll() {
		prepration(PATH_TO_HTML);
	}
	
	@Before
	public void beforeEach() {
		driver.manage().deleteAllCookies();
	}
	
	// login form and its content tests
	@Test
	public void testLoginForm() {
		driver.findElement(By.cssSelector("form[name='login-form'][method='POST'][autocomplete='off'][action='/login']"));
	}
	
	@Test
	public void testTelephoneLoginInput() {
		driver.findElement(By.cssSelector("form[name='login-form'] input[type='text'][name='telephone'][required='required'][pattern][title]:not([form])"));
	}
	
	@Test
	public void testPasswordLoginInput() {
		driver.findElement(By.cssSelector("form[name='login-form'] input[type='password'][name='password'][required='required'][pattern][title]:not([form])"));
	}
	
	@Test
	public void testSubmitLoginInput() {
		driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit'][value='login']:not([formaction])"));
	}
	
	@Test
	public void testTelephoneLoginInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a telephone error";
		
		fillInput(telephone, "01397534791", errorInformMessage, "your pattern match a telephone nubmer that start by 01", submitElement);
		fillInput(telephone, "19397534791", errorInformMessage, "your pattern does allow a nubmer which start by 19", submitElement);
		fillInput(telephone, "0939753479", errorInformMessage, "your pattern does allow a nubmer which hasn't enogh characters", submitElement);
		fillInput(telephone, "093975347911", errorInformMessage, "your pattern allow a number which has a greator than length", submitElement);
		fillInput(telephone, "mamadzoro12", "you have to see a telephone error","your pattern allowed the chars for telephone", submitElement);
		fillInput(telephone, "09397534791", "you have not to see any telephone error","your pattern doesn't allow a validate number", submitElement);
	}
	
	@Test
	public void testEmailLoginInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='login-form'] [name='telephone']"));
		WebElement password = driver.findElement(By.cssSelector("form[name='login-form'] [name='password']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a password error";
		
		// fill the telephone input by a validate nubmer which will help us to see the password errors
		fillInput(telephone, "09397534791");
		
		fillInput(password, "12341234", errorInformMessage, "pattern allowed only numberic password", submit);
		fillInput(password, "mamad1", errorInformMessage, "pattern allowed small length", submit);
		fillInput(password, "mamadmamad", errorInformMessage, "pattern allowed only numberic password", submit);
		fillInput(password, "adadbxoro12", "you have not to see any password error", "pattern allowed only numberic password", submit);
	}
	
	// register form and its content tests
	@Test
	public void testRegisterForm() {
		driver.findElement(By.cssSelector("form[name='register-form'][method='POST'][autocomplete='off'][action='/register']"));
	}
	
	@Test
	public void testTelephoneRegisterInput() {
		driver.findElement(By.cssSelector("form[name='register-form'] input[type='text'][name='telephone'][required='required'][pattern][title]:not([form])"));
	}
	
	@Test
	public void testPasswordRegisterInput() {
		driver.findElement(By.cssSelector("form[name='register-form'] input[type='password'][name='password'][required='required'][pattern][title]:not([form])"));
	}
	
	@Test
	public void testNameRegisterInput() {
		driver.findElement(By.cssSelector("form[name='register-form'] input[name='name'][type='text'][title][pattern][required='required']:not([form])"));
	}
	
	@Test
	public void testSubmitRegisterInput() {
		driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit'][value='register']:not([formaction])"));
	}
	
	@Test
	public void testTelephoneRegisterInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a telephone error";
		
		fillInput(telephone, "01397534791", errorInformMessage, "your pattern match a telephone nubmer that start by 01", submitElement);
		fillInput(telephone, "19397534791", errorInformMessage, "your pattern does allow a nubmer which start by 19", submitElement);
		fillInput(telephone, "0939753479", errorInformMessage, "your pattern does allow a nubmer which hasn't enogh characters", submitElement);
		fillInput(telephone, "093975347911", errorInformMessage, "your pattern allow a number which has a greator than length", submitElement);
		fillInput(telephone, "mamadzoro12", "you have to see a telephone error","your pattern allowed the chars for telephone", submitElement);
		fillInput(telephone, "09397534791", "you have not to see any telephone error","your pattern doesn't allow a validate number", submitElement);
	}
	
	@Test
	public void testNameRegisterInputPattern() throws Exception {
		WebElement name = driver.findElement(By.cssSelector("form[name='register-form'] [name='name']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformationMessage = "you have to see a name error";
		
		fillInput(name,"ab",errorInformationMessage,"less than 3 chars is accepted",submitElement);
		fillInput(name,"abolfl123",errorInformationMessage,"more than 8 chars is accepted",submitElement);
		fillInput(name,"abolfazl","you have to see nothing about name","correct value doesn't allowed",submitElement);
	}
	
	@Test
	public void testAgeRegisterInputPattern() throws Exception {
		WebElement age = driver.findElement(By.cssSelector("form[name='register-form'] [name='age']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformationMessage = "you have to see a name error";
		
		fillInput(age,"72",errorInformationMessage,"less than 71 is accepted",submitElement);
		fillInput(age,"6",errorInformationMessage,"less than 7 is accepted",submitElement);
		fillInput(age,"18","you have not to see anything error about age","correct value doesn't allowed",submitElement);
	}
	
	@Test
	public void testFamilyRegisterInputPattern() throws Exception {
		WebElement family = driver.findElement(By.cssSelector("form[name='register-form'] [name='family']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformationMessage = "you have to see a family error";
		
		fillInput(family,"ab",errorInformationMessage,"less than 3 chars is accepted",submitElement);
		fillInput(family,"abolfl123",errorInformationMessage,"more than 8 chars is accepted",submitElement);
		fillInput(family,"abolfazl","you have to see nothing about family","correct value doesn't allowed",submitElement);
	}
	
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
			if(cookie.getName().equals("telephone")) {
				if(cookie.getValue().equals("09397534791")) {
					isHasCorrectValueForTelephoneCookie = true;
				}
			}
			
			if(cookie.getName().equals("password")) {
				if(cookie.getValue().equals("mamadzoro12")) {
					isHasCorrectValueForPasswordCookie = true;
				}
			}
		}
		
		if(!isHasCorrectValueForTelephoneCookie || !isHasCorrectValueForPasswordCookie)
			throw new Exception("doesn't have correct value for telephone or password cookie");
	}
	
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
			if(cookie.getName().equals("telephone")) {
				isHasTelephoneCookie = true;
			}
			
			if(cookie.getName().equals("password")) {
				isHasPasswordCookie = true;
			}
		}
		
		if(isHasTelephoneCookie || isHasPasswordCookie)
			throw new Exception("have telephone or password cookie");
	}
	
	@Test
	public void testEmailRegisterPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='register-form'] [name='telephone']"));
		WebElement password = driver.findElement(By.cssSelector("form[name='register-form'] [name='password']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a password error";
		
		// fill the telephone input by a validate nubmer which will help us to see the password errors
		fillInput(telephone, "09397534791");
		
		fillInput(password, "12341234", errorInformMessage, "pattern allowed only numberic password", submit);
		fillInput(password, "mamad1", errorInformMessage, "pattern allowed small length", submit);
		fillInput(password, "mamadmamad", errorInformMessage, "pattern allowed only numberic password", submit);
		fillInput(password, "adadbxoro1212", "you have not to see any password error", "pattern allowed only numberic password", submit);
	}
}
