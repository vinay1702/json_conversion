/**
 * 
 */
package org.conversion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Vinay
 *
 */
public class StringToJson {

	private static final String FILENAME = "src/queryString.txt";

	public static void main(String[] args) throws UnsupportedEncodingException {

		try {
			String paramValue = getStringFromFile();

			JSONObject profileObj = new JSONObject();
			String[] mainObject = paramValue.split("\\*\\*");

			String profileStr = "";

			try {
				profileStr = mainObject[0];
			} catch (Exception e) {
			}

			String followers = "";
			try {
				followers = mainObject[1];
			} catch (Exception e) {
			}

			if (profileStr != null && !profileStr.equalsIgnoreCase("")) {
				profileObj = getJSONOBJECT(profileStr);
			}
			JSONArray followerJSONArray = new JSONArray();
			if (followers != null && !followers.equalsIgnoreCase("")) {
				String[] profiles = followers.split("\\@\\@");

				for (String follower : profiles) {
					JSONObject jsonObject2 = getJSONOBJECT(follower);
					followerJSONArray.put(jsonObject2);
				}
				//
			}
			profileObj.put("followers", followerJSONArray);

			System.err.println("profileObj----- " + profileObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JSONObject getJSONOBJECT(String firstMain) {

		JSONObject object = new JSONObject();
		object.put("id", firstMain.split("\\|")[1]);

		// name starts
		String nameString = firstMain.split("\\|")[2];
		String firstName = "";
		String secname = "";
		String third = "";
		String[] tokens = nameString.split("\\>");

		try {
			firstName = tokens[0].replaceAll("<", "");
		} catch (Exception e) {
		}
		try {
			secname = tokens[1].replaceAll("<", "");
		} catch (Exception e) {
		}
		try {
			third = tokens[2].replaceAll("<", "");
		} catch (Exception e) {
		}

		JSONObject nameObject = new JSONObject();
		nameObject.put("first", firstName);
		nameObject.put("middle", secname);
		nameObject.put("last", third);
		object.put("name", nameObject);
		// name ends

		// location starts
		String locString = null;
		try {
			locString = firstMain.split("\\|")[3];
		} catch (Exception e) {
		}
		JSONObject locObj = new JSONObject();
		JSONObject cordsJSON = new JSONObject();
		Double lat = 0.0, lon = 0.0;
		String name = "";

		if (locString != null && !locString.equalsIgnoreCase("")) {

			try {
				name = locString.split("\\>")[0].replaceAll("<", "");
			} catch (Exception e) {
			}

			try {
				String locCordsStr = locString.substring(locString.indexOf("<<") + 2, locString.indexOf(">>"));
				if (locCordsStr.length() > 3) {
					lon = Double.parseDouble(locCordsStr.split("><")[0] + "");
					lat = Double.parseDouble(locCordsStr.split("><")[1] + "");
				}
			} catch (Exception e) {
			}

		}

		locObj.put("name", name);

		cordsJSON.put("lat", lat);
		cordsJSON.put("long", lon);
		locObj.put("coords", cordsJSON);
		object.put("location", locObj);
		// location ends

		// image starts
		String ImageString = null;
		try {
			ImageString = firstMain.split("\\|")[4];
		} catch (Exception e) {

		}

		String imageName = "";
		if (ImageString != null && !ImageString.equalsIgnoreCase("")) {
			imageName = ImageString;
		}
		object.put("imageId", imageName);
		// image ends

		return object;
	}

	private static String getStringFromFile() {
		BufferedReader br = null;
		FileReader fr = null;
		String queryString = "";
		try {
			br = new BufferedReader(new FileReader(FILENAME));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				queryString = sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return queryString;
	}

	/*
	 * public static void splitSubQuery(Map<String, String> query_pairs2) throws
	 * UnsupportedEncodingException {
	 * 
	 * JSONObject profileObject = new JSONObject(); // String Encoded
	 * 
	 * String encodedString =
	 * java.net.URLEncoder.encode(query_pairs2.get("profile"), "UTF-8"); // split by
	 * | String[] splitByPipe = encodedString.split("%7C"); //
	 * profileObject.put("id", splitByPipe[0]); System.out.println("--------- " +
	 * encodedString);
	 * 
	 * String[] names = splitByPipe[1].split("%3E"); JSONObject nameJsonObj = new
	 * JSONObject(); nameJsonObj.put("first", names[0].replaceAll("%3C", ""));
	 * nameJsonObj.put("middle", names[1].replaceAll("%3C", ""));
	 * nameJsonObj.put("last", names[2].replaceAll("%3C", ""));
	 * 
	 * profileObject.put("name", nameJsonObj); String[] locs =
	 * splitByPipe[2].split("%3E"); JSONObject loc = new JSONObject();
	 * loc.put("name", locs[0].replaceAll("%3C", ""));
	 * 
	 * JSONObject coords = new JSONObject(); coords.put("long",
	 * locs[1].toString().replaceAll("%3C%3C", "")); coords.put("lat",
	 * locs[2].toString().replaceAll("%3C", ""));
	 * 
	 * loc.put("coords", coords);
	 * 
	 * profileObject.put("location", loc); profileObject.put("imageId",
	 * splitByPipe[3]);
	 * 
	 * encodedString = java.net.URLEncoder.encode(query_pairs2.get("followers"),
	 * "UTF-8"); System.out.println("-----> " + query_pairs2.get("followers"));
	 * 
	 * String[] followers = encodedString.split("%40%40"); JSONArray followersarray
	 * = new JSONArray();
	 * 
	 * for (String string : followers) { JSONObject followersObject = new
	 * JSONObject(); splitByPipe = string.split("%7C"); followersObject.put("id",
	 * splitByPipe[0]);
	 * 
	 * names = splitByPipe[1].split("%3E"); JSONObject followerJsonObj = new
	 * JSONObject(); followerJsonObj.put("first", names[0].replaceAll("%3C", ""));
	 * followerJsonObj.put("middle", names[1].replaceAll("%3C", ""));
	 * followerJsonObj.put("last", names[2].replaceAll("%3C", ""));
	 * 
	 * followersObject.put("name", followerJsonObj); locs =
	 * splitByPipe[2].split("%3E"); loc = new JSONObject(); loc.put("name",
	 * locs[0].replaceAll("%3C", ""));
	 * 
	 * coords = new JSONObject(); coords.put("long",
	 * locs[1].toString().replaceAll("%3C%3C", "")); coords.put("lat",
	 * locs[2].toString().replaceAll("%3C", ""));
	 * 
	 * loc.put("coords", coords);
	 * 
	 * followersObject.put("location", loc); followersObject.put("imageId",
	 * splitByPipe[3]); followersarray.put(followersObject);
	 * 
	 * }
	 * 
	 * profileObject.put("followers", followersarray);
	 * 
	 * System.out.println(profileObject.toString());
	 * 
	 * }
	 * 
	 * public static Map<String, String> splitQuery(String query) throws
	 * UnsupportedEncodingException { Map<String, String> query_pairs = new
	 * LinkedHashMap<String, String>(); String encodedString =
	 * java.net.URLEncoder.encode(query, "UTF-8"); encodedString =
	 * encodedString.replaceAll("\n", "").replaceAll("\t", ""); String[] pairs =
	 * encodedString.split("\\*\\*"); for (String pair : pairs) { int idx =
	 * pair.indexOf("%7C"); query_pairs.put(URLDecoder.decode(pair.substring(0,
	 * idx), "UTF-8"), URLDecoder.decode(pair.substring(idx), "UTF-8"));
	 * 
	 * // System.out.println(pair.substring(0, idx)+" >>> "+ pair.substring(idx +
	 * 3));
	 * 
	 * } return query_pairs; }
	 */
}
