package content.message;

import models.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class MessageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Message addMessage(Message message){
        entityManager.persist(message);
        return message;
    }

    public List<Message> findMessagesByUserAndRoom(long userId, long roomId){
        TypedQuery<Message> query = entityManager.createNamedQuery("findMessageByUserAndRoom", Message.class);
        query.setParameter("userid", userId).setParameter("roomid", roomId);

        return query.getResultList();
    }

    public List<Message> findMessagesByRoom(long roomId){
        TypedQuery<Message> query = entityManager.createNamedQuery("findMessageByRoom", Message.class);
        query.setParameter("roomid", roomId);

        return query.getResultList();
    }

}
