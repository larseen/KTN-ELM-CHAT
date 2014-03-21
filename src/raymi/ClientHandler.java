package raymi;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *							PROTOCOL VARIABLES
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
 *	These variables are in place in order to make the implementation
 *	of the protocol more readable to other people. Also, it enables
 *	the protocol to be edited with greater ease.
 */

public abstract class ClientHandler extends Thread implements Comparable<ClientHandler> {
	
	private static final String
	RESPONSE_FIELD = "response",
	REQUEST_FIELD = "request",
	STATUS_FIELD = "status",
	CONTEXT_FIELD = "context";

	private static final String[] COMMANDS = {"login", "logout", "message", "new message", "send message"};
	private static final int LOGIN = 0, LOGOUT = 1, MESSAGE = 2, NEW_MESSAGE = 3, SEND_MESSAGE = 4;
	
	private static final String[] STATUSES = {"error", "OK"};
	private static final int ERROR = 0, OK = 1;
	
	private static final String[] ERRORS = {"User already logged in", "Username already taken", "Not logged inn"};
	private static final int ALREADY_LOGGED_IN = 0, USERNAME_TAKEN = 1, NOT_LOGGED_IN = 2;
	
	private Server server;
	private int ID;
	private String username;
	
	public ClientHandler(Server server, int ID) {
		this.server = server;
		this.ID = ID;
	}
	
	public String getUsername() {
		return username;
	}

	protected void handleMessage(JSONObject message) throws JSONException {
		String request = message.getString(REQUEST_FIELD);
	    Integer requestTypeIndex = Arrays.asList(COMMANDS).indexOf(request);
		switch(requestTypeIndex) {
		case LOGIN:
			respondToLogin(message.getString("context"));
			break;
		case LOGOUT:
			respondToLogout();
			break;
		case MESSAGE:
			respondToMessage(message.getString("context"));
			break;
		}
	}
	
	private void respondToLogin(String username) throws JSONException {
		JSONObject responsObject = new JSONObject();
		if(this.username != null) {
			responsObject.put(RESPONSE_FIELD, COMMANDS[LOGIN]);
			responsObject.put(STATUS_FIELD, STATUSES[ERROR]);
			responsObject.put(CONTEXT_FIELD, ERRORS[ALREADY_LOGGED_IN]);
		}
		
		else if (isUsernameValid(username)) {
			this.username = username;
			responsObject.put(RESPONSE_FIELD, COMMANDS[LOGIN]);
			responsObject.put(STATUS_FIELD, STATUSES[OK]);
			responsObject.put(CONTEXT_FIELD, new JSONArray(server.getMessages()));
		} else {
			responsObject.put(RESPONSE_FIELD, COMMANDS[LOGIN]);
			responsObject.put(STATUS_FIELD, STATUSES[ERROR]);
			responsObject.put(CONTEXT_FIELD, ERRORS[USERNAME_TAKEN]);
		}
		sendJSONObject(responsObject);
	}
	
	private void respondToMessage(String message) throws JSONException {
		JSONObject responsObject = new JSONObject();
		if (this.username != null) {
			System.out.println("Got this: "+message);
			message = this.username + ": " + message;
			server.pushMessages(message);
			responsObject.put(RESPONSE_FIELD, COMMANDS[SEND_MESSAGE]);
			responsObject.put(STATUS_FIELD, STATUSES[OK]);
		} else {
			responsObject.put(RESPONSE_FIELD, COMMANDS[MESSAGE]);
			responsObject.put(STATUS_FIELD, STATUSES[ERROR]);
			responsObject.put(CONTEXT_FIELD, ERRORS[NOT_LOGGED_IN]);
		}
		sendJSONObject(responsObject);
	}
	
	private void respondToLogout() throws JSONException {
		JSONObject responsObject = new JSONObject();
		if (this.username != null) {
			responsObject.put(RESPONSE_FIELD, COMMANDS[LOGOUT]);
			responsObject.put(STATUS_FIELD, STATUSES[OK]);
			this.username = null;
		} else {
			responsObject.put(RESPONSE_FIELD, COMMANDS[LOGOUT]);
			responsObject.put(STATUS_FIELD, STATUSES[ERROR]);
			responsObject.put(CONTEXT_FIELD, ERRORS[NOT_LOGGED_IN]);
		}
		sendJSONObject(responsObject);
	}

	public void pushMessage(String message) throws JSONException {
		JSONObject responsObject = new JSONObject();
		responsObject.put(RESPONSE_FIELD, COMMANDS[NEW_MESSAGE]);
		responsObject.put(CONTEXT_FIELD, message);
		sendJSONObject(responsObject);
	}

	private boolean isUsernameValid(String username) {
		return server.isUsernameAvailable(username);
	}
	
	protected abstract void sendJSONObject(JSONObject response);
	
	@Override
	public String toString() {
		return username;
	}
	
	@Override
	public int compareTo(ClientHandler c) {
		return ID - c.ID;
	}
	
	public int getID() {
		return ID;
	}
	
	public abstract void terminate();


}