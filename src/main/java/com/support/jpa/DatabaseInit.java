package com.support.jpa;

import com.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class DatabaseInit {

	@Inject
	private EntityManagerProducer entityManagerProducer;
	
	public void init() {
		EntityManager entityManager = entityManagerProducer.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager
				.createNativeQuery("CREATE TABLE IF NOT EXISTS MESSAGE(id INTEGER NOT NULL, response varchar(255))")
				.executeUpdate();
		entityManager
				.createNativeQuery("CREATE TABLE IF NOT EXISTS MESSAGECOUNTER(id INTEGER NOT NULL, count INTEGER NOT " 
						+ "NULL, messageId INTEGER NOT NULL)")
				.executeUpdate();

		entityManager.persist(new Message(1, "This is message 1"));
		entityManager.persist(new Message(2, "This is message 2"));
		entityManager.persist(new Message(3, "This is message 3"));

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
