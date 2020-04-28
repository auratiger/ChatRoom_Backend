package user.login;

import models.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@ApplicationScoped
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> login(String email, String password){
        TypedQuery<User> query = entityManager.createNamedQuery("findUserByEmailAndPassword", User.class);
        query.setParameter("email", email).setParameter("password", password);

        try {
            User foundUser = query.getSingleResult();
            return Optional.of(foundUser);
        }catch (NoResultException ex){
            return Optional.empty();
        }
    }

    public boolean userNameExists(String username) {
        TypedQuery<User>query = entityManager.createNamedQuery("findUserByUsername", User.class);
        query.setParameter("username", username);

        try {
            query.getSingleResult();
            return true;
        }catch (NoResultException ex){
            return false;
        }
    }

    public User verifyEmail(Long id){

        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, id);

        if(user == null){
            throw new IllegalArgumentException("Cannot verify non-existing user");
        }

        user.setEmailVerified(true);

        System.out.println(entityManager.find(User.class, id));

        entityManager.getTransaction().commit();
        entityManager.close();

        return user;
    }

    public boolean emailExists(String email){
        TypedQuery<User> query = entityManager.createNamedQuery("findUserByEmail", User.class);
        query.setParameter("email", email);

        try {
            query.getSingleResult();
            return true;
        }catch (NoResultException ex){
            return false;
        }
    }

    public Optional<User> findUserById(Long id){
        return Optional.ofNullable(entityManager.find(User.class, id));
    }
}
