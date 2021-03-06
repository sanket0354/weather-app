package com.weather.services;

import java.io.IOException;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.weather.constants.ServiceConstants;
import com.weather.model.Location;

/**
 * Connects to the GeoCoding api of google to get the co-ordinates of the place
 * requested by the user to get the weather data on
 * 
 * @author Sanket.Patel
 *
 */
public class GeoLocationService {

	private String city;
	private String province;
	private String country;

	public GeoLocationService(String city, String province, String country) {
		this.city = city;
		this.province = province;
		this.country = country;

	}

	/**
	 * ping the google service to get the Co-ordinates of the requested location
	 * 
	 * @return location object
	 */
	public Location getCoOrdinates() {
		GeoApiContext context = new GeoApiContext.Builder().apiKey(ServiceConstants.GEO_CODING_API_KEY).build();
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context, getAddress()).await();
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (results.length == 0) {
			return null;
		}

		Location location = new Location();
		location.setLatitude(results[0].geometry.location.lat);
		location.setLongitude(results[0].geometry.location.lng);

		return location;
	}

	/**
	 * Build a address
	 * 
	 * @return string representing an address
	 */
	private String getAddress() {
		return city + "," + province + "," + country;
	}

}
