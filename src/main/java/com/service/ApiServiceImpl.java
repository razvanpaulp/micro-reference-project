package com.service;

import com.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@ApplicationScoped 
public class ApiServiceImpl implements ApiService {

	@Inject
	private EntityManager entityManager;

	public Message buildHelloMessage(String name)
			throws Exception {
		if (name == null) {
			throw new Exception("Name parameter must not be null!");
		}
		
		return new Message("Hello " + name);
	}

	public Message getMessageFromStorage() {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Message m = entityManager.find(Message.class, 1l);

		transaction.commit();

		return m;
	}
}
