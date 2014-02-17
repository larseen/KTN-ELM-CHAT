# Documentation

To write markdown use this: [Markdown Syntax](http://daringfireball.net/projects/markdown/syntax "Markdown Syntax")

## Classes

### Server

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| clients   | Private   | ArrayList[ClientHandler]  | Holds all clients, in no specific order     |
| messages  | Private   | ArrayList[String]         | Holds all messages as formmated strings     |
| IPAddress | Private   | ???                       | Holds the IP address of the server host     |
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





### Client

### ServerHandler
