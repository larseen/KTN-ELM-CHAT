# Documentation

To write markdown use this: [Markdown Syntax](http://daringfireball.net/projects/markdown/syntax "Markdown Syntax")

## Classes

### Server

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| clients   | Private   | ArrayList[ClientHandler]  | Holds all clients, in no specific order     |
| messages  | Private   | ArrayList[String]         | Holds all messages as formmated strings     |
| IPAddress | Private   | InetAddress               | Holds the IP address of the server host     |
| port      | Private   | Integer                   | Holds the TCP port for the server to run on |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| init()    | Private   | Void      | Sets IP address dynamically, starts the server and outputs status |
| run()     | Public    | Void      | Waits for connections and assigns new threads for each new client |
| pushMessage(String) | Public  | Void      | Pushes a message to the server, the server should then push the message to all members of the chat |
| getMessages() | Public  | ArrayList[String] | Returns a list of messages already sent in the chat |
| isUsernameAvailable(String) | Public  | Boolean   | Returns true if the username given is currently available, returns false otherwise |

### ClientHandler

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| clientSocket  | Private | Socket  | The socket between host and client, this is passed in the constructor |
| server  | Private | Server  | Reference to the server that created the ClientHandler, passed in the constructor |
| username  | Private | String  | Username for the client, should be Null whenever the client is not logged in |
| messagePool | Private | LinkedList[JSONObject] | A pool of messages waiting to be sent to the client.  |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| run() | Public  | Void  | Waits for input from the socket |
| handleMessage(JSONObject) | Private | Void  | Handles a message received from the client  |
| attemptLogin(String)  | Private | Void  | Attempts to log in with the desired username, respond in accordance to task description |
| attemptLogout() | Private | Void  | Attempt to log out the user, respond in accordance to task description |
| attemptSendMessage(String)  | Private | Void  | Attempt to send a message from a user, the message should only be passed to the server if the user is logged in, respond in accordance to task description |
| isUserNameValid(String) | Private | Boolean | Helper function for deciding if a given username is valid  |
| getUsername() | Public  | String  | Returns the username currently assigned to the socket |
| pushMessage(String) | Public  | Void  | Pushes a given message to the message pool |

### Client

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| server | Private   |  ServerHandler | Holds the object that handles socket connection |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| init()    | Private   | Void      | Establishes the socket connection to the server, assigns the socket its own thread |
| run()     | Public    | Void      | Waits for input from the user, passes input to handleInput()  |
| handleInput(String) | Private | Void  | Handles the given input command, should notify user if command was not recognized or something went wrong, and push a message to the socket if this is appropriate  |

### ServerHandler

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| serverSocket  | Private | Socket  | Holds the connection between client and host, passed in the constructor |
| client    | Private   | Client    | Reference to the Client that created the handler, passed in the constructor |
| messagePool | Private | LinkedList[JSONObject] | A pool of messages waiting to be sent to the server  |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| attemptLogin(String)  | Public  | Void  | Pushes a login-attempt with the given username to the message pool |
| attemptSendMessage(String)  | Public  | Void  | Pushes an attempt of sending a given message to the message pool |
| attemptLogout()   | Public  | Void  | Pushes logout-attempt to the message pool.  |
