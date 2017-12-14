package websocket;
import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebsocketClientEndpoint {

    Session userSession = null;
    private MessageHandler messageHandler;
    
    public WebsocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        userSession.setMaxBinaryMessageBufferSize(10240000);
        userSession.setMaxTextMessageBufferSize(10240000);
    }

 
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }


    @OnMessage
    public void onMessage(String message) {
    	System.out.println(message);
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }


    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

 
    public void sendMessage(String message)  {
    	try{
    		this.userSession.getAsyncRemote().flushBatch();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
        this.userSession.getAsyncRemote().sendText(message);
    }


    public static interface MessageHandler extends javax.websocket.MessageHandler{

        public void handleMessage(String message);
        
    }


	public Session getUserSession() {
		return userSession;
	}


	public void setUserSession(Session userSession) {
		this.userSession = userSession;
	}
    
}