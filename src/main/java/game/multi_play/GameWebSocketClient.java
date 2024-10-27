package game.multi_play;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class GameWebSocketClient {

    private Session session;
    private MessageHandler messageHandler;

    public GameWebSocketClient(String url) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to the server");
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public interface MessageHandler {
        void handleMessage(String message);
    }
}
