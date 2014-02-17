# Documentation

To write markdown use this: [Markdown Syntax](http://daringfireball.net/projects/markdown/syntax "Markdown Syntax")

### Table of Contents
- [Protocol](#protocol)
  - [Rough Description](#rough-description)
  - [JSON formatted requests](#json-formatted-requests)
  - [JSON formatted responses](#json-formatted-responses)
- [Classes](#classes)
  - [Server](#server)
  - [ClientHandler](#clienthandler)
  - [Client](#client)
  - [ServerHandler](#serverhandler)

## Protocol

The protocol is based on frequent communication, where the client is the active part sending frequent requests. The server needs to pool up messages ready to be sent to the client, as these can only be sent as a response to a request. However, the client also needs to pool up messages, as it may be taking input from the user at a time that it is currently attempting to retrieve new messages from the server.

### Rough description

| Client  | Server  |
|:-------:|:-------:|
| Request login | If granted, respond with OK and messages, if else respond with an error  |
| Send message  | If granted, respond with OK, if else respond with an error |
| Get messages  | If granted, respond with OK and messages, if else respond with an error |
| Request logout| If granted, respond with OK, if else respond with an error  |

Of course, steps 2. and 3. will usually be repeated frequently, while the login- and logout requests should only happen once; however, this is not for the program to take care of, the user may do as it pleases. The request of getting messages is done automatically by the client, as we want new messages to be displayed as fast as possible.

### JSON formatted requests

This section shows all requests the client should be able to send to the server.

#### Request: Login
| Field | Value |
|:-----:|:-----:|
| request | "login" |
| context  | _username |

#### Request: Send Message
| Field | Value |
|:-----:|:-----:|
| request | "send message" |
| context | _message  |

#### Request: Get Messages
| Field | Value |
|:-----:|:-----:|
| request | "get messages" |

#### Request: Logout
| Field | Value |
|:-----:|:-----:|
| request | "logout" |

### JSON formatted responses

This section shows all responses the server should be able to send to the client.

#### Response: Login
| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "error" |
| context | "username taken" |

| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "error" |
| context | "invalid username" |

| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "OK" |

#### Response: Send Message
| Field | Value |
|:-----:|:-----:|
| response | "send message" |
| status  | "error" |
| context | "not logged in" |

| Field | Value |
|:-----:|:-----:|
| response | "send message" |
| status  | "OK" |

#### Response: Get Messages
| Field | Value |
|:-----:|:-----:|
| response | "get messages" |
| status  | "error" |
| context | "not logged in" |

| Field | Value |
|:-----:|:-----:|
| response | "get messages" |
| status  | "OK" |
| context | [messages] |

#### Response: Logout
| Field | Value |
|:-----:|:-----:|
| response | "logout" |
| status  | "error" |
| context | "not logged in" |

| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "OK" |

## Classes

The classes are a work in progress, we're still working on them :)

![Chat UML](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/ktn_uml.png "Chat UML")

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
| respondToLogin(String)  | Private | Void  | Attempts to log in with the desired username, respond in accordance to task description |
| respondToLogout() | Private | Void  | Attempt to log out the user, respond in accordance to task description |
| respondToSendMessage(String)  | Private | Void  | Attempt to send a message from a user, the message should only be passed to the server if the user is logged in, respond in accordance to task description |
| respondToGetMessages() | Private  | Void  | Attempt to get pooled messages and send to user, respond in accordance to task description |
| isUserNameValid(String) | Private | Boolean | Helper function for deciding if a given username is valid  |
| getUsername() | Public  | String  | Returns the username currently assigned to the socket |
| pushMessage(String) | Public  | Void  | Pushes a given message to the message pool |

### Client

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| server | Private   |  ServerHandler | Holds the object that handles socket connection |
| messages |  Private | ArrayList[String] | Holds messages received |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| init()    | Private   | Void      | Establishes the socket connection to the server, assigns the socket its own thread |
| run()     | Public    | Void      | Waits for input from the user, passes input to handleInput()  |
| handleInput(String) | Private | Void  | Handles the given input command, should notify user if command was not recognized or something went wrong, and push a message to the socket if this is appropriate  |
| pushMessage(String) | Public  | Void  | Push a message to the list of messages  |

### ServerHandler

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| serverSocket  | Private | Socket  | Holds the connection between client and host, passed in the constructor |
| client    | Private   | Client    | Reference to the Client that created the handler, passed in the constructor |
| messagePool | Private | LinkedList[JSONObject] | A pool of messages waiting to be sent to the server  |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| requestLogin(String)  | Public  | Void  | Pushes a login-request with the given username to the message pool |
| requestSendMessage(String)  | Public  | Void  | Pushes a request of sending a given message to the message pool |
| requestLogout()   | Public  | Void  | Pushes logout-request to the message pool  |
| requestGetMessages()  | Private | Void  | Pushes request of getting messages from the server message pool |
| handleResponse(JSONObject) |  Private | Void  | Handles respones from the server |
