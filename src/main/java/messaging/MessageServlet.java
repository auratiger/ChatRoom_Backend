package messaging;

import Exceptions.NoSuchUserExistsException;
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
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.*;

@ServerEndpoint(value = "/chat/{userid}")
public class MessageServlet {

    private static final long serialVersionUID = -9064629862455451371L;
    private static final Logger logger = Logger.getLogger(MessageServlet.class);

    private static final Map<Long, Session> sessions =
            Collections.synchronizedMap(new HashMap<>());

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
        sessions.put(id, session);
        userDAO.findUserById(id).ifPresent(user -> activeUsers.put(id, user));

        Optional<User> user = userDAO.findUserById(id);
        if(user.isPresent()){
            activeUsers.put(id, user.get());
        }else {
            throw new NoSuchUserExistsException("User with the specified id does not exist");
        }

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
        Message message = new Message(content, user, room);

        messageDAO.addMessage(message);

        room.getUsers().forEach(u -> {
            Session ses = sessions.get(u.getId());
            if(ses.isOpen() && !ses.equals(session)){
                try {
                    ses.getBasicRemote().sendText(message.toJson().toString());
                } catch (IOException e) {
                    logger.error("session crash");
                    e.printStackTrace();
                }
            }
        });
    }

    @OnError
    public void onError(Session session, Throwable t) throws Throwable{
        int count = 0;
        Throwable root = t;
        while (root.getCause() != null && count < 20) {
            root = root.getCause();
            count ++;
        }
        if(root instanceof EOFException) {
            // Assume this is triggered by the user closing their browser and
            // ignore it.
        } else if( root instanceof NoSuchUserExistsException) {
            // should send an error to the client before closing the connection so it stops trying to reconnect
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "No such user exists!"));
            throw t;
        } else {
            throw t;
        }
    }

}
