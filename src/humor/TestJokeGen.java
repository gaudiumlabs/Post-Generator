package humor;

import standup.joke.*;
import standup.profiling.ProfileException;
import standup.profiling.ProfileManager;
import standup.utils.CommandLineArguments;

/**
 * A simple program that shows how to use the STANDUP riddle generator API functionality.
 * <p>
 * You may have to specify a larger heap size for the JVM, e.g.
 * <p>
 * {@code java -Xms384m -Xmx384m TestJokeGen}
 * 
 * @author <a href="mailto:ruli.manurung@ed.ac.uk">Ruli Manurung</a>
 */
public class TestJokeGen
{

    public static void main(String[] args) throws ProfileException,GeneratorException
    {
        // Process any command line arguments
        CommandLineArguments.parseCommandLineArguments(args);

        // Create a new Backend
        Backend myBackend = new BackendJokeSetOnly();
        
        // Initialize the profile manager and create a new profile
        ProfileManager.initialize();
        // TODO if this is the first time, uncomment the following line
        //ProfileManager.createUser("username",myBackend);
        ProfileManager.useProfile("username",myBackend);
        
        // Generate a 'new' joke
        JokeStructure myJoke = myBackend.getNewJoke();
        
        // Print out the question and answer
        System.out.println(myJoke.getQuestion());
        System.out.println(myJoke.getAnswer());
        
        // Add the joke to the user's favourites
        myBackend.addToFavourites(myJoke);
        
        // Save the profile
        ProfileManager.saveCurrentProfile(myBackend);
    }
}
