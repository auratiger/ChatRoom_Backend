package content.room;

import models.Room;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class RoomDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Room addRoom(Room room) {
        entityManager.persist(room);
        return room;
    }

    public Optional<Room> findRoomById(Long id) {
        return Optional.ofNullable(entityManager.find(Room.class, id));
    }
}
