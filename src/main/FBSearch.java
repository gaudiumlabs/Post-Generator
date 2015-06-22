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

//import org.codehaus.jackson.map.ObjectMapper;

//import com.restfb.DefaultFacebookClient;
//import com.restfb.Facebook;
//import com.restfb.FacebookClient;
//import com.restfb.exception.FacebookException;
//import com.restfb.types.Page;
//import com.restfb.types.User;

import java.util.Date;

import json.FBPost;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import sun.security.action.GetLongAction;
import test.InsertPostsToMySQL;

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
public class FBSearch {

	private static final String accessTokenStr = "CAACEdEose0cBADFDnw9OhWju8eenj7naXQYuC1PqrTKYmAD0MJiGhXvUzP297Tg0hxewpgpIN5kHRMjdGWUOf2iViHZB2XY9gaU1kI6UuKjmaBKveu5BVPFrGB27iBxHIGoFw7GTtqttO1ytPZBSjFvWiuAr6ZBayv3TGxOmIjfsowbFYi7OWKKflo46xQoNyF4dNih9n0RxVHGkpM6ErZBJXy6wu0wZD";

	public static void main(String[] args) throws FacebookException {
		// Get facebook object
		Facebook facebook = new FacebookFactory().getInstance();
		AccessToken at = new AccessToken(accessTokenStr);
		facebook.setOAuthAccessToken(at);

		// post searching information
		GeoLocation loc = new GeoLocation(40.688215, -73.939856);
		int distance = 49999;

		// run the search
		ArrayList<FBPost> coffeePostsFromNYC = getPostsFromSearch(facebook,
				"coffee", loc, distance);

		// filter any posts that don't have messages
		ArrayList<FBPost> nullPostsToRemove = new ArrayList<FBPost>();
		for (FBPost post : coffeePostsFromNYC) {
			if (post.getMessage() == null) {
				nullPostsToRemove.add(post);
			}
		}

		for (FBPost post : nullPostsToRemove) {
			coffeePostsFromNYC.remove(post);
			System.out.println("Removed post");
		}

		// add them to the database
		InsertPostsToMySQL.savePostsToDatabase(coffeePostsFromNYC);

		// save posts in json format to file
		// String jsonFileName = "data" + File.separator + "posts.json";
		// postsToJSONFile(coffeePostsFromNYC, jsonFileName);
		//
		// // read them back in
		// ArrayList<FBPost> postsFromJSONFile = postsFromJSONFile(new File(
		// jsonFileName));
		//
		// // do we get the exact same objects? if not, report an error
		// for (int i = 0; i < coffeePostsFromNYC.size(); i++) {
		// if (!postsFromJSONFile.get(i).equals(coffeePostsFromNYC.get(i))) {
		// System.err.println("==== [Post " + Integer.toString(i)
		// + " does not match] ==== ");
		// System.err.print("[BEFORE] " + postsFromJSONFile.get(i));
		// System.err.print("[AFTER] " + coffeePostsFromNYC.get(i));
		// }
		// }

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
				FBPost localPost = new FBPost();

				localPost.setMessage(post.getMessage());
				localPost.setLikeCount(post.getLikes().size());
				if (post.getSharesCount() != null) {
					localPost.setShareCount(post.getSharesCount());
				}
				// localPost.setDate(post.getCreatedTime());
				localPost.setSource(post.getName());

				posts.add(localPost);

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
}