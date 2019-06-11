package com.weather.modelController;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import com.weather.model.Location;
import com.weather.model.WeatherLog;
import com.weather.model.WeatherLog.SourceType;
import com.weather.utilities.EntityManagerFactoryManager;

/**
 * The WeatherLogController class would control database transaction for the
 * WeatherLog model
 * 
 * @author Sanket.Patel
 *
 */
public class WeatherLogController {
	private EntityManagerFactory entityManagerFactory;

	public WeatherLogController() {
		entityManagerFactory = EntityManagerFactoryManager.getEntityManagerFactory();
	}

	/**
	 * Add a new weather log fetched from the api
	 * 
	 * @param temperature
	 *            the current temperature of the location
	 * @param windSpeed
	 *            the current windspeed of the location
	 * @param conditionType
	 *            the current condition type of the the location
	 * @param location
	 *            the location to add log for
	 * @param source
	 *            the api source from which the data is coming from
	 * @return the WeatherLog object added to the database
	 */
	public WeatherLog addLog(double temperature, double windSpeed, String conditionType, Location location,
			SourceType source) {
		WeatherLog weatherLog = new WeatherLog(temperature, windSpeed, conditionType, location, source);

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(weatherLog);
		entityManager.getTransaction().commit();
		entityManager.close();

		return weatherLog;

	}

	/**
	 * Add a new weather log based on the WeatherLog object passed as an
	 * parameter
	 * 
	 * @param weatherLog
	 *            the log to add
	 * @return the loged added to the database
	 */
	public WeatherLog addLog(WeatherLog weatherLog) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(weatherLog);
		entityManager.getTransaction().commit();
		entityManager.close();

		return weatherLog;

	}

	/**
	 * Find a WeatherLog based on the location and the source id from the
	 * database
	 * 
	 * @param location
	 *            the location for which the logs need to be retrieved
	 * @param source
	 *            the api source that will correspond the location
	 * @return the WeatherLog fetched from the database
	 */
	public WeatherLog findLastLogBasedOnLocationAndSourceAndDate(Location location, SourceType source) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		WeatherLog weatherLog = null;
		try {
			// in case no result is found, catch the exception and return null
			weatherLog = (WeatherLog) entityManager
					.createQuery(
							"SELECT a FROM WeatherLog a where a.location = :location_id AND a.sourceId = :source_id ORDER BY a.id DESC")
					.setParameter("location_id", location).setParameter("source_id", source.getValue()).setMaxResults(1)
					.getSingleResult();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			return null;
		}
		if (weatherLog == null) {
			return null;
		}

		if (weatherLog.getLastUpdated().toLocalDate().equals(LocalDate.now())) {
			return weatherLog;
		}

		return null;
	}

}
