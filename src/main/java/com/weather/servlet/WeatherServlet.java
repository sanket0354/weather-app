package com.weather.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.weather.model.Location;
import com.weather.model.WeatherLog.SourceType;
import com.weather.model.WeatherLog;
import com.weather.modelController.LocationController;
import com.weather.modelController.WeatherLogController;
import com.weather.services.ApiuxWeatherService;
import com.weather.services.DarkSkyWeatherService;
import com.weather.services.GeoLocationService;
import com.weather.services.OpenWeatherMapService;
import com.weather.utilities.WeatherServiceManager;

@WebServlet(asyncSupported = true, urlPatterns = { "/weather-app/WeatherServlet" })
public class WeatherServlet extends HttpServlet {
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Get the city, province, country from the url parameters to locate the
		 * longitude and latitude
		 */
		String city = request.getParameter("city").toLowerCase();
		String province = request.getParameter("province").toLowerCase();
		String country = request.getParameter("country").toLowerCase();

		/*
		 * get the forceRefresh flag from url parameter. This will be used if
		 * the user requests the refresh of weather then we will fetch the
		 * latest data from api
		 * 
		 */
		boolean forceRefresh = Boolean.parseBoolean(request.getParameter("refresh"));

		// Get the co-ordinates from the google geo code service
		GeoLocationService geoLocationService = new GeoLocationService(city, province, country);
		Location location = geoLocationService.getCoOrdinates();

		JSONObject responseJson = processRequest(location, forceRefresh);

		PrintWriter sendResponse = response.getWriter();
		sendResponse.print(responseJson);
		sendResponse.flush();

	}

	/**
	 * process the user's request and build the JSON object
	 * 
	 * @param location
	 *            the location requested by user
	 * @param forceRefresh
	 *            refresh flag to actually fetch from weather api services
	 * @return the JSONObject to return to client
	 */
	public JSONObject processRequest(Location location, boolean forceRefresh) {
		LocationController locationController = new LocationController();
		WeatherLogController weatherLogController = new WeatherLogController();

		// check if the location data was already retrieved today for a
		// particular
		// co-ordinates
		Location dbLocation = locationController.findLocation(location.getLongitude(), location.getLatitude());

		ApiuxWeatherService apiuxWeatherService = WeatherServiceManager.getApiuxWeatherService();
		DarkSkyWeatherService darkSkyWeatherService = WeatherServiceManager.getdarkSkyWeatherService();
		OpenWeatherMapService openWeatherMapService = WeatherServiceManager.getopenWeatherMapService();

		/**
		 * if the location is not found in the database or user has requested
		 * the latest data then contact the api services to pull in the latest
		 * information else if the data is found in the database then return
		 * that to the user to minimize the api calls
		 */
		if (dbLocation == null || forceRefresh) {
			if (dbLocation == null) {
				dbLocation = locationController.addLocation(location.getLongitude(), location.getLatitude());
			}

			apiuxWeatherService.setLocation(dbLocation);
			darkSkyWeatherService.setLocation(dbLocation);
			openWeatherMapService.setLocation(dbLocation);

			WeatherLog firstSource = apiuxWeatherService.constructWeatherLog();
			WeatherLog secondSource = darkSkyWeatherService.constructWeatherLog();
			WeatherLog thirdSource = openWeatherMapService.constructWeatherLog();

			weatherLogController.addLog(firstSource);
			weatherLogController.addLog(secondSource);
			weatherLogController.addLog(thirdSource);

			return mergeJson(firstSource, secondSource, thirdSource);

		} else {
			WeatherLog retrivedLog1 = weatherLogController.findLastLogBasedOnLocationAndSourceAndDate(dbLocation,
					SourceType.SOURCE1);
			WeatherLog retrivedLog2 = weatherLogController.findLastLogBasedOnLocationAndSourceAndDate(dbLocation,
					SourceType.SOURCE2);
			WeatherLog retrivedLog3 = weatherLogController.findLastLogBasedOnLocationAndSourceAndDate(dbLocation,
					SourceType.SOURCE3);

			WeatherLog firstSource = null;
			WeatherLog secondSource = null;
			WeatherLog thirdSource = null;

			if (retrivedLog1 == null) {
				apiuxWeatherService.setLocation(dbLocation);
				firstSource = apiuxWeatherService.constructWeatherLog();
				weatherLogController.addLog(firstSource);
			} else {
				firstSource = retrivedLog1;
			}

			if (retrivedLog2 == null) {
				darkSkyWeatherService.setLocation(dbLocation);
				secondSource = darkSkyWeatherService.constructWeatherLog();
				weatherLogController.addLog(secondSource);
			} else {
				secondSource = retrivedLog2;
			}

			if (retrivedLog3 == null) {
				openWeatherMapService.setLocation(dbLocation);
				thirdSource = openWeatherMapService.constructWeatherLog();
				weatherLogController.addLog(thirdSource);
			} else {
				thirdSource = retrivedLog3;
			}

			return mergeJson(firstSource, secondSource, thirdSource);

		}

	}

	/**
	 * Merge multiple json object from three api services
	 * 
	 * @param firstLog
	 *            weather log from first source
	 * @param secondLog
	 *            weather log from second source
	 * @param thirdLog
	 *            weather log from third source
	 * @return json object created from the three log
	 */
	public JSONObject mergeJson(WeatherLog firstLog, WeatherLog secondLog, WeatherLog thirdLog) {
		JSONObject weatherObj = new JSONObject();

		weatherObj.put("apiux-data", firstLog.toJson());
		weatherObj.put("dark-sky-data", secondLog.toJson());
		weatherObj.put("open-weather-data", thirdLog.toJson());

		return weatherObj;

	}
}
