package com.service;

import com.model.Message;

public interface  MessageService {
	
	Message find(long id);
	
	Message persist(Message m);
	
	Message update(Message m);
}
