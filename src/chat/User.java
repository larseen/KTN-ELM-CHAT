package chat;

import java.awt.Color;

public class User {
	
	private static final int NICELENGTH = 10;
	
	private String username, password = "Close, but no cigar!";
	private String usernameColor = "000000";
	private boolean connected = false;
	private boolean moderator = false;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, String color) {
		this(username, password);
		usernameColor = color;
	}
	
	public void connect() {
		connected = true;
	}
	
	public void disconnect() {
		connected = false;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public boolean isPassword(String password) {
		return this.password.equals(password);
	}
	
	public String getName() {
		return username;
	}
	
	public String getNicename() {
		return username.length() > NICELENGTH ? username.substring(NICELENGTH) : username;
	}
	
	public Color getColor() {
		return Color.decode("#"+usernameColor);
	}
	
	public boolean equals(User u) {
		return u.username.equals(this.username);
	}
	
	public boolean isMod() {
		return moderator;
	}
}
