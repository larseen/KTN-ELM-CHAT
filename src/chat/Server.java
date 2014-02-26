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

	private ServerSocket serverSocket;
	private Integer port;
	private ArrayList<ClientHandler> clients;
	private ArrayList<String> messages;
	
	public static void main(String[] args) {
		Server s = new Server(8888);
	}
	
	public Server(Integer port) {
		this.port = port;
		clients = new ArrayList<ClientHandler>();
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

	public JSONArray getMessages() {
		JSONArray jsArray = new JSONArray(messages);
		return jsArray;
	}

	public void pushMessages(String message) {
		for (ClientHandler client : clients) {
			client.pushMessage(message);
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
		private Server server;
		private String username;
		private InputStream IS;
		private OutputStream OS;
		private PrintWriter out;
		
		@Override
		public void run() {
			while (true) {
				String message;
				try {
					message = ((new BufferedReader(new InputStreamReader(IS)))
							.readLine());
					JSONObject jsonObj = new JSONObject(message);
					handleMessage(jsonObj);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		

		public ClientHandler(Socket socket, Server server) {
			this.socket = socket;
			this.server = server;
			
			try {
				IS = socket.getInputStream();
				OS = socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getUsername() {
			return username;
		}

		private void handleMessage(JSONObject message) {
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

		private void respondToLogin(String username) {
			if (isUsernameValid(username)) {
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
				
				sendJSONObject(responsObject);
			} else {
				sendJSONObject(createRespond("login", "error", "username taken"));
			}
		}

		private void respondToMessage(String message) {
			if (this.username != null) {
				server.pushMessages(message);
				sendJSONObject(createRespond("send message", "OK", ""));
			} else {
				sendJSONObject(createRespond("message", "error",
						"not logged in"));
			}
		}

		private void respondToLogout() {
			if (this.username != null) {
				sendJSONObject(createRespond("login", "OK", ""));
			} else {
				sendJSONObject(createRespond("logout", "error", "not logged in"));
			}
		}

		public void pushMessage(String message) {
			sendJSONObject(createRespond("new message", "", message));
		}

		private boolean isUsernameValid(String username) {
			return server.isUsernameAvailable(username);
		}

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