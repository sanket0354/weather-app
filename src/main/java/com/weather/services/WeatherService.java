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

public class WeatherService {

	protected String url;
	protected Location location;
	protected String apiuxSecretKey;

	protected static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	protected JSONObject readJsonFromUrl() throws IOException, JSONException {
		String url = constructUrl();
//		System.out.println(url);
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
	
	public void setLocation(Location location){
		this.location = location;
	}

	protected String constructUrl() {
		return url.replace("{API_KEY}", apiuxSecretKey).replace("{LATITUDE}", String.valueOf(location.getLatitude()))
				.replace("{LONGITUDE}", String.valueOf(location.getLongitude()));
	}

}
