import java.net.InetAddress;
import java.util.Scanner;
public class Client implements Runnable {
	
	private String unserName;
	
	private Thread mainThread = new Thread();
	private ServerHandler serverHandler;
	
	//InetAddress address= new InetAddress();
	private int port;
	
	@Override
	public void run(){
		while(true){
			@SuppressWarnings("resource")
			String input = new Scanner(System.in).nextLine();
			resolveInput(input);
		}
	}
	
	private void resolveInput(String input){
		
	}
	
	private void pushMessage(String message){
		
	}
	
}
