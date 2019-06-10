package com.weather.services;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.constants.ServiceConstants;
import com.weather.model.Location;
import com.weather.model.WeatherLog.SourceType;
import com.weather.model.WeatherLog;

/**
 * class extends the WeatherService class to implement reading of data from Apiux Weather api 
 * @author Sanket.Patel
 *
 */
public class ApiuxWeatherService extends WeatherService {

	public ApiuxWeatherService() {
		super.url = ServiceConstants.APIUX_URL;
		super.apiKey = ServiceConstants.APIUX_API_KEY;
	}

	public ApiuxWeatherService(Location location) {
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

			JSONObject currentWeather = jsonObject.getJSONObject("current");
			double temperature = currentWeather.getDouble("temp_c");
			double windSpeed = currentWeather.getDouble("wind_kph");
			String condition = currentWeather.getJSONObject("condition").getString("text");

			weatherLog = new WeatherLog(temperature, windSpeed, condition, location, SourceType.SOURCE1);

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