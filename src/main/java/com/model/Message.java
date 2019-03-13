package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE")
public class Message {

	@Id
	private long id;
	
	@Column
	private String response;

	public Message() {
	}

	public Message(String response) {
		this.response = response;
	}

	public Message(long id, String response) {
		this.id = id;
		this.response = response;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
