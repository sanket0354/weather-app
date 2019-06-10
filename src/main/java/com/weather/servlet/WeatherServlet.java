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

		String city = request.getParameter("city").toLowerCase();
		String province = request.getParameter("province").toLowerCase();
		String country = request.getParameter("country").toLowerCase();
		boolean forceRefresh = Boolean.parseBoolean(request.getParameter("refresh"));

		GeoLocationService geoLocationService = new GeoLocationService(city, province, country);
		Location location = geoLocationService.getCoOrdinates();

//		System.out.println(location.getLatitude() + " " + location.getLongitude());
		JSONObject responseJson = processRequest(location, forceRefresh);

		PrintWriter sendResponse = response.getWriter();
		sendResponse.print(responseJson);
		sendResponse.flush();

	}

	public JSONObject processRequest(Location location, boolean forceRefresh) {
		LocationController locationController = new LocationController();
		WeatherLogController weatherLogController = new WeatherLogController();

		Location dbLocation = locationController.findLocation(location.getLongitude(), location.getLatitude());

		ApiuxWeatherService apiuxWeatherService = WeatherServiceManager.getApiuxWeatherService();
		DarkSkyWeatherService darkSkyWeatherService = WeatherServiceManager.getdarkSkyWeatherService();
		OpenWeatherMapService openWeatherMapService = WeatherServiceManager.getopenWeatherMapService();

		if (dbLocation == null || forceRefresh) {
			System.out.println("Db Location is null");
			dbLocation = locationController.addLocation(location.getLongitude(), location.getLatitude());

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
			System.out.println("Db Location is not null");
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
				System.out.println("retrieved1 log is null");
				apiuxWeatherService.setLocation(dbLocation);
				firstSource = apiuxWeatherService.constructWeatherLog();
				weatherLogController.addLog(firstSource);
			} else {
				System.out.println("retrieved1 log is not null");
				firstSource = retrivedLog1;
			}

			if (retrivedLog2 == null) {
				System.out.println("retrieved2 log is null");
				darkSkyWeatherService.setLocation(dbLocation);
				secondSource = darkSkyWeatherService.constructWeatherLog();
				weatherLogController.addLog(secondSource);
			} else {
				System.out.println("retrieved2 log is not null");
				secondSource = retrivedLog2;
			}

			if (retrivedLog3 == null) {
				System.out.println("retrieved3 log is null");
				openWeatherMapService.setLocation(dbLocation);
				thirdSource = openWeatherMapService.constructWeatherLog();
				weatherLogController.addLog(thirdSource);
			} else {
				System.out.println("retrieved3 log is not null");
				thirdSource = retrivedLog3;
			}

			return mergeJson(firstSource, secondSource, thirdSource);

		}

	}

	public JSONObject mergeJson(WeatherLog firstLog, WeatherLog secondLog, WeatherLog thirdLog) {
		JSONObject weatherObj = new JSONObject();
		
		weatherObj.put("apiux-data", firstLog.toJson());
		weatherObj.put("dark-sky-data", secondLog.toJson());
		weatherObj.put("open-weather-data", thirdLog.toJson());
		
		return weatherObj;

	}
}
