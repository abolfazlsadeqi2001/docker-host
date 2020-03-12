package main.general.authentication.models;

import java.time.LocalTime;

public class UserCache {
	private User user;
	private LocalTime expireTime;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public LocalTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalTime expireTime) {
		this.expireTime = expireTime;
	}
	
}
