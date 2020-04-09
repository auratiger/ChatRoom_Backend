package user.login;

import models.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@RequestScoped
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
}
