package com.weather.services;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.constants.ServiceConstants;
import com.weather.model.Location;
import com.weather.model.WeatherLog.SourceType;
import com.weather.model.WeatherLog;

import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.DarkSkyClient;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

/**
 * class extends the WeatherService class to implement reading of data from Dark
 * Sky Weather api
 * 
 * @author Sanket.Patel
 *
 */
public class DarkSkyWeatherService extends WeatherService {

	public DarkSkyWeatherService() {
		super.url = ServiceConstants.DARK_SKY_URL;
		super.apiKey = ServiceConstants.DARK_SKY_API_KEY;

	}

	public DarkSkyWeatherService(Location location) {
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

			JSONObject currentWeather = jsonObject.getJSONObject("currently");
			double temperature = currentWeather.getDouble("temperature");
			double windSpeed = currentWeather.getDouble("windGust");
			String condition = currentWeather.getString("summary");

			weatherLog = new WeatherLog(temperature, windSpeed, condition, location, SourceType.SOURCE2);
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
