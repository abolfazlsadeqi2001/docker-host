package general;

import java.io.File;
import java.io.FileInputStream;

/**
 * this class can read some properties like password,user name of database ,Kavenegar key etc
 * from a file then return each property
 * you must write the .config file in your user home which will run the spring application<br>
 * The config file must look like the following syntax:<br>
 * user-name-on-database|password-on-database|<br>
 * for instance:<br>
 * root|1234|<br>
 * take care that all of the elements like password etc must finished by | otherwise the reader read it by an enter in end of that for example:<br>
 * root|1234 will read as user='root' and password='1234<br>'
 * @author abolfazlsadeqi2001
 * every changes must make test for {@link main.general.configs.reader.ConfigReaderTest}
 */
public class ConfigReader {
	private static String body = null;
	static final String CONFIGURE_FILE_PATH = System.getProperty("user.home")+"/.configs";
	private static final String CONFIGURATION_SEPRATOR_PATTERN = "\\|";
	private static final int INDEX_OF_DATABASE_USER_NAME = 0;
	private static final int INDEX_OF_DATABASE_PASSWORD = 1;
	
	private static void readTheFile() {
		body = "";
		try {
			File configFile = new File(CONFIGURE_FILE_PATH);
			FileInputStream fis = new FileInputStream(configFile);
			byte[] bytes = fis.readAllBytes();
			for (byte b : bytes) {
				body += (char) b;
			}
			fis.close();
		} catch (Exception e) {
			System.out.println("problem in reading file: "+e.getMessage());
			body = null;
		}
	}
	
	private static String getAnIndex(int index) {
		if(body == null) {
			readTheFile();
		}
		return body.split(CONFIGURATION_SEPRATOR_PATTERN)[index];
	}
	
	public static String getDatabaseUserName() {
		return getAnIndex(INDEX_OF_DATABASE_USER_NAME);
	}
	
	public static String getDatabasePassword() {
		return getAnIndex(INDEX_OF_DATABASE_PASSWORD);
	}

}