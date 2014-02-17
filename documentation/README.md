# Documentation

To write markdown use this: [Markdown Syntax](http://daringfireball.net/projects/markdown/syntax "Markdown Syntax")

## Classes

### Server

| Field     | Scope     | Type                      | Description                                 |
|:---------:|:---------:|:-------------------------:|:--------------------------------------------|
| Clients   | Private   | ArrayList[ClientHandler]  | Holds all clients, in no specific order     |
| Messages  | Private   | ArrayList[String]         | Holds all messages as formmated strings     |
| IPAddress | Private   | ???                       | Holds the IP address of the server host     |
| Port      | Private   | Integer                   | Holds the TCP port for the server to run on |

| Function  | Scope     | Returns   | Description   |
|:---------:|:---------:|:---------:|:--------------|
| init()    | Private   | Void      | Sets IP address dynamically, starts the server and outputs status |
| run()     | Public    | Void      | Waits for connections and assigns new threads for each new client |
| pushMessage(String) | Public  | Void      | Pushes a message to the server, the server should then push the message to all members of the chat |
| getMessages() | Public  | ArrayList[String] | Returns a list of messages already sent in the chat |
| isUsernameAvailable(String) | Public  | Boolean   | Returns true if the username given is currently available, returns false otherwise |




### ClientHandler


### Client

### ServerHandler
