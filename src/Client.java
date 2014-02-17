import java.util.Scanner;

public class Client implements Runnable {
	
	private ServerHandler serverHandler;
	
	@Override
	public void run(){
		while(true){
			String input = new Scanner(System.in).nextLine();
			handleInput(input);
		}
	}
	
	private void handleInput(String input){
		
	}
	
	private void pushMessage(){
		
	}
	
}
