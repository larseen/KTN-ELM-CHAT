package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;


public class Server implements Runnable{

	private ServerSocket serverSocket;
	private InetAddress address;
	private Integer port;
	private ArrayList<ClientHandler> clients;
	private ArrayList<String> messages;
	
	
	public void run() {
		while(true){
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
	
	
	public void init(){
		try {
			address = InetAddress.getLocalHost();
			port = 8000;
			serverSocket = new ServerSocket(port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getMessages() {
		return messages;
	}
	
	
	public void pushMessages(String message){
		for(ClientHandler client : clients){
			client.pushMessage(message);
		}
	}
	
	
	public boolean isUsernameAvailable(String username){
		for(ClientHandler client : clients){
			if(username.equals(client.getUsername())){
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	class ClientHandler extends Thread {
		private Socket socket;
		private Server server;
		private String username;
		private LinkedList<String> messagePool;
		
		public ClientHandler(Socket socket, Server server) {
			this.socket = socket;
			this.server = server;
		}
		
		public String getUsername(){
			return username;
		}
		
		private void handleMessage(JSONObject message){
			try {
				if(((message.getString("request")).toLowerCase()).equals("message")){
					respondToMessage(message.getString("contex"));
				}
				if(((message.getString("request")).toLowerCase()).equals("logout")){
					respondToLogout();
				}
				if(((message.getString("request")).toLowerCase()).equals("login")){
					respondToLogin(message.getString("contex"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void pushMessage(String message){
			messagePool.add(message);
		}
		
		private void respondToLogin(String username){
			if(isUsernameValid(username)){
				this.username = username;
			}
		}
		
		private void respondToMessage(String message){
			server.pushMessages(message);
		}
		
		private void respondToLogout(){
			this.stop();
		}
		
		private void respondToGetMessage(){
			
		}
		
		@SuppressWarnings("unused")
		private boolean isUsernameValid(String username){
			return server.isUsernameAvailable(username);
		}
		
		private void resolveRequest(JSONObject request){
			
		}
		
	}














}
