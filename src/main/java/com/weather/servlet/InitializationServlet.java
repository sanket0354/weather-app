package com.weather.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.weather.utilities.EntityManagerFactoryManager;
import com.weather.utilities.WeatherServiceManager;

/**
 * Initializes the application
 * 
 * @author Sanket.Patel
 *
 */
public class InitializationServlet extends HttpServlet {

	/**
	 * Initialize the following:
	 *  - entity manager for database transaction
	 *  - intialize the weather service
	 */
	public void init() throws ServletException {
		EntityManagerFactoryManager.getEntityManagerFactory();
		WeatherServiceManager.initializeWeatherService();
	}

	/**
	 * close any resources as possible
	 */
	public void destroy() {
		EntityManagerFactoryManager.getEntityManagerFactory().close();
	}

}
