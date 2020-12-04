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

        Room room = roomDAO.addRoom(new Room("General"));

        Member john = memberDAO.addMember(new Member("john", "smith", "John",
                "Smith", "auratigera00@gmail.com", LocalDateTime.now()));

        Member adam = memberDAO.addMember(new Member("adam", "pass", "Adam",
                "Petals", "adam00@gmail.com", LocalDateTime.now()));

        Message message = messageDAO.addMessage(new Message("Hello", john, room));
        messageDAO.addMessage(new Message("Wasup", adam, room));
        messageDAO.addMessage(new Message("nothing much, you", john, room));

        System.out.println(userDAO.userNameExists("john"));
        System.out.println(userDAO.userNameExists("franck"));
        System.out.println(message);
        System.out.println(room.getId());

        List<Message> messages = messageDAO.findMessagesByUserAndRoom(john.getId(), room.getId());
        for(Message m: messages){
            System.out.println(m);
        }

        roomDAO.addRoom(new Room("new 1")).addUser(john);
        Room shared1 = roomDAO.addRoom(new Room("new 2"));
        Room shared2 = roomDAO.addRoom(new Room("new 3"));
        shared1.addUser(john);
        shared1.addUser(adam);
        shared2.addUser(john);
        shared2.addUser(adam);

        room.addUser(john);
        room.addUser(adam);

        List<Room> rooms = roomDAO.findOrderedRoomsByUser(john.getId());
        for(Room room1 : rooms){
            System.out.println(room1);
        }




    }
}
