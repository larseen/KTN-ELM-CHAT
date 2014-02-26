package chat;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private Socket serverSocket;
	private InetAddress address;
	private Integer port;
	private ArrayList<ClientHandler> clients;
	
	public void getMessages() {
		
	}
	
	
	
	
	public void pushMessages(String message){
		
	}
	
	
	
	
	public boolean isUsernameAvailable(String username){
		for(ClientHandler client : clients){
			if(username.equals(client.getUsername())){
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	class ClientHandler{
		
	}

}
