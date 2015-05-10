package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

//import com.restfb.DefaultFacebookClient;
//import com.restfb.Facebook;
//import com.restfb.FacebookClient;
//import com.restfb.exception.FacebookException;
//import com.restfb.types.Page;
//import com.restfb.types.User;

import java.util.Date;

import com.google.gson.Gson;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.GeoLocation;
import facebook4j.Place;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class SaveJSONFromFB {

	// private static FacebookClient facebookClient = null;

	private static final String accessTokenStr = "CAACEdEose0cBAGRmLtjz3mTIZA5N8VHTRni5PVHEfR7uiQngXufcPhAPrDlIlGN2Jlsy6NrGXrNVNPEDbBNf1nGmZClUElL16ugUZCZBOwm3vA1XEVgscyJnZCEhlUDymy8jjurfgjq9S94VbWH5WQ6L9io2VELvi1OR0uHNyjNHhlEMk4fbkedbxZBN0vbTukDelZAGZCkuI6pJqBbSkquMa7ZBqcZAuXjeAZD";

	public static void main(String[] args) throws FacebookException {
		useFacebook4j();
	}

	private static void useFacebook4j() throws FacebookException {
		Facebook facebook = new FacebookFactory().getInstance();
		// Use default values for oauth app id.
		// facebook.setOAuthAppId("", "");// SUPER IMPORTANT
		// ?? ONLY DONE ONCE ?? WHAT IS IT ASSOCIATED TO ? MY LOGIN?

		// Get an access token from:
		// https://developers.facebook.com/tools/explorer
		// Copy and paste it below.

		// Create a new data file
		File dataFile = new File("data");
		if (!dataFile.exists()) {
			if (dataFile.mkdir()) {
				System.out.println("Directory " + dataFile.getAbsolutePath()
						+ " is created!");
			} else {
				System.out.println("Failed to create directory: "
						+ dataFile.getAbsolutePath());
			}
		}

		File postsDataFile = new File("data/posts.txt");
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(postsDataFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			String accessTokenString = "CAACEdEose0cBAON6W8uFZAS1o5QZAsPnofZAJ8xEzsCRgRZAgU9gC0R5LP2f2LGf3WxRZARyBl0k24VucSoyKrPSXOXqIFsEmAm1P16yQ4CTttVpOlZCCXFy1ZAAvHQXFk03TDATka8hqR9A24ZC8cHOjrZAWRnITKtqwX0uF3cmrFvj8I17bwzYbBwa1MYEQDGiVweE3Ec2Fl8laBCXTGpMWbbXLy0UlE6IZD";
			AccessToken at = new AccessToken(accessTokenString);
			// Set access token.

			facebook.setOAuthAccessToken(at);

			System.out.println("AccessToken seems to be valid");

			// Search by name
			// ResponseList<Place> results = facebook.searchPlaces("coffee");

			// You can narrow your search to a specific location and distance
			// new york city
			GeoLocation center = new GeoLocation(40.688215, -73.939856);
			int distance = 49999;

			String searchTerm = "coffee";
			ResponseList<Place> results = facebook.searchPlaces(searchTerm,
					center, distance);

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String dateTimeStr = dateFormat.format(date);
			writer.append("==== Search: " + searchTerm + " on " + dateTimeStr
					+ "   ====\n");

			int count = 0;
			for (Place place : results) {

				
				
				writer.append("[PLACE] " + place.getName() + "\n");

				// appendNewLineToFile(place.getName(),
				// "FacebookSearchResults.txt");
				count++;

				ResponseList<Post> feed = facebook.getFeed(place.getId());
				int postCount = 0;
				for (Post post : feed) {
					// System.out.println("\t[POST] " + post.getMessage());
					
					if (post.getMessage() != null && !post.getMessage().toString().contains("null")) {
						writer.append("\t[POST] " + post.getMessage() + "\n");
						// appendNewLineToFile(post.getMessage(),
						// "FacebookSearchResults.txt");
						// appendNewLineToFile("=========END OF POST.",
						// "FacebookSearchResults.txt");
						postCount++;
						if (postCount == 1000) {
							break;
						}
					}
				}
				if (count == 1000)
					break;
			}// end for loop through the places

			writer.flush();
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void useRestFB() {
	// facebookClient = new DefaultFacebookClient(accessToken);
	//
	// fetchObjects();
	//
	// }
	//
	// static void fetchObjects() throws FacebookException {
	// System.out.println("* Fetching multiple objects at once *");
	//
	// FetchObjectsResults fetchObjectsResults = facebookClient.fetchObjects(
	// Arrays.asList("me", "cocacola"), FetchObjectsResults.class);
	//
	// System.out.println("User name: " + fetchObjectsResults.me.getName());
	// System.out.println("Page fan count: "
	// + fetchObjectsResults.page.getLikes());
	// }
	//
	// /**
	// * Holds results from a "fetchObjects" call.
	// */
	// public static class FetchObjectsResults {
	// @Facebook
	// User me;
	//
	// @Facebook(value = "cocacola")
	// Page page;
	// }

	// }
	
	private static void getPostJSONStr(Post post, File jsonFile) {
		Gson gson = new Gson();
		String json = gson.toJson(post);
	}
	
	private static ArrayList<Post> getPostsFromJSONFile(File jsonFile) {
		ArrayList<Post> posts = new ArrayList<Post>();
		
		return posts;
	}
}