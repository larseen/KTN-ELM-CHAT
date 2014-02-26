package olav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sockets {
	
	public static void main(String[] args) {
		new Sockets();
	}
	
	public Sockets(){ 
		System.out.println("Help");
	}

	
	class SocketReciever {
		
		BufferedReader bufferedReader;
		InputStream inputStream;
		OutputStream outputStream;
		
		String host;
		int port;
		
		Socket reciever;
		
		public SocketReciever(String host, int port){
			this.host = host;
			this.port = port;
			
			try {
				reciever = new Socket(host, port);
				inputStream = reciever.getInputStream();
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				System.out.println(bufferedReader.readLine());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	class SocketSender{
		
		String message = "Hello world";

		PrintWriter writer;
		
		InputStream inputStream;
		OutputStream outputStream;
		String host;
	
		int port;
		Socket sender;
		
		ServerSocket server;
		
		public SocketSender(int port){
			System.out.println("Hei fra ss");
			this.port = port;
		
			try {
				server = new ServerSocket(port);
				sender = server.accept();
				outputStream = sender.getOutputStream();
				writer = new PrintWriter(outputStream, true);
				writer.println(message);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
}
