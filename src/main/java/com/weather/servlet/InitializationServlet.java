package com.weather.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.weather.utilities.EntityManagerFactoryManager;
import com.weather.utilities.WeatherServiceManager;

public class InitializationServlet extends HttpServlet {

	/**
	 * Initialize the web application
	 */
	public void init() throws ServletException {
		System.out.println("Creating entity Manager Factory");
		EntityManagerFactoryManager.getEntityManagerFactory();
		WeatherServiceManager.initializeWeatherService();
		// Timer time = new Timer();
		// time.schedule(new WeatherApi(), 1000, 5000);
	}

	public void destroy() {
		System.out.println("Destroying the Entity Manager Factory...");
		EntityManagerFactoryManager.getEntityManagerFactory().close();
	}

}
