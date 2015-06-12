
package main;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import mysql.access.MySQLAccess;
import mysql.access.test.AllMySqlTests;

//import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Class to practice using the wordnet api
 * 
 * @author Dustin Dannehauer and Sriram Gopalakrishnan
 * 
 */
public class Main {

	public static void main(String[] args) {
		
		//this will run the core test suite for components like the sql database
		if (initializeAndCoreTest() == false){
			System.out.println("main ended as the initialization tests failed");	
		}
		
		

		//FOLLOW THE TUTORIAL AT http://www.vogella.com/tutorials/MySQLJava/article.html"
		// AND http://www.vogella.com/tutorials/MySQL/article.html
		//READ "DatabaseLearning.txt notes under main.Notes package.
		
		
	}


	/**
	 * initializeAndCoreTest initalizes and checks if all the pieces of the software are in 
	 * place and running
	 * @return boolean indicating the success or failure of the initalization
	 */
	public static boolean initializeAndCoreTest(){
		boolean returnValue;
		Result sqlTestResult = JUnitCore.runClasses(AllMySqlTests.class);
		if(sqlTestResult.getFailureCount() == 0){
			returnValue = true;
		}
		else{
			returnValue = false;
		}
		return returnValue;

	}//end of function initializeAndCoreTest()

}