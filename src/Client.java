import java.util.Scanner;

public class Client {
	
	private ServerHandler serverHandler implements Runnable;
	
	void run(){
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
