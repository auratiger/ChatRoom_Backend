package messaging;

import models.Message;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void message(Session session, String jsonMessage) throws IOException {

        logger.debug(jsonMessage);

        JsonReader reader = Json.createReader(new StringReader(jsonMessage));
        JsonObject json = reader.readObject();

        JsonObject user = json.getJsonObject("user");
        String username = user.getString("username");
        String message = json.getString("message");

        JsonObject response = Json.createObjectBuilder()
                .add("text", message)
                .add("time", "15:45")
                .add("user", username)
                .add("myMessage", true)
                .build();

        String resp = response.toString();
        logger.debug(resp);

        try {
            for (Session ses : sessions) {
                // not sure if I want the server to send the messages to all clients
                // including the sender or everyone except the sender
                if(ses.isOpen() /*&& !ses.equals(session)*/){
                    ses.getBasicRemote().sendText(resp);
                }
            }
        }catch (IOException ex){
            logger.error("session crash");
            ex.printStackTrace();
        }
    }

}
