package admin;

import content.message.MessageDAO;
import content.room.RoomDAO;
import models.Member;
import models.Message;
import models.Room;
import user.login.UserDAO;
import user.member.MemberDAO;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
@Startup
public class DataLoader {

    @Inject
    private MemberDAO memberDAO;

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private MessageDAO messageDAO;

    @Inject
    private UserDAO userDAO;

    @PostConstruct
    public void load(){

        Room room = roomDAO.addRoom(new Room("General", LocalDateTime.now()));

        Member john = memberDAO.addMember(new Member("john", "smith", "John",
                "Smith", "auratigera00@gmail.com", LocalDateTime.now(), ""));

        Member adam = memberDAO.addMember(new Member("adam", "pass", "Adam",
                "Petals", "adam00@gmail.com", LocalDateTime.now(), ""));

        Message message = messageDAO.addMessage(new Message("Hello", john, room, LocalDateTime.now()));
        messageDAO.addMessage(new Message("Wasup", adam, room, LocalDateTime.now()));
        messageDAO.addMessage(new Message("nothing much, you", john, room, LocalDateTime.now()));

        System.out.println("-------------------------------------");
        System.out.println(userDAO.userNameExists("john"));
        System.out.println(userDAO.userNameExists("franck"));
        System.out.println(message);
        System.out.println(room.getId());

        List<Message> messages = messageDAO.findMessagesByUserAndRoom(john.getId(), room.getId());
        for(Message m: messages){
            System.out.println(m);
        }


    }
}
