package general;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * this class must be the parent of all selenium test cases<br>
 * <b>each children must define a version annotation in its class document which refer to 
 * the version of html file for test (defined in top if each html file like <!-- version 1.0.0 -->)
 * to inform the programmer which version of file can be tested by this test case
 * @author abolfazlsadeqi2001
 * if the website address changed you have to change the value of {@link general.SeleniumTestParent#PAGE_URL} in test resource
 */
public class SeleniumTestParent {
	// webdriver defined in global to access by all @test methods
	protected static WebDriver driver = null;
	// the base url 
	protected static final String PAGE_URL = "http://localhost:8080";
	// the time out which mean if loading page long greator than this number per second you have to implements some optimization
	protected static final int TIME_OUT_TIME = 10;
	
	/**
	 * as the test cases have diffrent URI (path from base url to a particular destination like /authentication)
	 * <b>I think its better to define the @BeforeClass method in children then call this method to prepare the test</b><br>
	 * this method include just some preprations like open firefox maximize it set url and set timeout
	 * @param PAGE_URI path from {@link general.SeleniumTestParent#PAGE_URL} to destination html file like /authentication/index.html
	 */
	public static void prepration(String PAGE_URI) {
		driver = new FirefoxDriver();
		driver.get(PAGE_URL + PAGE_URI);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(TIME_OUT_TIME, TimeUnit.SECONDS);
	}
	
	/**
	 * this method will be runned when the test finished to close the browser
	 */
	@AfterClass
	public static void exitation() {
		driver.quit();
	}
}
