package general.database.mysql;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

public class MysqlConnectorTest {
	@Test
	public void testSet() throws Exception {
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('09397534791','adadbxoro12')";
		MysqlConnector.set(insertQuery);
		
		String updateQuery = "UPDATE users SET tel='12341234' WHERE tel like '09397534791' and pass like 'adadbxoro12' ";
		MysqlConnector.set(updateQuery);
		
		String deleteQuery = "DELETE from users WHERE tel like '12341234' and pass like 'adadbxoro12'";
		MysqlConnector.set(deleteQuery);
	}
	
	@Test
	public void testGet() throws Exception {
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('11111111111','adadbxoro12')";
		MysqlConnector.set(insertQuery);
		
		String selectQuery = "SELECT * FROM users WHERE tel like '11111111111' and pass like 'adadbxoro12'";
		ResultSet set = MysqlConnector.get(selectQuery);
		
		if(!set.next()) {
			throw new Exception("inserted entry not found!");
		}
		
		String deleteQuery = "DELETE from users WHERE tel like '11111111111' and pass like 'adadbxoro12'";
		MysqlConnector.set(deleteQuery);
		
		set.close();
	}
}
