package raymi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerManager extends Thread {

	BufferedReader in;
	Server server;
	
	public ServerManager(Server server) {
		in = new BufferedReader(new InputStreamReader(System.in));
		this.server = server;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String input = in.readLine();
				if(input.equals("stop")) break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try { in.close(); } 
		catch (IOException e) { e.printStackTrace(); }
		System.out.println("Attempting to terminate the server ...");
		server.stop();
	}
}
