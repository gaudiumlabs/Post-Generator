package mysql.access;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class MySQLAccess class that allows a connection to the mySQL server
 * currently just an example code as of version 1.0 may 10th 2015
 * @author Sriram
 * @version 1.0 May 10th 2015
 *
 */
public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	//this simple function does all the work
	
	@SuppressWarnings("deprecation")
	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost?"
							+ "user=root&password=Happymoney10");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			
			//Result set get the result of the SQL query
			int resultInt = 0;
			try{
				resultInt = statement.executeUpdate("DROP DATABASE feedback");
			}
			catch(Exception e){
				System.out.println(e);
			}
									
			 resultInt = statement										
					.executeUpdate("CREATE DATABASE feedback");			
			System.out.println(resultInt);
									
			resultSet = statement
					.executeQuery("use feedback");
			//writeResultSet(resultSet);
			
			String createTableStatement = new String("CREATE TABLE comments (id INT NOT NULL AUTO_INCREMENT," 
					+"MYUSER VARCHAR(30) NOT NULL,"
					+"EMAIL VARCHAR(30)," 
					+"WEBPAGE VARCHAR(100) NOT NULL," 
					+"DATUM DATE NOT NULL," 
					+"SUMMARY VARCHAR(40) NOT NULL,"
					+"COMMENTS VARCHAR(400) NOT NULL,"
					+"PRIMARY KEY (ID));");
	
			preparedStatement = connect.prepareStatement(createTableStatement);
						
			System.out.println(preparedStatement.toString());
			preparedStatement.executeUpdate();
			
			String createFirstEntry = new String("INSERT INTO comments values (default, 'lars', 'myemail@gmail.com','http://www.vogella.com', '2009-09-14 10:33:11', 'Summary','My first comment');");
			preparedStatement = connect.prepareStatement(createFirstEntry);
			preparedStatement.executeUpdate();
			
			
			resultSet = statement
					.executeQuery("select * from feedback.comments");
						
			writeResultSet(resultSet);			

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
			// "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
			// Parameters start with 1
			preparedStatement.setString(1, "Test");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "TestWebpage");
			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			preparedStatement.setString(5, "TestSummary");
			preparedStatement.setString(6, "TestComment");
			preparedStatement.executeUpdate();
			

			preparedStatement = connect
					.prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);			
			
			// Remove again the insert comment
			preparedStatement = connect
					.prepareStatement("delete from feedback.comments where myuser= ? ; ");
			preparedStatement.setString(1, "Test");
			preparedStatement.executeUpdate();

			resultSet = statement
					.executeQuery("select * from feedback.comments");
			writeMetaData(resultSet);
						
			statement.executeUpdate("drop database feedback");
			 			 			
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}
	/**
	 * @category Sql class
	 * @description writeMetaData Function 
	 * @param resultSet
	 * @throws SQLException
	 */
	private void writeMetaData(ResultSet resultSet) throws SQLException {
		//   Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summary = resultSet.getString("summary");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("summary: " + summary);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
}
