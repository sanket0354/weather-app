package com.weather.services;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.constants.ServiceConstants;
import com.weather.model.Location;
import com.weather.model.Source;
import com.weather.model.WeatherLog;

import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.DarkSkyClient;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

public class DarkSkyWeatherService extends WeatherService {

	public DarkSkyWeatherService() {
		super.url = "https://api.darksky.net/forecast/{API_KEY}/{LATITUDE},{LONGITUDE}?units=si&exclude=minutely,hourly,daily,alerts,flags";
		super.apiuxSecretKey = "080bbcfe5e94c9c33a0f763255d1fabf";

	}

	public DarkSkyWeatherService(Location location) {
		this();
		super.location = location;
	}

	public WeatherLog constructWeatherLog() {
		WeatherLog weatherLog = null;

		try {
			JSONObject jsonObject = super.readJsonFromUrl();

			JSONObject currentWeather = jsonObject.getJSONObject("currently");
			double temperature = currentWeather.getDouble("temperature");
			double windSpeed = currentWeather.getDouble("windGust");
			String condition = currentWeather.getString("summary");

			weatherLog = new WeatherLog(temperature, windSpeed, condition, location, Source.SOURCE2);
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
