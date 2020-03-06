package main.general.database.mysql;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

import main.general.database.mysql.MysqlConnector;

public class MysqlConnectorTest {
	private final String[] phones = new String[] {"1111","2222"};
	private final String[] passwords = new String[] {"aa11","bb22"};
	
	@Test
	public void testSet() throws Exception {
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('"+phones[0]+"','"+passwords[0]+"')";
		MysqlConnector.set(insertQuery);
		
		String updateQuery = "UPDATE users SET tel='"+phones[1]+"' WHERE tel like '"+phones[0]+"' and pass like '"+passwords[0]+"' ";
		MysqlConnector.set(updateQuery);
		
		String deleteQuery = "DELETE from users WHERE tel like '"+phones[1]+"' and pass like '"+passwords[0]+"'";
		MysqlConnector.set(deleteQuery);
	}
	
	@Test
	public void testGet() throws Exception {
		String insertQuery = "INSERT INTO users(tel,pass) VALUES('"+phones[0]+"','"+passwords[0]+"')";
		MysqlConnector.set(insertQuery);
		
		String selectQuery = "SELECT * FROM users WHERE tel like '"+phones[0]+"' and pass like '"+passwords[0]+"'";
		ResultSet set = MysqlConnector.get(selectQuery);
		
		if(!set.next()) {
			throw new Exception("inserted entry not found!");
		}
		
		String deleteQuery = "DELETE from users WHERE tel like '"+phones[0]+"'";
		MysqlConnector.set(deleteQuery);
		
		set.close();
	}
}
