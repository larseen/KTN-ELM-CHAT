package chat;

<<<<<<< HEAD
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
=======

import org.json.JSONObject;
>>>>>>> FETCH_HEAD

public class Server implements Runnable{

	private ServerSocket serverSocket;
	private InetAddress address;
	private Integer port;
	private ArrayList<ClientHandler> clients;
	private ArrayList<String> messages;
	
	
	public void run() {
		while(true){
			
		}
	}
	
	
	
	
	public void init(){
		address = InetAddress.getLocalHost();
		port = 8000;
		serverSocket = new ServerSocket(port);
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
		
		public void pushMessage(String message){
			
		}
		
		private void respondToLogin(String username){
			
		}
		
		private void respondToMessage(){
			
		}
		
		private void resolveRequest(JSONObject request){
			
		}
		
	}














}
