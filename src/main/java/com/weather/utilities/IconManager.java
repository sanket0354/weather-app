package com.weather.utilities;

import org.apache.commons.lang3.StringUtils;

/**
 * this is just a helper class created for presentation/demo purpose which if
 * sees some keywords in the weather condition retrieved from the api services
 * then adds a particular icon to display on the front-end
 * 
 * @author Sanket.Patel
 *
 */
public class IconManager {

	public static String getIcon(String condition) {

		System.out.println(condition);
		if (StringUtils.containsIgnoreCase(condition, "rain") || StringUtils.containsIgnoreCase(condition, "drizzle")
				|| StringUtils.containsIgnoreCase(condition, "sprinkle")) {
			return "wi-rain";
		} else if (StringUtils.containsIgnoreCase(condition, "sun")
				|| StringUtils.containsIgnoreCase(condition, "clear")) {
			return "wi-day-sunny";
		} else if (StringUtils.containsIgnoreCase(condition, "snow")
				|| StringUtils.containsIgnoreCase(condition, "hail")) {
			return "wi-snow";
		} else if (StringUtils.containsIgnoreCase(condition, "gust")
				|| StringUtils.containsIgnoreCase(condition, "wind")) {
			return "wi-cloudy-windy";
		} else if (StringUtils.containsIgnoreCase(condition, "cloud")) {

			return "wi-cloud";
		}

		return null;
	}
}