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

        Room room = roomDAO.addRoom(new Room("first", LocalDateTime.now()));

        Member member = memberDAO.addMember(new Member("john", "smith", "John",
                "Smith", "auratigera00@gmail.com", LocalDateTime.now(), ""));
        System.out.println("hello");

        Message message = messageDAO.addMessage(new Message("new message", member, room, LocalDateTime.now()));

        System.out.println("-------------------------------------");
        System.out.println(userDAO.userNameExists("john"));
        System.out.println(userDAO.userNameExists("franck"));
        System.out.println(message);


    }
}
