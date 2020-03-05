package authentication;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;

import general.SeleniumTestParent;

/**
 * @author abolfazlsadeqi2001
 * @version 1.0.0
 * @see
 * {@link general.SeleniumTestParent}
 */
public class Index extends SeleniumTestParent {
	// setup the address to test
	@BeforeClass
	public static void beforeAll() {
		prepration("/authentication/index.html");
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
}
