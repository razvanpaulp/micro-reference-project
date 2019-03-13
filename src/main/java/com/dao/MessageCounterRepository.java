package com.dao;

import com.model.MessageCounter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class MessageCounterRepository implements EntityRepository<MessageCounter> {
	
	@Inject 
	private EntityManager entityManager;
	
	@Override
	public MessageCounter find(long id) {
		System.out.println("MessageCounterRepository em is : " + entityManager);
		return entityManager.find(MessageCounter.class, id);
	}

	@Override 
	public MessageCounter persist(MessageCounter entity) {
		System.out.println("MessageCounter (persist) em is : " + entityManager);
		entityManager.persist(entity);
		return entity;
	}

	@Override public MessageCounter update(MessageCounter entity) {
		return entityManager.merge(entity);
	}
}
