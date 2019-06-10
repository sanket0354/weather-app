package com.weather.constants;

public class ServiceConstants {

	// Google Geo Coding API key to get latitude/longitude of a place
	public static final String GEO_CODING_API_KEY = "AIzaSyCgDUN9ngqKBAE29TeaBIolS_KG9lVoIok";
	

	public static final String OPEN_WEATHER_MAP_API_KEY = "386d6fad9fad06c56d0ed4e67bb36f3c";
	public static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat={LATITUDE}&lon={LONGITUDE}&appId={API_KEY}&units=metric";
	
	public static final String DARK_SKY_API_KEY = "080bbcfe5e94c9c33a0f763255d1fabf";
	public static final String DARK_SKY_URL = "https://api.darksky.net/forecast/{API_KEY}/{LATITUDE},{LONGITUDE}?units=si&exclude=minutely,hourly,daily,alerts,flags";

	public static final String APIUX_API_KEY = "e3269e9700b749959dd184144190606";
	public static final String APIUX_URL = "http://api.apixu.com/v1/current.json?key={API_KEY}&q={LATITUDE},{LONGITUDE}";
	
}
