package com.weather.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.model.Location;

/**
 * This is a superclass which will be used by three child class to implement api
 * specific methods to fetch the weather data
 * 
 * @author Sanket.Patel
 *
 */
public class WeatherService {

	/**
	 * url from which the weather data will be fetched
	 */
	protected String url;
	/**
	 * location for which the weather data needs to be fetched
	 */
	protected Location location;
	/**
	 * api key for the weather api
	 */
	protected String apiKey;

	protected static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * Read from the weather api url
	 * 
	 * @return the JSONObject of the weather data
	 * @throws IOException
	 * @throws JSONException
	 */
	protected JSONObject readJsonFromUrl() throws IOException, JSONException {
		String url = constructUrl();
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	/**
	 * Set the location
	 * 
	 * @param location current location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * construct the url by replacing the api key and location
	 * 
	 * @return the constructed api url to hit to get data
	 */
	protected String constructUrl() {
		return url.replace("{API_KEY}", apiKey).replace("{LATITUDE}", String.valueOf(location.getLatitude()))
				.replace("{LONGITUDE}", String.valueOf(location.getLongitude()));
	}

}
