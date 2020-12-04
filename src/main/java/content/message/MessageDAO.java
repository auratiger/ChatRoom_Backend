package content.message;

import models.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MessageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Message addMessage(Message message){
        entityManager.persist(message);
        return message;
    }

    public Message findMessageById(long id){
        Message message = entityManager.find(Message.class, id);
        if(message == null){
            throw new EntityNotFoundException("Can't find Artist for ID " + id);
        }

        return message;
    }

    public List<Message> findMessagesByUserAndRoom(long userId, long roomId){
        TypedQuery<Message> query = entityManager.createNamedQuery("findMessageByUserAndRoom", Message.class);
        query.setParameter("userid", userId).setParameter("roomid", roomId);

        return query.getResultList();
    }

    public List<Message> findMessagesByRoom(long roomId){
        TypedQuery<Message> query = entityManager.createNamedQuery("findMessagesByRoom", Message.class);
        query.setParameter("roomid", roomId);

        return query.getResultList();
    }

    public Optional<Message> findLatestMessageByRoom(long roomId){
        TypedQuery<Message> query = entityManager.createNamedQuery("findLatestMessagesByRoom", Message.class);
        query.setMaxResults(1);
        query.setParameter("roomid", roomId);

        try{
            return Optional.of(query.getSingleResult());
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public int deleteMessageById(long message_id){
        TypedQuery<Message> query = entityManager.createNamedQuery("deleteMessageById", Message.class);
        query.setParameter("message_id", message_id);

        return query.executeUpdate();
    }

}
