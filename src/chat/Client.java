package chat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Client implements Runnable {
	
	// Just some testing environment :)
	public static void main(String[] args) {
		
		Client c = new Client();
		
		String s = "login";
		String lowerCasedS = s.toLowerCase();
		
		String userName;
		List<String> acceptedLogins =Arrays.asList("login", "log in", "signin", "sign in", "connect");
		int indexOfLogin = acceptedLogins.indexOf(s);
		boolean stringIsContained = acceptedLogins.contains(lowerCasedS);
		
		if(stringIsContained){
			c.findUserName(lowerCasedS, indexOfLogin);
			System.out.println(s);
			
		}
		else {
			System.out.println("Nope");
		}
	}
	
	private void findUserName(String input, int index){
		String userName = input.subSequence(index, input.length()).toString();
		System.out.println(userName);
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
