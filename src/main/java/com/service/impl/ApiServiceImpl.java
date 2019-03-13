package com.service.impl;

import com.dao.MessageCounterRepository;
import com.dao.MessageRepository;
import com.model.Message;
import com.model.MessageCounter;
import com.service.ApiService;
import com.support.aop.AsyncTransactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@AsyncTransactional
@Transactional
public class ApiServiceImpl implements ApiService {

	@Inject
	private MessageRepository messageRepository;
	@Inject
	private MessageCounterRepository messageCounterRepository;
	
	public Message buildHelloMessage(String name)
			throws Exception {
		if (name == null) {
			throw new Exception("Name parameter must not be null!");
		}
		
		return new Message("Hello " + name);
	}

	public Message getMessageFromStorage(long id) {
		if (id == 0 ) {
			return messageRepository.getSleepMessage();
		}
		return messageRepository.find(id);
	}

	@Override 
	public Message getMessageWithCounter(long id) {
		Message dbMessage = messageRepository.find(id);
		MessageCounter messageCounter = messageCounterRepository.find(id);
		
		if (messageCounter == null) {
			messageCounter = new MessageCounter();
			messageCounter.setId(dbMessage.getId());
			messageCounter.setMessageId(dbMessage.getId());
			messageCounter.incrementCounter();
			
			messageCounter = messageCounterRepository.persist(messageCounter);
		} else {
			messageCounter.incrementCounter();
		}
		
		Message responseMessage = new Message();
		responseMessage.setResponse("Db message : '" + dbMessage.getResponse() + "' has been viewed " + messageCounter.getCount() + 
				" times.");
		
		return responseMessage;
	}
}
