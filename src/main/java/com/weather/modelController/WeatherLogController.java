package com.weather.modelController;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.weather.model.Location;
import com.weather.model.Source;
import com.weather.model.WeatherLog;
import com.weather.utilities.EntityManagerFactoryManager;

public class WeatherLogController {
	private EntityManagerFactory entityManagerFactory;

	public WeatherLogController() {
		entityManagerFactory = EntityManagerFactoryManager.getEntityManagerFactory();
	}

	public WeatherLog addLog(double temperature, double windSpeed, String conditionType, Location location,
			Source source) {
		WeatherLog weatherLog = new WeatherLog(temperature, windSpeed, conditionType, location, source);

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(weatherLog);
		entityManager.getTransaction().commit();
		entityManager.close();

		return weatherLog;

	}

	public WeatherLog addLog(WeatherLog weatherLog) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(weatherLog);
		entityManager.getTransaction().commit();
		entityManager.close();

		return weatherLog;

	}

	public WeatherLog findLastLogBasedOnLocationAndSourceAndDate(Location location, Source source) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		WeatherLog weatherLog = (WeatherLog) entityManager
				.createQuery("SELECT a FROM WeatherLog a where a.location = :location_id AND a.sourceId = :source_id")
				.setParameter("location_id", location).setParameter("source_id", source.getValue()).getSingleResult();

		if (weatherLog == null) {
			return null;
		}

		if (weatherLog.getLastUpdated().toLocalDate().equals(LocalDate.now())) {
			return weatherLog;
		}

		return null;
	}

}
