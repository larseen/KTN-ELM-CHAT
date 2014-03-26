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
	//TODO: Add the BufferedReader as a field.
	
	public JavaClientHandler(Server server, Socket socket, int ID) {
		super(server, ID);
		this.socket = socket;
		
		try {
			IS = socket.getInputStream();
			OS = socket.getOutputStream();
			out = new PrintWriter(OS, true);
			/*
			 * TODO: We don't really need the input- and output stream. Instead,
			 * initiate the BufferedReader and PrintWriter here.
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void sendJSONObject(JSONObject respons) {
		String responsString = respons.toString();
		out.println(responsString);
	}
	
	@Override
	public void run() {
		while (isAlive) {
			String message;
			try {
				//TODO: Clean
				message = ((new BufferedReader(new InputStreamReader(IS,"UTF-8"))).readLine());
				if(message == null) {
					terminate();
					return;
				}
				
				JSONObject jsonObj = new JSONObject(message);
				handleMessage(jsonObj);
			} 
			catch (IOException e1) { 
				if(isAlive) terminate();
			} 
			catch (JSONException e) { 
				 
			}
		}
	}

	@Override
	public void terminate() {
		super.terminate();
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
