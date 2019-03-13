package com.support.jpa;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerProducer {
	
	@Inject
	private AppPersistenceProvider appPersistenceProvider;

	private EntityManagerFactory ENTITY_MANAGER_FACTORY;

	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		if (ENTITY_MANAGER_FACTORY == null) {
			ENTITY_MANAGER_FACTORY = appPersistenceProvider.getEntityManagerFactory();
		}
		
		return ENTITY_MANAGER_FACTORY.createEntityManager();
	}

	public void close(@Disposes EntityManager entityManager) {
		entityManager.close();
	}

}
