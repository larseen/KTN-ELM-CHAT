package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class Server implements Runnable {
	
	private ServerSocket serverSocket;
	private final Server server;
	private String IP;
	private int port;
	SocketIOServer jsServer;
	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private ArrayList<String> messages = new ArrayList<String>();
	private boolean alive = true;
	
	public static void main(String[] args) {
		new Server(Integer.valueOf(args[0]));
	}
	
	public Server(int port) {		
		
		server = this;
		(new ServerManager(this)).start();
		
		try {
			// FINNER IP
			this.IP = InetAddress.getLocalHost().getHostAddress();
			this.port = port;
			
			// SETTER JAVASCRIPT SERVER
			startJavaScriptServer(port, IP);

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
		
		while (alive) {
			ClientHandler client;
			try {
				Socket socket = serverSocket.accept();
				int ID = generateID();
				client = new JavaClientHandler(this, socket, ID);
				clients.add(client);
				client.start();
			} catch (IOException e) {
				System.out.println("Socket was forcibly shut down");
			}
		}
		
		cleanup();
	}
	
	public void stop() {
		this.alive = false;
		jsServer.stop();
		try { serverSocket.close(); } 
		catch (IOException e) { e.printStackTrace(); }	
	}
	
	private void cleanup() {
		System.out.println("Cleaning up ...");
		ClientHandler[] toRemove = new ClientHandler[clients.size()];
		int i = 0;
		for(ClientHandler c : clients) {
			toRemove[i] = c;
			i++;
		}
		for(ClientHandler c : toRemove) {
			c.terminate();
		}
		
		System.out.println("All done!");
	}
	
	
	//TODO: Should return an ArrayList<String> rather than a JSONArray
	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public void pushMessages(String message) throws JSONException {
		messages.add(message);
		for (ClientHandler client : clients) {
			if(client.getUsername() != null) {
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
	
	// TODO: CREATE FUNCTION
	public int generateID() {
		return -1;
	}
	
	private void startJavaScriptServer(int port, String host) {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port+1);
		jsServer = new SocketIOServer(config);
		final HashMap<SocketIOClient, JavaScriptClientHandler> jsHandlers = new HashMap<SocketIOClient, JavaScriptClientHandler>();
		
		jsServer.addConnectListener(new ConnectListener() {
	        public void onConnect(SocketIOClient client) {
	        	int ID = generateID();
	        	JavaScriptClientHandler handler = new JavaScriptClientHandler(server, client, ID);
				jsHandlers.put(client, handler);
				clients.add(handler);
	        }
	    });
		
		jsServer.addDisconnectListener(new DisconnectListener() {
	        public void onDisconnect(SocketIOClient client) {
	        	//JavaScriptClientHandler handler = jsHandlers.get(client);
	        	
	        	// TODO: Remove from both map and list
	        	JavaScriptClientHandler h = jsHandlers.remove(client);
	        	h.terminate();
	        	
	        	//int ID = handler.getID();
	        }
	    });
		
		jsServer.addMessageListener(new DataListener<String>() {
	        public void onData(SocketIOClient client, String jsonString, AckRequest ackRequest) {
	        	try {
					JavaScriptClientHandler handler = jsHandlers.get(client);
					handler.handleMessage(new JSONObject(jsonString));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
		
		jsServer.start();
	}
	
	public void removeClient(ClientHandler c) {
		clients.remove(c);
	}
	
	public void printUsers() {
		System.out.println(clients);
	}
	
	public void printStatus() {
		System.out.println("Server is online");
		System.out.println("Java server: "+IP+":"+port);
		System.out.println("Web server: "+IP+":"+(port+1));
	}
}