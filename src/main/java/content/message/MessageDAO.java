package content.message;

import models.Message;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RequestScoped
public class MessageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Message addMessage(Message message){
        entityManager.persist(message);
        return message;
    }

}
