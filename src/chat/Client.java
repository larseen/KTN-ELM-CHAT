package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
public class Client {
	
	private String outputMessage;
	private String inputMessage;
	private String userName;
	private Thread mainThread = new Thread();
	private ServerHandler serverHandler;
	private int port;
	private List<String> viableLogins = Arrays.asList("login ", "log in ", "signin ", "sign in "
			, "connect ", "enter ");
	private List<String> viableLogouts = Arrays.asList("logout", "log out", "signout", "sign out"
			, "disconnect", "exit");
	
	public static void main(String[] args) {
		new Client();
		/*
		String message = "log in lolav";
		String modifiedMessage =  (String) message.subSequence(5, message.length());
		System.out.println(modifiedMessage);*/
	}
	
	public Client() {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	
		// Main while true loop
		while(true){
			getInputs(bf);
			resolveInput(inputMessage);
			System.out.println(userName);
		}
	}
	
	// Metod for getting input from user
	public void getInputs(BufferedReader bf){
		try {
			inputMessage = bf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(inputMessage);
	}

	//Checks if we are trying to log in
	private boolean resolveLogin(String message){
		
		for(int i=0; i< viableLogins.size();i++){
			if(message.startsWith(viableLogins.get(i))){
				userName = cropMessage(message, viableLogins.get(i));
				return true;
			}	
		}
		return false;
	}
	
	// We are going to send a message here.
	private void resolveMessage(String message){
		outputMessage = message;
	}
	
	// Crops message
	private String cropMessage(String message, String loginMessage){
		return (String) message.subSequence(loginMessage.length(), message.length());
	}
	// Checks if we are trying to log out
	private boolean resolveLogout(){
		for(int i=0; i< viableLogouts.size();i++){
			if (inputMessage.equals(viableLogouts.get(i))){
				//Disconnect here
				return true;
			}
		}
		return false;
	}
	
	// Tries to do find out what the message means
	private void resolveInput(String input){
		if(resolveLogin(input)){}
		else if(resolveLogout()){}
		else{
			resolveMessage(input);			
		}
	}
	private void pushMessage(String message){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	class ServerHandler{
		private Socket socket;
		private InputStream inputStream;
		private OutputStream outputStream;
		
		public void requestLogin(String username){
			JSONObject request = new JSONObject();
			try {
				request.put("request", "login");
				request.put("context", username);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public void requestSendMessage(String message){
			JSONObject request = new JSONObject();
			try {
				request.put("request", "message");
				request.put("context", message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		public void requestLogout(){
			JSONObject request = new JSONObject();
			try {
				request.put("request", "logout");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		private void sendRequest(JSONObject request) {
			
		}
		
		private void resolveResponse(JSONObject response){
			
		}
	}
}
