package com.weather.services;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.constants.ServiceConstants;
import com.weather.model.Location;
import com.weather.model.WeatherLog.SourceType;
import com.weather.model.WeatherLog;

/**
 * class extends the WeatherService class to implement reading of data from Open
 * Weather api
 * 
 * @author Sanket.Patel
 *
 */
public class OpenWeatherMapService extends WeatherService {

	public OpenWeatherMapService() {
		super.url = ServiceConstants.OPEN_WEATHER_MAP_URL;
		super.apiKey = ServiceConstants.OPEN_WEATHER_MAP_API_KEY;
	}

	public OpenWeatherMapService(Location location) {
		this();
		super.location = location;
	}

	/**
	 * construct a WeatherLog object from the json data read from the API
	 * @return Weather log object
	 */
	public WeatherLog constructWeatherLog() {
		WeatherLog weatherLog = null;

		try {
			JSONObject jsonObject = super.readJsonFromUrl();

			double temperature = jsonObject.getJSONObject("main").getDouble("temp");
			double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
			String condition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

			weatherLog = new WeatherLog(temperature, windSpeed, condition, location, SourceType.SOURCE3);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return weatherLog;
	}
}
