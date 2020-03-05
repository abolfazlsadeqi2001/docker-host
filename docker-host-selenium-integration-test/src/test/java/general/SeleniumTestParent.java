package general;

import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
	// how much you have to wait for fill an input then show confirm dialog
	protected static final int FILL_INPUT_TIME = 500;
	// the current path to html file
	protected static String URI_PATH = "";
	
	/**
	 * as the test cases have diffrent URI (path from base url to a particular destination like /authentication)
	 * <b>I think its better to define the @BeforeClass method in children then call this method to prepare the test</b><br>
	 * this method include just some preprations like open firefox maximize it set url and set timeout
	 * @param PAGE_URI path from {@link general.SeleniumTestParent#PAGE_URL} to destination html file like /authentication/index.html
	 */
	public static void prepration(String PAGE_URI) {
		driver = new FirefoxDriver();
		URI_PATH = PAGE_URI;
		driver.get(PAGE_URL + PAGE_URI);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(TIME_OUT_TIME, TimeUnit.SECONDS);
	}
	
	/**
	 * this method is used when you want to show a dialog to tester which inform him something like what will must 
	 * happend
	 * @param message the message that must show
	 */
	protected void showInformation(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	/**
	 * this method is used to when you want to confirm something for instance something you attend to tester will must happen
	 * but it didn't happen so tester click on no and it will throw an exception<br>
	 * this method wait white {@value general.SeleniumTestParent#FILL_INPUT_TIME} miliseconds then show confirm dialog
	 * @param errorMessage error message that will be thrown
	 * @throws Exception error when click on no
	 */
	protected void confirmDialog(String errorMessage) throws Exception {
		try {
			Thread.sleep(FILL_INPUT_TIME);
		} catch (Exception e) {
			System.out.println("sleep of thread is suspended");
		}
		int response = JOptionPane.showConfirmDialog(null, "did it happen?");
		if(response != JOptionPane.OK_OPTION) {
			throw new Exception(errorMessage);
		}
	}
	
	/**
	 * this method get a web element then fill it by value<br>
	 * also this method will clear the previous content of element
	 * <b>this method won't show any message (just filling)</b>
	 * @param element web element to fill
	 * @param value fill by this value
	 */
	protected void fillInput(WebElement element,String value) {
		element.clear();
		element.sendKeys(value);
	}
	/**
	 * this method get a web element then fill it by value<br>
	 * also this method will clear the previous content of element
	 * @param element web element to fill
	 * @param value value that used to fill the web element
	 * @param message message that will be showed as information message
	 * @param errorMessage error message that will be showed when click on no option
	 * @param submit submit button to click on
	 * @throws Exception an exception to inform the junit (don't handle it by try-catch just by throws signature on container method)
	 */
	protected void fillInput(WebElement element,String value,String message,String errorMessage,WebElement submit) throws Exception {
		showInformation(message);
		fillInput(element, value);
		submit.click();
		confirmDialog(errorMessage);
	}
	
	/**
	 * this method will be runned before each test case method which will change the firefox url to destination test url
	 * (sometimes the url changed like click on a link or submit a form)
	 */
	@Before
	public void beforeNewTest() {
		driver.get(PAGE_URL + URI_PATH);
	}
	
	/**
	 * this method will be runned when the test finished to close the browser
	 */
	@AfterClass
	public static void exitation() {
		driver.quit();
	}
}
