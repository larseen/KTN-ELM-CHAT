package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class JavaClientHandler extends ClientHandler {
	private Socket socket;
	private InputStream IS;
	private OutputStream OS;
	private PrintWriter out;
	private boolean isAlive = true;
	//TODO: Add the BufferedReader as a field.
	
	public JavaClientHandler(Server server, Socket socket, int ID) {
		super(server, ID);
		this.socket = socket;
		
		try {
			IS = socket.getInputStream();
			OS = socket.getOutputStream();
			/*
			 * TODO: We don't really need the input- and output stream. Instead,
			 * initiate the BufferedReader and PrintWriter here.
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void sendJSONObject(JSONObject respons) {
		String responsString = respons.toString();
		out = new PrintWriter(OS, true);
		out.println(responsString);
	}
	
	//Not done
	public void kill() throws IOException {
		socket.close();
	}
	
	@Override
	public void run() {
		while (isAlive) {
			String message;
			try {
				//TODO: Clean
				message = ((new BufferedReader(new InputStreamReader(IS))).readLine());
				if(!isAlive) { return; }
				
				JSONObject jsonObj = new JSONObject(message);
				handleMessage(jsonObj);
			} 
			catch (IOException e1) { 
				// TODO: Clean up the client by logging out and notifying the server
			} 
			catch (JSONException e) { /* TODO Auto-generated catch block */ }
		}
	}

	@Override
	public void terminate() {
		isAlive = false;
		try {
			IS.close();
			OS.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
