package main.general.database.mysql;

import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.general.database.mysql.MysqlConnector;

public class MysqlConnectorTest {
	private final String[] phones = new String[] {"1111","2222"};
	private final String[] passwords = new String[] {"aa11","bb22"};
	private final String[] names = new String[] {"abolfazl","alis"};
	private final String[] families = new String[] {"abolfazl","alis"};
	private final int[] ages = new int[] {12,14};
	
	@BeforeEach
	public void beforeEach() throws Exception {
		String deleteTemplate = "DELETE FROM users WHERE telephone like '%s'";
		for (String i : phones) {
			MysqlConnector.set(String.format(deleteTemplate, i));
		}
	}
	
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
	
	/**
	 * this method first insert two row then get them the id property must changed
	 * @throws Exception
	 */
	@Test
	public void testAutoIncrementOfId() throws Exception {
		String insertTemplate = "INSERT INTO users(telephone,password,name,family,age) VALUES('%s','%s','%s','%s','%s')";
		String selectTemplate = "SELECT id FROM users WHERE telephone='%s'";
		
		String insertQuery1 = String.format(insertTemplate, phones[0],passwords[0],names[0],families[0],ages[0]);
		MysqlConnector.set(insertQuery1);
		String insertQuery2 = String.format(insertTemplate, phones[1],passwords[1],names[1],families[1],ages[1]);
		MysqlConnector.set(insertQuery2);
		
		String selectQuery1 = String.format(selectTemplate, phones[0]);
		ResultSet set1 = MysqlConnector.get(selectQuery1);
		set1.next();
		
		String selectQuery2 = String.format(selectTemplate, phones[1]);
		ResultSet set2 = MysqlConnector.get(selectQuery2);
		set2.next();
		
		if(set1.getInt("id") >= set2.getInt("id"))
			throw new Exception("id wasn't increased");
	}
	
	@Test
	public void testUniqueOfTelephone() throws Exception {
		String insertTemplate = "INSERT INTO users(telephone,password,name,family,age) VALUES('%s','%s','%s','%s','%s')";
		// insert first element by telephone equals index 0
		String insertQuery1 = String.format(insertTemplate, phones[0],passwords[0],names[0],families[0],ages[0]);
		MysqlConnector.set(insertQuery1);
		// insert second element by telephone equals index 0 must handle it
		boolean isHasException = false;
		try {
			String insertQuery2 = String.format(insertTemplate, phones[0],passwords[1],names[1],families[1],ages[1]);
			MysqlConnector.set(insertQuery2);
		} catch (Exception e) {
			isHasException = true;
		}
		if(!isHasException) {
			throw new Exception("duplicated telephone elements added to database (telephone must be unique)");
		}
	}
	
	@Test
	public void testNotNullOfTelephone() throws Exception {
		String insertTemplate = "INSERT INTO users(password,name,family,age) VALUES('%s','%s','%s','%s')";
		boolean isHasException = false;
		try {
			String insertQuery2 = String.format(insertTemplate, passwords[1],names[1],families[1],ages[1]);
			MysqlConnector.set(insertQuery2);
		} catch (Exception e) {
			isHasException = true;
		}
		if(!isHasException) {
			throw new Exception("not null property doesn't attach to this column");
		}
	}
	
	@Test
	public void testNotNullOfPassword() throws Exception {
		String insertTemplate = "INSERT INTO users(telephone,name,family,age) VALUES('%s','%s','%s','%s')";
		boolean isHasException = false;
		try {
			String insertQuery2 = String.format(insertTemplate, phones[1],names[1],families[1],ages[1]);
			MysqlConnector.set(insertQuery2);
		} catch (Exception e) {
			isHasException = true;
		}
		if(!isHasException) {
			throw new Exception("not null property doesn't attach to this column");
		}
	}
	
	@Test
	public void testNotNullOfName() throws Exception {
		String insertTemplate = "INSERT INTO users(telephone,password,family,age) VALUES('%s','%s','%s','%s')";
		boolean isHasException = false;
		try {
			String insertQuery2 = String.format(insertTemplate, phones[1],passwords[1],families[1],ages[1]);
			MysqlConnector.set(insertQuery2);
		} catch (Exception e) {
			isHasException = true;
		}
		if(!isHasException) {
			throw new Exception("not null property doesn't attach to this column");
		}
	}
	
	@Test
	public void testNotNullOfFamily() throws Exception {
		String insertTemplate = "INSERT INTO users(telephone,password,name,age) VALUES('%s','%s','%s','%s')";
		boolean isHasException = false;
		try {
			String insertQuery2 = String.format(insertTemplate, phones[1],passwords[0],names[1],ages[1]);
			MysqlConnector.set(insertQuery2);
		} catch (Exception e) {
			isHasException = true;
		}
		if(!isHasException) {
			throw new Exception("not null property doesn't attach to this column");
		}
	}
	
	@Test
	public void testNotNullOfAge() throws Exception {
		String insertTemplate = "INSERT INTO users(telephone,password,name,family) VALUES('%s','%s','%s','%s')";
		boolean isHasException = false;
		try {
			String insertQuery2 = String.format(insertTemplate, phones[1],passwords[0],names[1],families[1]);
			MysqlConnector.set(insertQuery2);
		} catch (Exception e) {
			isHasException = true;
		}
		if(!isHasException) {
			throw new Exception("not null property doesn't attach to this column");
		}
	}
}
