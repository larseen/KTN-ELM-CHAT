package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;
public class Client {
	
	private String message;
	private String userName;
	private Thread mainThread = new Thread();
	private ServerHandler serverHandler;
	private int port;	
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
	
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			getInputs(bf);
		}
	}
	
	// Metod for getting input from user
	public void getInputs(BufferedReader bf){
		String melding = "";
		try {
			melding = bf.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(melding);
	}

	
	private void resolveInput(String input){
		
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
