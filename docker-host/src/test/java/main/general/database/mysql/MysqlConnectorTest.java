package main.general.database.mysql;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

import main.general.database.mysql.MysqlConnector;

public class MysqlConnectorTest {
	// TODO check auto increment up
	// TODO check unique up
	private final String[] phones = new String[] {"1111","2222"};
	private final String[] passwords = new String[] {"aa11","bb22"};
	private final String[] names = new String[] {"abolfazl","alis"};
	private final String[] families = new String[] {"abolfazl","alis"};
	private final int[] ages = new int[] {12,14};
	
	@Test
	public void testSet() throws Exception {
		String insertQuery = "INSERT INTO users(telephone,password,name,family,age) VALUES('"+phones[0]+"','"+
				passwords[0]+"','"+
				names[0]+"','"+
				families[0]+"','"+
				ages[0]+"')";
		MysqlConnector.set(insertQuery);
		
		String updateQuery = "UPDATE users SET telephone='"+phones[1]+"' WHERE telephone like '"+phones[0]+"' and password like '"+passwords[0]+"' ";
		MysqlConnector.set(updateQuery);
		
		String deleteQuery = "DELETE from users WHERE telephone like '"+phones[1]+"' and password like '"+passwords[0]+"'";
		MysqlConnector.set(deleteQuery);
	}
	
	@Test
	public void testGet() throws Exception {
		String insertQuery = "INSERT INTO users(telephone,password,name,family,age) VALUES('"+phones[0]+"','"+
				passwords[0]+"','"+
				names[0]+"','"+
				families[0]+"','"+
				ages[0]+"')";
		MysqlConnector.set(insertQuery);
		
		String selectQuery = "SELECT * FROM users WHERE telephone like '"+phones[0]+"' and password like '"+passwords[0]+"'";
		ResultSet set = MysqlConnector.get(selectQuery);
		
		if(!set.next()) {
			throw new Exception("inserted entry not found!");
		}
		
		String deleteQuery = "DELETE from users WHERE telephone like '"+phones[0]+"'";
		MysqlConnector.set(deleteQuery);
		
		set.close();
	}
}
