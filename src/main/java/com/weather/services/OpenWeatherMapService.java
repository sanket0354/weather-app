package com.weather.services;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weather.model.Location;
import com.weather.model.WeatherLog.SourceType;
import com.weather.model.WeatherLog;

public class OpenWeatherMapService extends WeatherService {

	public OpenWeatherMapService() {
		super.url = "http://api.openweathermap.org/data/2.5/weather?lat={LATITUDE}&lon={LONGITUDE}&appId={API_KEY}&units=metric";
		super.apiuxSecretKey = "386d6fad9fad06c56d0ed4e67bb36f3c";
	}

	public OpenWeatherMapService(Location location) {
		this();
		super.location = location;
	}

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
