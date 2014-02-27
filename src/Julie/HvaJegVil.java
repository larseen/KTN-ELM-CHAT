package julie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HvaJegVil {
	
	
	class SocketReciever{
		Socket reciever;
		InputStream inputStream;
		OutputStream outputStream;
		BufferedReader in;
		
		
		public SocketReciever(String host, int port){
			try {
				reciever = new Socket(host, port);
				inputStream = reciever.getInputStream();
				in = new BufferedReader(new InputStreamReader(inputStream));
				System.out.println(in.readLine());
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
	}
	
	class SocketSender{
		Socket sender;
		InputStream inputStream;
		OutputStream outputStream;
		ServerSocket server;
		PrintWriter out;
		
		public SocketSender(String host, int port){
			try {
				server = new ServerSocket(port);
				sender = server.accept();
				outputStream = sender.getOutputStream();
				out = new PrintWriter(outputStream, true);
				out.println("hei");
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public HvaJegVil(){
		new SocketReciever("78.91.18.24", 8888);
	}
	
	public static void main(String[] args) {
		
		new HvaJegVil();
		
	}
}
