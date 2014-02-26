package chat;

<<<<<<< HEAD
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
=======
import java.net.Socket;

import org.json.JSONObject;
>>>>>>> FETCH_HEAD

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
	
	
	
	
	
	
	
	
	
	
	
	
	class ClientHandler extends Thread {
		private Socket socket;
		private String username;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
		}
		
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
