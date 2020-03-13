package main.general.configs.reader;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * this class <strong>is harmful </strong> so take care when using this.
 * It must changes the .configs file content
 * @author abolfazlsadeqi2001
 *
 */
public class ConfigReaderTest {
	private final String USER = "test-user";
	private final String PASSWORD = "1234";
	
	@BeforeEach
	public void beforeEach() throws Exception {
		String body = "";
		body += USER+"|";
		body += PASSWORD+"|";
		
		File configFile = new File(ConfigReader.CONFIGURE_FILE_PATH);
		FileOutputStream fos = new FileOutputStream(configFile);
		fos.write(body.getBytes());
		fos.close();
	}
	
	@Test
	public void testGetName() throws Exception {
		boolean isEqual = ConfigReader.getDatabaseUserName().equals(USER);
		if(!isEqual)
			fail("doesn't equal to user name");
	}
	
	@Test
	public void testGetPassword() {
		boolean isEqual = ConfigReader.getDatabasePassword().equals(PASSWORD);
		if(!isEqual)
			fail("doesn't equal to password");
	}
	
}
