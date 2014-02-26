package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
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
				client = new ClientHandler(serverSocket.accept());
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
		private String username;
		private LinkedList<JSONObject> messagePool;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
		}
		
		public String getUsername(){
			return username;
		}
		
		private void handleMessage(JSONObject message){
			
		}
		
		public void pushMessage(String message){

		}
		
		private void respondToLogin(String username){
			
		}
		
		private void respondToMessage(){
			
		}

		private void respondToSendMessage(String message){
			
		}
		
		private void respondToGetMessage(){
			
		}
		
		private boolean isUsernameValid(String username){
			
		}
		
		private void respondToLogout(){
		
		}
		
		private void resolveRequest(JSONObject request){
			
		}
		
	}














}
