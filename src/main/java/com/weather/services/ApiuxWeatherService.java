package com.weather.services;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.model.Location;
import com.weather.model.Source;
import com.weather.model.WeatherLog;

public class ApiuxWeatherService extends WeatherService {

	public ApiuxWeatherService() {
		super.url = "http://api.apixu.com/v1/current.json?key={API_KEY}&q={LATITUDE},{LONGITUDE}";
		super.apiuxSecretKey = "e3269e9700b749959dd184144190606";
	}

	public ApiuxWeatherService(Location location) {
		this();
		super.location = location;
	}

	public WeatherLog constructWeatherLog() {
		WeatherLog weatherLog = null;
		try {
			JSONObject jsonObject = super.readJsonFromUrl();

			JSONObject currentWeather = jsonObject.getJSONObject("current");
			double temperature = currentWeather.getDouble("temp_c");
			double windSpeed = currentWeather.getDouble("wind_kph");
			String condition = currentWeather.getJSONObject("condition").getString("text");

			weatherLog = new WeatherLog(temperature, windSpeed, condition, location, Source.SOURCE1);

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