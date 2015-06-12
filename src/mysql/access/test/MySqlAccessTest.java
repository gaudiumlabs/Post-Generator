/**
 * 
 */
package mysql.access.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sriram
 *
 */
public class MySqlAccessTest {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	//this simple function does all the work

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {										
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
		catch (Exception E){
			//ignore. This exception can happen if the database was not previously created
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
		preparedStatement.executeUpdate();
			
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
		statement.executeUpdate("drop database feedback");
		close();
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
	}//end function close();
	
	private String writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		String returnString = null;
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
			
			returnString = user + " "+ website +" "+ summary +" "+ date +" "+ comment;					
		}
		return returnString;
	}//end of function writeResultSet
	

	@Test
	public void test() {
		String createFirstEntry = new String("INSERT INTO comments values (default, 'lars', 'myemail@gmail.com','http://www.vogella.com', '2009-09-14 10:33:11', 'Summary','My first comment');");
		try{
			preparedStatement = connect.prepareStatement(createFirstEntry);
			preparedStatement.executeUpdate();
			resultSet = statement
					.executeQuery("select * from feedback.comments");						
			assertEquals(writeResultSet(resultSet),"lars http://www.vogella.com Summary 2009-09-14 My first comment");			
		}
		catch (Exception e){
			fail("Test 1 fail: Inserting into database. Exception is "+ e);
		}
	}//end of test function
	
	@Test
	public void test2(){
		
	}
	
}
