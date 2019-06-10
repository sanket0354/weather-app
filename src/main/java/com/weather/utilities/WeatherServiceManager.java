package com.weather.utilities;

import com.weather.services.ApiuxWeatherService;
import com.weather.services.DarkSkyWeatherService;
import com.weather.services.OpenWeatherMapService;

/**
 * This is also helper class that initializes the weather service and then you
 * can retrieve the service objects to make calls to api
 * 
 * @author Sanket.Patel
 *
 */
public class WeatherServiceManager {

	private static ApiuxWeatherService apiuxWeatherService;
	private static DarkSkyWeatherService darkSkyWeatherService;
	private static OpenWeatherMapService openWeatherMapService;

	public static void initializeWeatherService() {
		createApiuxWeatherService();
		createDarkSkyWeatherService();
		createOpenWeatherMapService();
	}

	private static void createApiuxWeatherService() {
		apiuxWeatherService = new ApiuxWeatherService();
	}

	private static void createDarkSkyWeatherService() {
		darkSkyWeatherService = new DarkSkyWeatherService();
	}

	private static void createOpenWeatherMapService() {
		openWeatherMapService = new OpenWeatherMapService();
	}

	public static ApiuxWeatherService getApiuxWeatherService() {
		return apiuxWeatherService;
	}

	public static DarkSkyWeatherService getdarkSkyWeatherService() {
		return darkSkyWeatherService;
	}

	public static OpenWeatherMapService getopenWeatherMapService() {
		return openWeatherMapService;
	}

}
