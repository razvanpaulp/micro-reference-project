package com.service.impl;

import com.dao.MessageRepository;
import com.model.Message;
import com.service.MessageService;
import com.support.aop.AsyncTransactional;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
@AsyncTransactional
public class MessageServiceImpl implements MessageService {
	
	@Inject
	private MessageRepository messageRepository;
	
	@Override 
	public Message find(long id) {
		return messageRepository.find(id);
	}

	@Override 
	public Message persist(Message m) {
		return messageRepository.persist(m);
	}

	@Override
	public Message update(Message m) {
		return messageRepository.update(m);
	}

}
