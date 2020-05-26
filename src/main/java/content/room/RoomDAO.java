package content.room;

import models.Room;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
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

    public List<Room> findRoomByUser(Long userId) {
        TypedQuery<Room> query = entityManager.createNamedQuery("findRoomsByUserId", Room.class);
        query.setParameter("user_id", userId);

        return query.getResultList();
    }
}
