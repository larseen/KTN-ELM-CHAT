import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;


public class ClientHandler {
	
	private Socket socket;
	private String username;
	private Server server;
	
	public ClientHandler(Socket socket, Server server){
		this.server = server;
		this.socket = socket;	
	}
	
	public String getUsername() {
		return username;
	}
	
	private void handleMessage(JSONObject message) throws JSONException{
		this.username = message.getString("username");
	}
	
	private boolean usernameValid(String username){
		return false;
	}
	
	public String pushMessage(){
		return "";
	}
	
	private void attemptLogin(){
		server.getMessages();
		
	}
	
	private void attemptLogout(){
		
	}
	
	private void attemptSendMessage(String message){
		
	}
	
	
	

}
