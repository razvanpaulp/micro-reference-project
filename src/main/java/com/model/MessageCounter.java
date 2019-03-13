package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGECOUNTER")
public class MessageCounter {

	@Id
	private long id;
	
	@Column
	private long count;
	
	@Column
	private long messageId;

	public MessageCounter() {
		
	}
	
	public void incrementCounter() {
		count++;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
}
