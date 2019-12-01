package client;

import com.corundumstudio.socketio.SocketIOClient;

import static server.Server.GAME_UPDATE_EVENT;

public class ClientWrapper {
    private final SocketIOClient client;

    public ClientWrapper(final SocketIOClient client) {
        this.client = client;
    }

    void sendGameUpdateEvent(final ClientMessage message) {
        client.sendEvent(GAME_UPDATE_EVENT, message);
    }
}
