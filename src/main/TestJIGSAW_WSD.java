package main;

import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.SynsetID;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nlp.wsd.jigsaw.JIGSAW;
import nlp.wsd.jigsaw.data.TokenGroup;
import nlp.wsd.jigsaw.mwn.MWNapi_ext;
import nlp.wsd.jigsaw.mwn.MultiWordNet;
import nlp.wsd.jigsaw.utils.DBAccess;

public class TestJIGSAW_WSD {

	public static final String TEST_PROPERTIES_FILENAME = "jigsaw-resources/jigsaw-test-eclipse.properties";
	// public static final String TEST_INPUT =
	// "My friend is a baseball player and he hits the ball very hard with his bat when he swings.";
	public static final String TEST_INPUT = "Bats are nocturnal creatures that fly through the night and eat insects";

	public static void main(String[] args) {
		//simpleDemo();
		comparePOSTweetsFromCorpus();
	}

	private static void simpleDemo() {
				System.out.println("Successfully created JIGSAW object");

		runJIGSAWOnText(TEST_INPUT); // hardcore NLP

		System.out.println("Finished Successfully");

	}

	private static void runJIGSAWOnText(String inputText){
		/** Get a connection to Wordnet **/
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		// System.out.println("Trying path: " + path);
		URL url = null;
		try {
			url = new URL("file", null, path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// construct the wordnet dictionary object and open it
		IDictionary dict = new Dictionary(url);
		try {
			dict.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** Read in properties file for JIGSAW **/
		Properties props = new Properties();
		try {
			props.load(new FileReader(TEST_PROPERTIES_FILENAME));

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** Create JIGSAW object and initialize **/
		JIGSAW jigsaw = new JIGSAW(new File(TEST_PROPERTIES_FILENAME));

		TokenGroup tg = null;
		try {
			tg = jigsaw.mapText(inputText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < tg.size(); i++) {
			String pos = tg.get(i).getPosTag();
			String stem = tg.get(i).getStem();
			String predictedSynsetOffset = tg.get(i).getSyn();
			System.out.print("[word=" + tg.get(i).getToken().toString() + "] ");
			// System.out.print(tg.get(i).getStem().toString() + " ");
			System.out.print("[pos=" + tg.get(i).getPosTag().toString() + "] ");
			// System.out.print(tg.get(i).getLemma().toString() + " ");
			// System.out.print(tg.get(i).getSyn().toString()+" ");
			try {
				System.out.print("[meaning="
						+ myGetGloss(dict, pos, stem, predictedSynsetOffset)
						+ "]");
			} catch (Exception e) {
				System.out.println("Error w/ Gloss");
			}
			System.out.println();
			// System.out.println("");

		}

	}

	private static String myGetGloss(IDictionary dict, String pos, String stem,
			String synsetOffset) throws Exception {
		IIndexWord indexWordVar = null;
		String gloss = "";
		try {
			indexWordVar = dict.getIndexWord(stem,
					getWordnetPOSFromJIGSAWTag(pos));
		} catch (Exception e) {
		}
		if (indexWordVar != null) {
			// System.out.println("  [indexWordVar] "+indexWordVar.toString());
			// System.out.println("  [corresponding word ids]");
			for (IWordID wordID : indexWordVar.getWordIDs()) {
				String currSynsetOffset = String.valueOf(dict.getWord(wordID)
						.getSynset().getOffset());
				if (currSynsetOffset.equals(synsetOffset)) {

					if (!gloss.equals("")) {
						throw new Exception(
								"More than one synset match for word " + stem);
					} else {
						gloss = dict.getWord(wordID).getSynset().getGloss();
					}
				}
			}
		}
		return gloss;
	}

	private static POS getWordnetPOSFromJIGSAWTag(String tag) {
		switch (tag) {
		case "n":
			return POS.NOUN;
		case "a":
			return POS.ADJECTIVE;
		case "v":
			return POS.VERB;
		default:
			return null;
		}
	}

	private static String  getStanfordCodeFromJIGSAWTag(String tag) {
		switch (tag) {
		case "n":
			return "NN";
		case "a":
			return "JJ";
		case "v":
			return "VB";
		default:
			return null;
		}
	}

	/**
	 * Read some tagged tweets from twitter corpus and compare against JIGSAW ACCURACY
	 */
	private static void comparePOSTweetsFromCorpus() {
		// read in some tweets
		BufferedReader tweetsFileReader = null;
		try {
			tweetsFileReader = new BufferedReader(new FileReader(new File("data/twitter_bootstrap_corpus/gate_twitter_bootstrap_corpus.1543K.tokens")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// read a few lines from the file
		int numTweetsToRead = 10;
		ArrayList<String> taggedTweets = new ArrayList<String>();
		ArrayList<String> untaggedTweets = new ArrayList<String>();
		for (int i = 0; i < numTweetsToRead; i++) {
			String currUntaggedTweet = "";
			try {
				String line = tweetsFileReader.readLine();
				taggedTweets.add(line);
				System.out.println("tweet from file: "+line);
				String[] tokens = line.split("\\s");
				//System.out.print("    tokens are");
				for (String token : tokens) {
					//System.out.print(" "+token);
				}
				//System.out.println();
				//System.out.print("new tokens are");
				for (String token : tokens) {
					int underscoreIndex = token.indexOf("_");
					if (underscoreIndex >= 0) {
						// there exists an underscore
						String word = token.substring(0,underscoreIndex);
						String stanfordTag = token.substring(underscoreIndex+1, token.length());
						//System.out.print(" "+word+"+"+stanfordTag);
						currUntaggedTweet += word + " ";

					}
				}
				untaggedTweets.add(currUntaggedTweet);
				runJIGSAWOnText(currUntaggedTweet);
				System.out.println("untagged tweet: "+currUntaggedTweet);
				//System.out.println();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
