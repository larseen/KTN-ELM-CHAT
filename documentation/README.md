# Documentation

To write markdown use this: [Markdown Syntax](http://daringfireball.net/projects/markdown/syntax "Markdown Syntax")

### Table of Contents
- [Protocol](#protocol)
  - [Sequence Diagram](#sequence-diagram)
  - [JSON formatted requests and responses](#json-formatted-requests-and-responses)
- [Structure](#structure)
  - [Basic Structure](#basic-structure)
  - [Threading](#threading)
- [Classes](#classes)
  - [Server](#server)
  - [ClientHandler](#clienthandler)
  - [Client](#client)
  - [ServerHandler](#serverhandler)

## Protocol

The protocol is based on interchanging messages called requests and responses. An array of different requests are available to the client, starting with simple operation such as "login", "logout" and "message". Whenever the client sends a request, the server needs to send a response supported in the protocol.

In most cases, the server only sends responses when a request is received. However, when a new message is received by the server, the server needs to broadcast this to all users. In order to do this, a response is generated without any request, this type of response is called a "newMessage", this keyword is still up for consideration.

### Sequence Diagram

The sequence diagram describes how the protocol works. Note however that any command can be given by the user at any time. For example, a user might try to send a message, even though he/she is not logged in. If this happens, the response from the server needs to reflect upon this error. The sequence diagram portraits what a succesful run through the program might look like.

![Sequence Diagram](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/Sequence%20Diagram.png "Sequence Diagram")

### JSON formatted requests and responses

All the requests and responses are formatted in JSON notation. This is not particularly useful for a Java implementation, but it is required by the task description. JSON formating may prove more useful later on in the project. Here's a list of current request/response formats:

- [Request: Login](#request-login)
- [Request: Send Message](#request-send-message)
- [Request: Logout](#request-logout)
- [Response: Login](#response-login)
- [Response: Send Message](#response-send-message)
- [Response: Logout](#response-logout)
- [Response: New Message](#response-new-message)

#### Request: Login
| Field | Value |
|:-----:|:-----:|
| request | "login" |
| context | username |

In order to send a login request, use the request "login" along with the desired username in the context field.

#### Request: Send Message
| Field | Value |
|:-----:|:-----:|
| request | "message" |
| context | message  |

In order to send a message, use the request "message" along with the desired message in the context field.

#### Request: Logout
| Field | Value |
|:-----:|:-----:|
| request | "logout" |

In order to send a logout request, use the request "logout" (no context required).

#### Response: Login
| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "error" |
| context | "username taken" |

If the desired username was taken, the response should return with the status "error" along with the context "username taken".

| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "error" |
| context | "username invalid" |

If the desired username was invalid, the response should return with the status "error" along with the context "username invalid".

| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "OK" |
| messages | [messages] |

If the desired username met the server's requirements, the response should return with the status "OK" along with all messages posted in the chat put in the "messages" field.

#### Response: Send Message
| Field | Value |
|:-----:|:-----:|
| response | "message" |
| status  | "error" |
| context | "not logged in" |

If the message was sent by a client not logged in, the response should return with the status "error" along with the context "not logged in".

| Field | Value |
|:-----:|:-----:|
| response | "send message" |
| status  | "OK" |

If the message was received successfully and accepted by the server, the response should return with the status "OK".

#### Response: Logout
| Field | Value |
|:-----:|:-----:|
| response | "logout" |
| status  | "error" |
| context | "not logged in" |

If the client was not logged in when requesting a logout, the response should return with the status "error" along with the context "not logged in".

| Field | Value |
|:-----:|:-----:|
| response | "login" |
| status  | "OK" |

If the client was successfully logged out from the server, the response should return with the status "OK".

#### Response: New Message
| Field | Value |
|:-----:|:-----:|
| response | "new message" |
| context  | message |

In the "new message" response, the message that was broadcasted by the server should be placed in the context field.

## Structure

### Basic Structure

The chat consists of four classes, the Client with the inner class ServerHandler, and the Server with the inner class ClientHandler. This is shown in the simple structural UML shown below:

![Simple Structure](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/Simple%20Structure.png "Simple Structure")

The Client and Server classes implement the chat without taking the protocol into the account, as well as leaving the real communication to the handler classes. This is roughly shown in the data flowchart below:

![Data Flowchart](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/Data%20Flowchart.png "Data Flowchart")

Notice how the server needs to have a individual ClientHandler for each client connected to the server. The individual classes are explained more in depth in the [Classes](#classes) section.

### Threading

The operations needed to read input from console and sockets cause the program to halt, making the implementation impossible without the use of multi-threading. Every time we need a thread to stay put and listen for input, we spawn a new thread so that the program can still respond to other events. This is illustrated by two thread flow charts:

![Thread Chart - Client](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/Thread%20Chart%20-%20Client.png "Thread Chart - Client")

The main thread is the one of the client, where input from the user is resolved and brought to the ServerHandler as requests. It is responsible for interrupting the two other threads when the program is asked exit.

![Thread Chart - Server](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/Thread%20Chart%20-%20Server.png "Thread Chart - Server")

The server thread needs no user input, but in order to serve multiple clients, it needs to assign a new thread to each client establishing a connection. Furthermore, we need to take use both of the input- and outputstream of the client's socket. This means that each ClientHandler object also needs to create a new thread in order to be able to listen to the socket at the same time as sending data through it.

In addition, the server thread needs to close all threads it has started upon exiting, and each thread needs to interrupt its own socket thread before closing.

## Classes

The classes are a work in progress, we're still working on them! As a result, some of the tables are somewhat inaccurate.

### Server

![Server UML](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/UML%20-%20Server.png "Server UML")

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

![Server UML](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/UML%20-%20ClientHandler.png "Server UML")

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

![Server UML](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/UML%20-%20Client.png "Server UML")

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

![Server UML](https://raw2.github.com/larseen/KTN-ELM-CHAT/master/documentation/resources/UML%20-%20ServerHandler.png "Server UML")

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
