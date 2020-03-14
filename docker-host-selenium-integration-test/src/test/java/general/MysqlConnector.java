package general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import general.ConfigReader;

public class MysqlConnector {
	// then throw a exception by a better message
	private static final String DATA_BASE_ADDRESS = "jdbc:mysql://localhost:3306/dockerhost";
	private static final String USER = ConfigReader.getDatabaseUserName();
	private static final String PASSWORD = ConfigReader.getDatabasePassword();

	/**
	 * this method include execute insert,delete,update queries
	 * 
	 * @param query a query to execute like insert delete or update
	 * @throws Exception throw any connection/execute command exception
	 */
	public static void set(String query) throws Exception {
		Connection con = DriverManager.getConnection(DATA_BASE_ADDRESS, USER, PASSWORD);
		Statement st = con.createStatement();
		st.executeUpdate(query);
		st.close();
		con.close();
	}

	/**
	 * like the above method
	 * {@link main.general.database.mysql.MysqlConnector#set(String)} but just is
	 * used for select query
	 * 
	 * @param query query to execute (just select query)
	 * @return return a result set of database response
	 * @throws Exception throw any exception on execute query or connecting to
	 *                   database
	 */
	public static ResultSet get(String query) throws Exception {
		Connection con = DriverManager.getConnection(DATA_BASE_ADDRESS, USER, PASSWORD);
		Statement st = con.createStatement();
		ResultSet set = st.executeQuery(query);

		return set;
	}
}