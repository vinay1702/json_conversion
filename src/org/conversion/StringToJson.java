/**
 * 
 */
package org.conversion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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
			}
			profileObj.put("followers", followerJSONArray);
			System.err.println(profileObj);
			writeToFile(profileObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeToFile(JSONObject profileObj) {
		try (FileWriter file = new FileWriter("src/jsonFile.json")) {

			file.write(profileObj.toString());
			file.flush();

		} catch (IOException e) {
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
				queryString = queryString + sCurrentLine;
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
}
