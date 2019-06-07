package com.weather.utilities;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * This class is to create EntityManagerFactory instance, so as one common
 * instance is shared by all the DatabaseManager Classes
 * 
 * This will be usefull as EntityManagerFactory is a heavy instance, takes time
 * in creation but when we share one instance, it will save a lot of time as the
 * instance will be created at compile time
 *
 * 
 *
 */
public class EntityManagerFactoryManager {

	/**
	 * entityManagerFactory: making private so as to implement singleton design
	 * pattern
	 * 
	 * 
	 * NOTE: DO NOT CLOSE ENTITYMANAGERFACTORY IN ANY OTHER CLASS
	 */
	private static EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory("weather-app");
	
	/**
	 * 
	 * @return EntityManagerFactory get the instance of EntityManagerFactory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	/**
	 * close the entity Manager factory
	 */
	public static void closeEntityManagerFactory() {
		entityManagerFactory.close();
	}

}
