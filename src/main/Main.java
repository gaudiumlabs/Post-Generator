
package main;
import mysql.access.MySQLAccess;

//import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Class to practice using the wordnet api
 * 
 * @author dustin
 * 
 */
/**
 * @author Sriram
 *
 */
/**
 * @author Sriram
 *
 */
public class Main {

	public static void main(String[] args) {

		initializeAndCoreTest();

		//FOLLOW THE TUTORIAL AT http://www.vogella.com/tutorials/MySQLJava/article.html"
		// AND http://www.vogella.com/tutorials/MySQL/article.html
		//READ "DatabaseLearning.txt notes under main.Notes package.
		System.out.println("SQL testing, if your mySQL database is setup, this should"
				+ "return a valid sql response. If not, you will get a sql error\n"
				+ " Edit the test Sql function to play with it");
		testSqlAccess();//dummy test function that should be removed later
	}


	/**
	 * Dummy function to test the sql access class
	 * Should not be there when the actual code prototype exists
	 * @author - Ram
	 * @deprecated
	 * 
	 */
	private static void testSqlAccess(){

		MySQLAccess sqlAccessor = new MySQLAccess();
		try{
			sqlAccessor.readDataBase();
		}
		catch (Exception E){
			System.out.println("SQL exception happened " + E);
		}

	}//end function testSqlAccess


	/**
	 * initializeAndCoreTest initalizes and checks if all the pieces of the software are in 
	 * place and running
	 * @return boolean indicating the success or failure of the initalization
	 */
	public static boolean initializeAndCoreTest(){
		return true;
	}//end of function initializeAndCoreTest()

}