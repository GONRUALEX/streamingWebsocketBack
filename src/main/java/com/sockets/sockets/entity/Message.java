package com.sockets.sockets.entity;

import java.io.Serializable;

public class Message implements Serializable{
	
	private String type;
	
	private int from;

	private String fromUserName;
	
	private String message;
	
	private Byte[] data;

	public Message() {	}

	public Message(String type, int from, String fromUserName, String message) {
		this.from = from;
		this.fromUserName = fromUserName;
		this.message = message;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Byte[] getData() {
		return data;
	}

	public void setData(Byte[] data) {
		this.data = data;
	}


	
	
}