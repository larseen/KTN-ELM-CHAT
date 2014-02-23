import java.net.InetAddress;
import java.util.Scanner;
public class Client implements Runnable {
	
	public static void main(String[] args) {
		Client c = new Client();
		String s = "Login";
		// Login or login -> [L|l]og\\s?+in
		if(s.matches("[L|l]og\\s?+in")){
			System.out.println(s);
		}
		else {
			System.out.println("Nope");
		}
	}
	private String userName;
	
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
