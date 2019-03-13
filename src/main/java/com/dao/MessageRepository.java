package com.dao;

import com.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@ApplicationScoped
public class MessageRepository implements EntityRepository<Message> {
	
	@Inject
	private EntityManager entityManager;

	@Override 
	public Message find(long id) {
		Query query = entityManager.createQuery("SELECT m FROM Message m WHERE m.id =:id");
		query.setParameter("id", id);
		
		try {
			return (Message) query.getSingleResult();	
		} catch (NoResultException ex) {
			return null;
		}
		
	}

	@Override 
	public Message persist(Message entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Override public Message update(Message entity) {
		return entityManager.merge(entity);
	}

	public Message getSleepMessage() {
		entityManager.createNativeQuery("SELECT SLEEP(5)").getResultList();
		
		return new Message("Dummy message returned after DB SLEEP (5)");
	}
}
