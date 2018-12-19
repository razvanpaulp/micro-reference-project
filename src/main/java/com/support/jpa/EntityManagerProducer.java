package com.support.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer {

	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
			.createEntityManagerFactory("micro-reference-project");
	
	@Produces
	//@RequestScoped
	@ApplicationScoped
	public EntityManager createEntityManager() {
		return ENTITY_MANAGER_FACTORY.createEntityManager();
	}

	public void closeEM(@Disposes EntityManager manager) {
		manager.close();
	}
	
}
