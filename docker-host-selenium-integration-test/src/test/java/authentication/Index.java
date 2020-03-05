package authentication;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import general.SeleniumTestParent;

/**
 * @author abolfazlsadeqi2001
 * @version 1.0.0
 * @see
 * {@link general.SeleniumTestParent}
 */
public class Index extends SeleniumTestParent {
	private static final String PATH_TO_HTML = "/authentication/index.html";
	
	// setup the address to test
	@BeforeClass
	public static void beforeAll() {
		prepration(PATH_TO_HTML);
	}
	// login form and its content tests
	@Test
	public void testLoginForm() {
		driver.findElement(By.cssSelector("form[name='login-form'][method='POST'][autocomplete='off'][action='/login']"));
	}
	
	@Test
	public void testTelephoneLoginInput() {
		driver.findElement(By.cssSelector("form[name='login-form'] input[type='text'][name='tel'][required='required'][pattern][title]:not([form])"));
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
		WebElement telephone = driver.findElement(By.cssSelector("form[name='login-form'] [name='tel']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a telephone error";
		
		fillInput(telephone, "01397534791", errorInformMessage, "your pattern match a telephone nubmer that start by 01", submitElement);
		fillInput(telephone, "19397534791", errorInformMessage, "your pattern does allow a nubmer which start by 19", submitElement);
		fillInput(telephone, "0939753479", errorInformMessage, "your pattern does allow a nubmer which hasn't enogh characters", submitElement);
		fillInput(telephone, "093975347911", errorInformMessage, "your pattern allow a number which has a greator than length", submitElement);
		fillInput(telephone, "09397534791", "you have not to see any telephone error","your pattern doesn't allow a validate number", submitElement);
	}
	
	@Test
	public void testEmailLoginInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='login-form'] [name='tel']"));
		WebElement password = driver.findElement(By.cssSelector("form[name='login-form'] [name='password']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='login-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a password error";
		
		// fill the tel input by a validate nubmer which will help us to see the password errors
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
		driver.findElement(By.cssSelector("form[name='register-form'] input[type='text'][name='tel'][required='required'][pattern][title]:not([form])"));
	}
	
	@Test
	public void testPasswordRegisterInput() {
		driver.findElement(By.cssSelector("form[name='register-form'] input[type='password'][name='password'][required='required'][pattern][title]:not([form])"));
	}
	
	@Test
	public void testSubmitRegisterInput() {
		driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit'][value='register']:not([formaction])"));
	}
	
	@Test
	public void testTelephoneRegisterInputPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='register-form'] [name='tel']"));
		WebElement submitElement = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a telephone error";
		
		fillInput(telephone, "01397534791", errorInformMessage, "your pattern match a telephone nubmer that start by 01", submitElement);
		fillInput(telephone, "19397534791", errorInformMessage, "your pattern does allow a nubmer which start by 19", submitElement);
		fillInput(telephone, "0939753479", errorInformMessage, "your pattern does allow a nubmer which hasn't enogh characters", submitElement);
		fillInput(telephone, "093975347911", errorInformMessage, "your pattern allow a number which has a greator than length", submitElement);
		fillInput(telephone, "09397534791", "you have not to see any telephone error","your pattern doesn't allow a validate number", submitElement);
	}
	
	@Test
	public void testEmailLoginRegisterPattern() throws Exception {
		WebElement telephone = driver.findElement(By.cssSelector("form[name='register-form'] [name='tel']"));
		WebElement password = driver.findElement(By.cssSelector("form[name='register-form'] [name='password']"));
		WebElement submit = driver.findElement(By.cssSelector("form[name='register-form'] input[type='submit']"));
		String errorInformMessage = "you have to see a password error";
		
		// fill the tel input by a validate nubmer which will help us to see the password errors
		fillInput(telephone, "09397534791");
		
		fillInput(password, "12341234", errorInformMessage, "pattern allowed only numberic password", submit);
		fillInput(password, "mamad1", errorInformMessage, "pattern allowed small length", submit);
		fillInput(password, "mamadmamad", errorInformMessage, "pattern allowed only numberic password", submit);
		fillInput(password, "adadbxoro1212", "you have not to see any password error", "pattern allowed only numberic password", submit);
	}
}
