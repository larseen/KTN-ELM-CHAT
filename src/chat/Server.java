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
	
	private static final String[] COMMANDS = {"login", "logout", "message", "new message"};
	private static final int LOGIN = 0, LOGOUT = 1, MESSAGE = 2, NEW_MESSAGE = 3;
	
	private static final String[] STATUSES = {"error", "OK"};
	private static final int ERROR = 0, OK = 1;
	
	
	
	private ServerSocket serverSocket;
	private Integer port;
	private ArrayList<ClientHandler> clients;
	private ArrayList<String> messages;
	
	public static void main(String[] args) {
		new Server(8888);
	}
	
	public Server(Integer port) {
		//TODO: Do we really need this field?
		this.port = port;
		
		//TODO: Initiate the two ArrayLists in the variable declaration
		clients = new ArrayList<ClientHandler>();
		messages = new ArrayList<String>();
		
		try {
			serverSocket = new ServerSocket(port);
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
				client = new ClientHandler(serverSocket.accept(), this);
				clients.add(client);
				client.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//TODO: Should return an ArrayList<String> rather than a JSONArray
	public JSONArray getMessages() {
		JSONArray jsArray = new JSONArray(messages);
		return jsArray;
	}
	
	public void pushMessages(String message) {
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
		/*
		 * TODO:
		 *	Okay, so one of the perks of having ClientHandler as an inner 
		 *	class is the fact that all fields and methods of the Server 
		 *	(including the private ones) are accesible. You should remove 
		 *	the server field and make necessary changes to the issues that
		 *	 rise as a consequence.
		 */
		
		private Socket socket;
		private Server server;
		private String username;
		
		private InputStream IS;
		private OutputStream OS;
		private PrintWriter out;
		//TODO: Add the BufferedReader as a field.
		
		public ClientHandler(Socket socket, Server server) {
			this.socket = socket;
			this.server = server;
			
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
<<<<<<< HEAD

		private void handleMessage(JSONObject message) throws JSONException {
			String request = (message.getString("request")).toLowerCase();
			switch(request) {
			case "login":
				
				break;
			case "logout":
				
				break;
				
			case "message":
				
				break;
			}
			
=======
		
		/* 	TODO: Rename this to resolveRequest()
		 * 	TODO: Use the protocol variables provided at the
		 * 	beginning of the document to make this code more readable.
		 *	TODO: Throw JSONExceptions 
		 */
		private void handleMessage(JSONObject message) {
>>>>>>> FETCH_HEAD
			try {
				if (((message.getString("request")).toLowerCase())
						.equals("message")) {
					respondToMessage(message.getString("context"));
				}
				if (((message.getString("request")).toLowerCase())
						.equals("logout")) {
					respondToLogout();
				}
				if (((message.getString("request")).toLowerCase())
						.equals("login")) {
					respondToLogin(message.getString("context"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//TODO: Clean this up a bit
		private void respondToLogin(String username) {
			if(this.username != null) {
				sendJSONObject(createRespond("login", "error", "already logged in"));
			}
			
			else if (isUsernameValid(username)) {
				this.username = username;
				JSONObject responsObject = new JSONObject();
				try {
					responsObject.put("response", "login");
					responsObject.put("status", "OK");
					responsObject.put("messages", server.getMessages());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(responsObject.toString());
				System.out.println(server.getMessages());
				sendJSONObject(responsObject);
			} else {
				sendJSONObject(createRespond("login", "error", "username taken"));
			}
		}
		
		//TODO: Clean this up a bit
		private void respondToMessage(String message) {
			if (this.username != null) {
				message = this.username + ": " + message;
				server.pushMessages(message);
				sendJSONObject(createRespond("send message", "OK", ""));
			} else {
				sendJSONObject(createRespond("message", "error",
						"not logged in"));
			}
		}
		
		private void respondToLogout() {
			if (this.username != null) {
				sendJSONObject(createRespond("logout", "OK", ""));
				this.username = null;
			} else {
				sendJSONObject(createRespond("logout", "error", "not logged in"));
			}
		}

		public void pushMessage(String message) {
			JSONObject responsObject = new JSONObject();
			try {
				responsObject.put("response", "new message");
				responsObject.put("context", message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendJSONObject(responsObject);
		}

		private boolean isUsernameValid(String username) {
			return server.isUsernameAvailable(username);
		}
		
		/* TODO:
		 * 
		 * 	This isn't really a good solution, it would be better if only
		 * 	the data strictly necessary was sent. Have each respondTo-method
		 * 	wrap the data in JSON objects by themselves.
		 * 
		 */
		private JSONObject createRespond(String respons, String status,
				String context) {
			JSONObject responsObject = new JSONObject();
			try {
				responsObject.put("response", respons);
				responsObject.put("status", status);
				responsObject.put("context", context);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return responsObject;
		}
		
		/*
		 * You probably won't be needing this.
		 */
		private void sendJSONObject(JSONObject respons) {
			String responsString = respons.toString();
			out = new PrintWriter(OS, true);
			out.println(responsString);
		}
		
		@Override
		public String toString() {
			return username;
		}

	}
}