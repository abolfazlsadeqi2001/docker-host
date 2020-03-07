package authentication;

import javax.swing.JOptionPane;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import general.SeleniumTestParent;

/**
 * this class just test that the error container contain anyerror or not<br>
 * the other things like short password length is run by unit test
 * @author abolfazlsadeqi2001
 *
 */
public class IndexRegisterDivContainer extends SeleniumTestParent {
	// pathes
	private static final String PATH = "/authentication";

	// variables
	ClientInfo[] invalidAuthentiactions = new ClientInfo[] {
			new ClientInfo("099999999","ababdxoro12"),
			new ClientInfo("09397534791","ababdxoro11")
	};
	ClientInfo validAuthentication = new ClientInfo("09397534791","ababdxoro12");
	
	@BeforeClass
	public static void testDiv() {
		prepration(PATH);
	}
	
	@Test
	public void testErrorContainer() {
		String telephoneQuery = "form[name='login-form'] input[type='text'][name='tel'][required='required'][pattern][title]:not([form])";
		String passwordQuery = "form[name='login-form'] input[type='password'][name='password'][required='required'][pattern][title]:not([form])";
		String submitQuery = "form[name='login-form'] input[type='submit'][value='login']:not([formaction])";
		
		JOptionPane.showMessageDialog(null, "before click on ok make sure that your database has an entry by 09397534791 tel and ababdxoro11 pass and don't have anyother tel like that (tel field must uniqe)");
		
		WebElement telephone = driver.findElement(By.cssSelector(telephoneQuery));
		WebElement password = driver.findElement(By.cssSelector(passwordQuery));
		WebElement submit = driver.findElement(By.cssSelector(submitQuery));
		
		telephone.sendKeys(invalidAuthentiactions[0].getTelephone());
		password.sendKeys(invalidAuthentiactions[0].getPassword());
		submit.click();
	}
	
}
