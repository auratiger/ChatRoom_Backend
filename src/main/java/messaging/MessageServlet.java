package messaging;

import content.message.MessageDAO;
import content.room.RoomDAO;
import models.Member;
import models.Message;
import models.Room;
import models.User;
import org.apache.log4j.Logger;
import user.login.UserDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.*;

@ServerEndpoint(value = "/chat/{userid}")
public class MessageServlet {

    private static final long serialVersionUID = -9064629862455451371L;
    private static final Logger logger = Logger.getLogger(MessageServlet.class);

    private static final Set<Session>sessions =
            Collections.synchronizedSet(new HashSet<>());

    private static final Map<Long, User> activeUsers =
            Collections.synchronizedMap(new HashMap<>());

    @Inject
    private UserDAO userDAO;

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private MessageDAO messageDAO;

    @OnOpen
    public void openConnection(@PathParam("userid") long id, Session session){
        logger.info("User has connected: " + session);
        sessions.add(session);
        userDAO.findUserById(id).ifPresent(user -> activeUsers.put(id, user));
        // TODO should give exception if no user by the given id is found
        logger.debug("active user added");
    }

    @OnClose
    public void closeConnection(@PathParam("userid") long id, Session session){
        logger.info("User has disconnected: " + session);
        sessions.remove(session);
        activeUsers.remove(id);
        logger.debug("active user removed");
    }

    @OnMessage
    public void message(@PathParam("userid") long id, Session session, String jsonMessage) throws IOException {

        JsonReader reader = Json.createReader(new StringReader(jsonMessage));
        JsonObject json = reader.readObject();
        long roomid = json.getInt("roomid");
        String content = json.getString("message");

        User user = activeUsers.get(id);
        Room room = roomDAO.findRoomById(roomid).orElse(new Room());
        Message message = new Message(content, user, room, LocalDateTime.now());
        messageDAO.addMessage(message);

        try {
            for (Session ses : sessions) {
                if(ses.isOpen() && !ses.equals(session)){
                    ses.getBasicRemote().sendText(message.toJson().toString());
                }
            }
        }catch (IOException ex){
            logger.error("session crash");
            ex.printStackTrace();
        }
    }

//    @OnError
//    public void onError(Session session, Throwable t){
//        logger.error(t.getMessage());
//    }

}
