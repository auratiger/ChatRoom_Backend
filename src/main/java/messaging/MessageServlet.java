package messaging;

import models.Message;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class MessageServlet {

    private static final long serialVersionUID = -9064629862455451371L;
    private static final Logger logger = Logger.getLogger(MessageServlet.class);

    private static final Set<Session>sessions =
            Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void openConnection(Session session){
        logger.info("User has connected: " + session);
        sessions.add(session);
    }

    @OnClose
    public void closeConnection(Session session){
        logger.info("User has disconnected: " + session);
        sessions.remove(session);
    }

    @OnMessage
    public void message(String message) throws IOException {
        System.out.println(message);
        logger.info("Message: " + message);

//        String userName = message.substring(0, message.indexOf(":"));
//        String messageContent = message.substring(message.indexOf(":") + 1);
//        String messageJson = Json.createObjectBuilder()
//                .add("message", messageContent)
//                .add("author", userName)
//                .build().toString();
//        for (Session session : sessions) {
//            session.getBasicRemote().sendText(messageJson);
//        }
    }

}
