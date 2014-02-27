package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
	 *							PROTOCOL VARIABLES
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 *	These variables are in place in order to make the implementation
	 *	of the protocol more readable to other people. Also, it enables
	 *	the protocol to be edited with greater ease.
	 */
	private static final String
		RESPONSE_FIELD = "response",
		REQUEST_FIELD = "request",
		STATUS_FIELD = "status",
		CONTEXT_FIELD = "context";
	
	private static final String[] COMMANDS = {"login", "logout", "message", "new message"};
	private static final int LOGIN = 0, LOGOUT = 1, MESSAGE = 2, NEW_MESSAGE = 3;
	
	private static final String[] STATUSES = {"error", "OK"};
	private static final int ERROR = 0, OK = 1;
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *							   OUTPUT STRINGS
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 *	These variables are in place in order to more easily edit the
	 *	output given to the user, as well as for other people to get
	 *	an idea of what to expect from the program.
	 */
	private static final String
		HOST_NOT_FOUND = "The host you were trying to connect to was not found! Please try again.",
		IO_ERROR = "Something went wrong with the I/O, please contact Raymi",
		JSON_ERROR = "Something went wrong with JSON formatting, please contact Raymi",
		LOGIN_GRANTED = "Successfully logged in!",
		LOGOUT_GRANTED = "Successfully logged out!";
	
	
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
		serverHandler = new ServerHandler(HOST, PORT);
		serverHandler.start();
		
		// Main while loop.
		while(true){
			interpretInput(getKeyboardInput(in));
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 						MAIN BREAD AND BUTTER INPUTRESOLVER
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
			case "":
				attemptSendMessage(input);
				break;
		}
	}
	
	/*
	 * CHANGES
	 * getInputs renamed to getKeyboardInput
	 * now returns String
	 */
	public String getKeyboardInput(BufferedReader in){
		try {
			return in.readLine();
		} catch (IOException e) {
			//TODO: Should respond with appropriate message (use pushMessage())
			e.printStackTrace();
			return null;
		}
	}

	//	Tries to log in
	private void attemptLogin(String inputMessage, String loginCommand){
		String userName;
		userName = findUserName(inputMessage, loginCommand);
		serverHandler.requestLogin(userName);
	}
	
	// We are going to send a message here.
	private void attemptSendMessage(String message){
		serverHandler.requestSendMessage(message);
	}
	
	// Finds username given the original message and the appropriate loginCommand.
	private String findUserName(String message, String loginCommand){
		return (String) message.subSequence(loginCommand.length(), message.length());
	}
	
	// Attempts to log out
	private void attemptLogout(){
		serverHandler.requestLogout();
	}
	
	
	private void pushMessage(String message){
		System.out.println(message);
	}
	
	
	/* 	If this is heavily unneeded/a bad way to do it, then my all means kill the structure :)
	 *	Also, the name is pretty shit.
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
	
	
	
	
	
	
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *							SERVER HANDLER
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 *	The server handler carries out all requests given by the client
	 *	by wrapping data into JSON objects and sending it through a
	 *	socket to the server. It also handles all responses from the
	 *	server and notifies the client of any important events such as
	 *	new messages and errors.
	 *
	 *	In essence, the server handler carries out the protocol provided
	 *	in the documentation in a safe and efficient way.
	 *
	 *	NOTE: The server handler runs in a thread of its own. In order
	 *	for the server handler to start listening to the server, you
	 *	need to start it by using .start();
	 */
	class ServerHandler extends Thread {
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *							INITIALIZING VARIABLES
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	In order for the server handler to work properly, we need
		 *	to successfully initialize the BufferedReader to read strings
		 *	from the socket, and the PrintWriter to write strings to it.
		 */
		public ServerHandler(String HOST, int PORT) {
			try {				
				socket = new Socket(HOST, PORT);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				
			}
			catch (UnknownHostException e) { pushMessage(HOST_NOT_FOUND); } 
			catch (IOException e) { pushMessage(IO_ERROR); }
		}
		
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *								RUN
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	As long as the program is running, we wait for the server
		 *	to push messages (responses), we need to handle these
		 *	appropriately through resolveResponse();
		 */
		@Override
		public void run() {
			
			while(true) {
				try { 	
					JSONObject response = new JSONObject(in.readLine());
					resolveResponse(response); 
				} 	
				catch (IOException e) { pushMessage(IO_ERROR); } 
				catch (JSONException e) { pushMessage(JSON_ERROR); }		
			}
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *							RESOLVE RESPONSE
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	resolveResponse() takes a JSONObject, reads its contents and
		 *	uses the protocol to figure out what messages should be pushed
		 *	to the client. The command is given in the response field,
		 *	you can find all available commands in the COMMANDS variable.
		 */
		private void resolveResponse(JSONObject response) throws JSONException {
			
			String responseType = response.getString(RESPONSE_FIELD);
			Integer responseTypeIndex = Arrays.asList(COMMANDS).indexOf(responseType);
			
			switch(responseTypeIndex) {
				case LOGIN:
					resolveLogin(response);
					break;
					
				case LOGOUT:
					resolveLogout(response);
					break;
					
				case MESSAGE:
					resolveMessage(response);
					break;
					
				case NEW_MESSAGE:
					resolveNewMessage(response);
					break;
			}
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *							RESOLVE LOGIN
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	If the login request was denied, we need to notify the client
		 *	with the proper error message (in the context field). If it
		 *	was successful, we tell the client that the request was granted
		 *	and proceed with pushing all messages provided by the server.
		 */
		private void resolveLogin(JSONObject response) throws JSONException {
			String status = response.getString(STATUS_FIELD);
			Integer statusIndex = Arrays.asList(STATUSES).indexOf(status);
			
			switch(statusIndex) {
				case ERROR:
					pushMessage("Error: "+response.getString(CONTEXT_FIELD));
					break;
					
				case OK:
					pushMessage(LOGIN_GRANTED);
					JSONArray messages = response.getJSONArray(CONTEXT_FIELD);
					for(int i = 0; i < messages.length(); i++) { 
						pushMessage( messages.getString(i) );
					}
					break;
			}
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *							RESOLVE LOGOUT
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	If the logout request was erroneous, we need to notify the client,
		 *	if it was successful, we tell the client that the logout was
		 *	granted by the server.
		 */
		private void resolveLogout(JSONObject response) throws JSONException {
			String status = response.getString(STATUS_FIELD);
			Integer statusIndex = Arrays.asList(STATUSES).indexOf(status);
			
			switch(statusIndex) {
				case ERROR:
					pushMessage("Error: "+response.getString(CONTEXT_FIELD));
					break;
					
				case OK:
					pushMessage(LOGOUT_GRANTED);
			}
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *							RESOLVE MESSAGE
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	Retrieve the status from the status field. If the message was sent
		 *	successfully, we do not need to do anything, if the response
		 *	however returns with an error, we need to notify the client.
		 */
		private void resolveMessage(JSONObject response) throws JSONException {
			String status = response.getString(STATUS_FIELD);
			if(status.equals(STATUSES[ERROR])) {
				pushMessage("Error: "+response.getString(CONTEXT_FIELD));
			}
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *						  RESOLVE NEW MESSAGE
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	Retrieve the message from the context field and push it to
		 *	the client. No statuses are necessary.
		 */
		private void resolveNewMessage(JSONObject response) throws JSONException {
			String message = response.getString(CONTEXT_FIELD);
			pushMessage(message);
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *						  		REQUESTS
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	All requests are easily formed using JSON objects. They are made
		 *	in accordance with the protocol provided in the documentation.
		 *	If something should go wrong during the building of JSON objects,
		 *	we notify the client with a JSON_ERROR.
		 */
		public void requestLogin(String username){
			try {
				JSONObject request = new JSONObject();
				request.put(REQUEST_FIELD, COMMANDS[LOGIN]);
				request.put(CONTEXT_FIELD, username);
				sendRequest(request);
			} catch (JSONException e) { pushMessage(JSON_ERROR); }
		}
		
		public void requestSendMessage(String message){
			try {
				JSONObject request = new JSONObject();
				request.put(REQUEST_FIELD, COMMANDS[MESSAGE]);
				request.put(CONTEXT_FIELD, message);
				sendRequest(request);
			} catch (JSONException e) { pushMessage(JSON_ERROR); }
		}
		
		public void requestLogout(){
			try {
				JSONObject request = new JSONObject();
				request.put(REQUEST_FIELD, COMMANDS[LOGOUT]);
				sendRequest(request);
			} catch (JSONException e) { pushMessage(JSON_ERROR); }
			
		}
		
		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
		 *						  		SEND REQUEST
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
		 *	By using our already defined PrintWriter out, it is very simple
		 *	to send JSONObjects as strings.
		 */
		private void sendRequest(JSONObject request) {
			out.println(request.toString());
		}

	}
	
}