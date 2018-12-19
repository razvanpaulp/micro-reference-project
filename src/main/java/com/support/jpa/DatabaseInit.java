package com.support.jpa;

import com.model.Message;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class DatabaseInit {

	@Inject
	private EntityManager entityManager;
	
	private void initDatabase(@Observes @Initialized(ApplicationScoped.class) Object init) {
		createTable();
		insertData();
	}

	private void createTable() {
		entityManager.getTransaction().begin();
		entityManager
				.createNativeQuery("CREATE TABLE IF NOT EXISTS message(id INTEGER NOT NULL, response varchar(255))")
				.executeUpdate();

		entityManager.getTransaction().commit();
	}

	private void insertData() {
		entityManager.getTransaction().begin();
		entityManager.persist(new Message(1, "TEST"));
		entityManager.getTransaction().commit();
	}
}
