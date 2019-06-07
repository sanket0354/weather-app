package com.weather.modelController;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.weather.model.Location;
import com.weather.utilities.EntityManagerFactoryManager;

public class LocationController {
	private EntityManagerFactory entityManagerFactory;

	public LocationController() {
		entityManagerFactory = EntityManagerFactoryManager.getEntityManagerFactory();
	}

	public Location addLocation(double longitude, double latitude) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Location location = new Location(longitude, latitude);

		entityManager.getTransaction().begin();
		entityManager.persist(location);
		entityManager.getTransaction().commit();
		entityManager.close();

		return location;

	}

	public Location findLocation(double longitude, double latitude) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List results = entityManager
				.createQuery("SELECT a FROM Location a where a.longitude = :longitude AND a.latitude = :latitude")
				.setParameter("longitude", longitude).setParameter("latitude", latitude).getResultList();

		if (results.isEmpty()) {
			return null;
		}

		return (Location) results.get(0);
	}

}
