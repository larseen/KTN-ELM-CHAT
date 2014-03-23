package chat;

import java.nio.charset.Charset;

import org.json.JSONObject;

import com.corundumstudio.socketio.SocketIOClient;

public class JavaScriptClientHandler extends ClientHandler {
	
	SocketIOClient client;
	public JavaScriptClientHandler(Server server, SocketIOClient client, int ID) {
		super(server, ID);
		this.client = client;
	}


	@Override
	protected void sendJSONObject(JSONObject response) {
		client.sendMessage(response.toString());
	}


	@Override
	public void terminate() { return; }

}
