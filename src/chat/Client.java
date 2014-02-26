package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class Client {
	
	private String host ;
	private int port;
	
	private String outputMessage = "I don't have a message yet!";
	private String inputMessage;
	private String userName;
	private Thread mainThread;
	private ServerHandler serverHandler;
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
		//serverHandler = new ServerHandler(host, port);
		
		// Main while true loop
		while(true){
			getInputs(bf);
			resolveInput(inputMessage);
			System.out.println(userName);
			System.out.println(outputMessage);
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
		for(int i = 0; i< viableLogouts.size();i++){
			if (inputMessage.equals(viableLogouts.get(i))){
				serverHandler.requestLogout();
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
	
	
	
	
	
	
	
	
	
	
	
	class ServerHandler extends Thread {
		private Socket socket;
		private InputStream inputStream;
		private OutputStream outputStream;
		private BufferedReader in;
		private PrintWriter out;
		
		public ServerHandler(String host, int port) {
			try {
				socket = new Socket(host, port);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				in = new BufferedReader(new InputStreamReader(inputStream));
				out = new PrintWriter(outputStream, true);
				
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
			
			while(true) {
				
				try {
					//Get response
					JSONObject response = new JSONObject(in.readLine());
					resolveResponse(response);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		
		public void requestLogin(String username){
			JSONObject request = new JSONObject();
			try {
				request.put("request", "login");
				request.put("context", username);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendRequest(request);
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
			sendRequest(request);
		}
		
		public void requestLogout(){
			JSONObject request = new JSONObject();
			try {
				request.put("request", "logout");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sendRequest(request);
		}
		
		private void sendRequest(JSONObject request) {
			out.println(request.toString());
		}
		
		private void resolveResponse(JSONObject r){
			try {
				String response = r.getString("response");
				if (response.equals("login")){
					
					String status = r.getString("status");
					if (status.equals("error")){
						pushMessage("Error: "+ r.getString("context"));
					}
					else if (status.equals("OK")){
						JSONArray a = r.getJSONArray("messages");
						for (int i = 0; i<a.length(); i++){
							pushMessage(a.getString(i));
						}
					}
					
				}
				else if (response.equals("message")){
					
					String status = r.getString("status");
					if (status.equals("error")){
						pushMessage("Error: "+ r.getString("context"));
					}
					
				}
				else if (response.equals("logout")){
					
					String status = r.getString("status");
					if (status.equals("error")){
						pushMessage("Error: "+ r.getString("context"));
					}
					else if (status.equals("OK")){
						pushMessage("You have been logged out");
					}
					
				}
				else if(response.equals("new message")){
					
					pushMessage(r.getString("context"));
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
