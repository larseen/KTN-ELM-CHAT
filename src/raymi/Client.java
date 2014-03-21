package raymi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 								CLIENT CLASS
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 	There are two classes in Client, namely Client and ServerHandler.
 * 	Documentation for ServerHandler is found somewhere further down in the document.
 * 
 *	The Client is mainly taking input from the user and interpret what it means.
 *	The message is fetched by using a buffered reader to get one message at a time from the user.
 *	When the message is gathered, it will undergo three checks to classify if it either:
 *		1) Is a login message.
 *		2) Is a logout message.
 *		3) Is an actual message the user wants to send.
 *	When a message is classified as one of the above, the appropriate corresponding
 *	method in ServerHandler is called upon.
 *
 *	The Client is always in a while(true) loop, initiated in the constructor.
 *	In the constructor a new ServerHandler is spawned.
 */
public class Client {
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *							COMMAND TRIGGERS
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 *	These are lists which contains the login and logout messages we are accepting.
	 *	They are used in requestLogin and requestLogut respectively.
	 */
	// TODO: Reconsider variable names
	private static final List<String> viableLogins = Arrays.asList("login ", "log in ", "signin ", "sign in "
			, "connect ", "enter "),
			viableLogouts = Arrays.asList("logout", "log out", "signout", "sign out",
					"disconnect", "exit");
	
	/*  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 							SERVER HANDLER
	 *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 	Every client has a ServerHandler which will help the client send output and gather input 
	 * (not from the keyboard, but rather messages from the main server).
	 * 	serverHandler is called upon when the Client has classified what type of input the user gave it.  
	 */
	private static ServerHandler serverHandler;
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 								INITIALIZE VARIABLES
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	To get the server up and running we need to initialize variables concerning the 
	 *	fundamental operation of the Client, such as a ServerHandler and a BufferedReader.
	 *	As documented in the ServerHandler class, after initializing the ServerHandler,
	 *	one must also start() it for it to function properly.
	 *
	 *	The Client will at all times sit in the while loop in its constructor.
	 *	Here it is always trying to interpret what has been given to it, and if 
	 *	it can do something with the input and then do it.
	 */
	public Client(String HOST, int PORT) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		serverHandler = new ServerHandler(this, HOST, PORT);
		serverHandler.start();
		
		// Main while loop.
		while(true){
			interpretInput(getKeyboardInput(in));
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 						MAIN BREAD AND BUTTER INPUTINTERPRETER
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	Here, the Client has recognized it has been given input and is now trying to classify
	 *	which of the three possible inputs the given command is, then do the appropriate action.
	 *
	 *	Firstly we check if the input is a login function, then whether it is a logout function.
	 *	If the command in none of the above, it is a message the user wants to send 
	 *	and attempeSendMessage is called upon.
	 * 
	 */
	private void interpretInput(String input){
		
		InputCommand command = new InputCommand(input);
		switch (command.getTypeOfCommand()){
			case "login":
				attemptLogin(input, command.getCommand());
				break;
			case "logout":
				attemptLogout();
				break;
			case "message":
				attemptSendMessage(input);
				break;
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 					READING INPUT FROM THE USER
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	InterpretInput needs to get the actual input form somewhere,
	 *	this is where getKeyboardInput is called.
	 *	The method reads line by line from a BufferedReader.
	 *	If something goes wrong it prints an errormessage to the user
	 *	and terminates the program. 
	 */
	public String getKeyboardInput(BufferedReader in){
		try {
			return in.readLine();
		} catch (IOException e) {
			pushMessage("Something went wrong when trying to read input");
			e.printStackTrace();
			return null;
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 						LOGIN SEQUENCE
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 	Here, we already figured out that the inputmessage is a login command.
	 * 	This we did in interpretInput.
	 * 	
	 * 	attemptLogin finds the desired username and calls the serverhandler's
	 *  requestLogin with said username. 
	 */
	private void attemptLogin(String inputMessage, String loginCommand){
		String userName;
		userName = findUserName(inputMessage, loginCommand);
		serverHandler.requestLogin(userName);
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 						SENDING A MESSAGE
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	interpretInput concluded that the given input is a message to be
	 *	sent to other users. attemptSendMessage is then called with the 
	 *	given message and calls the serverhandler's 
	 *	requestSendMessage-method with said message.
	 */
	private void attemptSendMessage(String message){
		serverHandler.requestSendMessage(message);
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *						FINDING A USERNAME
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 	findUserName is called in attemptLogin to get a sensible username 
	 * 	out of the command.
	 * 	
	 * 	findUserName is really straightforward, it gets the original message
	 * 	with the corresponding command, crops the message so it only contains 
	 * 	the desired username.
	 */
	private String findUserName(String message, String loginCommand){
		return (String) message.subSequence(loginCommand.length(), message.length());
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 						USER WANTS TO LOG OUT
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	If interpretInput concluded that the user wants to log out, attemptLogout
	 *	is called.
	 *	The method tries to log out, using the serverhandler's method
	 *	'requestLogout()'.
	 */
	private void attemptLogout(){
		serverHandler.requestLogout();
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 						SIMPLE METHOD FOR PRINTING A MESSAGE
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	If we want to print a message to the user, pushMessage will take
	 *	care of it. This includes, but is not limited to sending errormessages.
	 */
	public void pushMessage(String message){
		System.out.println(message);
	}
	
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *			REPRESENTING A COMMAND AND IT'S TYPE AS ONE OBJECT
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *	If we want to return a command, wether it be a login or logout, 
	 *	we can also represent it with its type('logout' or 'login').
	 *	InputCommand is made to do just this. It also finds the type
	 *	of command in a given input, it does not do anything more.
	 *	Well, it can compare two commands..
	 */
	class InputCommand{
		private String command, typeOfCommand;
		
		public InputCommand(String input){
			for(String viableLogin: viableLogins){
				if(input.startsWith(viableLogin)){
					this.command = viableLogin;
					this.typeOfCommand = "login";
					return;
				}
			}
			
			for(String viableLogout: viableLogouts){
				if(input.startsWith(viableLogout)){
					this.command = viableLogout;
					this.typeOfCommand = "logout";
					return;
				}
			}
			
			this.command = null;
			this.typeOfCommand = "message";
		}
		
		public String getCommand(){
			return command;
		}
		
		public String getTypeOfCommand(){
			return typeOfCommand;
		}
		
		public boolean isCommand(String staticCommand){
			if(command.equals(staticCommand)){
				return true;
			}
			return false;	
		}
	}
	
	public static void main(String[] args) {
		new Client(args[0], Integer.valueOf(args[1]));
	}
	
}