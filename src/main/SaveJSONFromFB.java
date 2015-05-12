package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class SaveJSONFromFB {

	private static final String accessTokenStr = "CAACEdEose0cBAMNEY4ksqE5AhOIcQwpRFH7yZARPFqptdZA1I7r7aOfTnjYOruc8mkyxmv4ZAeNIvbcIt8IWlPvODSNElmZAxBZBJxxZBIUT9mgptAqdcXNnBrO5VyKoDvnbgu96SAksibXZA1RvhsmA6VjWSFAIaCZBG3CL2ob2n5ZCiE8jEVo1SwsG5vD1ZCU0ctPYQxEzbSKwZCBWTf4dna6ZCJQAxiR30DsZD";

	public static void main(String[] args) throws FacebookException {
		// Get facebook object
		Facebook facebook = new FacebookFactory().getInstance();
		AccessToken at = new AccessToken(accessTokenStr);
		facebook.setOAuthAccessToken(at);

		// post searching information
		GeoLocation loc = new GeoLocation(40.688215, -73.939856);
		int distance = 49999;

		// run the search
		ArrayList<Post> coffeePostsFromNYC = getPostsFromSearch(facebook,
				"coffee", loc, distance);

		// save posts in json format to file
		String jsonFileName = "data" + File.separator + "posts.json";
		postsToJSONFile(coffeePostsFromNYC, jsonFileName);

		// read them back in
		ArrayList<Post> postsFromJSONFile = postsFromJSONFile(new File(
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
	}

	private static ArrayList<Post> getPostsFromSearch(Facebook facebook,
			String keyword, GeoLocation loc, int dist) throws FacebookException {
		ArrayList<Post> posts = new ArrayList<Post>();

		// a few safety parameters so we don't download all of facebook
		int maxPlaceCount = 10;
		int maxPostCountPerPlace = 5;

		// run search on facebook
		ResponseList<Place> results = facebook.searchPlaces(keyword, loc, dist);

		// get posts per place
		int placeCount = 0;
		for (Place place : results) {
			ResponseList<Post> feed = facebook.getFeed(place.getId());

			int postCount = 0;
			for (Post post : feed) {
				posts.add(post);

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

	private static String postToJSONStr(Post post, Gson gson) {
		return gson.toJson(post);
	}

	private static void postsToJSONFile(ArrayList<Post> posts,
			String jsonFileName) {
		File jsonFile = new File(jsonFileName);
		Gson gson = new Gson();

		try {
			BufferedWriter jsonFileWriter = new BufferedWriter(new FileWriter(
					jsonFile));
			for (Post post : posts) {
				jsonFileWriter.append(postToJSONStr(post, gson) + "\n");
			}
			jsonFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<Post> postsFromJSONFile(File jsonFile) {
		ArrayList<Post> posts = new ArrayList<Post>();
		Gson gson = new Gson();
		
		try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	posts.add(gson.fromJson(line, Post.class));
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
}