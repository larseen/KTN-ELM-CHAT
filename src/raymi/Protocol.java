package raymi;

public class Protocol {
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *							PROTOCOL VARIABLES
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  
	 *	These variables are in place in order to make the implementation
	 *	of the protocol more readable to other people. Also, it enables
	 *	the protocol to be edited with greater ease.
	 *	
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 *						   AVAILABLE FUNCTIONALITY
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *
	 *	new message
	 * 	new user
	 * 	login
	 * 	logout
	 * 	update
	 * 	delete
	 *	 
	 * 	backlog
	 * 	users
	 *	 
	 * 	user created
	 * 	user kicked
	 * 	user disconnected
	 * 	user connected
	 * 	user changed name
	 * 	user became mod
	 * 	user wrote message
	 * 	user set motd
	 * 	user deleted
	 * 
	 */
	
	@SuppressWarnings("unused")
	private static final String
		RESPONSE_FIELD = "response",
		REQUEST_FIELD = "request",
		SERVER_PUSH_FIELD = "server push",
		STATUS_FIELD = "status",
		ERROR_FIELD = "error";
	
	@SuppressWarnings("unused")
	private static final String[] COMMANDS = {
		"new message", "new user", "login", "logout", "update", "kick", "delete",
		"backlog", "users",
		"user created", "user kicked", "user disconnected", "user connected",
		"user changed name", "user became mod", "user wrote message", "user set motd",
		"user deleted" };
	
	@SuppressWarnings("unused")
	private static final int 
	NEW_MESSAGE = 0, NEW_USER = 1, LOGIN = 2, LOGOUT = 3, UPDATE = 4, KICK = 5, DELETE = 6,
	BACKLOG = 7, USERS = 8,
	USER_CREATED = 9, USER_KICKED = 10, USER_DISCONNECTED = 11, USER_CONNECTED = 12,
	USER_CHANGED_NAME = 13, USER_BECAME_MOD = 14, USER_WROTE_MESSAGE = 15, USER_SET_MOTD = 16,
	USER_DELETED = 17;
	
	@SuppressWarnings("unused")
	private static final String[] STATUSES = {"ERROR", "OK"};
	
	@SuppressWarnings("unused")
	private static final int ERROR = 0, OK = 1;
	
	@SuppressWarnings("unused")
	private static final String[] ERRORS = {
		"You are already logged in!", 
		"The requested username is already taken.", 
		"You are not logged in.", 
		"You are not a moderator."};
	
	@SuppressWarnings("unused")
	private static final int 
	ALREADY_LOGGED_IN = 0, USERNAME_TAKEN = 1, 
	NOT_LOGGED_IN = 2, NOT_MODERATOR = 3;

	@SuppressWarnings("unused")
	private static final String[] CTX = { //CONTEXT
		"username",
		"password",
		"message",
		"timestamp",
		"color",
		"userlist",
		"messagelist",
		"id" };
	
	@SuppressWarnings("unused")
	private static final int
	USERNAME = 0, PASSWORD = 1, MESSAGE = 2,
	TIMESTAMP = 3, COLOR = 4, USERLIST = 5,
	MESSAGELIST = 6, ID = 7;
}
