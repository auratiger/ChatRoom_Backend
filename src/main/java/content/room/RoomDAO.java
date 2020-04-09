package content.room;

import models.Room;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RequestScoped
public class RoomDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Room addRoom(Room room){
        entityManager.persist(room);
        return room;
    }
}
