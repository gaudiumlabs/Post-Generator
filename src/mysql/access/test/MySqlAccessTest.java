/**
 * 
 */
package mysql.access.test;

import mysql.access.MySQLAccess;

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		MySQLAccess sqlAccessor = new MySQLAccess();
		try{
			sqlAccessor.readDataBase();
		}
		catch (Exception E){
			System.out.println("SQL exception happened " + E);			
		}
		//fail("Not yet implemented");
	}

}
