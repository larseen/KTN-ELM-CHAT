KTN-ELM-CHAT
===========

##Contributors
- [Kristoffer Larsen](https://github.com/larseen "larseen@github")
- [Heidi Svendsen](https://github.com/larseen "heidisv@github")
- [Mari Bergendahl](https://github.com/larseen "mabergen@github")
- [Olav Markussen](https://github.com/larseen "shorm@github")
- [Julie Vanessa Skåtun](https://github.com/larseen "julievs@github")
- [Raymi T. Eldby](https://github.com/larseen "khrall@github")

##Parts

The App consists of three parts
- Java Server
- Java Client
- Web Client

> Note: You need to run both Java Server and Java Client as UTF-8 encoded Java for special characters. This is done by adding the prefix ```-Dfile.encoding=UTF-8```, see examples below.

###Java Server
Located in the [runnable folder](/runnable) as server.jar, serves clients of choice on a given port.

```java -Dfile.encoding=UTF-8 -jar server.jar 8999```

Hosts a chat server on port ```8999```.

Once the program has started, the following commands are available:
- ```status``` Displays the IP and ports that the server is running on.
- ```users``` Displays all connected clients. Clients not logged in are shown as ```null```.
- ```stop``` Disconnects all connected clients, shuts the server down and terminates the program properly.

###Java Client
Located in the [runnable folder](/runnable) as client.jar, connects to the server with running on the given IP with given port.

```java -Dfile.encoding=UTF-8 -jar client.jar 192.168.1.4 8999```

Connects to the server running on ```192.168.1.4``` with port ```8999```.

Once the program has started, the following commands are available:
- ```login [username]``` Attempts to log in without the given username.
- ```[message]``` Attempts to send a message to the server.
- ```logout``` Attempts to log out of the server.
- ```quit``` Disconnects from the server and terminates the program properly.


###Web Client
Located in the [web folder](/web) is an example web client. Just open "index.html", and it will attempt to connect to a server. In order to set the right server IP and port, use:

```/setServer 192.168.1.4:9000```

On doing this, the client will attempt tp connect to the server running on IP ```192.168.1.4``` with port ```9000``` 
> Note that the web server port is the port number given when starting the server plus one, use ```status``` in the console window running the server in order to review your server parameters.

Once the client is connected, the following commands are available:
- ```/login [username]``` Attempts to log in without the given username.
- ```[message]``` Attempts to send a message to the server.
- ```/logout``` Attempts to log out of the server.
