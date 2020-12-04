package content.room;

import models.Room;
import models.User;
import org.apache.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RoomDAO {

    private static Logger logger = Logger.getLogger(RoomDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Room addRoom(Room room) {
        entityManager.persist(room);
        return room;
    }

    public Optional<Room> findRoomById(Long id) {
//        return Optional.ofNullable(entityManager.find(Room.class, id));
        TypedQuery<Room> query = entityManager.createNamedQuery("findRoomById", Room.class);
        query.setParameter("room_id", id);

        try {
            Room foundRoom = query.getSingleResult();
            return Optional.of(foundRoom);
        }catch (NoResultException ex){
            logger.error("Two or more rooms found with the same id");
            return Optional.empty();
        }
    }

    public List<Room> findRoomsByUser(Long userId) {
        TypedQuery<Room> query = entityManager.createNamedQuery("findRoomsByUserId", Room.class);
        query.setParameter("user_id", userId);

        return query.getResultList();
    }

    public List<Room> findOrderedRoomsByUser(Long userId){
        TypedQuery<Room> query = entityManager.createNamedQuery("findOrderedRoomsByUserId", Room.class);
        query.setParameter("user_id", userId);

        return query.getResultList();
    }
}
