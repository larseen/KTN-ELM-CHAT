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

import org.json.JSONException;
import org.json.JSONObject;
public class Client {
	
	private String message;
	private String userName;
	private Thread mainThread = new Thread();
	private ServerHandler serverHandler;
	private int port;
	private List<String> viableLogins = Arrays.asList("login", "log in", "signin", "sign in"
			, "connect");
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
	BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	
	// Main while true loop
		while(true){
			getInputs(bf);
			resolveInput(message);
		}
	}
	
	// Metod for getting input from user
	public void getInputs(BufferedReader bf){
		try {
			message = bf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(message);
	}

	private void resolveLogin(String message){
		for(int i=0; i< viableLogins.size();i++){
			if(message.startsWith(viableLogins.get(i))){
				System.out.println("Det funker");
			}	
		}
	}
	
	private void resolveLogout(){
		
	}
	
	private void resolveInput(String input){
		resolveLogin(input);
		resolveLogout();
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
