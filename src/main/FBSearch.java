package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

//import org.codehaus.jackson.map.ObjectMapper;

//import com.restfb.DefaultFacebookClient;
//import com.restfb.Facebook;
//import com.restfb.FacebookClient;
//import com.restfb.exception.FacebookException;
//import com.restfb.types.Page;
//import com.restfb.types.User;

import java.util.Date;
import java.util.Properties;

import json.FBPost;
import nlp.wsd.jigsaw.JIGSAW;
import nlp.wsd.jigsaw.data.TokenGroup;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import sun.security.action.GetLongAction;

import com.google.gson.Gson;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.GeoLocation;
import facebook4j.Place;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

/**
 * Store and retrieve facebook posts in JSON format to/from a text file.
 *
 * NOTE: Currently when posts are written to a file in JSON format, each Post
 * object is in JSON format but the entire file (i.e. posts.json) is technically
 * not legal json. To return the file of json Post objects to java classes, the
 * file is read line by line, where each line is a single Post object.
 *
 * @author dustin
 *
 */
public class FBSearch {

	private static final String accessTokenStr = "CAACEdEose0cBAMea5rZAJvzg7KpntLAXrBqu33j5lqDyjK7dYGZBgoMQmFkvZCfsCIUdfiGcX81YdpwdoeYTZBAdmT3W0YJUdVkHSIl7HWeEwuXNbt564p5O8X99sitYBXeG5AtN4s8bj1Rf7QhZAB2xwmQMtQnGP2IuX1gO4PPF4KRBWchHZBzOnB5zhJkImICxtLvQ4PnZAmYvoIlZATtWSREA3BN8UwcZD";

	public static void main(String[] args) throws FacebookException {
		// Get facebook object
		Facebook facebook = new FacebookFactory().getInstance();
		AccessToken at = new AccessToken(accessTokenStr);
		facebook.setOAuthAccessToken(at);

		// post searching information
		GeoLocation loc = new GeoLocation(40.688215, -73.939856);
		int distance = 49999;

		// run the search
		System.out.println("Searching Facebook...");
		ArrayList<FBPost> coffeePostsFromNYC = getPostsFromSearch(facebook,
				"coffee", loc, distance);

		// run and print Jigsaw WSD analysis
		System.out.println("Running JIGSAW WSD...");
		IDictionary wordnetDict = initializeWordnetDict();
		JIGSAW jigsawWSD = initializeJigsaw();
		for (int i = 0; i < coffeePostsFromNYC.size(); i++) {
			System.out.println("--------------- Post "+ i + " ---------------");
			if (coffeePostsFromNYC.get(i) != null && coffeePostsFromNYC.get(i).getMessage() != null) {
				System.out.println(coffeePostsFromNYC.get(i).getMessage());
				printJigsawWSDAnalysis(jigsawWSD, wordnetDict, coffeePostsFromNYC.get(i).getMessage());
			}
			System.out.println();
		}

		// save posts in json format to file
		String jsonFileName = "data" + File.separator + "posts.json";
		postsToJSONFile(coffeePostsFromNYC, jsonFileName);

		// read them back in
		ArrayList<FBPost> postsFromJSONFile = postsFromJSONFile(new File(
				jsonFileName));

		// do we get the exact same objects? if not, report an error
		for (int i = 0; i < coffeePostsFromNYC.size(); i++) {
			if (!postsFromJSONFile.get(i).equals(coffeePostsFromNYC.get(i))) {
				System.err.println("==== [Post " + Integer.toString(i)
						+ " does not match] ==== ");
				System.err.print("[BEFORE] " + postsFromJSONFile.get(i));
				System.err.print("[AFTER] " + coffeePostsFromNYC.get(i));
			}
		}

		System.out.println("++++ End of Program ++++");
	}

	private static ArrayList<FBPost> getPostsFromSearch(Facebook facebook,
			String keyword, GeoLocation loc, int dist) throws FacebookException {
		ArrayList<FBPost> posts = new ArrayList<FBPost>();

		// a few safety parameters so we don't download all of facebook
		int maxPlaceCount = 100;
		int maxPostCountPerPlace = 50;

		// run search on facebook
		ResponseList<Place> results = facebook.searchPlaces(keyword, loc, dist);

		// get posts per place
		int placeCount = 0;
		for (Place place : results) {
			ResponseList<Post> feed = facebook.getFeed(place.getId());

			int postCount = 0;
			for (Post post : feed) {
				posts.add(new FBPost(post.getMessage(), post.getLikes().size()));

				postCount++;
				if (postCount == maxPostCountPerPlace) {
					break;
				}
			}

			placeCount++;
			if (placeCount == maxPlaceCount) {
				break;
			}
		}

		return posts;
	}

	private static String postToJSONStr(FBPost post, ObjectMapper mapper) {
		String result = "";
		try {
			result = mapper.writeValueAsString(post);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private static void postsToJSONFile(ArrayList<FBPost> posts,
			String jsonFileName) {
		File jsonFile = new File(jsonFileName);

		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();

		try {
			BufferedWriter jsonFileWriter = new BufferedWriter(new FileWriter(
					jsonFile));
			for (FBPost post : posts) {
				jsonFileWriter.append(postToJSONStr(post, mapper) + "\n");
			}
			jsonFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<FBPost> postsFromJSONFile(File jsonFile) {
		ArrayList<FBPost> posts = new ArrayList<FBPost>();
		ObjectMapper mapper = new ObjectMapper();

		try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				posts.add(mapper.readValue(line, FBPost.class));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return posts;
	}

	private static IDictionary initializeWordnetDict() {
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
		return dict;
	}

	private static JIGSAW initializeJigsaw() {
		final String TEST_PROPERTIES_FILENAME = "jigsaw-resources/jigsaw-test-eclipse.properties";

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
		return jigsaw;
	}

	private static void printJigsawWSDAnalysis(JIGSAW jigsaw, IDictionary dict, String input) {

		TokenGroup tg = null;
		try {
			tg = jigsaw.mapText(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < tg.size(); i++) {
			String word = tg.get(i).getToken().toString();
			String pos = tg.get(i).getPosTag();
			String stem = tg.get(i).getStem();
			String predictedSynsetOffset = tg.get(i).getSyn();

			// using JIGSAW get the gloss
			String gloss = "";
			try {
				gloss = myGetGloss(dict, pos, stem, predictedSynsetOffset);
			} catch (Exception e) {
				// for now do nothing
			}

			// only print out if the word has a meaning
			if (gloss.length() > 0) {
				System.out.print("[word=" + word + "] ");
				//System.out.print("[pos=" + pos + "] ");
				System.out.print("[meaning=" + gloss+"]");
				System.out.println();
			}
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
}