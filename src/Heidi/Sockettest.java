package Heidi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sockettest {

	
	class Socketsender{
		InputStream IS;
		OutputStream OS;
		PrintWriter out;
		Socket sender;
		ServerSocket server;
		public Socketsender(int port){
			try {
				server = new ServerSocket(port);
				sender = server.accept();
				IS = sender.getInputStream();
				OS = sender.getOutputStream();
				out = new PrintWriter(OS, true);
				out.println("hei");
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Socketreceiver{
		Socket receiver;
		InputStream IS;
		OutputStream OS;
		BufferedReader in;
		public Socketreceiver(String host, int port){
			try {
				receiver = new Socket(host, port);
				IS = receiver.getInputStream();
				OS = receiver.getOutputStream();
				in = new BufferedReader(new InputStreamReader(IS));
				System.out.println(in.readLine());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Sockettest(){
		new Socketreceiver("78.91.27.86",8888);
	}
	
	public static void main(String[] args) {
		new Sockettest();

	}

}
