package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Server implements Runnable {
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *							PROTOCOL VARIABLES
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 *	These variables are in place in order to make the implementation
	 *	of the protocol more readable to other people. Also, it enables
	 *	the protocol to be edited with greater ease.
	 */
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
	
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private ArrayList<String> messages = new ArrayList<String>();
	
	public static void main(String[] args) {
		new Server();
	}
	
	public Server() {		
		try {
			serverSocket = new ServerSocket();
			run();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//TODO: Doesn't really need a run method, this could be moved in the constructor
	@Override
	public void run() {
		while (true) {
			System.out.println(clients);
			ClientHandler client;
			try {
				client = new ClientHandler(serverSocket.accept());
				clients.add(client);
				client.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//TODO: Should return an ArrayList<String> rather than a JSONArray
	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public void pushMessages(String message) throws JSONException {
		messages.add(message);
		for (ClientHandler client : clients) {
			if(client.username != null) {
				client.pushMessage(message);
			}
		}
	}

	public boolean isUsernameAvailable(String username) {
		for (ClientHandler client : clients) {
			if (username.equals(client.getUsername())) {
				return false;
			}
		}
		return true;
	}

	class ClientHandler extends Thread {
		
		private Socket socket;
		private String username;
		
		private InputStream IS;
		private OutputStream OS;
		private PrintWriter out;
		//TODO: Add the BufferedReader as a field.
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			
			try {
				IS = socket.getInputStream();
				OS = socket.getOutputStream();
				/*
				 * TODO: We don't really need the input- and output stream. Instead,
				 * initiate the BufferedReader and PrintWriter here.
				 */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			while (true) {
				String message;
				try {
					//TODO: Clean
					message = ((new BufferedReader(new InputStreamReader(IS)))
							.readLine());
					JSONObject jsonObj = new JSONObject(message);
					handleMessage(jsonObj);
				} 
				catch (IOException e1) { /* TODO Auto-generated catch block */ } 
				catch (JSONException e) { /* TODO Auto-generated catch block */ }
			}
		}
		
		public String getUsername() {
			return username;
		}

		private void handleMessage(JSONObject message) throws JSONException {
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
				responsObject.put(CONTEXT_FIELD, new JSONArray(getMessages()));
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
				message = this.username + ": " + message;
				pushMessages(message);
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
			return isUsernameAvailable(username);
		}
		
		private void sendJSONObject(JSONObject respons) {
			String responsString = respons.toString();
			out = new PrintWriter(OS, true);
			out.println(responsString);
		}
		
		@Override
		public String toString() {
			return username;
		}
		
		//Not done
		public void kill() throws IOException {
			socket.close();
		}

	}
}