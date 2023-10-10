package com.sockets.sockets.entity;

public class User {
	
	private final int id;
	
	private final String userName;

	private boolean isOnline;
	
	public User(int id, String userName, boolean isOnline) {
		this.id = id;
		this.userName = userName;
		this.isOnline = isOnline;
	}

	public int getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (id != user.id) return false;
		return userName.equals(user.userName);
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + userName.hashCode();
		return result;
	}
}